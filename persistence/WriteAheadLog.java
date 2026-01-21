package persistence;

import java.io.FileWriter;
import java.io.IOException;

public class WriteAheadLog {

    private final FileWriter writer;

    public WriteAheadLog(String file) throws IOException {
        this.writer = new FileWriter(file, true);
    }

    public synchronized void logPut(String key, String value, long expiry) {
        write("PUT|" + key + "|" + value + "|" + expiry);
    }

    public synchronized void logDelete(String key) {
        write("DEL|" + key);
    }

    private void write(String record) {
        try {
            writer.write(record + "\n");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("WAL write failed", e);
        }
    }
}
