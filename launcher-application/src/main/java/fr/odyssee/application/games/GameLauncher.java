package fr.odyssee.application.games;

import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.util.CrashReporter;

public abstract class GameLauncher {
    public abstract void update() throws Exception;

    public abstract void launch() throws LaunchException;

    public abstract void stopUpdate();

    public abstract CrashReporter getCrashReporter();

    public abstract AuthInfos getAuthInfos();

    public abstract void setAuthInfos(AuthInfos authInfos);
}