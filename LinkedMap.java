import java.util.Iterator;

class LinkedMap<Key, Value> implements Map<Key, Value>, Iterable<Entry> {

    private Entry dummy;
    private int size;

    public LinkedMap() {
        dummy = new Entry();
        size = 0;
    }

    public Iterator<Entry> iterator() {
        return new LinkedMapIterator();
    }

    private class LinkedMapIterator implements Iterator<Entry> {
        private Entry current = dummy;
        public boolean hasNext() { return current.next != null; }
        public Entry next() {
            Entry t = current.next;
            current = current.next;
            return t;
        }
        public void remove() { }
    }

    public void put(Key key, Value value) {
        Entry current = dummy;
        while (current.next != null) {
            if (current.next.key.equals(key)) {
//                 current.next.value = value;
//                 return;
                throw new IllegalStateException("Key already exists.");
            }
            current = current.next;
        }
        current.next = new Entry(key, value);
        size++;
    }

    public Value get(Key key) {
        Entry current = dummy;
        while (current.next != null) {
            if (current.next.key.equals(key)) {
                return (Value)current.next.value;
            }
            current = current.next;
        }
        return null;
    }

    public void remove(Key key) {
        Entry current = dummy;
        while (current.next != null) {
            if (current.next.key.equals(key)) {
                System.out.println(current.next.key); // To avoid 0 ms results
                current.next = current.next.next;
                size--;
                return;
            }
            current = current.next;
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        Entry current = dummy;
        while (current.next != null) {
            sb.append(current.next + " ");
            current = current.next;
        }
        return sb.toString();
    }

}