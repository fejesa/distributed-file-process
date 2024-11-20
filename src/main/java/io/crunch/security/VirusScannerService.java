package io.crunch.security;

import io.crunch.media.PendingMediaFiles;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Service responsible for handling virus scanning operations for pending media files.
 * This service interacts with the {@link PendingMediaFiles} interface to process files awaiting virus scanning.
 */
@ApplicationScoped
public class VirusScannerService {

    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PendingMediaFiles pendingMediaFiles;

    public VirusScannerService(PendingMediaFiles pendingMediaFiles) {
        this.pendingMediaFiles = pendingMediaFiles;
    }

    /**
     * Checks pending files waiting for virus scanning and schedules them for scanning.
     * This method logs each file being scheduled and simulates a delay for the scanning process.
     */
    public void checkAwaiting() {
        pendingMediaFiles.getAwaitingVirusScanning()
                .forEach(name -> {
                    logger.info("Schedule media file [{}] virus scanning", name);
                    try {
                        Thread.sleep(3000); // Simulates a delay of 3 second to mimic the scanning process
                        pendingMediaFiles.virusScanningCompleted(new VirusScanResult(name, false));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                });
    }
}
