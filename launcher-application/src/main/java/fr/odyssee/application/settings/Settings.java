package fr.odyssee.application.settings;

public enum Settings {
    USERNAME("username", ""),
    PASSWORD("password", ""),
    MINECRAFT_RAM("minecraft_launch_ram", "1024"),
    LAUNCHER_ANIMATION("launcher_animation", "true"),
    LAUNCHER_CLOSEAFTERLAUNCHINGOFAGAME("launcher_closeafterlaunchingofagame", "true"),
    TESTS_SERVERS("launcher_show_tests_servers", "false");

    private final String name;
    private final String defaultValue;

    Settings(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
