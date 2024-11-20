package io.crunch.media;

import io.crunch.cache.PendingFileStatusCache;
import io.crunch.security.VirusScanResult;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

@ApplicationScoped
public class PendingMediaFilesService implements PendingMediaFiles {

    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PendingFileStatusCache pendingFileStatusCache;

    public PendingMediaFilesService(PendingFileStatusCache pendingFileStatusCache) {
        this.pendingFileStatusCache = pendingFileStatusCache;
    }

    @Override
    public void createMediaFile(CreateMediaFile createMediaFile) {
        logger.info("Creating media file [{}]", createMediaFile.name());
        pendingFileStatusCache.update(createMediaFile.name(), "PENDING_VIRUS_SCAN");
    }

    @Override
    public Stream<String> getAwaitingVirusScanning() {
        return pendingFileStatusCache.getAwaiting("PENDING_VIRUS_SCAN");
    }

    @Override
    public Stream<String> getAwaitingTypeChecking() {
        return pendingFileStatusCache.getAwaiting("PENDING_TYPE_CHECK");
    }

    @Override
    public Stream<String> getAwaitingTransferring() {
        return pendingFileStatusCache.getAwaiting("PENDING_TRANSFER");
    }

    @Override
    public void virusScanningCompleted(VirusScanResult result) {
        logger.info("Virus scanning of media file completed with result: [{}]", result);
        var status = result.infectionDetected() ? "INFECTED" : "PENDING_TYPE_CHECK";
        pendingFileStatusCache.update(result.name(), status);
    }

    @Override
    public void mediaTypeCheckingCompleted(ContentTypeCheckResult result) {
        logger.info("Content type checking of media file completed with result: [{}]", result);
        var status = result.supported() ? "PENDING_TRANSFER" : "UNSUPPORTED_FORMAT";
        pendingFileStatusCache.update(result.name(), status);
    }

    @Override
    public void transferCompleted(String name) {
        logger.info("Media file [{}] transfer completed", name);
        pendingFileStatusCache.delete(name);
    }
}
