package fr.odyssee.application.updater;

import fr.odyssee.application.LauncherFrame;
import fr.theshark34.supdate.GlobalBarAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UpdaterManager {
    private static final Map<UUID, UpdaterTask> updaters = new HashMap<>();
    private static ScheduledExecutorService progress = Executors.newScheduledThreadPool(1);
    private static boolean progressStatus = false;

    public static synchronized UUID addUpdater(UpdaterTask updater) throws InterruptedException {
        updaters.put(updater.getUUID(), updater);
        logoUpdate(true);
        return updater.getUUID();
    }

    public static UpdaterTask getUpdater(UUID updaterUUID) {
        return updaters.get(updaterUUID);
    }

    public static synchronized void removeUpdater(UUID updaterUUID) throws InterruptedException {
        updaters.remove(updaterUUID);
        logoUpdate(false);
    }

    public static synchronized void logoUpdate(boolean enable) throws InterruptedException {
        if (enable) {
            if (!progressStatus) {
                progress.scheduleAtFixedRate(() -> {
                    if (GlobalBarAPI.getNumberOfFileToDownload() > 0) {
                        int val = (int) (GlobalBarAPI.getNumberOfTotalDownloadedBytes() / 1000);
                        int max = (int) (GlobalBarAPI.getNumberOfTotalBytesToDownload() / 1000);
                        LauncherFrame.getInstance().getLauncherPanel().getLogoProgressBar().setValue(val);
                        LauncherFrame.getInstance().getLauncherPanel().getLogoProgressBar().setMaximum(max);
                    }
                }, 0, 50, TimeUnit.MILLISECONDS);
                progressStatus = true;
            }
        } else {
            if (updaters.size() == 0 && progressStatus) {
                progress.shutdown();
                progress.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
                LauncherFrame.getInstance().getLauncherPanel().getLogoProgressBar().setValue(0);
                progress = Executors.newScheduledThreadPool(1);
                progressStatus = false;
            }
        }
    }
}
