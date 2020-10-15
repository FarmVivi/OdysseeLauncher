package fr.odyssee.bootstrap;

import fr.odyssee.bootstrap.parameters.Parameters;
import fr.odyssee.bootstrap.parameters.ParametersManager;
import fr.odyssee.bootstrap.parameters.UnableToGetParameterException;
import fr.odyssee.common.AppDataDir;
import fr.odyssee.common.OperatingSystem;
import fr.odyssee.common.maths.DetermineIsNumber;
import fr.theshark34.openlauncherlib.LanguageManager;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.supdate.exception.BadServerResponseException;
import fr.theshark34.supdate.exception.BadServerVersionException;
import fr.theshark34.supdate.exception.ServerDisabledException;
import fr.theshark34.supdate.exception.ServerMissingSomethingException;
import fr.theshark34.swinger.Swinger;
import net.wytream.wylog.BasicLogger;
import net.wytream.wylog.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static fr.theshark34.swinger.Swinger.setResourcePath;

public class Main {
    public static final String version = "1.0.0.0";
    public static final boolean dev = true;
    public static final String name = "Odyssee";
    public static final String version_check_url = "http://manifest.odyssee.avadia.fr/v1/";
    public static final String server_url = "http://odyssee.avadia.fr/";

    public static final BasicLogger logger = LoggerFactory.getLogger(name + " | Bootstrap");

