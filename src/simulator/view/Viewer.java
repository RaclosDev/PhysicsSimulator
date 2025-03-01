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
import java.util.List;

public class Viewer extends JComponent implements SimulatorObserver {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;

    private int centerX;
    private int centerY;
    private double scale;
    private List<Body> bodies;
    private boolean showHelp;

    Viewer(Controller controller) {
        initGUI();
        controller.addObserver(this);
        repaint();
    }

    private void initGUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.black, 2),
                "Viewer",
                TitledBorder.LEFT, TitledBorder.TOP));
        setSize(WIDTH, HEIGHT);
        bodies = List.of();
        scale = 1.0;
        showHelp = true;

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case '-':
                        scale *= 1.1;
                        break;
                    case '+':
                        scale = Math.max(1000.0, scale / 1.1);
                        break;
                    case '=':
                        autoScale();
                        break;
                    case 'h':
                        showHelp = !showHelp;
                        break;
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

        // Draw help text
        gr.setColor(Color.RED);
        gr.drawString("h: toggle help, +: zoom in, -: zoom out, =: fit", 5, 25);
        gr.drawString("Scaling ratio: " + scale, 5, 40);

        // Calculate the center
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;

        // Draw a cross at the center
        gr.drawLine(centerX, centerY + 10, centerX, centerY - 10);
        gr.drawLine(centerX + 10, centerY, centerX - 10, centerY);

        // Draw bodies
        for (Body body : bodies) {
            int x = centerX + (int) (body.getPosition().coordinate(0) / scale) - 5;
            int y = centerY - (int) (body.getPosition().coordinate(1) / scale) - 5;
            gr.fillOval(x, y, 10, 10);
            gr.drawString(body.getId(), x, y);
        }
    }

    private void autoScale() {
        double max = 1.0;
        for (Body body : bodies) {
            Vector position = body.getPosition();
            for (int i = 0; i < position.dim(); i++) {
                max = Math.max(max, Math.abs(position.coordinate(i)));
            }
        }
        double size = Math.max(1.0, Math.min(getWidth(), getHeight()));
        scale = max > size ? 4.0 * max / size : 1.0;
    }

    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String gravityLawsDesc) {
        updateBodies(bodies);
    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String gravityLawsDesc) {
        updateBodies(bodies);
    }

    @Override
    public void onBodyAdded(List<Body> bodies, Body body) {
        updateBodies(bodies);
    }

    @Override
    public void onAdvance(List<Body> bodies, double time) {
        SwingUtilities.invokeLater(this::repaint);
    }

    @Override
    public void onDeltaTimeChanged(double dt) {
    }

    @Override
    public void onGravityLawChanged(String gravityLawsDesc) {
    }

    private void updateBodies(List<Body> bodies) {
        SwingUtilities.invokeLater(() -> {
            this.bodies = bodies;
            autoScale();
            repaint();
        });
    }
}