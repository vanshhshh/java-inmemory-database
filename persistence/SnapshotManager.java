package persistence;

import store.Value;

import java.io.*;
import java.util.Map;

public class SnapshotManager {

    private final String file;

    public SnapshotManager(String file) {
        this.file = file;
    }

    public synchronized void save(Map<String, Value> store) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(store);
        } catch (IOException e) {
            throw new RuntimeException("Snapshot save failed", e);
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized Map<String, Value> load() {
        File f = new File(file);
        if (!f.exists()) return null;

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, Value>) in.readObject();
        } catch (Exception e) {
            throw new RuntimeException("Snapshot load failed", e);
        }
    }
}
