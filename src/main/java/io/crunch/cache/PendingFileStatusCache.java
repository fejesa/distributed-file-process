package io.crunch.cache;

import java.util.stream.Stream;

/**
 * Interface for managing the cache of pending file statuses, including
 * operations for updating, retrieving, and checking cache readiness.
 * <p>The key of the cache is the name of the pending file, and the value is the status of the file.
 */
public interface PendingFileStatusCache {

    /**
     * Saves or updates the status of a pending file in the cache.
     *
     * @param name   the name of the pending file.
     * @param status the status to associate with the file.
     */
    void update(String name, String status);

    /**
     * Removes a pending file from the cache.
     *
     * @param name the name of the pending file to delete.
     */
    void delete(String name);

    /**
     * Retrieves a stream of pending file names that match the specified status.
     *
     * @param status the status to filter files by.
     * @return a stream of file names with the specified status.
     * @apiNote Returns the locally owned set of file names.
     * The stream may be lazily evaluated and should be consumed in a timely manner.
     */
    Stream<String> getByStatus(String status);

    /**
     * Checks if the cache instance is active and operational.
     *
     * @return {@code true} if the cache instance is ready, {@code false} otherwise.
     */
    boolean isReady();
}
