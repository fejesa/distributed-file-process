package io.crunch.media;

import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Service responsible for handling media file transfer operations for pending media files.
 * This service interacts with the {@link PendingMediaFiles} interface to process files awaiting transfer.
 * <p><b>Usage Notes:</b>
 * This class is a placeholder for the actual media file transfer implementation and does not perform any real transfer.
 */
@ApplicationScoped
public class MediaFileTransferService {

    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PendingMediaFiles pendingMediaFiles;

    public MediaFileTransferService(PendingMediaFiles pendingMediaFiles) {
        this.pendingMediaFiles = pendingMediaFiles;
    }

    /**
     * Checks pending files waiting for transfer and schedules them for transfer.
     * This simulates a delay for the transfer process.
     */
    public void checkAwaiting() {
        pendingMediaFiles.getAwaitingTransferring()
            .forEach(name -> {
                logger.info("Schedule media file [{}] transfer", name);
                try {
                    Thread.sleep(2000); // Simulates a delay of 2 second to mimic the transfer process
                    pendingMediaFiles.transferCompleted(name);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            });
    }
}
