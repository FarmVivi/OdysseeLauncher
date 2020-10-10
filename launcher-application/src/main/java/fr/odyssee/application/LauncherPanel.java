package fr.odyssee.application;

import fr.odyssee.application.color.ColorAPI;
import fr.odyssee.application.resources.LauncherResource;
import fr.odyssee.application.resources.LauncherResources;
import fr.odyssee.application.settings.Settings;
import fr.odyssee.application.settings.SettingsManager;
import fr.odyssee.application.updater.UpdaterManager;
import fr.odyssee.common.components.RoundProgressBarComponent;
import fr.theshark34.swinger.animation.Animator;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static fr.theshark34.swinger.Swinger.TRANSPARENT;

@SuppressWarnings("serial")
public class LauncherPanel extends JPanel implements SwingerEventListener {
    private final JPanel gamesPane;
    private final STexturedButton quitButton = new STexturedButton(Objects.requireNonNull(LauncherResources.getResourceBufferedImage("Fermer0.png")), LauncherResources.getResourceBufferedImage("Fermer1.png"));
    private final STexturedButton hideButton = new STexturedButton(Objects.requireNonNull(LauncherResources.getResourceBufferedImage("Reduire0.png")), LauncherResources.getResourceBufferedImage("Reduire1.png"));
    private final STexturedButton settingsButton = new STexturedButton(Objects.requireNonNull(LauncherResources.getResourceBufferedImage("Options0.png")), LauncherResources.getResourceBufferedImage("Options1.png"));
    private final STexturedButton backButton = new STexturedButton(Objects.requireNonNull(LauncherResources.getResourceBufferedImage("Retour0.png")), LauncherResources.getResourceBufferedImage("Retour1.png"));
    private final RoundProgressBarComponent logoProgressBar = new RoundProgressBarComponent(ColorAPI.ORANGE_CUSTOM);
    private BufferedImage background = LauncherResources.getResourceBufferedImage("background.png");
    private LauncherServers selectionnedServer = LauncherServers.DEFAULT;
    private LauncherGames selectionnedGame = LauncherGames.UNKNOWN;
    private LauncherStatus launcherStatus = LauncherStatus.GAMES;
    private JScrollPane gamesScrollPane;

    public LauncherPanel() {
        this.setLayout(null);

        JLabel home = new JLabel("", JLabel.CENTER);
        home.setBounds(0, 0, 1280, 720);
        home.setForeground(Color.WHITE);
        home.setFont(new Font("Arial", Font.PLAIN, 25));
        home.setVisible(false);
        this.add(home);

        quitButton.setBounds(1170, 78, 30, 30);
        quitButton.addEventListener(this);
        this.add(quitButton);

        hideButton.setBounds(1135, 78, 30, 30);
        hideButton.addEventListener(this);
        this.add(hideButton);

        settingsButton.setBounds(1100, 78, 30, 30);
        settingsButton.addEventListener(this);
        this.add(settingsButton);

        backButton.setBounds(80, 78, 30, 30);
        backButton.addEventListener(this);
        backButton.setVisible(false);
        this.add(backButton);

        JLabel logo = new JLabel("", LauncherResources.getResource("logo_200.png"), JLabel.CENTER);
        logo.setBounds(540, 0, 201, 200);
        this.add(logo);

        logoProgressBar.setBounds(547 - 2, 6, 188, 188);
        this.add(logoProgressBar);

        gamesPane = new JPanel();
        gamesPane.setBackground(TRANSPARENT);
        gamesPane.setLayout(null);

        int panex = -10;
        int paney = 0;
        int increment;
        int nbGames = 0;
        int sizeOccuped = 0;

        for (LauncherGames servGames : LauncherGames.values()) {
            if (servGames == LauncherGames.UNKNOWN) {
                continue;
            }
            sizeOccuped += servGames.getResourceBufferedImage(LauncherResource.AFFICHE).getWidth();
            nbGames++;
        }
        increment = (1130 - sizeOccuped) / (nbGames + 1);
        if (increment < 10) {
            increment = 10;
        } else {
            panex = 0;
        }
        for (LauncherGames servGames : LauncherGames.values()) {
            if (servGames == LauncherGames.UNKNOWN) {
                continue;
            }
            panex += increment;
            LauncherGame servGame = new LauncherGame(this, servGames);
            servGame.getAfficheButton().setBounds(panex, paney);
            panex += servGame.getAfficheButton().getWidth();
            gamesPane.add(servGame.getAfficheButton());
            LauncherFrame.getInstance().getGames().put(servGame.getGame(), servGame);
        }

        gamesPane.setVisible(false);

        if (increment == 10) {
            gamesPane.setPreferredSize(new Dimension(panex, 415));

            gamesScrollPane = new JScrollPane(gamesPane);
            gamesScrollPane.setBounds(new Rectangle(75, 210, 1130, 435));
            gamesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            gamesScrollPane.setBackground(TRANSPARENT);
            gamesScrollPane.setBorder(null);
            gamesScrollPane.getHorizontalScrollBar().setUnitIncrement(5);
            gamesScrollPane.getHorizontalScrollBar().addAdjustmentListener(e -> repaint());

            this.add(gamesScrollPane);
        } else {
            gamesPane.setBounds(new Rectangle(75, 210, 1130, 435));

            this.add(gamesPane);
        }

        gamesPane.setVisible(true);
    }

