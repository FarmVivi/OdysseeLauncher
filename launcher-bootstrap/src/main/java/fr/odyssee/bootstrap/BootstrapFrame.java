package fr.odyssee.bootstrap;

import fr.odyssee.bootstrap.parameters.Parameters;
import fr.odyssee.bootstrap.parameters.ParametersManager;
import fr.odyssee.bootstrap.parameters.UnableToGetParameterException;
import fr.odyssee.common.AppDataDir;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ClasspathConstructor;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.openlauncherlib.util.explorer.ExploredDirectory;
import fr.theshark34.openlauncherlib.util.explorer.Explorer;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.application.integrated.FileDeleter;
import fr.theshark34.supdate.application.integrated.FolderDeleter;
import fr.theshark34.supdate.exception.BadServerResponseException;
import fr.theshark34.supdate.exception.BadServerVersionException;
import fr.theshark34.supdate.exception.ServerDisabledException;
import fr.theshark34.supdate.exception.ServerMissingSomethingException;
import fr.theshark34.swinger.util.WindowMover;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static fr.theshark34.swinger.Swinger.TRANSPARENT;
import static fr.theshark34.swinger.Swinger.getResource;

public class BootstrapFrame extends JFrame {
    public static final int interval = 10;

    private static BootstrapFrame instance;
    private final BootstrapPanel bootstrapPanel;

    private final File DIR = AppDataDir.createGameDir(Main.name);
    private final CrashReporter crashReporter = new CrashReporter(Main.name + " | Bootstrap", new File(DIR, "launcher/crashes/"));
    private final ScheduledExecutorService update = Executors.newScheduledThreadPool(1);
    private final String[] args;

    public BootstrapFrame(String[] args) {
        this.args = args;
        this.setTitle(Main.name + " - V" + Main.version);
        this.setSize(getResource("Splash.png").getWidth() + interval * 2, getResource("Splash.png").getHeight() + interval * 2);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);
        this.setIconImage(getResource("logo_64.png"));
        this.setContentPane(bootstrapPanel = new BootstrapPanel(this));
        this.setBackground(TRANSPARENT);

        WindowMover mover = new WindowMover(this);
        this.addMouseListener(mover);
        this.addMouseMotionListener(mover);

        this.setVisible(true);
    }

    public static BootstrapFrame getInstance() {
        return instance;
    }

    public static void setInstance(BootstrapFrame instance) {
        BootstrapFrame.instance = instance;
    }

    public void doUpdate() throws BadServerResponseException, IOException, BadServerVersionException, ServerDisabledException, ServerMissingSomethingException, InterruptedException, UnableToGetParameterException {
        File LAUNCHER_DIR = new File(DIR, "launcher");
        if (!LAUNCHER_DIR.exists()) {
            //noinspection ResultOfMethodCallIgnored
            LAUNCHER_DIR.mkdirs();
        }
        SUpdate updater = new SUpdate(ParametersManager.getParameter(Parameters.UPDATER_LINK), LAUNCHER_DIR);
        updater.addApplication(new FileDeleter());
        updater.addApplication(new FolderDeleter());

        boolean firstInstall = false;
        File ASSETS_DIR = new File(LAUNCHER_DIR, "assets");
        if (!ASSETS_DIR.exists()) {
            firstInstall = true;
        }

        boolean finalFirstInstall = firstInstall;

        Runnable updateRunnable = () -> {
            switch (updater.getStatus()) {
                case NONE:
                    bootstrapPanel.getInfo().setText("Veuillez patienter...");
                    break;
                case CONNECTING:
                    bootstrapPanel.getLoading().setIcon(bootstrapPanel.getLoading_2_red());
                    bootstrapPanel.getInfo().setText("Connexion... (1/2)");
                    break;
                case CHECKING:
                    bootstrapPanel.getLoading().setIcon(bootstrapPanel.getLoading_2_red_fast());
                    bootstrapPanel.getInfo().setText("Vérifications...");
                    break;
                case CHECKINGFILES:
                    bootstrapPanel.getLoading().setIcon(bootstrapPanel.getLoading_2_red_fast());
                    if (updater.getBarAPI().getNumberOfFileToCheck() == 0) {
                        bootstrapPanel.getInfo().setText("Vérification...");
                    } else {
                        bootstrapPanel.getInfo().setText("Vérification... (" + updater.getBarAPI().getNumberOfCheckedFiles() + "/" + updater.getBarAPI().getNumberOfFileToCheck() + ")");
                    }
                    break;
                case DOWNLOADINGFILES:
                    bootstrapPanel.getLoading().setIcon(bootstrapPanel.getLoading_2_red_fast());
                    if (finalFirstInstall) {
                        bootstrapPanel.getInfo().setText("Installation...");
                    } else {
                        bootstrapPanel.getInfo().setText("Mise à jour...");
                    }

                    if (updater.getBarAPI().getNumberOfFileToDownload() != 0) {
                        int val = (int) (updater.getBarAPI().getNumberOfTotalDownloadedBytes() / 1000);
                        int max = (int) (updater.getBarAPI().getNumberOfTotalBytesToDownload() / 1000);

                        bootstrapPanel.getLogoProgressBar().setValue(val);
                        bootstrapPanel.getLogoProgressBar().setMaximum(max);
                    }

                    break;
            }
        };
        update.scheduleAtFixedRate(updateRunnable, 0, 50, TimeUnit.MILLISECONDS);
        updater.getUpdater().start(true);
        update.shutdown();
        update.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        updateRunnable.run();
    }

    public void launchLauncher() throws LaunchException, UnableToGetParameterException {
        bootstrapPanel.getInfo().setText("Lancement...");
        bootstrapPanel.getLoading().setIcon(bootstrapPanel.getLoading_2_red_fast());
        ClasspathConstructor constructor = new ClasspathConstructor();
        ExploredDirectory gameDir = Explorer.dir(new File(DIR, "launcher"));
        constructor.add(gameDir.sub("assets/libraries").allRecursive().files().match("^(.*\\.((jar)$))*$"));
        constructor.add(gameDir.get("launcher.jar"));

        ExternalLaunchProfile profile = new ExternalLaunchProfile(ParametersManager.getParameter(Parameters.LAUNCHER_MAIN_CLASS), constructor.make());
        if (args.length > 0)
            profile.setArgs(Arrays.asList(args));
        ExternalLauncher launcher = new ExternalLauncher(profile);

        Process p = launcher.launch();

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (args.length > 0 && Arrays.asList(args).contains("waitlauncher")) {
            this.setVisible(false);
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                crashReporter.catchError(e, "Impossible d'attendre la fermeture du launcher pour fermer le bootstrap !");
            }
        }
        System.exit(0);
    }

    public CrashReporter getCrashReporter() {
        return crashReporter;
    }

    public BootstrapPanel getBootstrapPanel() {
        return bootstrapPanel;
    }
}
