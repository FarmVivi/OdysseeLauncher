package fr.odyssee.application.settings;

import fr.odyssee.application.Main;
import fr.odyssee.common.AppDataDir;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.openlauncherlib.util.explorer.ExploredDirectory;
import fr.theshark34.openlauncherlib.util.explorer.Explorer;

import java.io.File;

public class SettingsManager {
    public static String getSetting(Settings setting) {
        return getSaver().get(setting.getName());
    }

    public static void setSetting(Settings setting, String value) {
        Saver saver = getSaver();
        saver.set(setting.getName(), value);
        saver.save();
    }

    public static Saver getSaver() {
        return setupSaver();
    }

    public static Saver setupSaver() {
        File DIR = AppDataDir.createGameDir(Main.name);
        File PROGRAM_DIR = new File(DIR, "launcher");
        if (!PROGRAM_DIR.exists()) {
            //noinspection ResultOfMethodCallIgnored
            PROGRAM_DIR.mkdirs();
        }
        ExploredDirectory gameDir = Explorer.dir(PROGRAM_DIR);
        File saverfile = new File(gameDir.get(), "launcher.properties");
        Saver saver = new Saver(saverfile);
        saver.save();
        saver.load();
        for (Settings setting : Settings.values()) {
            if (saver.get(setting.getName()) == null) {
                saver.set(setting.getName(), setting.getDefaultValue());
            }
        }
        saver.save();
        saver.load();
        return saver;
    }
}
