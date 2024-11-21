package io.crunch.media;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import jakarta.ws.rs.core.MediaType;
import org.awaitility.Durations;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

@QuarkusTest
class MediaFileResourceTest {

    @InjectSpy
    PendingMediaFilesService pendingMediaFilesService;

    @Test
    void uploadMediaFile() throws Exception {
        // Given a media file
        // When the media file is uploaded
        // Then the virus scan, and content type check, and transfer to final storage are completed
        var requestBody = new ObjectMapper().writeValueAsString(new CreateMediaFile("test-file", "Hello, World!"));
        given()
            .body(requestBody)
            .when()
            .header("Accept", MediaType.APPLICATION_JSON)
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .post("/api/file")
            .then()
            .statusCode(RestResponse.Status.CREATED.getStatusCode());

        await()
            .atMost(Duration.ofSeconds(15))
            .pollInterval(Durations.ONE_SECOND)
            .untilAsserted(() -> {
                verify(pendingMediaFilesService, atLeast(1)).getAwaitingVirusScanning();
                verify(pendingMediaFilesService, atLeast(1)).getAwaitingTypeChecking();
                verify(pendingMediaFilesService, atLeast(1)).getAwaitingTransferring();
                verify(pendingMediaFilesService, times(1)).transferCompleted("test-file");
            });
    }
}
