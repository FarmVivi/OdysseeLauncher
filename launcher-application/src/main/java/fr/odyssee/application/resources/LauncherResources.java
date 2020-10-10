package fr.odyssee.application.resources;

import fr.odyssee.application.LauncherFrame;
import fr.odyssee.application.Main;
import fr.odyssee.common.AppDataDir;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LauncherResources {
    private static final File DIR = AppDataDir.createGameDir(Main.name);
    private static final File ASSETS_DIR = new File(DIR, "launcher/assets/resources/");

    public static ImageIcon getResource(String path) {
        return new ImageIcon(new File(ASSETS_DIR, path).getPath());
    }

    public static BufferedImage getResourceBufferedImage(String path) {
        File img = new File(ASSETS_DIR, path);

        /*if (!img.exists())
            return null;*/

        try {
            return ImageIO.read(img);
        } catch (IOException e) {
            LauncherFrame.getInstance().getCrashReporter().catchError(e, "Impossible de charger l'image (" + path + ")");
            return null;
        }
    }
}
