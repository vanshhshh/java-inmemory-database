package store;

import java.io.Serializable;

public class Value implements Serializable {

    private static final long serialVersionUID = 1L;

    public final String data;
    public final long expiryTime;

    public Value(String data, long expiryTime) {
        this.data = data;
        this.expiryTime = expiryTime;
    }

    public boolean isExpired(long now) {
        return expiryTime != -1 && now > expiryTime;
    }
}
