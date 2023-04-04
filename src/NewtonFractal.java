import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

public class NewtonFractal extends JFrame {

    private FractalPanel fractalPanel;

    public NewtonFractal() {
        setTitle("Newton Fractal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        fractalPanel = new FractalPanel();
        add(fractalPanel, BorderLayout.CENTER);

        pack();
        setVisible(true);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                fractalPanel.updateFractal();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NewtonFractal::new);
    }

    private static class FractalPanel extends JPanel {

        private BufferedImage fractalImage;

        public FractalPanel() {
            setPreferredSize(new Dimension(800, 800));
            updateFractal();
        }

        public void updateFractal() {
            int width = Math.max(getWidth(), 1);
            int height = Math.max(getHeight(), 1);
            fractalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            double xMin = -2.0;
            double xMax = 2.0;
            double yMin = -2.0;
            double yMax = 2.0;

            double xRange = xMax - xMin;
            double yRange = yMax - yMin;

            int maxIterations = 50;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    double zx = x * xRange / width + xMin;
                    double zy = y * yRange / height + yMin;

                    double c = zx;
                    double d = zy;

                    int iterations = 0;
                    while (iterations < maxIterations) {
                        double zxNew = (2.0 / 3) * zx + (zx * zx - zy * zy) / (3 * Math.pow(zx * zx + zy * zy, 2));
                        double zyNew = (2.0 / 3) * zy * (1 - zx / (3 * Math.pow(zx * zx + zy * zy, 2)));

                        zx = zxNew;
                        zy = zyNew;

                        iterations++;

                        if (Math.abs(zx * zx + zy * zy) > 2) {
                            break;
                        }
                    }

                    int color = Color.HSBtoRGB(((float) iterations / maxIterations + 0.5f) % 1, 1, iterations < maxIterations ? 1 : 0);
                    fractalImage.setRGB(x, y, color);
                }
            }

            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fractalImage, 0, 0, null);
        }
    }
}
