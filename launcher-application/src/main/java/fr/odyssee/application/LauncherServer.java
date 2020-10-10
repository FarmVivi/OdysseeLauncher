package fr.odyssee.application;

import fr.odyssee.application.color.ColorAPI;
import fr.odyssee.application.components.NewsComponent;
import fr.odyssee.application.components.SeparatorComponent;
import fr.odyssee.application.games.GameLauncher;
import fr.odyssee.application.games.minecraft.LauncherMinecraft;
import fr.odyssee.application.resources.LauncherResource;
import fr.odyssee.application.resources.LauncherResources;
import fr.theshark34.swinger.colored.SColoredBar;
import fr.theshark34.swinger.colored.SColoredButton;
import fr.theshark34.swinger.textured.STexturedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

import static fr.theshark34.swinger.Swinger.*;

public class LauncherServer {
    private final LauncherServers server;
    private final STexturedButton afficheButton;
    private final JPanel serverInfos = new JPanel();
    private final SColoredButton playButton = new SColoredButton(ColorAPI.principalColor, ColorAPI.principalColor_hovered, ColorAPI.principalColor);
    private final SColoredBar playButtonProgressBar = new SColoredBar(TRANSPARENT, ColorAPI.ORANGE_CUSTOM);
    private final JLabel playButtonLaunchLabel = new JLabel(" Jouer", new ImageIcon(Objects.requireNonNull(LauncherResources.getResourceBufferedImage("fleche_droite.png"))), JLabel.CENTER);
    private GameLauncher gameLauncher;

    public LauncherServer(LauncherPanel tempPanel, LauncherServers server) {
        this.server = server;
        JLabel afficheButtonLabel = new JLabel(" " + server.getName(), server.getResource(LauncherResource.ICON), JLabel.CENTER);
        afficheButtonLabel.setBounds(0, 0, server.getResource(LauncherResource.AFFICHE).getIconWidth(), 50);
        afficheButtonLabel.setForeground(Color.WHITE);
        afficheButtonLabel.setFont(new Font("Arial", Font.BOLD, 20));
        afficheButton = new STexturedButton(server.getResourceBufferedImage(LauncherResource.AFFICHE), dye(server.getResourceBufferedImage(LauncherResource.AFFICHE), HOVER_COLOR), dye(server.getResourceBufferedImage(LauncherResource.AFFICHE), DISABLED_COLOR));
        afficheButton.setBounds(0, 0, server.getResourceBufferedImage(LauncherResource.AFFICHE).getWidth(), server.getResourceBufferedImage(LauncherResource.AFFICHE).getHeight());
        afficheButton.add(afficheButtonLabel);
        afficheButton.addEventListener(tempPanel);


        serverInfos.setBackground(TRANSPARENT);
        serverInfos.setLayout(null);
        serverInfos.setOpaque(false);

        JLabel affiche = new JLabel(server.getResource(LauncherResource.AFFICHE));
        affiche.setBounds(75, 210, affiche.getIcon().getIconWidth(), affiche.getIcon().getIconHeight());

        JLabel serverName = new JLabel(" " + server.getName(), server.getResource(LauncherResource.ICON), JLabel.CENTER);
        serverName.setBounds(affiche.getX() + affiche.getWidth() + 25, 180, 1280 - 75 - affiche.getX() - affiche.getWidth() - 25, 50);
        serverName.setForeground(Color.WHITE);
        serverName.setFont(new Font("Arial", Font.BOLD, 20));

        playButton.setBounds(affiche.getX() + affiche.getWidth() + 25, 235, 1280 - 75 - affiche.getX() - affiche.getWidth() - 25, 45);
        playButton.addEventListener(tempPanel);

        playButtonLaunchLabel.setBounds(0, 0, playButton.getWidth(), playButton.getHeight());
        playButtonLaunchLabel.setForeground(Color.WHITE);
        playButtonLaunchLabel.setFont(new Font("Arial", Font.BOLD, 20));
        playButton.add(playButtonLaunchLabel);

        playButtonProgressBar.setBounds(0, playButton.getHeight() - 2, playButton.getWidth(), 2);
        playButton.add(playButtonProgressBar);

        SeparatorComponent newsSeparator = new SeparatorComponent("Actualités");
        newsSeparator.setBounds(affiche.getX() + affiche.getWidth() + 25, 295, 1280 - 75 - affiche.getX() - affiche.getWidth() - 25, 35);
        serverInfos.add(newsSeparator);

        new Thread(() -> {
            NewsComponent news1 = new NewsComponent(null, "Nouveau launcher !", null, "FarmVivi", "    À l'occasion de la sortis des nouveaux serveurs de Otago, le launcher est ici !");
            news1.setBounds(affiche.getX() + affiche.getWidth() + 25 + 10, 340, 1280 - 75 - affiche.getX() - affiche.getWidth() - 25 - 20, 100);
            serverInfos.add(news1);
        }).start();

        serverInfos.add(serverName);
        serverInfos.add(affiche);
        serverInfos.add(playButton);
        serverInfos.setVisible(false);
        serverInfos.setBounds(0, 0, 1280, 720);
        tempPanel.add(serverInfos);

        //noinspection SwitchStatementWithTooFewBranches
        switch (server.getGame()) {
            case MINECRAFT:
                gameLauncher = new LauncherMinecraft(server, server.getGameLauncherData().getVERSION(), server.getGameLauncherData().getTWEAK());
                break;
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

    public LauncherServers getServer() {
        return server;
    }

    public STexturedButton getAfficheButton() {
        return afficheButton;
    }

    public JPanel getServerInfos() {
        return serverInfos;
    }

    public SColoredButton getPlayButton() {
        return playButton;
    }

    public SColoredBar getPlayButtonProgressBar() {
        return playButtonProgressBar;
    }

    public JLabel getPlayButtonLaunchLabel() {
        return playButtonLaunchLabel;
    }

    public GameLauncher getGameLauncher() {
        return gameLauncher;
    }

    public void setGameLauncher(GameLauncher gameLauncher) {
        this.gameLauncher = gameLauncher;
    }

    public void refreshNews() {

    }
}
