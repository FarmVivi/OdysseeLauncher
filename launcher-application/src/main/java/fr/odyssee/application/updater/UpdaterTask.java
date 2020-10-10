package fr.odyssee.application.updater;

import fr.odyssee.application.LauncherServers;
import fr.theshark34.supdate.SUpdate;

import java.io.File;
import java.util.UUID;

public class UpdaterTask {
    private final SUpdate sUpdate;
    private final LauncherServers server;
    private final UpdaterType type;
    private final UUID uuid = UUID.randomUUID();

    public UpdaterTask(String serverUrl, File outputFolder, LauncherServers server, UpdaterType type) {
        sUpdate = new SUpdate(serverUrl, outputFolder);

        this.server = server;
        this.type = type;
    }

    public SUpdate getSUpdate() {
        return sUpdate;
    }

    public LauncherServers getServer() {
        return server;
    }

    public UpdaterType getType() {
        return type;
    }

    public UUID getUUID() {
        return uuid;
    }
}
