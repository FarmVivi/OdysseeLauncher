package fr.odyssee.common;

import java.io.File;

public class AppDataDir {
    public static File createGameDir(String name) {
        String userHome = System.getProperty("user.home");
        File workingDirectory;
        switch (OperatingSystem.getCurrentPlatform()) {
            case LINUX:
                workingDirectory = new File(userHome, "." + name.toLowerCase());
                break;
            case WINDOWS:
                String applicationData = System.getenv("APPDATA");
                String folder = applicationData != null ? applicationData : userHome;
                workingDirectory = new File(folder, "." + name.toLowerCase());
                break;
            case OSX:
                workingDirectory = new File(userHome, "Library/Application Support/" + name.toLowerCase());
                break;
            default:
                workingDirectory = new File(userHome, name.toLowerCase());
        }
        return workingDirectory;
    }
}
