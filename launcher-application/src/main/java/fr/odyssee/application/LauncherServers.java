package fr.odyssee.application;

import fr.odyssee.application.games.GameLauncher;
import fr.odyssee.application.games.GameLauncherData;
import fr.odyssee.application.games.minecraft.LauncherMinecraftData;
import fr.odyssee.application.login.LoginListener;
import fr.odyssee.application.resources.LauncherResource;
import fr.odyssee.application.settings.Settings;
import fr.odyssee.application.settings.SettingsManager;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.GameTweak;
import fr.theshark34.openlauncherlib.minecraft.GameType;
import fr.theshark34.openlauncherlib.minecraft.GameVersion;
import fr.theshark34.openlauncherlib.util.Saver;

import javax.swing.*;
import java.awt.image.BufferedImage;

public enum LauncherServers {
    DEFAULT(LauncherGames.UNKNOWN, "Inconnu", "unknown", null, null),
    MINECRAFT_FACTION(LauncherGames.MINECRAFT, "Odyssée | Faction", "faction", () -> {
        GameLauncher gameLauncher = LauncherFrame.getInstance().getGames().get(LauncherGames.MINECRAFT).getServers().get(LauncherServers.valueOf("MINECRAFT_FACTION")).getGameLauncher();
        LauncherFrame.getInstance().getGames().get(LauncherGames.MINECRAFT).getServers().get(LauncherServers.valueOf("MINECRAFT_FACTION")).getPlayButton().setEnabled(false);
        LauncherFrame.getInstance().getLoginFrame().display(Main.name + " - Pseudonyme", defaultLoginLostener(gameLauncher, LauncherServers.valueOf("MINECRAFT_FACTION")), SettingsManager.getSaver(), "Choisissez un pseudonyme", false);
    }, new LauncherMinecraftData(new GameVersion("1.7.10", GameType.V1_7_10), GameTweak.FORGE)),
    TEST_MINECRAFT(LauncherGames.MINECRAFT, "Odyssée | TEST", "test", () -> {
        JOptionPane.showMessageDialog(LauncherFrame.getInstance(), "Indisponible pour le moment...", Main.name + " | Launcher", JOptionPane.INFORMATION_MESSAGE);
//            GameLauncher gameLauncher = LauncherFrame.getInstance().getGames().get(LauncherGames.MINECRAFT).getServers().get(TEST_MINECRAFT).getGameLauncher();
//            LauncherFrame.getInstance().getGames().get(LauncherGames.MINECRAFT).getServers().get(TEST_MINECRAFT).getPlayButton().setEnabled(false);
//            LauncherFrame.getInstance().getLoginFrame().display(Main.name + " - Pseudonyme", defaultLoginLostener(gameLauncher, TEST_MINECRAFT), SettingsManager.getSaver(), "Choisissez un pseudonyme", false);
    }, new LauncherMinecraftData(new GameVersion("1.7.10", GameType.V1_7_10), GameTweak.FORGE));

    private final LauncherGames game;
    private final String name;
    private final String resourcename;
    private final ServerListener serverListener;
    private final GameLauncherData gameLauncherData;

    LauncherServers(LauncherGames game, String name, String resourcename, ServerListener serverListener, GameLauncherData gameLauncherData) {
        this.game = game;
        this.name = name;
        this.resourcename = resourcename;
        this.serverListener = serverListener;
        this.gameLauncherData = gameLauncherData;
    }

    public static LoginListener defaultLoginLostener(GameLauncher gameLauncher, LauncherServers server) {
        return new LoginListener() {
            @Override
            public boolean login(JFrame frame, String username, String password, Saver saver) {
                return true;
            }

            @Override
            public void onSuccessLogin(String username, String password, Saver saver) {
                gameLauncher.setAuthInfos(new AuthInfos(username, "sry", "nope"));
                saver.set(Settings.USERNAME.getName(), username);
                if (password != null && !password.equals(""))
                    saver.set(Settings.PASSWORD.getName(), password);
                saver.save();

                new Thread(() -> {
                    try {
                        gameLauncher.update();
                    } catch (Exception e1) {
                        gameLauncher.stopUpdate();
                        gameLauncher.getCrashReporter().catchError(e1, "Impossible de mettre le jeu à jour !");
                        return;
                    }

                    try {
                        gameLauncher.launch();
                    } catch (LaunchException e1) {
                        gameLauncher.getCrashReporter().catchError(e1, "Impossible de lancer le jeu !");
                    }
                }).start();

                LauncherFrame.getInstance().getGames().get(LauncherGames.MINECRAFT).getServers().get(server).getPlayButton().setEnabled(false);
            }

            @Override
            public void onFailedLogin() {

            }

            @Override
            public void onCancel() {
                LauncherFrame.getInstance().getGames().get(LauncherGames.MINECRAFT).getServers().get(server).getPlayButton().setEnabled(true);
            }
        };
    }

    public LauncherGames getGame() {
        return game;
    }

    public String getName() {
        return name;
    }

    public ImageIcon getResource(LauncherResource resource) {
        return game.getResource("servers/" + resourcename, resource);
    }

    public BufferedImage getResourceBufferedImage(LauncherResource resource) {
        return game.getResourceBufferedImage("servers/" + resourcename, resource);
    }

    public ServerListener getServerListener() {
        return serverListener;
    }

    public String getResourcename() {
        return resourcename;
    }

    public GameLauncherData getGameLauncherData() {
        return gameLauncherData;
    }
}
