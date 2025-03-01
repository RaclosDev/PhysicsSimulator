package simulator.view;

import simulator.control.Controller;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class BodiesTable extends JPanel {

    public BodiesTable(Controller ctrl) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.black, 2),
                "Bodies",
                TitledBorder.LEFT, TitledBorder.TOP));

        JTable table = new JTable(new BodiesTableModel(ctrl));
        table.setFillsViewportHeight(true); // Asegura que la tabla ocupe todo el espacio disponible
        add(new JScrollPane(table), BorderLayout.CENTER); // Añade la tabla con scroll
    }
}