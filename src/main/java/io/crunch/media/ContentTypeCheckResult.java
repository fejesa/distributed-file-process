package io.crunch.media;

/**
 * Represents the result of a content type check.
 *
 * @param name      The name of the file.
 * @param supported {@code true} if the detected format is supported, {@code false} otherwise.
 */
public record ContentTypeCheckResult(String name, boolean supported) {
}
