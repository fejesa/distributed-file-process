package io.crunch.media;

import io.crunch.cache.FileStatusCache;
import io.crunch.contentcheck.ContentTypeCheckResult;
import io.crunch.security.VirusScanResult;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

@ApplicationScoped
public class PendingMediaFilesService implements PendingMediaFiles {

    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final FileStatusCache fileStatusCache;

    public PendingMediaFilesService(FileStatusCache fileStatusCache) {
        this.fileStatusCache = fileStatusCache;
    }

    @Override
    public void createMediaFile(CreateMediaFile createMediaFile) {
        logger.info("Creating media file [{}]", createMediaFile.name());
        fileStatusCache.update(createMediaFile.name(), "PENDING_VIRUS_SCAN");
    }

    @Override
    public Stream<String> getAwaitingVirusScanning() {
        return fileStatusCache.getByStatus("PENDING_VIRUS_SCAN");
    }

    @Override
    public Stream<String> getAwaitingTypeChecking() {
        return fileStatusCache.getByStatus("PENDING_TYPE_CHECK");
    }

    @Override
    public Stream<String> getAwaitingTransferring() {
        return fileStatusCache.getByStatus("PENDING_TRANSFER");
    }

    @Override
    public void virusScanningCompleted(VirusScanResult result) {
        logger.info("Virus scanning of media file completed with result: [{}]", result);
        var status = result.infectionDetected() ? "INFECTED" : "PENDING_TYPE_CHECK";
        fileStatusCache.update(result.name(), status);
    }

    @Override
    public void mediaTypeCheckingCompleted(ContentTypeCheckResult result) {
        logger.info("Content type checking of media file completed with result: [{}]", result);
        var status = result.supported() ? "PENDING_TRANSFER" : "UNSUPPORTED_FORMAT";
        fileStatusCache.update(result.name(), status);
    }

    @Override
    public void transferCompleted(String name) {
        logger.info("Media file [{}] transfer completed", name);
        fileStatusCache.delete(name);
    }
}
