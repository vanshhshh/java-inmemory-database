# Java In-Memory Database (Redis-like)

A **Redis-inspired in-memory key–value database** built from scratch in Java.  
This project focuses on **core system design concepts** such as data structures, eviction policies, TTL expiry, concurrency, and persistence — without using any external frameworks.

---

## 🚀 Why This Project

This project was built to deeply understand:
- How in-memory databases like **Redis** work internally
- Data-structure-driven system design
- Trade-offs between performance, memory, and durability
- Thread safety and background processing

It is intentionally framework-free to highlight **core engineering fundamentals**.

---

## 🧠 High-Level Architecture

Client
|
KeyValueStore (Public API)
|
| ConcurrentHashMap | LRU Cache | TTL Manager |
|
Persistence Layer (WAL + Snapshots)

yaml
Copy code

---

## ⚙️ Features

- **Thread-safe key–value store**
- **O(1) LRU eviction** using HashMap + Doubly Linked List
- **TTL support** with:
  - Lazy expiration (on read)
  - Active expiration (background scheduler)
- **Write-Ahead Logging (WAL)** for durability
- **Snapshot persistence** with crash recovery
- Clean separation of concerns

---

## 🧵 Thread Safety

- Core storage uses `ConcurrentHashMap`
- LRU operations are synchronized
- TTL expiry runs in a background scheduler
- Safe under concurrent read/write workloads

---

## 📂 Project Structure

.
├── Main.java
├── store/
│ ├── KeyValueStore.java
│ └── Value.java
├── eviction/
│ └── LRUCache.java
├── ttl/
│ ├── ExpiryEntry.java
│ └── ExpiryManager.java
├── persistence/
│ ├── WriteAheadLog.java
│ └── SnapshotManager.java
└── util/
└── TimeUtil.java

yaml
Copy code

---

## ▶️ How to Run

### Compile
```bash
javac -d . Main.java store/*.java eviction/*.java ttl/*.java persistence/*.java util/*.java
Run
bash
Copy code
java -cp . Main
🧪 Testing Scenarios Covered
Correctness: PUT / GET / DELETE

LRU Eviction under capacity constraints

TTL Expiry (lazy + background cleanup)

Persistence using snapshots

Crash recovery simulation

Concurrent access using multiple threads

🗄️ Persistence Design
Write-Ahead Log (WAL)
Every mutation is logged before applying

Enables durability guarantees

Snapshots
Periodic full dump of in-memory state

Fast recovery on restart

Corrupt snapshots are safely ignored

🔄 Trade-offs & Design Decisions
Blocking operations chosen for clarity over complexity

Bounded memory using LRU instead of unbounded growth

Java serialization used for simplicity (not version-tolerant)

Background TTL cleanup balances performance vs accuracy

📌 Future Improvements
WAL replay on startup

Custom binary snapshot format

Configurable eviction policies (LFU, FIFO)

Network interface (TCP protocol)

Metrics & monitoring

👤 Author
Vansh Sharma
Software Development Engineer (SDE)

yaml
Copy code

---

## 3️⃣ Commit README

```powershell
git add README.md
git commit -m "Add README for in-memory database project"
git push