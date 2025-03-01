package simulator.view;

import simulator.control.Controller;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.*;

public class MainWindow extends JFrame {
    ControlPanel controlPanel;
    BodiesTable bodiesTable;
    StatusBar statusBar;
    Viewer viewer;
    Controller _ctrl;

    public MainWindow(Controller ctrl) {
        super("Physics Simulator");
        _ctrl = ctrl;
        initGUI();
    }

    private void initGUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1920, 1000); //Ventana Principal
        this.setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        setContentPane(mainPanel);
        mainPanel.add(centerPanel,CENTER);
        controlPanel = new ControlPanel(_ctrl);
        bodiesTable = new BodiesTable(_ctrl);
        viewer = new Viewer(_ctrl);
        statusBar = new StatusBar(_ctrl);
        mainPanel.add(controlPanel, PAGE_START);
        mainPanel.add(statusBar,PAGE_END);
        bodiesTable.setMaximumSize(new Dimension(Integer.MAX_VALUE, bodiesTable.getMinimumSize().height));

        centerPanel.add(bodiesTable);
        centerPanel.add(viewer);


// TODO complete this method to build the GUI
// ..
    }
// other private/protected methods
// ...
}
