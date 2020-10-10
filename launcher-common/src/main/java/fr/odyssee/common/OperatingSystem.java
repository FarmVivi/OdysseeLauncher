package fr.odyssee.common;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public enum OperatingSystem {
    LINUX("linux", "linux", "unix"), WINDOWS("windows", "win"), OSX("osx", "mac"), UNKNOWN("unknown");

    private final String name;
    private final String[] aliases;

    OperatingSystem(String name, String... aliases) {
        this.name = name;
        this.aliases = (aliases == null ? new String[0] : aliases);
    }

    public static boolean getJavaProcess() {
        boolean bl = "32".equals(System.getProperty("sun.arch.data.model"));
        if (bl) {
            boolean bl2 = (System.getenv("PROCESSOR_ARCHITEW6432") != null) && (System.getenv("PROCESSOR_ARCHITEW6432").equals("AMD64"));
            if (!bl2) {
                bl2 = (System.getenv("PROCESSOR_ARCHITECTURE") != null) && (System.getenv("PROCESSOR_ARCHITECTURE").equals("AMD64"));
                return false;
            } else {
                return true;
            }
        }
        return bl;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Exception openLink(URI link) {
        try {
            Class desktopClass = Class.forName("java.awt.Desktop");
            Object o = desktopClass.getMethod("getDesktop").invoke(null);
            desktopClass.getMethod("browse", URI.class).invoke(o, link);
            return null;
        } catch (Exception e) {
            if (getCurrentPlatform() == OSX) {
                try {
                    Runtime.getRuntime().exec(new String[]{"/usr/bin/open", link.toString()});
                    return null;
                } catch (IOException localIOException) {
                    return localIOException;
                }
            }
            return e;
        }
    }

    public static OperatingSystem getCurrentPlatform() {
        String osName = System.getProperty("os.name").toLowerCase();
        OperatingSystem[] arrayOfOperatingSystem;
        int j = (arrayOfOperatingSystem = values()).length;
        for (int i = 0; i < j; i++) {
            OperatingSystem os = arrayOfOperatingSystem[i];
            String[] arrayOfString;
            int m = (arrayOfString = os.getAliases()).length;
            for (int k = 0; k < m; k++) {
                String alias = arrayOfString[k];
                if (osName.contains(alias)) {
                    return os;
                }
            }
        }
        return UNKNOWN;
    }

    public String getName() {
        return this.name;
    }

    public String[] getAliases() {
        return this.aliases;
    }

    public boolean isSupported() {
        return this != UNKNOWN;
    }

    public String getJavaDir() {
        String separator = System.getProperty("file.separator");
        String path = System.getProperty("java.home") + separator + "bin" + separator;
        if ((getCurrentPlatform() == WINDOWS) && (new File(path + "javaw.exe").isFile())) {
            return path + "javaw.exe";
        }
        return path + "java";
    }
}