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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static java.lang.Integer.valueOf;

public class Main {

    // default values for some parameters
    //
    private final static Double _dtimeDefaultValue = 2500.0;

    // some attributes to stores values corresponding to command-line parameters
    //
    private static Double _dtime = null;
    private static String _inFile = null;
    private static String _outFile = null;
    private static JSONObject _gravityLawsInfo = null;
    private static Integer _steps = null;
    private static String _mode = null;
    // factories
    private static Factory<Body> _bodyFactory;
    private static Factory<GravityLaws> _gravityLawsFactory;

    private static void init() {
        //_bodyFactory se inicializa con:
        ArrayList<Builder<Body>> bodyBuilders = new ArrayList<>();
        bodyBuilders.add(new BasicBodyBuilder());
        bodyBuilders.add(new MassLosingBodyBuilder());
        _bodyFactory = new BuilderBasedFactory<Body>(bodyBuilders);
        //_gravityLawsFactory se inicializa con:
        ArrayList<Builder<GravityLaws>> gravityLawsBuilders = new ArrayList<>();
        gravityLawsBuilders.add(new NewtonUniversalGravitationBuilder());
        gravityLawsBuilders.add(new FallingToCenterGravityBuilder());
        gravityLawsBuilders.add(new NoGravityBuilder());
        _gravityLawsFactory = new BuilderBasedFactory<GravityLaws>(gravityLawsBuilders);

    }

    private static void parseArgs(String[] args) {

        // define the valid command line options
        //
        Options cmdLineOptions = buildOptions();

        // parse the command line as provided in args
        //
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
            // if there are some remaining arguments, then something wrong is
            // provided in the command line!
            //
            String[] remaining = line.getArgs();
            if (remaining.length > 0) {
                String error = "Illegal arguments:";
                for (String o : remaining)
                    error += (" " + o);
                throw new ParseException(error);
            }

        } catch (ParseException e) {
            System.err.println(e.getLocalizedMessage());
            System.exit(1);
        }

    }

    private static Options buildOptions() {
        Options cmdLineOptions = new Options();

        // help
        cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

        // input file
        cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());

        // delta-time
        cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
                .desc("A double representing actual time, in seconds, per simulation step. Default value: "
                        + _dtimeDefaultValue + ".")
                .build());

        // gravity laws -- there is a workaround to make it work even when
        // _gravityLawsFactory is null.
        //
        String gravityLawsValues = "N/A";
        String defaultGravityLawsValue = "N/A";
        if (_gravityLawsFactory != null) {
            gravityLawsValues = "";
            for (JSONObject fe : _gravityLawsFactory.getInfo()) {
                if (gravityLawsValues.length() > 0) {
                    gravityLawsValues = gravityLawsValues + ", ";
                }
                gravityLawsValues = gravityLawsValues + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
            }
            defaultGravityLawsValue = _gravityLawsFactory.getInfo().get(0).getString("type");
        }
        cmdLineOptions.addOption(Option.builder("gl").longOpt("gravity-laws").hasArg()
                .desc("Gravity laws to be used in the simulator. Possible values: " + gravityLawsValues
                        + ". Default value: '" + defaultGravityLawsValue + "'.")
                .build());

        // output file
        cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("output <arg> Output file, where output is written. Default value: thestandard output.").build());

        // steps
        cmdLineOptions.addOption(Option.builder("s").longOpt("steps").hasArg().desc("steps <arg> An integer representing the number of simulation steps. Default value: 150.").build());

        // mode
        cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Execution Mode. Possible values: ’batch’ (Batch mode), ’gui’ (Graphical User Interface mode). Default value: ’batch’.").build());


        return cmdLineOptions;
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
        if (_inFile == null && line.getOptionValue("m").equals("batch")) {
            throw new ParseException("An input file of bodies is required");
        }
    }

    private static void parseOutFileOption(CommandLine line) throws ParseException {
        _outFile = line.getOptionValue("o");
        if (_outFile == null && line.getOptionValue("m").equals("batch")) {
            throw new ParseException("Error en OutFile");
        }
    }

    private static void parseStepsOption(CommandLine line) throws ParseException {
        if (line.getOptionValue("m").equals("batch")) {
            _steps = valueOf(line.getOptionValue("s"));
            if (_steps == null) {
                throw new ParseException("No hay steps");
            }
        }
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

        // this line is just a work around to make it work even when _gravityLawsFactory
        // is null, you can remove it when've defined _gravityLawsFactory
        if (_gravityLawsFactory == null)
            return;

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

        _mode = line.getOptionValue("m");
        if (_mode == null) {
            throw new ParseException("Error en el modo");
        }

    }

    private static void startBatchMode() throws Exception {

        InputStream in = new FileInputStream(_inFile);
        OutputStream out = new FileOutputStream(_outFile);
        //_gravityLawsFactory.createInstance(_gravityLawsInfo); lo he metido abajo
        PhysicsSimulator simulator = new PhysicsSimulator(_gravityLawsFactory.createInstance(_gravityLawsInfo), _dtime);
        Controller controller = new Controller(simulator, _bodyFactory, _gravityLawsFactory);
        controller.loadBodies(in);
        controller.run(_steps, out);

    }

    private static void startGUIMode() throws Exception {

        PhysicsSimulator simulator = new PhysicsSimulator(_gravityLawsFactory.createInstance(_gravityLawsInfo), _dtime);
        Controller ctrl = new Controller(simulator, _bodyFactory, _gravityLawsFactory);

        if (_inFile != null) {
            InputStream in = new FileInputStream(_inFile);
            ctrl.loadBodies(in);
        }


        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                MainWindow window = new MainWindow(ctrl);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                window.setSize(screenSize.width, screenSize.height);
                window.setLocationRelativeTo(null);
                window.setVisible(true);
            }
        });

    }

    private static void start(String[] args) throws Exception {
        parseArgs(args);

        switch (_mode) {
            case "batch":
                startBatchMode();
                break;
            case "gui":
                startGUIMode();
                break;

        }
        //todo poner para ambos modos
    }

    public static void main(String[] args) {
        try {
            init();
            start(args);
        } catch (Exception e) {
            System.err.println("Something went wrong ...");
            System.err.println();
            e.printStackTrace();
        }
    }
}
