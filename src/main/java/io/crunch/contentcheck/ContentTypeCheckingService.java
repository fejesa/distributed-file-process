package io.crunch.contentcheck;

import io.crunch.media.PendingMediaFiles;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Service responsible for handling content type checking of pending media files.
 * This service interacts with the {@link PendingMediaFiles} interface to process files awaiting type validation.
 * <p><b>Usage Notes:</b>
 * This class is a placeholder for the actual content type checking implementation and does not perform any real validation.
 */
@ApplicationScoped
public class ContentTypeCheckingService {

    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PendingMediaFiles pendingMediaFiles;

    public ContentTypeCheckingService(PendingMediaFiles pendingMediaFiles) {
        this.pendingMediaFiles = pendingMediaFiles;
    }

    /**
     * Checks pending files waiting for content type checking and schedules them for validation.
     * This simulates a delay for the validation process.
     */
    public void checkAwaiting() {
        pendingMediaFiles.getAwaitingTypeChecking()
            .forEach(name -> {
                logger.info("Schedule media file [{}] content type checking", name);
                try {
                    Thread.sleep(500); // Simulates a delay of 0.5 second to mimic the scanning process
                    pendingMediaFiles.mediaTypeCheckingCompleted(new ContentTypeCheckResult(name, true));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            });
    }
}
