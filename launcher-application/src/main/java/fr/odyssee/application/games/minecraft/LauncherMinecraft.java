package fr.odyssee.application.games.minecraft;

import fr.odyssee.application.LauncherFrame;
import fr.odyssee.application.LauncherServers;
import fr.odyssee.application.Main;
import fr.odyssee.application.games.GameLauncher;
import fr.odyssee.application.resources.LauncherResources;
import fr.odyssee.application.settings.Settings;
import fr.odyssee.application.settings.SettingsManager;
import fr.odyssee.application.updater.UpdaterManager;
import fr.odyssee.application.updater.UpdaterTask;
import fr.odyssee.application.updater.UpdaterType;
import fr.odyssee.common.AppDataDir;
import fr.odyssee.common.maths.DetermineIsNumber;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.openlauncherlib.util.ProcessLogManager;
import fr.theshark34.supdate.application.integrated.FileDeleter;
import fr.theshark34.swinger.Swinger;

import javax.swing.*;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LauncherMinecraft extends GameLauncher {
    //TODO Faire que lon ne doit pas a chaque fois retélécharger les assets ect...

    private static final String base_url = "http://odyssee.avadia.fr/launcher/games/";

    private final LauncherServers SERVER;
    private final GameInfos INFOS;
    private final File DIR;
    private final ScheduledExecutorService update = Executors.newScheduledThreadPool(1);
    private final ScheduledExecutorService updateConfig = Executors.newScheduledThreadPool(1);
    private final CrashReporter CRASHES_REPORTER;
    private AuthInfos authInfos;
    private UUID updater;
    private UUID updaterConfig;

    public LauncherMinecraft(LauncherServers SERVER, GameVersion VERSION, GameTweak TWEAK) {
        this.SERVER = SERVER;
        this.INFOS = new GameInfos(SERVER.getName(), new File(AppDataDir.createGameDir(Main.name), "games/" + SERVER.getGame().getResourcename() + "/" + SERVER.getResourcename()), VERSION, new GameTweak[]{TWEAK});
        this.DIR = INFOS.getGameDir();
        //noinspection ResultOfMethodCallIgnored
        DIR.mkdirs();
        File CRASHES_DIR = new File(DIR, "crashes");
        //noinspection ResultOfMethodCallIgnored
        CRASHES_DIR.mkdirs();
        this.CRASHES_REPORTER = new CrashReporter(SERVER.getName(), CRASHES_DIR);
    }

    @Override
    public void update() throws Exception {
        /*
         *
         *
         * Updater Files
         *
         *
         * */

        Main.logger.info("Mise à jour de " + SERVER.getGame().getName() + " sur le serveur " + SERVER.getName() + " en cours...");

        Icon loading = LauncherResources.getResource("loading.gif");
        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setIcon(loading);
        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setText(" Connexion au serveur...");
        updater = UpdaterManager.addUpdater(new UpdaterTask(base_url + SERVER.getGame().getResourcename() + "/" + SERVER.getResourcename() + "/updater/fichiers/", DIR, SERVER, UpdaterType.FILES));
        UpdaterManager.getUpdater(updater).getSUpdate().addApplication(new FileDeleter());

        Runnable updateRunnable = () -> {
            switch (UpdaterManager.getUpdater(updater).getSUpdate().getStatus()) {
                case NONE:
                    LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setText(" Veuillez patienter...");
                    break;
                case CONNECTING:
                    LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setText(" Connexion...");
                    break;
                case CHECKING:
                    LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setText(" Vérifications...");
                    break;
                case CHECKINGFILES:
                    if (UpdaterManager.getUpdater(updater).getSUpdate().getBarAPI().getNumberOfFileToCheck() == 0) {
                        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setText(" Vérification des fichiers...");
                    } else {
                        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setText(" Vérification des fichiers... (" + UpdaterManager.getUpdater(updater).getSUpdate().getBarAPI().getNumberOfCheckedFiles() + "/" + UpdaterManager.getUpdater(updater).getSUpdate().getBarAPI().getNumberOfFileToCheck() + ")");
                        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonProgressBar().setMaximum(UpdaterManager.getUpdater(updater).getSUpdate().getBarAPI().getNumberOfFileToCheck());
                        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonProgressBar().setValue(UpdaterManager.getUpdater(updater).getSUpdate().getBarAPI().getNumberOfCheckedFiles());
                    }
                    break;
                case DOWNLOADINGFILES:
                    if (UpdaterManager.getUpdater(updater).getSUpdate().getBarAPI().getNumberOfFileToDownload() == 0) {
                        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setText(" Veuillez patienter...");
                    } else {
                        int val = (int) (UpdaterManager.getUpdater(updater).getSUpdate().getBarAPI().getNumberOfTotalDownloadedBytes() / 1000);
                        int max = (int) (UpdaterManager.getUpdater(updater).getSUpdate().getBarAPI().getNumberOfTotalBytesToDownload() / 1000);

                        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setText(" Mise à jour des fichiers... (" + UpdaterManager.getUpdater(updater).getSUpdate().getBarAPI().getNumberOfDownloadedFiles() + "/" + UpdaterManager.getUpdater(updater).getSUpdate().getBarAPI().getNumberOfFileToDownload() + " | " + Swinger.percentage(val, max) + "%)");
                        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonProgressBar().setMaximum(max);
                        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonProgressBar().setValue(val);
                    }

                    break;
            }
        };
        update.scheduleAtFixedRate(updateRunnable, 0, 50, TimeUnit.MILLISECONDS);
        UpdaterManager.getUpdater(updater).getSUpdate().getUpdater().start(true);
        update.shutdown();
        update.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        updateRunnable.run();
        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonProgressBar().setValue(0);
        UpdaterManager.removeUpdater(updater);
        updater = null;

        /*
         *
         *
         * Updater Config
         *
         *
         * */

        Main.logger.info("Mise à jour des configurations de " + SERVER.getGame().getName() + " sur le serveur " + SERVER.getName() + " en cours...");
        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setText(" Connexion au serveur de configuration...");
        updaterConfig = UpdaterManager.addUpdater(new UpdaterTask(base_url + SERVER.getGame().getResourcename() + "/" + SERVER.getResourcename() + "/updater/config/", DIR, SERVER, UpdaterType.CONFIGFILES));

        Runnable updateConfigRunnable = () -> {
            switch (UpdaterManager.getUpdater(updaterConfig).getSUpdate().getStatus()) {
                case NONE:
                    LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setText(" Veuillez patienter...");
                    break;
                case CONNECTING:
                    LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setText(" Connexion...");
                    break;
                case CHECKING:
                    LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setText(" Vérifications...");
                    break;
                case CHECKINGFILES:
                    if (UpdaterManager.getUpdater(updaterConfig).getSUpdate().getBarAPI().getNumberOfFileToCheck() == 0) {
                        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setText(" Vérification des configurations...");
                    } else {
                        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setText(" Vérification des configurations... (" + UpdaterManager.getUpdater(updaterConfig).getSUpdate().getBarAPI().getNumberOfCheckedFiles() + "/" + UpdaterManager.getUpdater(updaterConfig).getSUpdate().getBarAPI().getNumberOfFileToCheck() + ")");
                        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonProgressBar().setMaximum(UpdaterManager.getUpdater(updaterConfig).getSUpdate().getBarAPI().getNumberOfFileToCheck());
                        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonProgressBar().setValue(UpdaterManager.getUpdater(updaterConfig).getSUpdate().getBarAPI().getNumberOfCheckedFiles());
                    }
                    break;
                case DOWNLOADINGFILES:
                    if (UpdaterManager.getUpdater(updaterConfig).getSUpdate().getBarAPI().getNumberOfFileToDownload() == 0) {
                        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setText(" Veuillez patienter...");
                    } else {
                        int val = (int) (UpdaterManager.getUpdater(updaterConfig).getSUpdate().getBarAPI().getNumberOfTotalDownloadedBytes() / 1000);
                        int max = (int) (UpdaterManager.getUpdater(updaterConfig).getSUpdate().getBarAPI().getNumberOfTotalBytesToDownload() / 1000);

                        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setText(" Mise à jour des configurations... (" + UpdaterManager.getUpdater(updaterConfig).getSUpdate().getBarAPI().getNumberOfDownloadedFiles() + "/" + UpdaterManager.getUpdater(updaterConfig).getSUpdate().getBarAPI().getNumberOfFileToDownload() + " | " + Swinger.percentage(val, max) + "%)");
                        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonProgressBar().setMaximum(max);
                        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonProgressBar().setValue(val);
                    }

                    break;
            }
        };
        updateConfig.scheduleAtFixedRate(updateConfigRunnable, 0, 50, TimeUnit.MILLISECONDS);
        UpdaterManager.getUpdater(updaterConfig).getSUpdate().getUpdater().start(false);
        updateConfig.shutdown();
        updateConfig.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        updateConfigRunnable.run();
        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonProgressBar().setValue(0);
        UpdaterManager.removeUpdater(updaterConfig);
        updaterConfig = null;
    }

    @Override
    public void launch() throws LaunchException {
        Main.logger.info("Lancement de " + SERVER.getGame().getName() + " sur le serveur " + SERVER.getName() + " en cours...");

        Icon loading = LauncherResources.getResource("loading.gif");
        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setIcon(loading);
        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setText(" Lancement du jeu...");
        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonProgressBar().setValue(0);
        ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(INFOS, GameFolder.BASIC, authInfos);
        if (SettingsManager.getSetting(Settings.MINECRAFT_RAM) == null
                || !DetermineIsNumber.isInteger(SettingsManager.getSetting(Settings.MINECRAFT_RAM))) {
            SettingsManager.setSetting(Settings.MINECRAFT_RAM, "1024");
        }

        List<String> args = new ArrayList<>();
        long freemem = ((com.sun.management.OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean()).getFreePhysicalMemorySize();
        Main.logger.info("RAM disponible: " + freemem);
        long launchmem = (Integer.parseInt(SettingsManager.getSetting(Settings.MINECRAFT_RAM)) - 512) * 1000000;
        Main.logger.info("RAM nécessaire: " + launchmem);
        if (freemem > launchmem) {
            args.add("-Xms" + (Integer.parseInt(SettingsManager.getSetting(Settings.MINECRAFT_RAM)) - 512) + "M");
            args.add("-Xmx" + SettingsManager.getSetting(Settings.MINECRAFT_RAM) + "M");
        } else {
            JOptionPane.showMessageDialog(LauncherFrame.getInstance(), "Erreur, vous ne pouvez pas lancer le jeu avec autant de ram.");
        }
        profile.getVmArgs().addAll(args);
        ExternalLauncher launcher = new ExternalLauncher(profile);

        LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setText(" Lancement du jeu...");
        Process p = launcher.launch();

        ProcessLogManager manager = new ProcessLogManager("Minecraft | " + SERVER.getName(), p.getInputStream(), new File(DIR, "logs.txt"));
        manager.start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Boolean.parseBoolean(SettingsManager.getSetting(Settings.LAUNCHER_CLOSEAFTERLAUNCHINGOFAGAME))) {
                System.exit(0);
            }
            LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButton().setEnabled(true);
            LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setIcon(LauncherResources.getResource("fleche_droite.png"));
            LauncherFrame.getInstance().getGames().get(SERVER.getGame()).getServers().get(SERVER).getPlayButtonLaunchLabel().setText(" Jouer");
            try {
                TimeUnit.MINUTES.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }).start();
    }

    @Override
    public void stopUpdate() {
        update.shutdown();
        if (updater != null && UpdaterManager.getUpdater(updater) != null) {
            UpdaterManager.getUpdater(updater).getSUpdate().getFileManager().stopNow();
        }
        updateConfig.shutdown();
        if (updaterConfig != null && UpdaterManager.getUpdater(updaterConfig) != null) {
            UpdaterManager.getUpdater(updaterConfig).getSUpdate().getFileManager().stopNow();
        }
    }

    @Override
    public CrashReporter getCrashReporter() {
        return CRASHES_REPORTER;
    }

    @Override
    public AuthInfos getAuthInfos() {
        return authInfos;
    }

    @Override
    public void setAuthInfos(AuthInfos authInfos) {
        this.authInfos = authInfos;
    }
}
