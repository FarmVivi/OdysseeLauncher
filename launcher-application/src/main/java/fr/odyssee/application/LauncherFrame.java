package fr.odyssee.application;

import com.sun.awt.AWTUtilities;
import fr.odyssee.application.login.LoginFrame;
import fr.odyssee.application.resources.LauncherResources;
import fr.odyssee.application.settings.Settings;
import fr.odyssee.application.settings.SettingsFrame;
import fr.odyssee.application.settings.SettingsManager;
import fr.odyssee.common.AppDataDir;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.animation.Animator;
import fr.theshark34.swinger.util.WindowMover;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LauncherFrame extends JFrame {
    private static LauncherFrame instance;
    private final LauncherPanel launcherPanel;
    private final SettingsFrame settingsFrame;
    private final LoginFrame loginFrame;
    private final CrashReporter crashReporter = new CrashReporter(Main.name + " Launcher", new File(AppDataDir.createGameDir(Main.name), "launcher/crashes/"));

    private final Map<LauncherGames, LauncherGame> games = new HashMap<>();

    public LauncherFrame() {
        instance = this;

        this.setTitle(Main.name + " - V" + Main.version);
        this.setSize(1280, 720);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);
        this.setIconImage(LauncherResources.getResource("logo_64.png").getImage());
        this.setContentPane(launcherPanel = new LauncherPanel());
        this.setBackground(Swinger.TRANSPARENT);

        String showTestsServers = SettingsManager.getSetting(Settings.LAUNCHER_ANIMATION);
        if (Boolean.parseBoolean(showTestsServers)) {
            AWTUtilities.setWindowOpacity(this, 0.0F);
            Animator.fadeInFrame(this, 2);
        }

        WindowMover mover = new WindowMover(this);
        this.addMouseListener(mover);
        this.addMouseMotionListener(mover);

        this.setVisible(true);

        loginFrame = new LoginFrame();
        settingsFrame = new SettingsFrame();
    }

    public static LauncherFrame getInstance() {
        return instance;
    }

    public LauncherPanel getLauncherPanel() {
        return launcherPanel;
    }

    public LoginFrame getLoginFrame() {
        return loginFrame;
    }

    public SettingsFrame getSettingsFrame() {
        return settingsFrame;
    }

    public CrashReporter getCrashReporter() {
        return crashReporter;
    }

    public Map<LauncherGames, LauncherGame> getGames() {
        return games;
    }
}