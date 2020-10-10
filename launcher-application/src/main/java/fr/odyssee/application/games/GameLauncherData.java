package fr.odyssee.application.games;

import fr.theshark34.openlauncherlib.minecraft.GameTweak;
import fr.theshark34.openlauncherlib.minecraft.GameVersion;

public abstract class GameLauncherData {
    public abstract GameVersion getVERSION();

    public abstract GameTweak getTWEAK();

    public abstract String getIp();
}