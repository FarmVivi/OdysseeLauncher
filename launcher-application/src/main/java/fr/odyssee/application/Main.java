package fr.odyssee.application;

import fr.odyssee.application.settings.SettingsManager;
import fr.odyssee.common.AppDataDir;
import fr.odyssee.common.OperatingSystem;
import fr.theshark34.openlauncherlib.LanguageManager;
import fr.theshark34.swinger.Swinger;
import net.wytream.wylog.BasicLogger;
import net.wytream.wylog.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

public class Main {
    //TODO Make only one SUpdateServer for all files to updater
    public static final String version = "1.0.0.0";
    public static final String name = "Odyssee";

    public static final BasicLogger logger = LoggerFactory.getLogger(name + " | Launcher");

    public static void main(String[] args) {
        File DIR = AppDataDir.createGameDir(name);
        File LOGS_DIR = new File(DIR, "launcher/logs");
        if (!LOGS_DIR.exists()) {
            //noinspection ResultOfMethodCallIgnored
            LOGS_DIR.mkdirs();
        }
        File logs = new File(LOGS_DIR, "launcher.txt");

        if (!(args.length > 0 && Arrays.asList(args).contains("noredirectlogs"))) {
            try {
                System.setOut(new PrintStream(logs));
                System.setErr(new PrintStream(logs));
            } catch (FileNotFoundException e) {
                logger.error("Impossible d'output les logs !", e);
                e.printStackTrace();
            }
        }

        try {
            LoggerFactory.addSharedFileHandler(logs);
        } catch (IOException e) {
            logger.error("Impossible de dupliquer les logs dans le fichier !", e);
            e.printStackTrace();
        }

        logger.info("Démarrage du launcher " + name + " en cours sur " + OperatingSystem.getCurrentPlatform().getName() + "...");

        logger.info("System.getProperty('os.name') == '" + System.getProperty("os.name") + "'");
        logger.info("System.getProperty('os.version') == '" + System.getProperty("os.version") + "'");
        logger.info("System.getProperty('os.arch') == '" + System.getProperty("os.arch") + "'");
        logger.info("System.getProperty('java.version') == '" + System.getProperty("java.version") + "'");
        logger.info("System.getProperty('java.vendor') == '" + System.getProperty("java.vendor") + "'");
        logger.info("System.getProperty('sun.arch.data.model') == '" + System.getProperty("sun.arch.data.model") + "'");


        LanguageManager.setLang(LanguageManager.FRENCH);

        Swinger.setSystemLookNFeel();

        SettingsManager.setupSaver();

        new LauncherFrame();
    }
}
