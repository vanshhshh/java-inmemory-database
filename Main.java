import store.KeyValueStore;

public class Main {
    public static void main(String[] args) throws Exception {

        KeyValueStore kv = new KeyValueStore(3);

        kv.put("a", "1", -1);
        kv.put("b", "2", -1);
        kv.put("c", "3", -1);

        kv.get("a");
        kv.put("d", "4", -1); // evicts b

        System.out.println(kv.get("b")); // null
        System.out.println(kv.get("a")); // 1

        kv.put("temp", "x", 2000);
        Thread.sleep(3000);
        System.out.println(kv.get("temp")); // null

        kv.snapshot();
    }
}