    public static void main(String[] args) {
        File DIR = AppDataDir.createGameDir(name);
        File LOGS_DIR = new File(DIR, "launcher/logs");
        if (!LOGS_DIR.exists()) {
            //noinspection ResultOfMethodCallIgnored
            LOGS_DIR.mkdirs();
        }
        File logs = new File(LOGS_DIR, "bootstrap.txt");

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

        logger.info("Démarrage du bootstrap " + name + " (V" + version + ") en cours sur " + OperatingSystem.getCurrentPlatform().getName() + "...");

        logger.info("System.getProperty('os.name') == '" + System.getProperty("os.name") + "'");
        logger.info("System.getProperty('os.version') == '" + System.getProperty("os.version") + "'");
        logger.info("System.getProperty('os.arch') == '" + System.getProperty("os.arch") + "'");
        logger.info("System.getProperty('java.version') == '" + System.getProperty("java.version") + "'");
        logger.info("System.getProperty('java.vendor') == '" + System.getProperty("java.vendor") + "'");
        logger.info("System.getProperty('sun.arch.data.model') == '" + System.getProperty("sun.arch.data.model") + "'");
        logger.info("System.getProperty('http.agent') == '" + System.getProperty("http.agent") + "'");
        System.setProperty("http.agent", "Chrome");

        LanguageManager.setLang(LanguageManager.FRENCH);

        Swinger.setSystemLookNFeel();

        setResourcePath("/");

        logger.info("Setup de l'instance...");
        BootstrapFrame.setInstance(new BootstrapFrame(args));
        logger.info("Instance setup !");

        BootstrapFrame.getInstance().getBootstrapPanel().getInfo().setText("Connexion... (0/2)");

        logger.info("Vérification des informations du bootstrap en ligne...");
        try {
            URL url;
            if (dev) {
                url = new URL(version_check_url + "dev/bootstrap_infos.txt");
            } else {
                url = new URL(version_check_url + "bootstrap_infos.txt");
            }

            // read text returned by server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains(": ")) {
                    for (Parameters parameter : Parameters.values()) {
                        if (parameter.getName().equals(line.split(": ", 2)[0])) {
                            ParametersManager.addParameter(parameter, line.split(": ", 2)[1]);
                        }
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            BootstrapFrame.getInstance().getCrashReporter().catchError(e, "Vérifiez votre connexion internet ou essayez de retélécharger le launcher sur " + server_url + " !");
            System.exit(0);
            return;
        }

        logger.info("Vérification de mise à jour du bootstrap...");
        String[] latestVersionT;
        try {
            latestVersionT = ParametersManager.getParameter(Parameters.LATEST_VERSION).split("\\.");
        } catch (UnableToGetParameterException e) {
            BootstrapFrame.getInstance().getCrashReporter().catchError(e, "Impossible d'identifier la dernière version");
            System.exit(0);
            return;
        }
        String[] thisVersionT = version.split("\\.");
        StringBuilder latestVersionS = new StringBuilder();
        StringBuilder thisVersionS = new StringBuilder();

        for (String latestV : latestVersionT) {
            latestVersionS.append(latestV);
        }
        for (String thisV : thisVersionT) {
            thisVersionS.append(thisV);
        }

        if (!DetermineIsNumber.isInteger(latestVersionS.toString())
                || !DetermineIsNumber.isInteger(thisVersionS.toString())) {
            logger.info("Erreur vérification de la version:");
            try {
                logger.info("Version serveur: " + ParametersManager.getParameter(Parameters.LATEST_VERSION) + ", Version client: " + version);
            } catch (UnableToGetParameterException e) {
                BootstrapFrame.getInstance().getCrashReporter().catchError(e, "Impossible d'identifier la dernière version");
                System.exit(0);
                return;
            }
            JOptionPane.showMessageDialog(BootstrapFrame.getInstance(), "Erreur vérification de la version du bootstrap", name + " | Erreur", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            return;
        }
        int latestVersion = Integer.parseInt(latestVersionS.toString());
        int thisVersion = Integer.parseInt(thisVersionS.toString());

        try {
            logger.info("Version serveur: " + ParametersManager.getParameter(Parameters.LATEST_VERSION));
        } catch (UnableToGetParameterException e) {
            BootstrapFrame.getInstance().getCrashReporter().catchError(e, "Impossible d'identifier la dernière version");
            System.exit(0);
            return;
        }
        logger.info("Version client: " + version);

        if (latestVersion > thisVersion) {
            logger.info("Nouvelle version disponible !");
            if (JOptionPane.showConfirmDialog(BootstrapFrame.getInstance(), "Nouvelle version disponible !\n\nVoulez-vous la télécharger?", name + " | Nouvelle version", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION) {
                Exception result;
                try {
                    result = OperatingSystem.openLink(new URI(ParametersManager.getParameter(Parameters.DOWNLOAD_LINK)));
                } catch (URISyntaxException | UnableToGetParameterException e) {
                    result = e;
                }
                if (result != null)
                    BootstrapFrame.getInstance().getCrashReporter().catchError(result, "Impossible d'ouvrir le navigateur pour télécharger la nouvelle version !");
            }
            System.exit(0);
            return;
        } else {
            logger.info("Bootstrap à jour !");
        }

        try {
            if (Boolean.parseBoolean(ParametersManager.getParameter(Parameters.INFO_ENABLE))) {
                String info_msg = ParametersManager.getParameter(Parameters.INFO_MSG);
                logger.info("Mode information (ON): " + info_msg);
                JOptionPane.showMessageDialog(BootstrapFrame.getInstance(), new String(info_msg.replace("\\n", "\n").getBytes(), StandardCharsets.UTF_8), name + " | Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (UnableToGetParameterException e) {
            BootstrapFrame.getInstance().getCrashReporter().catchError(e, "Impossible d'identifier les infos du message d'information");
            System.exit(0);
            return;
        }

        try {
            if (Boolean.parseBoolean(ParametersManager.getParameter(Parameters.RESTRICTED_ENABLE))) {
                String restricted_msg = ParametersManager.getParameter(Parameters.RESTRICTED_MSG);
                logger.info("Mode restreint (ON): " + restricted_msg);
                JOptionPane.showMessageDialog(BootstrapFrame.getInstance(), new String(restricted_msg.replace("\\n", "\n").getBytes(), StandardCharsets.UTF_8), name + " | Mode restreint", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
                return;
            }
        } catch (UnableToGetParameterException e) {
            BootstrapFrame.getInstance().getCrashReporter().catchError(e, "Impossible d'identifier les infos de restriction");
            System.exit(0);
            return;
        }

        try {
            logger.info("Vérification mise à jour du launcher...");
            BootstrapFrame.getInstance().doUpdate();
            logger.info("Mise à jour launcher: OK");
        } catch (BadServerResponseException | IOException | BadServerVersionException | ServerDisabledException | ServerMissingSomethingException | InterruptedException | UnableToGetParameterException e) {
            BootstrapFrame.getInstance().getCrashReporter().catchError(e, "Impossible de mettre à jour le launcher !");
        }
        try {
            logger.info("Lancement du launcher...");
            BootstrapFrame.getInstance().launchLauncher();
        } catch (LaunchException | UnableToGetParameterException e) {
            BootstrapFrame.getInstance().getCrashReporter().catchError(e, "Impossible de lancer le launcher !");
        }
    }
}