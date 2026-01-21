package store;

import eviction.LRUCache;
import persistence.SnapshotManager;
import persistence.WriteAheadLog;
import ttl.ExpiryManager;
import util.TimeUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KeyValueStore {

    private final ConcurrentHashMap<String, Value> store =
            new ConcurrentHashMap<>();

    private final LRUCache lru;
    private final WriteAheadLog wal;
    private final SnapshotManager snapshot;
    private final ExpiryManager expiryManager;

    public KeyValueStore(int capacity) throws Exception {
        this.lru = new LRUCache(capacity);
        this.wal = new WriteAheadLog("wal.log");
        this.snapshot = new SnapshotManager("snapshot.dat");
        this.expiryManager = new ExpiryManager(this);

        Map<String, Value> recovered = snapshot.load();
        if (recovered != null) store.putAll(recovered);
    }

    public void put(String key, String value, long ttlMillis) {
        long expiry = ttlMillis > 0 ? TimeUtil.now() + ttlMillis : -1;
        Value val = new Value(value, expiry);

        store.put(key, val);
        wal.logPut(key, value, expiry);

        if (expiry != -1) expiryManager.register(key, expiry);

        String evicted = lru.access(key);
        if (evicted != null) {
            delete(evicted);
        }
    }

    public String get(String key) {
        Value val = store.get(key);
        if (val == null) return null;

        if (val.isExpired(TimeUtil.now())) {
            delete(key);
            return null;
        }

        lru.access(key);
        return val.data;
    }

    public void delete(String key) {
        store.remove(key);
        lru.remove(key);
        wal.logDelete(key);
    }

    // Called by ExpiryManager
    public void expireKey(String key, long expectedExpiry) {
        Value val = store.get(key);
        if (val != null && val.expiryTime == expectedExpiry) {
            delete(key);
        }
    }

    public void snapshot() {
        snapshot.save(store);
    }
}
