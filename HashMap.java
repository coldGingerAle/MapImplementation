import java.util.Iterator;

class HashMap<Key, Value> implements Map<Key, Value>, Iterable<LinkedMap> {

    private int N; // number of entries

    private int M; // hash table size

    private static final double MAX_LOAD_FACTOR = 0.75; // load factor constant

    private static final int[] primes = { // array of primes less than Integer.MAX_VALUE that are close to powers of 2
            31, 61, 127, 251, 509, 1021, 2039, 4093, 8191,
            16381, 32749, 65521, 131071, 262139, 524287,
            1048573, 2097143, 4194301, 8388593, 16777213,
            33554393, 67108859, 134217689, 268435399, 536870909,
            1073741789, 2147483647
    };

    private int primeIndex = 0; // to indicate which prime should be used when array grows

    private LinkedMap<Key, Value>[] buckets; // array of linked maps

    public HashMap() {
        this(31);
    }

    public HashMap(int M) {
        this.M = M;
        buckets = (LinkedMap<Key, Value>[]) new LinkedMap[M];
        for (int i = 0; i < M; i++) {
            buckets[i] = new LinkedMap<Key, Value>();
        }
    }

    public Iterator<LinkedMap> iterator() {
        return new HashMapIterator();
    }

    private class HashMapIterator implements Iterator<LinkedMap> {
        private int current = 0;
        public boolean hasNext() {
            return current < M - 1;
        }
        public LinkedMap next() {
            while(buckets[current].isEmpty() && current < M - 1) current++;
            return buckets[current++];
        }
        public void remove() { }
    }

    private int hash(Key key) { // Mask the hashCode so that it is positive
        return (key.hashCode() & 0x7fffffff) % M;
    }

    public void grow() {
        if (primeIndex == primes.length - 1) return; // largest prime in the array was used, so do not grow
        M = primes[++primeIndex]; // use the next larger prime for new array size
        LinkedMap<Key, Value>[] newBuckets = (LinkedMap<Key, Value>[]) new LinkedMap[M];
        for (int i = 0; i < M; i++) {
            newBuckets[i] = new LinkedMap<Key, Value>();
        }
        for (int i = 0; i < buckets.length; i++) {
            LinkedMap<Key, Value> l = buckets[i];
            for (Entry<Key, Value> e : l) { // rehash keys in each entry of LinkedMap
                newBuckets[hash(e.key)].put(e.key, e.value); // and reinsert into appropriate bucket
            }
        }
        buckets = newBuckets;
    }

    public void put(Key key, Value value) {
        if ((double)(N + 1) / M >= MAX_LOAD_FACTOR) grow(); // load factor exceeded, so grow bucket array
        try { // attempt to hash key and place in appropriate bucket
            buckets[hash(key)].put(key, value);
            N++;
        } catch (Exception e) {
            return; // do nothing if exception is caught
        }
    }

    // Retrieve the value associated with the key.
    public Value get(Key key) {
        return (Value) buckets[hash(key)].get(key);
    }

    // Remove and return the value associated with the key.
    public void remove(Key key) {
        buckets[hash(key)].remove(key);
        N--;
    }

    // Return the number of elements in the map.
    public int size() {
        return N;
    }

    // Return true if there are no elements in the map.
    public boolean isEmpty() {
        return N == 0;
    }

}