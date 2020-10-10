package fr.odyssee.application.settings;

import fr.odyssee.application.LauncherFrame;
import fr.odyssee.application.Main;
import fr.odyssee.application.resources.LauncherResources;
import fr.theshark34.swinger.Swinger;

import javax.swing.*;

public class SettingsFrame extends JFrame {
    private final SettingsPanel settingsPanel;

    public SettingsFrame() {
        this.setTitle(Main.name + " - Options");
        this.setSize(800, 450);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(LauncherFrame.getInstance());
        this.setUndecorated(true);
        this.setIconImage(LauncherResources.getResourceBufferedImage("logo_64.png"));
        this.setContentPane(settingsPanel = new SettingsPanel());
        this.setBackground(Swinger.TRANSPARENT);
        this.setVisible(false);
    }

    public void display() {
        settingsPanel.resetSettings();
        this.setVisible(true);
    }

    public SettingsPanel getSettingsPanel() {
        return settingsPanel;
    }
}