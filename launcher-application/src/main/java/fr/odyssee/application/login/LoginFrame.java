package fr.odyssee.application.login;

import fr.odyssee.application.LauncherFrame;
import fr.odyssee.application.Main;
import fr.odyssee.application.resources.LauncherResources;
import fr.odyssee.application.settings.Settings;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.swinger.Swinger;

import javax.swing.*;

public class LoginFrame extends JFrame {
    private final LoginPanel loginPanel;
    private LoginListener loginListener;
    private Saver loginSaver;

    public LoginFrame() {
        this.setTitle(Main.name + " - Login");
        this.setSize(800, 450);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(LauncherFrame.getInstance());
        this.setUndecorated(true);
        this.setIconImage(LauncherResources.getResourceBufferedImage("logo_64.png"));
        this.setContentPane(loginPanel = new LoginPanel());
        this.setBackground(Swinger.TRANSPARENT);
        this.setVisible(false);
    }

    public void display(String frameName, LoginListener loginListener, Saver loginSaver, String info, boolean password) {
        this.setTitle(frameName);
        this.loginListener = loginListener;
        this.loginSaver = loginSaver;
        loginPanel.getTextField().setText(loginSaver.get(Settings.USERNAME.getName()));
        loginPanel.getPasswordField().setText(loginSaver.get(Settings.PASSWORD.getName()));
        loginPanel.getInfoLabel().setText(info);
        loginPanel.getPasswordField().setVisible(password);
        this.setVisible(true);
    }

    public LoginPanel getLoginPanel() {
        return loginPanel;
    }

    public LoginListener getLoginListener() {
        return loginListener;
    }

    public Saver getLoginSaver() {
        return loginSaver;
    }
}