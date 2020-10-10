package fr.odyssee.application.resources;

public enum LauncherResourceExtension {
    PNG("png"),
    JPEG("jpeg");

    private final String extension;

    LauncherResourceExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
