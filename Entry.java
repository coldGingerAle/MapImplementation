class Entry<Key, Value> {
    Key key;
    Value value;
    Entry next;
    public Entry() {};
    public Entry(Key key, Value value) {
        this.key = key;
        this.value = value;
    }
    public Entry(Key key, Value value, Entry next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    @Override
    public String toString() {
        return "{ Key: " + key + ", Value: " + value + " }";
    }
}