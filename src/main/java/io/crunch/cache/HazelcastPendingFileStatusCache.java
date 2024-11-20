package io.crunch.cache;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.function.Predicate;
import java.util.stream.Stream;

@ApplicationScoped
public class HazelcastPendingFileStatusCache implements PendingFileStatusCache {

    public static final String PENDING_MEDIA_FILE_CACHE_NAME = "PENDING_STATUS_CACHE";

    public static final String HAZELCAST_INSTANCE_NAME = "HZ-INSTANCE";

    private final HazelcastInstance hazelcastInstance;

    public HazelcastPendingFileStatusCache(
            @ConfigProperty(name = "app.hazelcast.status.cache.size ", defaultValue = "1000") int cacheSize) {
        // It tries to load Hazelcast configuration from a list of well-known locations,
        // and then applies overrides found in environment variables/system properties.
        // Note: We do not use custom builder, for example Yaml, otherwise we have to set the overridden
        // properties programmatically
        var config = Config.load();
        config.setInstanceName(HAZELCAST_INSTANCE_NAME)
                .setClusterName("uploader-cluster")
                .addMapConfig(new MapConfig()
                        .setEvictionConfig(new EvictionConfig()
                                .setMaxSizePolicy(MaxSizePolicy.PER_NODE)
                                .setSize(cacheSize))
                        .setName(PENDING_MEDIA_FILE_CACHE_NAME)
                        .setInMemoryFormat(InMemoryFormat.BINARY));

        // Use logging bridge to use the right format
        config.setProperty("hazelcast.logging.type", "slf4j");
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        this.hazelcastInstance = Hazelcast.getOrCreateHazelcastInstance(config);
    }

    @Override
    public void update(String name, String status) {
        getStatusCache().set(name, status);
    }

    @Override
    public void delete(long name) {
        getStatusCache().delete(name);
    }

    @Override
    public Stream<String> getAwaiting(String status) {
        return getFilesNames(entry -> entry.status().equals(status));
    }

    @Override
    public boolean isReady() {
        return hazelcastInstance.getLifecycleService().isRunning();
    }

    private Stream<String> getFilesNames(Predicate<Entry> predicate) {
        var cache = getStatusCache();
        return cache.localKeySet().stream()
                .map(key -> new Entry(key, cache.get(key)))
                .filter(predicate)
                .map(Entry::name);
    }

    private IMap<String, String> getStatusCache() {
        return hazelcastInstance.getMap(PENDING_MEDIA_FILE_CACHE_NAME);
    }

    record Entry(String name, String status) {}
}
