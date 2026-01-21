package eviction;

import java.util.HashMap;
import java.util.Map;

public class LRUCache {

    private static class Node {
        String key;
        Node prev, next;
        Node(String key) { this.key = key; }
    }

    private final int capacity;
    private final Map<String, Node> map = new HashMap<>();
    private Node head, tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
    }

    public synchronized String access(String key) {
        if (map.containsKey(key)) {
            moveToHead(map.get(key));
            return null;
        }

        Node node = new Node(key);
        map.put(key, node);
        addToHead(node);

        if (map.size() > capacity) {
            String evictedKey = tail.key;
            removeNode(tail);
            map.remove(evictedKey);
            return evictedKey;
        }
        return null;
    }

    public synchronized void remove(String key) {
        Node node = map.remove(key);
        if (node != null) removeNode(node);
    }

    private void moveToHead(Node node) {
        removeNode(node);
        addToHead(node);
    }

    private void addToHead(Node node) {
        node.prev = null;
        node.next = head;
        if (head != null) head.prev = node;
        head = node;
        if (tail == null) tail = node;
    }

    private void removeNode(Node node) {
        if (node.prev != null) node.prev.next = node.next;
        else head = node.next;

        if (node.next != null) node.next.prev = node.prev;
        else tail = node.prev;
    }
}
