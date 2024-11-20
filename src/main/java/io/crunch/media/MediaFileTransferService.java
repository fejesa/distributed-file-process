package io.crunch.media;

import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

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
