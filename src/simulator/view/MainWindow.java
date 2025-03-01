package simulator.view;

import simulator.control.Controller;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.*;

public class MainWindow extends JFrame {

    private final Controller controller;

    public MainWindow(Controller controller) {
        super("Physics Simulator");
        this.controller = controller;
        initGUI();
    }

    private void initGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1000); // Tamaño de la ventana principal
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        // Panel principal con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        // Panel central con BoxLayout (vertical)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Inicializar componentes
        ControlPanel controlPanel = new ControlPanel(controller);
        BodiesTable bodiesTable = new BodiesTable(controller);
        Viewer viewer = new Viewer(controller);
        StatusBar statusBar = new StatusBar(controller);

        // Añadir componentes al panel principal
        mainPanel.add(controlPanel, PAGE_START);
        mainPanel.add(statusBar, PAGE_END);

        // Configurar tamaño máximo de la tabla de cuerpos
        bodiesTable.setMaximumSize(new Dimension(Integer.MAX_VALUE, bodiesTable.getMinimumSize().height));

        // Añadir componentes al panel central
        centerPanel.add(bodiesTable);
        centerPanel.add(viewer);

        // Añadir panel central al panel principal
        mainPanel.add(centerPanel, CENTER);
    }
}