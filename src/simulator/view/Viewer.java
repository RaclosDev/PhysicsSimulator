package simulator.view;

import simulator.control.Controller;
import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class Viewer extends JComponent implements SimulatorObserver {
    private static final int _WIDTH = 1000;
    private static final int _HEIGHT = 1000;
    // Añade constantespara los colores
    private int _centerX;
    private int _centerY;
    private double _scale;
    private List<Body> _bodies;
    private boolean _showHelp;

    Viewer(Controller ctrl) {
        initGUI();
        ctrl.addObserver(this);
        repaint();
    }

    private void initGUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.black, 2),
                "Viewer",
                TitledBorder.LEFT, TitledBorder.TOP));
        this.setSize(_WIDTH, _HEIGHT);
        _bodies = new ArrayList<>();
        _scale = 1;
        _showHelp = true;

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            // ...
            @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyChar()) {
                    case '-':
                        _scale = _scale * 1.1;
                        break;
                    case '+':
                        _scale = Math.max(1000.0, _scale / 1.1);
                        break;
                    case '=':
                        autoScale();
                        break;
                    case 'h':
                        _showHelp = !_showHelp;
                        break;
                    default:
                }
                repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            // ...
            @Override
            public void mouseEntered(MouseEvent e) {
                requestFocus();
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gr = (Graphics2D) g;
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        gr.setColor(Color.RED);
        gr.drawString("h: toggle help, +: zoom in, -: zoom out, =: fit", 5, 25);
        gr.drawString("Scaling ratio: " + _scale, 5, 40);
        // use ’gr’ to draw not ’g’
// calculate the center
        _centerX = getWidth() / 2;
        _centerY = getHeight() / 2;
// TODO draw a cross at center
        gr.drawLine(_centerX, _centerY + 10, _centerX, _centerY - 10);
        gr.drawLine(_centerX + 10, _centerY, _centerX - 10, _centerY);
        //gr.drawLine((int) ((_centerX + 10) / _scale), (int) ((_centerY) / _scale), (int) ((_centerX - 10) / _scale), (int) ((_centerY) / _scale));
        //gr.drawLine((int) ((_centerX) / _scale), (int) ((_centerY + 10) / _scale), (int) ((_centerX) / _scale), (int) ((_centerY - 10) / _scale));
// TODO draw bodies
        for (Body b : _bodies) {

            gr.fillOval(_centerX + (int) (b.getPosition().coordinate(0) / _scale) - 5, _centerY - (int) (b.getPosition().coordinate(1) / _scale) - 5, 10, 10);
            gr.drawString(b.getId(), _centerX + (int) (b.getPosition().coordinate(0) / _scale) - 5, _centerY - (int) (b.getPosition().coordinate(1) / _scale) - 5);
        }

// TODO draw help if _showHelp is true

    }

    // other private/protected methods
// ...
    private void autoScale() {
        double max = 1.0;
        for (Body b : _bodies) {
            Vector p = b.getPosition();
            for (int i = 0; i < p.dim(); i++)
                max = Math.max(max,
                        Math.abs(b.getPosition().coordinate(i)));
        }
        double size = Math.max(1.0, Math.min((double) getWidth(),
                (double) getHeight()));
        _scale = max > size ? 4.0 * max / size : 1.0;
    }

    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _bodies = bodies;
                autoScale();
                repaint();
            }
        });
    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _bodies = bodies;
                autoScale();
                repaint();
            }
        });
    }

    @Override
    public void onBodyAdded(List<Body> bodies, Body b) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _bodies = bodies;
                autoScale();
                repaint();
            }
        });
    }

    @Override
    public void onAdvance(List<Body> bodies, double time) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                repaint();
            }
        });
    }

    @Override
    public void onDeltaTimeChanged(double dt) {

    }

    @Override
    public void onGravityLawChanged(String gLawsDesc) {

    }
// SimulatorObserver methods
// ...
}