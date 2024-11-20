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

    }

    @Override
    public Stream<String> getAwaitingVirusScanning() {
        return Stream.empty();
    }

    @Override
    public Stream<String> getAwaitingTypeChecking() {
        return Stream.empty();
    }

    @Override
    public Stream<String> getAwaitingTransferring() {
        return Stream.empty();
    }

    @Override
    public void virusScanningCompleted(VirusScanResult result) {

    }

    @Override
    public void mediaTypeCheckingCompleted(ContentTypeCheckResult result) {

    }

    @Override
    public void transferCompleted(String name) {

    }
}
