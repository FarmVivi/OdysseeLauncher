package fr.odyssee.common.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;

public class RoundProgressBarComponent extends JComponent {
    private Color color;
    private double value = 0;
    private double maximum = 100;

    public RoundProgressBarComponent(Color color) {
        this.color = color;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        AffineTransform old = g2.getTransform();

        double progress = value / maximum * 100;

        if (progress > 0) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(this.getWidth() / 2, this.getHeight() / 2);
            g2.rotate(Math.toRadians(270));
            Arc2D.Float arc = new Arc2D.Float(Arc2D.PIE);
            arc.setFrameFromCenter(new Point(0, 0), new Point(this.getWidth() / 2, this.getHeight() / 2));
            arc.setAngleStart(0);
            arc.setAngleExtent(-progress * 3.6);
            g2.setColor(color);
            g2.fill(arc);
            g2.setTransform(old);
        }
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        if (this.value != value) {
            this.value = value;

            this.repaint();
        }
    }

    public double getMaximum() {
        return maximum;
    }

    public void setMaximum(double maximum) {
        if (this.maximum != maximum) {
            this.maximum = maximum;

            this.repaint();
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (this.color != color) {
            this.color = color;

            this.repaint();
        }
    }
}