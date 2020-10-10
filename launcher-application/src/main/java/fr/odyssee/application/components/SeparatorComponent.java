package fr.odyssee.application.components;

import fr.odyssee.application.color.ColorAPI;
import fr.theshark34.swinger.Swinger;

import javax.swing.*;
import java.awt.*;

public class SeparatorComponent extends JComponent {
    private final String serverInfoText;

    public SeparatorComponent(String serverInfoText) {
        this.serverInfoText = serverInfoText;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Background
        g.setColor(Swinger.getTransparentInstance(ColorAPI.principalColor, 200));
        g.fillRect(0, 0, getWidth(), getHeight());
        //Separator text
        Swinger.activateAntialias(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial Black", Font.PLAIN, 18));
        Swinger.drawCenteredString(g, serverInfoText, new Rectangle(0, 0, getWidth(), getHeight() * 2 + 4));
    }
}