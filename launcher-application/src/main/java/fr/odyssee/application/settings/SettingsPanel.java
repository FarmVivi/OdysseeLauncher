package fr.odyssee.application.settings;

import fr.odyssee.application.LauncherFrame;
import fr.odyssee.application.Main;
import fr.odyssee.application.color.ColorAPI;
import fr.odyssee.application.resources.LauncherResources;
import fr.odyssee.application.utils.FileManager;
import fr.odyssee.application.utils.TextField;
import fr.odyssee.common.AppDataDir;
import fr.odyssee.common.maths.DetermineIsNumber;
import fr.theshark34.swinger.colored.SColoredButton;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static fr.theshark34.swinger.Swinger.drawFullsizedImage;

public class SettingsPanel extends JPanel implements SwingerEventListener {
    private final Image background = LauncherResources.getResourceBufferedImage("background_settings.png");

    private final SColoredButton confirmButton = new SColoredButton(ColorAPI.principalColor, ColorAPI.principalColor_hovered, ColorAPI.principalColor_disabled);
    private final SColoredButton cancelButton = new SColoredButton(ColorAPI.principalColor, ColorAPI.principalColor_hovered, ColorAPI.principalColor_disabled);

    private final TextField ramField = new TextField("");

    private final JCheckBox animationCheckBox = new JCheckBox();

    private final JCheckBox closeAfterLaunchCheckBox = new JCheckBox();

    private final JCheckBox testServersCheckBox = new JCheckBox();

    private final SColoredButton deleteDir = new SColoredButton(ColorAPI.RED, ColorAPI.RED_HOVERED, ColorAPI.RED_DISABLED);

    public SettingsPanel() {
        this.setLayout(null);

        confirmButton.setBounds(70, 300, 325, 74);
        confirmButton.addEventListener(this);
        this.add(confirmButton);

        cancelButton.setBounds(410, 300, 325, 74);
        cancelButton.addEventListener(this);
        this.add(cancelButton);

        JLabel confirmLabel = new JLabel("Confirmer", JLabel.CENTER);
        confirmLabel.setBounds(0, 0, confirmButton.getWidth(), confirmButton.getHeight());
        confirmLabel.setForeground(Color.WHITE);
        confirmLabel.setFont(new Font("Arial Black", Font.PLAIN, 25));
        confirmButton.add(confirmLabel);

        JLabel cancelLabel = new JLabel("Annuler", JLabel.CENTER);
        cancelLabel.setBounds(0, 0, cancelButton.getWidth(), cancelButton.getHeight());
        cancelLabel.setForeground(Color.WHITE);
        cancelLabel.setFont(new Font("Arial Black", Font.PLAIN, 25));
        cancelButton.add(cancelLabel);

        deleteDir.setBounds(70, 265, 150, 25);
        deleteDir.addEventListener(this);
        this.add(deleteDir);

        JLabel deleteDirLabel = new JLabel("Supprimer ." + Main.name.toLowerCase(), JLabel.CENTER);
        deleteDirLabel.setBounds(0, 0, deleteDir.getWidth(), deleteDir.getHeight());
        deleteDirLabel.setForeground(Color.WHITE);
        deleteDirLabel.setFont(new Font("Arial Black", Font.PLAIN, 15));
        deleteDir.add(deleteDirLabel);

        JLabel ramLabel = new JLabel("RAM:       M ", SwingConstants.LEFT);
        ramLabel.setBounds(75, 65, 650, 30);
        ramLabel.setForeground(Color.WHITE);
        ramLabel.setFont(new Font("Arial Black", Font.PLAIN, 25));
        this.add(ramLabel);

        ramField.setForeground(Color.BLACK);
        ramField.setFont(new Font("Arial Black", Font.PLAIN, 15));
        ramField.setAlignmentY(JTextField.CENTER);
        ramField.setCaretColor(Color.BLACK);
        ramField.setBounds(150, 65, 48, 30);
        ramField.setPlaceholder("Ram");
        ramField.setText(SettingsManager.getSetting(Settings.MINECRAFT_RAM));
        this.add(ramField);

        JLabel animationLabel = new JLabel("Fondre le launcher à son ouverture et à sa fermeture", SwingConstants.LEFT);
        animationLabel.setBounds(100, 100, 650, 30);
        animationLabel.setForeground(Color.WHITE);
        animationLabel.setFont(new Font("Arial", Font.BOLD, 18));
        this.add(animationLabel);

        animationCheckBox.setBounds(75, 108, 21, 13);
        animationCheckBox.setSelected(Boolean.parseBoolean(SettingsManager.getSetting(Settings.LAUNCHER_ANIMATION)));
        animationCheckBox.setOpaque(false);
        this.add(animationCheckBox);

        JLabel closeAfterLaunchLabel = new JLabel("Fermer le launcher après que le jeu soit lancer", SwingConstants.LEFT);
        closeAfterLaunchLabel.setBounds(100, 135, 650, 30);
        closeAfterLaunchLabel.setForeground(Color.WHITE);
        closeAfterLaunchLabel.setFont(new Font("Arial", Font.BOLD, 18));
        this.add(closeAfterLaunchLabel);

        closeAfterLaunchCheckBox.setBounds(75, 143, 21, 13);
        closeAfterLaunchCheckBox.setSelected(Boolean.parseBoolean(SettingsManager.getSetting(Settings.LAUNCHER_CLOSEAFTERLAUNCHINGOFAGAME)));
        closeAfterLaunchCheckBox.setOpaque(false);
        this.add(closeAfterLaunchCheckBox);

        JLabel testServersLabel = new JLabel("Afficher les serveurs de test (Redémarrage requis)", SwingConstants.LEFT);
        testServersLabel.setBounds(100, 170, 650, 30);
        testServersLabel.setForeground(Color.WHITE);
        testServersLabel.setFont(new Font("Arial", Font.BOLD, 18));
        this.add(testServersLabel);

        testServersCheckBox.setBounds(75, 178, 21, 13);
        testServersCheckBox.setSelected(Boolean.parseBoolean(SettingsManager.getSetting(Settings.TESTS_SERVERS)));
        testServersCheckBox.setOpaque(false);
        this.add(testServersCheckBox);
    }

