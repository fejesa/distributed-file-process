package io.crunch.media;

import io.crunch.security.VirusScanResult;
import jakarta.validation.Valid;

import java.util.stream.Stream;

/**
 * Interface representing operations for managing pending media files, including their
 * lifecycle and status updates during various processing stages.
 */
public interface PendingMediaFiles {

    /**
     * Creates a new media file.
     *
     * @param createMediaFile an object containing details about the newly uploaded file.
     *                        This parameter is validated to ensure all required fields are properly set.
     */
    void createMediaFile(@Valid CreateMediaFile createMediaFile);

    /**
     * Retrieves a stream of media files that are awaiting virus scanning.
     *
     * @return a stream of file names that have not yet undergone virus scanning.
     */
    Stream<String> getAwaitingVirusScanning();

    /**
     * Retrieves a stream of media files that are pending content type validation.
     *
     * @return a stream of file names awaiting verification of their content type.
     */
    Stream<String> getAwaitingTypeChecking();

    /**
     * Retrieves a stream of media files that are ready for transfer to final storage.
     * This indicates all required checks (virus scanning and content type validation) have been completed.
     *
     * @return a stream of file names ready for transfer.
     */
    Stream<String> getAwaitingTransferring();

    /**
     * Updates the status of a media file after completing the virus scanning process.
     *
     * @param result an object representing the outcome of the virus scanning process.
     *               It typically contains the status and any relevant details about the scan.
     */
    void virusScanningCompleted(VirusScanResult result);

    /**
     * Updates the status of a media file after completing the content type validation.
     *
     * @param result an object representing the outcome of the content type verification process.
     *               It includes a status indicating whether the file format is supported.
     */
    void mediaTypeCheckingCompleted(ContentTypeCheckResult result);

    /**
     * Marks a media file as successfully transferred and removes it from the pending list.
     *
     * @param name the name of the media file that has been successfully transferred.
     */
    void transferCompleted(String name);
}
