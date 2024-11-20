package io.crunch.media;

import io.crunch.cache.PendingFileStatusCache;
import io.crunch.contentcheck.ContentTypeCheckingService;
import io.crunch.security.VirusScannerService;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class PendingMediaFileScheduler {

    private final PendingFileStatusCache pendingMediaFileStatusCache;

    private final VirusScannerService virusScannerService;

    private final ContentTypeCheckingService contentTypeCheckingService;

    private final MediaFileTransferService mediaFileTransferService;

    public PendingMediaFileScheduler(PendingFileStatusCache pendingMediaFileStatusCache, VirusScannerService virusScannerService,
                                     ContentTypeCheckingService contentTypeCheckingService, MediaFileTransferService mediaFileTransferService) {
        this.pendingMediaFileStatusCache = pendingMediaFileStatusCache;
        this.virusScannerService = virusScannerService;
        this.contentTypeCheckingService = contentTypeCheckingService;
        this.mediaFileTransferService = mediaFileTransferService;
    }

    @Scheduled(every = "{app.virus.scan.scheduler.period:5s}", delay = 14, delayUnit = TimeUnit.SECONDS)
    public void checkWaitingVirusScanning() {
        execute(virusScannerService::checkAwaiting);
    }

    @Scheduled(every = "{app.content.type.checker.scheduler.period:5s}", delay = 12, delayUnit = TimeUnit.SECONDS)
    public void checkAwaitingTypeChecking() {
        execute(contentTypeCheckingService::checkAwaiting);
    }

    @Scheduled(every = "{app.file.transfer.scheduler.period:5s}", delay = 5, delayUnit = TimeUnit.SECONDS)
    public void checkAwaitingTransferring() {
        execute(mediaFileTransferService::checkAwaiting);
    }

    private void execute(Runnable job) {
        if (pendingMediaFileStatusCache.isReady()) job.run();
    }
}
