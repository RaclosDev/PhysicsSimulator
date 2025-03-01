package simulator.view;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {

    private static final String[] COLUMN_NAMES = {"Id", "Mass", "Position", "Velocity", "Acceleration"};
    private List<Body> bodies;

    public BodiesTableModel(Controller controller) {
        this.bodies = List.of(); // Inicializa con una lista vacía
        controller.addObserver(this);
    }

    @Override
    public int getRowCount() {
        return bodies.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Body body = bodies.get(rowIndex);
        return switch (COLUMN_NAMES[columnIndex]) {
            case "Id" -> body.getId();
            case "Mass" -> body.getMass();
            case "Position" -> body.getPosition();
            case "Velocity" -> body.getVelocity();
            case "Acceleration" -> body.getAcceleration();
            default -> null;
        };
    }

    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String gravityLawsDesc) {
        updateTable(bodies);
    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String gravityLawsDesc) {
        updateTable(bodies);
    }

    @Override
    public void onBodyAdded(List<Body> bodies, Body body) {
        updateTable(bodies);
    }

    @Override
    public void onAdvance(List<Body> bodies, double time) {
        updateTable(bodies);
    }

    @Override
    public void onDeltaTimeChanged(double dt) {
        // No es necesario actualizar la tabla
    }

    @Override
    public void onGravityLawChanged(String gravityLawsDesc) {
        // No es necesario actualizar la tabla
    }

    private void updateTable(List<Body> bodies) {
        SwingUtilities.invokeLater(() -> {
            this.bodies = bodies;
            fireTableStructureChanged();
        });
    }
}