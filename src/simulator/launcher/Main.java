package simulator.launcher;

/*
 * Examples of command-line parameters:
 *
 *  -h
 *  -i resources/examples/ex4.4body.txt -s 100
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100 -gl ftcg
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100 -gl nlug
 *
 */

import org.apache.commons.cli.*;
import org.json.JSONObject;
import simulator.control.Controller;
import simulator.factories.*;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;
import simulator.view.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {

    // Default values for some parameters
    private final static Double _dtimeDefaultValue = 2500.0;
    private final static int DEFAULT_STEPS = 150;
    private final static String DEFAULT_MODE = "batch";

    // Attributes to store values corresponding to command-line parameters
    private static Double _dtime = null;
    private static String _inFile = null;
    private static String _outFile = null;
    private static JSONObject _gravityLawsInfo = null;
    private static Integer _steps = null;
    private static ExecutionMode _mode = null;

    // Factories
    private static Factory<Body> _bodyFactory;
    private static Factory<GravityLaws> _gravityLawsFactory;

    // Execution modes
    private enum ExecutionMode {
        BATCH, GUI
    }

    /**
     * Initializes the factories for bodies and gravity laws.
     */
    private static void init() {
        initFactories();
    }

    /**
     * Initializes the body and gravity laws factories.
     */
    private static void initFactories() {
        // Initialize _bodyFactory
        ArrayList<Builder<Body>> bodyBuilders = new ArrayList<>();
        bodyBuilders.add(new BasicBodyBuilder());
        bodyBuilders.add(new MassLosingBodyBuilder());
        _bodyFactory = new BuilderBasedFactory<>(bodyBuilders);

        // Initialize _gravityLawsFactory
        ArrayList<Builder<GravityLaws>> gravityLawsBuilders = new ArrayList<>();
        gravityLawsBuilders.add(new NewtonUniversalGravitationBuilder());
        gravityLawsBuilders.add(new FallingToCenterGravityBuilder());
        gravityLawsBuilders.add(new NoGravityBuilder());
        _gravityLawsFactory = new BuilderBasedFactory<>(gravityLawsBuilders);
    }

    /**
     * Parses the command-line arguments and sets the corresponding parameters.
     *
     * @param args Command-line arguments.
     */
    private static void parseArgs(String[] args) {
        Options cmdLineOptions = buildOptions();
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine line = parser.parse(cmdLineOptions, args);
            parseHelpOption(line, cmdLineOptions);
            parseInFileOption(line);
            parseDeltaTimeOption(line);
            parseOutFileOption(line);
            parseGravityLawsOption(line);
            parseStepsOption(line);
            parseMode(line);

            // Additional validation
            if (_mode == ExecutionMode.BATCH && (_inFile == null || _outFile == null || _steps == null)) {
                throw new ParseException("In batch mode, input file, output file, and steps are required.");
            }

        } catch (ParseException e) {
            System.err.println("Error parsing command-line arguments: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Builds the command-line options.
     *
     * @return The command-line options.
     */
    private static Options buildOptions() {
        Options options = new Options();

        options.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());
        options.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());
        options.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
                .desc("A double representing actual time, in seconds, per simulation step. Default value: " + _dtimeDefaultValue + ".").build());
        options.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file, where output is written. Default value: the standard output.").build());
        options.addOption(Option.builder("s").longOpt("steps").hasArg().desc("An integer representing the number of simulation steps. Default value: " + DEFAULT_STEPS + ".").build());
        options.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Execution Mode. Possible values: 'batch' (Batch mode), 'gui' (Graphical User Interface mode). Default value: '" + DEFAULT_MODE + "'.").build());

        return options;
    }

    private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
        if (line.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
            System.exit(0);
        }
    }

    private static void parseInFileOption(CommandLine line) throws ParseException {
        _inFile = line.getOptionValue("i");
        if (_inFile == null && _mode == ExecutionMode.BATCH) {
            throw new ParseException("An input file of bodies is required in batch mode.");
        }
    }

    private static void parseOutFileOption(CommandLine line) throws ParseException {
        _outFile = line.getOptionValue("o");
        if (_outFile == null && _mode == ExecutionMode.BATCH) {
            throw new ParseException("An output file is required in batch mode.");
        }
    }

    private static void parseStepsOption(CommandLine line) throws ParseException {
        String steps = line.getOptionValue("s");
        if (_mode == ExecutionMode.BATCH && steps == null) {
            throw new ParseException("The number of steps is required in batch mode.");
        }
        _steps = steps != null ? Integer.parseInt(steps) : DEFAULT_STEPS;
    }

    private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
        String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
        try {
            _dtime = Double.parseDouble(dt);
            assert (_dtime > 0);
        } catch (Exception e) {
            throw new ParseException("Invalid delta-time value: " + dt);
        }
    }

    private static void parseGravityLawsOption(CommandLine line) throws ParseException {
        if (_gravityLawsFactory == null) return;

        String gl = line.getOptionValue("gl");
        if (gl != null) {
            for (JSONObject fe : _gravityLawsFactory.getInfo()) {
                if (gl.equals(fe.getString("type"))) {
                    _gravityLawsInfo = fe;
                    break;
                }
            }
            if (_gravityLawsInfo == null) {
                throw new ParseException("Invalid gravity laws: " + gl);
            }
        } else {
            _gravityLawsInfo = _gravityLawsFactory.getInfo().get(0);
        }
    }

    private static void parseMode(CommandLine line) throws ParseException {
        String mode = line.getOptionValue("m", DEFAULT_MODE).toLowerCase();
        switch (mode) {
            case "batch":
                _mode = ExecutionMode.BATCH;
                break;
            case "gui":
                _mode = ExecutionMode.GUI;
                break;
            default:
                throw new ParseException("Invalid execution mode: " + mode);
        }
    }

    private static void startBatchMode() throws Exception {
        try (InputStream in = Files.newInputStream(Paths.get(_inFile));
             OutputStream out = Files.newOutputStream(Paths.get(_outFile))) {
            PhysicsSimulator simulator = new PhysicsSimulator(_gravityLawsFactory.createInstance(_gravityLawsInfo), _dtime);
            Controller controller = new Controller(simulator, _bodyFactory, _gravityLawsFactory);
            controller.loadBodies(in);
            controller.run(_steps, out);
        }
    }

    private static void startGUIMode() throws Exception {
        PhysicsSimulator simulator = new PhysicsSimulator(_gravityLawsFactory.createInstance(_gravityLawsInfo), _dtime);
        Controller ctrl = new Controller(simulator, _bodyFactory, _gravityLawsFactory);

        if (_inFile != null) {
            try (InputStream in = Files.newInputStream(Paths.get(_inFile))) {
                ctrl.loadBodies(in);
            }
        }

        SwingUtilities.invokeAndWait(() -> {
            MainWindow window = new MainWindow(ctrl);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            window.setSize(screenSize.width, screenSize.height);
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }

    private static void start(String[] args) throws Exception {
        parseArgs(args);

        switch (_mode) {
            case BATCH:
                startBatchMode();
                break;
            case GUI:
                startGUIMode();
                break;
        }
    }

    public static void main(String[] args) {
        try {
            init();
            start(args);
        } catch (Exception e) {
            System.err.println("Error during simulation execution:");
            System.err.println(" - Cause: " + e.getMessage());
            System.err.println(" - Details:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}