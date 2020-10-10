package fr.odyssee.application.login;

import fr.odyssee.application.LauncherFrame;
import fr.odyssee.application.color.ColorAPI;
import fr.odyssee.application.resources.LauncherResources;
import fr.odyssee.application.utils.PasswordField;
import fr.odyssee.application.utils.TextField;
import fr.theshark34.swinger.colored.SColoredButton;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;

import javax.swing.*;
import java.awt.*;

import static fr.theshark34.swinger.Swinger.drawFullsizedImage;

public class LoginPanel extends JPanel implements SwingerEventListener {
    private final Image background = LauncherResources.getResourceBufferedImage("background_login.png");

    private final TextField textField = new TextField("");
    private final PasswordField passwordField = new PasswordField("");

    private final SColoredButton loginButton = new SColoredButton(ColorAPI.principalColor, ColorAPI.principalColor_hovered, ColorAPI.principalColor_disabled);
    private final SColoredButton cancelButton = new SColoredButton(ColorAPI.principalColor, ColorAPI.principalColor_hovered, ColorAPI.principalColor_disabled);

    private final JLabel infoLabel = new JLabel("", JLabel.CENTER);

    public LoginPanel() {
        this.setLayout(null);

        textField.setForeground(Color.BLACK);
        textField.setFont(new Font("Arial Black", Font.PLAIN, 15));
        textField.setAlignmentY(JTextField.CENTER);
        textField.setCaretColor(Color.BLACK);
        //textField.setOpaque(false);
        //textField.setBorder(null);
        textField.setBounds(70, 190, 665, 30);
        textField.setPlaceholder("Pseudonyme");
        this.add(textField);

        passwordField.setForeground(Color.BLACK);
        passwordField.setFont(textField.getFont());
        passwordField.setAlignmentY(JTextField.CENTER);
        passwordField.setCaretColor(Color.BLACK);
        //passwordField.setOpaque(false);
        //passwordField.setBorder(null);
        passwordField.setBounds(70, 230, 665, 30);
        passwordField.setPlaceholder("Mot de Passe");
        this.add(passwordField);

        loginButton.setBounds(70, 300, 325, 74);
        loginButton.addEventListener(this);
        this.add(loginButton);

        cancelButton.setBounds(410, 300, 325, 74);
        cancelButton.addEventListener(this);
        this.add(cancelButton);

        JLabel loginLabel = new JLabel("Se connecter", JLabel.CENTER);
        loginLabel.setBounds(0, 0, loginButton.getWidth(), loginButton.getHeight());
        loginLabel.setForeground(Color.WHITE);
        loginLabel.setFont(new Font("Arial Black", Font.PLAIN, 25));
        loginButton.add(loginLabel);

        JLabel cancelLabel = new JLabel("Annuler", JLabel.CENTER);
        cancelLabel.setBounds(0, 0, cancelButton.getWidth(), cancelButton.getHeight());
        cancelLabel.setForeground(Color.WHITE);
        cancelLabel.setFont(new Font("Arial Black", Font.PLAIN, 25));
        cancelButton.add(cancelLabel);

        infoLabel.setBounds(0, 50, 800, 125);
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(infoLabel);
    }

    @SuppressWarnings({"deprecation"})
    @Override
    public void onEvent(SwingerEvent e) {
        if (e.getSource() == loginButton) {
            setFieldsEnabled(false);

            if (textField.getText().replaceAll(" ", "").length() == 0) {
                JOptionPane.showMessageDialog(this, "Erreur, veuillez entrer un identifiant valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                setFieldsEnabled(true);
                return;
            }

            new Thread(() -> {
                if (LauncherFrame.getInstance().getLoginFrame().getLoginListener()
                        .login(LauncherFrame.getInstance().getLoginFrame(),
                                textField.getText(),
                                passwordField.getText(),
                                LauncherFrame.getInstance().getLoginFrame().getLoginSaver())) {
                    LauncherFrame.getInstance().getLoginFrame().getLoginListener()
                            .onSuccessLogin(textField.getText(),
                                    passwordField.getText(),
                                    LauncherFrame.getInstance().getLoginFrame().getLoginSaver());
                    LauncherFrame.getInstance().getLoginFrame().setVisible(false);
                    LauncherFrame.getInstance().getLoginFrame().getLoginPanel().setFieldsEnabled(true);
                } else {
                    LauncherFrame.getInstance().getLoginFrame().getLoginListener().onFailedLogin();
                    setFieldsEnabled(true);
                }
            }).start();
        } else if (e.getSource() == cancelButton) {
            LauncherFrame.getInstance().getLoginFrame().setVisible(false);
            LauncherFrame.getInstance().getLoginFrame().getLoginListener().onCancel();
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        drawFullsizedImage(graphics, this, background);
    }

    private void setFieldsEnabled(boolean enabled) {
        textField.setEnabled(enabled);
        passwordField.setEnabled(enabled);
        loginButton.setEnabled(enabled);
        cancelButton.setEnabled(enabled);
    }

    public TextField getTextField() {
        return textField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public JLabel getInfoLabel() {
        return infoLabel;
    }
}