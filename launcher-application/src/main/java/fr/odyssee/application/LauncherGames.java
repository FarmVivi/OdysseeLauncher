package fr.odyssee.application;

import fr.odyssee.application.resources.LauncherResource;
import fr.odyssee.application.resources.LauncherResources;

import javax.swing.*;
import java.awt.image.BufferedImage;

public enum LauncherGames {
    UNKNOWN("Inconnu", "https://odyssee.avadia.fr", "unknown"),
    MINECRAFT("Minecraft", "https://odyssee.avadia.fr/minecraft", "minecraft");

    private final String name;
    private final String site;

    private final String resourcename;

    LauncherGames(String name, String site, String resourcename) {
        this.name = name;
        this.site = site;
        this.resourcename = resourcename;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public ImageIcon getResource(LauncherResource resource) {
        return LauncherResources.getResource("games/" + resourcename + "/" + resource.getFileName() + "." + resource.getExtension().getExtension());
    }

    public ImageIcon getResource(String morePath, LauncherResource resource) {
        return LauncherResources.getResource("games/" + resourcename + "/" + morePath + "/" + resource.getFileName() + "." + resource.getExtension().getExtension());
    }

    public BufferedImage getResourceBufferedImage(LauncherResource resource) {
        return LauncherResources.getResourceBufferedImage("games/" + resourcename + "/" + resource.getFileName() + "." + resource.getExtension().getExtension());
    }

    public BufferedImage getResourceBufferedImage(String morePath, LauncherResource resource) {
        return LauncherResources.getResourceBufferedImage("games/" + resourcename + "/" + morePath + "/" + resource.getFileName() + "." + resource.getExtension().getExtension());
    }

    public String getResourcename() {
        return resourcename;
    }
}
