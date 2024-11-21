package io.crunch.media;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * RESTful API resource for managing media files.
 *
 * <p>This class provides endpoints for interacting with media files, including creating new media files.
 */
@Path("/api")
public class MediaFileResource {

    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PendingMediaFiles pendingMediaFiles;

    public MediaFileResource(PendingMediaFiles pendingMediaFiles) {
        this.pendingMediaFiles = pendingMediaFiles;
    }

    /**
     * Handles HTTP POST requests for creating a new media file.
     *
     * <p>This endpoint accepts a JSON payload representing the media file metadata, validates it,
     * and delegates the creation process to the {@code PendingMediaFiles} service.
     *
     * @param createMediaFile the media file metadata provided in the request body.
     * @return a {@code RestResponse} with HTTP status 201 (Created) if the operation is successful.
     */
    @POST
    @Path("/file")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<Void> createMediaFile(@Valid CreateMediaFile createMediaFile) {
        logger.info("Media file upload request with params [{}]", createMediaFile.name());
        pendingMediaFiles.createMediaFile(createMediaFile);
        return RestResponse.status(RestResponse.Status.CREATED);
    }
}
