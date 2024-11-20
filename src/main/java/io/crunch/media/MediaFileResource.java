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

@Path("/api")
public class MediaFileResource {

    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PendingMediaFiles pendingMediaFiles;

    public MediaFileResource(PendingMediaFiles pendingMediaFiles) {
        this.pendingMediaFiles = pendingMediaFiles;
    }

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