    @Override
    public void onEvent(SwingerEvent e) {
        if (e.getSource() == quitButton) {
            for (LauncherGame game : LauncherFrame.getInstance().getGames().values()) {
                for (LauncherServer server : game.getServers().values()) {
                    server.getGameLauncher().stopUpdate();
                }
            }
            try {
                UpdaterManager.logoUpdate(false);
            } catch (InterruptedException e1) {
                LauncherFrame.getInstance().getCrashReporter().catchError(e1, "Impossible de stopper la ProgressBar du logo !");
            }

            String showTestsServers = SettingsManager.getSetting(Settings.LAUNCHER_ANIMATION);
            if (Boolean.parseBoolean(showTestsServers)) {
                new Thread(() -> {
                    Animator.fadeOutFrame(LauncherFrame.getInstance(), 2, () -> System.exit(0));
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    System.exit(0);
                }).start();
                return;
            }
            System.exit(0);
        } else if (e.getSource() == hideButton) {
            LauncherFrame.getInstance().setState(JFrame.ICONIFIED);
        } else if (e.getSource() == settingsButton) {
            LauncherFrame.getInstance().getSettingsFrame().display();
        } else if (e.getSource() == backButton) {
            if (launcherStatus.equals(LauncherStatus.SERVERS)) {
                LauncherFrame.getInstance().getGames().get(selectionnedGame).setServersVisible(false);
                setGamesVisible(true);
                launcherStatus = LauncherStatus.GAMES;
                setBackground(LauncherResources.getResourceBufferedImage("background.png"));
            } else if (launcherStatus.equals(LauncherStatus.SERVERINFO)) {
                LauncherFrame.getInstance().getGames().get(selectionnedServer.getGame()).getServers().get(selectionnedServer).getServerInfos().setVisible(false);
                LauncherFrame.getInstance().getGames().get(selectionnedServer.getGame()).setServersVisible(true);
                launcherStatus = LauncherStatus.SERVERS;
                setBackground(selectionnedGame.getResourceBufferedImage(LauncherResource.BACKGROUND));
            }
        } else {
            for (LauncherGame game : LauncherFrame.getInstance().getGames().values()) {
                if (e.getSource() == game.getAfficheButton()) {
                    selectionnedGame = game.getGame();
                    launcherStatus = LauncherStatus.SERVERS;
                    setBackground(selectionnedGame.getResourceBufferedImage(LauncherResource.BACKGROUND));
                    setGamesVisible(false);
                    game.setServersVisible(true);
                }
            }
            for (LauncherServer server : LauncherFrame.getInstance().getGames().get(selectionnedGame).getServers().values()) {
                if (e.getSource() == server.getAfficheButton()) {
                    selectionnedServer = server.getServer();
                    launcherStatus = LauncherStatus.SERVERINFO;
                    setBackground(selectionnedServer.getResourceBufferedImage(LauncherResource.BACKGROUND));
                    LauncherFrame.getInstance().getGames().get(selectionnedGame).setServersVisible(false);
                    server.getServerInfos().setVisible(true);
                } else if (e.getSource() == server.getPlayButton()) {
                    server.getServer().getServerListener().onPlayButton();
                }
            }
        }
        backButton.setVisible(!launcherStatus.equals(LauncherStatus.GAMES));
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2 = (Graphics2D) graphics;

        g2.drawImage(background, 0, 0, null);
        //g2.drawImage(LauncherResources.getResourceBufferedImage("logo_200.png"), 540, 0, null);
    }

    public RoundProgressBarComponent getLogoProgressBar() {
        return logoProgressBar;
    }

    public void setBackground(BufferedImage background) {
        this.background = background;
        this.repaint();
    }

    public void setGamesVisible(boolean visible) {
        if (gamesScrollPane != null) {
            gamesScrollPane.setVisible(visible);
        } else if (gamesPane != null) {
            gamesPane.setVisible(visible);
        }
    }
}