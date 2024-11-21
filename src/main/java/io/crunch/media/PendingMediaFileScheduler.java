package io.crunch.media;

import io.crunch.cache.PendingFileStatusCache;
import io.crunch.contentcheck.ContentTypeCheckingService;
import io.crunch.security.VirusScannerService;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.TimeUnit;

import static io.quarkus.scheduler.Scheduled.ConcurrentExecution.SKIP;

/**
 * Scheduler for processing pending media files through various stages such as virus scanning,
 * content type checking, and file transferring.
 *
 * <p>This class uses Quarkus' scheduler to periodically invoke tasks at configurable intervals.
 */
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

    /**
     * Scheduled task to check and process media files awaiting virus scanning.
     */
    @Scheduled(every = "{app.virus.scan.scheduler.period:5s}", delay = 1, delayUnit = TimeUnit.SECONDS, concurrentExecution = SKIP)
    public void checkWaitingVirusScanning() {
        execute(virusScannerService::checkAwaiting);
    }

    /**
     * Scheduled task to check and process media files awaiting content type checking.
     */
    @Scheduled(every = "{app.content.type.checker.scheduler.period:5s}", delay = 2, delayUnit = TimeUnit.SECONDS, concurrentExecution = SKIP)
    public void checkAwaitingTypeChecking() {
        execute(contentTypeCheckingService::checkAwaiting);
    }

    /**
     * Scheduled task to check and process media files awaiting transfer to final storage.
     */
    @Scheduled(every = "{app.file.transfer.scheduler.period:5s}", delay = 3, delayUnit = TimeUnit.SECONDS, concurrentExecution = SKIP)
    public void checkAwaitingTransferring() {
        execute(mediaFileTransferService::checkAwaiting);
    }

    /**
     * Executes a given job if the pending media file status cache is ready and operational.
     *
     * @param job the task to be executed.
     */
    private void execute(Runnable job) {
        if (pendingMediaFileStatusCache.isReady()) job.run();
    }
}
