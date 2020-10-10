package fr.odyssee.application;

import fr.odyssee.application.resources.LauncherResource;
import fr.odyssee.application.settings.Settings;
import fr.odyssee.application.settings.SettingsManager;
import fr.theshark34.swinger.textured.STexturedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import static fr.theshark34.swinger.Swinger.*;

public class LauncherGame {
    private final LauncherGames game;

    private final Map<LauncherServers, LauncherServer> servers = new HashMap<>();

    private final STexturedButton afficheButton;
    private final JPanel serversPane;
    private JScrollPane serversScrollPane;

    public LauncherGame(LauncherPanel tempPanel, LauncherGames game) {
        this.game = game;
        JLabel afficheButtonLabel = new JLabel(" " + game.getName(), game.getResource(LauncherResource.ICON), JLabel.CENTER);
        afficheButtonLabel.setBounds(0, 0, game.getResource(LauncherResource.AFFICHE).getIconWidth(), 50);
        afficheButtonLabel.setForeground(Color.WHITE);
        afficheButtonLabel.setFont(new Font("Arial", Font.BOLD, 20));
        afficheButton = new STexturedButton(game.getResourceBufferedImage(LauncherResource.AFFICHE), dye(game.getResourceBufferedImage(LauncherResource.AFFICHE), HOVER_COLOR), dye(game.getResourceBufferedImage(LauncherResource.AFFICHE), DISABLED_COLOR));
        afficheButton.setBounds(0, 0, game.getResourceBufferedImage(LauncherResource.AFFICHE).getWidth(), game.getResourceBufferedImage(LauncherResource.AFFICHE).getHeight());
        afficheButton.add(afficheButtonLabel);
        afficheButton.addEventListener(tempPanel);

        serversPane = new JPanel();
        serversPane.setBackground(TRANSPARENT);
        serversPane.setLayout(null);

        int panex = -10;
        int paney = 0;
        int increment;
        int nbServers = 0;
        int sizeOccuped = 0;

        for (LauncherServers servServers : LauncherServers.values()) {
            if (servServers.getGame().equals(game)) {
                String showTestsServers = SettingsManager.getSetting(Settings.TESTS_SERVERS);
                if (!Boolean.parseBoolean(showTestsServers)) {
                    if (servServers.name().toLowerCase().contains("test")) {
                        continue;
                    }
                }
                sizeOccuped += servServers.getResourceBufferedImage(LauncherResource.AFFICHE).getWidth();
                nbServers++;
            }
        }
        increment = (1130 - sizeOccuped) / (nbServers + 1);
        if (increment < 10) {
            increment = 10;
        } else {
            panex = 0;
        }
        for (LauncherServers servServers : LauncherServers.values()) {
            if (servServers == LauncherServers.DEFAULT) {
                continue;
            }
            if (servServers.getGame().equals(game)) {
                String showTestsServers = SettingsManager.getSetting(Settings.TESTS_SERVERS);
                if (!Boolean.parseBoolean(showTestsServers)) {
                    if (servServers.name().toLowerCase().contains("test")) {
                        continue;
                    }
                }
                panex += increment;
                LauncherServer servServer = new LauncherServer(tempPanel, servServers);
                servServer.getAfficheButton().setBounds(panex, paney);
                servServer.getAfficheButton().addEventListener(tempPanel);
                panex += servServer.getAfficheButton().getWidth();
                serversPane.add(servServer.getAfficheButton());
                servers.put(servServers, servServer);
            }
        }

        if (increment == 10) {
            serversPane.setPreferredSize(new Dimension(panex, 415));

            serversScrollPane = new JScrollPane(serversPane);
            serversScrollPane.setBounds(new Rectangle(75, 210, 1130, 435));
            serversScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            serversScrollPane.setBackground(TRANSPARENT);
            serversScrollPane.setBorder(null);
            serversScrollPane.getHorizontalScrollBar().setUnitIncrement(5);
            serversScrollPane.getHorizontalScrollBar().addAdjustmentListener(e -> LauncherFrame.getInstance().getLauncherPanel().repaint());

            serversScrollPane.setVisible(false);
            tempPanel.add(serversScrollPane);
        } else {
            serversPane.setBounds(new Rectangle(75, 210, 1130, 435));
            serversPane.setVisible(false);

            tempPanel.add(serversPane);
        }
    }

    private static BufferedImage dye(BufferedImage image, Color color) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage dyed = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dyed.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.setComposite(AlphaComposite.SrcAtop);
        g.setColor(color);
        g.fillRect(0, 0, w, h);
        g.dispose();
        return dyed;
    }

    public LauncherGames getGame() {
        return game;
    }

    public STexturedButton getAfficheButton() {
        return afficheButton;
    }

    public Map<LauncherServers, LauncherServer> getServers() {
        return servers;
    }

    public void setServersVisible(boolean visible) {
        if (serversScrollPane != null) {
            serversScrollPane.setVisible(visible);
        } else if (serversPane != null) {
            serversPane.setVisible(visible);
        }
    }
}
