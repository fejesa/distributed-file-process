package io.crunch.security;

/**
 * Represents the result of a virus scan.
 *
 * @param name              Name of the file.
 * @param infectionDetected {@code true} if trojan, virus, malware & other malicious threat is detected in the given file.
 */
public record VirusScanResult(String name, boolean infectionDetected) {
}
