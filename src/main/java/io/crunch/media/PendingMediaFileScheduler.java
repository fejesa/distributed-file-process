package io.crunch.media;

import io.crunch.cache.PendingFileStatusCache;
import io.crunch.security.VirusScannerService;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class PendingMediaFileScheduler {

    private final PendingFileStatusCache pendingMediaFileStatusCache;

    private final VirusScannerService virusScannerService;

    public PendingMediaFileScheduler(PendingFileStatusCache pendingMediaFileStatusCache, VirusScannerService virusScannerService) {
        this.pendingMediaFileStatusCache = pendingMediaFileStatusCache;
        this.virusScannerService = virusScannerService;
    }

    @Scheduled(every = "{app.virus.scan.scheduler.period:5s}", delay = 14, delayUnit = TimeUnit.SECONDS)
    public void checkWaitingVirusScanning() {
        execute(virusScannerService::checkAwaiting);
    }

    private void execute(Runnable job) {
        if (pendingMediaFileStatusCache.isReady()) job.run();
    }
}