    @Override
    public void onEvent(SwingerEvent e) {
        if (e.getSource() == confirmButton) {
            if (ramField.getText() != null) {
                if (DetermineIsNumber.isInteger(ramField.getText())) {
                    SettingsManager.setSetting(Settings.MINECRAFT_RAM, ramField.getText());
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur, veuillez entrer un nombre correct !", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            SettingsManager.setSetting(Settings.LAUNCHER_ANIMATION, animationCheckBox.isSelected() + "");
            SettingsManager.setSetting(Settings.LAUNCHER_CLOSEAFTERLAUNCHINGOFAGAME, closeAfterLaunchCheckBox.isSelected() + "");
            SettingsManager.setSetting(Settings.TESTS_SERVERS, testServersCheckBox.isSelected() + "");

            LauncherFrame.getInstance().getSettingsFrame().setVisible(false);
        } else if (e.getSource() == cancelButton) {
            LauncherFrame.getInstance().getSettingsFrame().setVisible(false);
        } else if (e.getSource() == deleteDir) {
            enableButtons(false);
            LauncherFrame.getInstance().setVisible(false);
            File DIR = AppDataDir.createGameDir(Main.name);
            if (DIR.exists()) {
                FileManager.deleteDirectory(DIR.getAbsolutePath());
            }
            System.exit(0);
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        drawFullsizedImage(graphics, this, background);
    }

    public void enableButtons(boolean enabled) {
        cancelButton.setEnabled(enabled);
        confirmButton.setEnabled(enabled);
        deleteDir.setEnabled(enabled);
    }

    public void resetSettings() {
        animationCheckBox.setSelected(Boolean.parseBoolean(SettingsManager.getSetting(Settings.LAUNCHER_ANIMATION)));
        closeAfterLaunchCheckBox.setSelected(Boolean.parseBoolean(SettingsManager.getSetting(Settings.LAUNCHER_CLOSEAFTERLAUNCHINGOFAGAME)));
        testServersCheckBox.setSelected(Boolean.parseBoolean(SettingsManager.getSetting(Settings.TESTS_SERVERS)));
    }
}