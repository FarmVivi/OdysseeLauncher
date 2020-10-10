package fr.odyssee.application.games.minecraft;

import fr.odyssee.application.games.GameLauncherData;
import fr.theshark34.openlauncherlib.minecraft.GameTweak;
import fr.theshark34.openlauncherlib.minecraft.GameVersion;

public class LauncherMinecraftData extends GameLauncherData {
    private final GameVersion VERSION;
    private final GameTweak TWEAK;

    public LauncherMinecraftData(GameVersion VERSION, GameTweak TWEAK) {
        this.VERSION = VERSION;
        this.TWEAK = TWEAK;
    }

    @Override
    public GameVersion getVERSION() {
        return VERSION;
    }

    @Override
    public GameTweak getTWEAK() {
        return TWEAK;
    }

    @Override
    public String getIp() {
        return null;
    }
}
