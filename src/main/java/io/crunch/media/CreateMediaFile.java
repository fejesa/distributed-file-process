package io.crunch.media;

import jakarta.validation.constraints.NotBlank;

public record CreateMediaFile(@NotBlank String name, String content) {
}
