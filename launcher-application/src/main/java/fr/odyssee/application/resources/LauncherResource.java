package fr.odyssee.application.resources;

public enum LauncherResource {
    ICON("icon", LauncherResourceExtension.PNG),
    AFFICHE("affiche", LauncherResourceExtension.PNG),
    BACKGROUND("background", LauncherResourceExtension.PNG);

    private final String filename;
    private final LauncherResourceExtension extension;

    LauncherResource(String filename, LauncherResourceExtension extension) {
        this.filename = filename;
        this.extension = extension;
    }

    public String getFileName() {
        return filename;
    }

    public LauncherResourceExtension getExtension() {
        return extension;
    }
}
