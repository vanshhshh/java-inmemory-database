package ttl;

import store.KeyValueStore;
import util.TimeUtil;

import java.util.PriorityQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExpiryManager {

    private final PriorityQueue<ExpiryEntry> pq = new PriorityQueue<>();
    private final ScheduledExecutorService executor =
            Executors.newSingleThreadScheduledExecutor();

    public ExpiryManager(KeyValueStore store) {
        executor.scheduleAtFixedRate(() -> {
            long now = TimeUtil.now();
            synchronized (pq) {
                while (!pq.isEmpty() && pq.peek().expiryTime <= now) {
                    ExpiryEntry entry = pq.poll();
                    store.expireKey(entry.key, entry.expiryTime);
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void register(String key, long expiryTime) {
        synchronized (pq) {
            pq.add(new ExpiryEntry(key, expiryTime));
        }
    }
}
