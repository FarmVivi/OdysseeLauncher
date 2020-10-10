package fr.odyssee.bootstrap.parameters;

public enum Parameters {
    LATEST_VERSION("latest_version"),
    DOWNLOAD_LINK("download_link"),
    UPDATER_LINK("updater_link"),
    LAUNCHER_MAIN_CLASS("launcher_main_class"),
    RESTRICTED_ENABLE("restricted"),
    RESTRICTED_MSG("restricted_msg"),
    INFO_ENABLE("info"),
    INFO_MSG("info_msg");

    private final String name;

    Parameters(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
