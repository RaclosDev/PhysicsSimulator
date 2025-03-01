package simulator.view;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {
    // Añade los nombres de columnas
    private String[] columNames = {"Id", "Mass", "Position", "Velocity", "Aceleration"};
    private List<Body> _bodies;

    BodiesTableModel(Controller ctrl) {
        _bodies = new ArrayList<>();
        ctrl.addObserver(this);
    }

    @Override
    public int getRowCount() {
        return _bodies.size();
    }

    @Override
    public int getColumnCount() {
        return columNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String dato = columNames[columnIndex];
        switch (dato) {
            case "Id":
                return _bodies.get(rowIndex).getId();

            case "Mass":
                return _bodies.get(rowIndex).getMass();

            case "Position":
                return _bodies.get(rowIndex).getPosition();

            case "Velocity":
                return _bodies.get(rowIndex).getVelocity();

            case "Aceleration":
                return _bodies.get(rowIndex).getAcceleration();


        }
        return null;
    }


    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _bodies = bodies;
                fireTableStructureChanged();
            }
        });
    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {

    }

    @Override
    public void onBodyAdded(List<Body> bodies, Body b) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _bodies = bodies;
                fireTableStructureChanged();
            }
        });
    }

    @Override
    public void onAdvance(List<Body> bodies, double time) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _bodies = bodies;
                fireTableStructureChanged();
            }
        });

    }

    @Override
    public void onDeltaTimeChanged(double dt) {

    }

    @Override
    public void onGravityLawChanged(String gLawsDesc) {

    }
}
