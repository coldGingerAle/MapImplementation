import java.util.*;

class BSTMap<Key extends Comparable<Key>, Value> implements Map<Key, Value> {

    private Node<Key, Value> root;

    // Construct a new BST
    public BSTMap() {}

    // Adds the entry to the tree
    public void put(Key key, Value value) {
        root = put(key, value, root);
    }

    // Returns the number of elements in the map
    public int size() {
        if (root == null) return 0;
        return root.size;
    }

    // Returns true if there are no elements in the map
    public boolean isEmpty() {
        return root == null;
    }

    // Retrieves value associated with key
    public Value get(Key key) {
        return get(key, root);
    }

    public Value get(Key key, Node<Key, Value> root) {
        if (root == null) return null;
        int compare = key.compareTo(root.key);
        if (compare < 0) {
            return get(key, root.left);
        } else if (compare > 0) {
            return get(key, root.right);
        }
        return root.value;
    }

    // Adds the entry to the tree and returns the root
    private Node<Key, Value> put(Key key, Value value, Node<Key, Value> root) {
        if (root == null) return new Node<Key, Value>(key, value); // Add key since it was not found
        int compare = key.compareTo(root.key);
        if (compare < 0) {
            root.left = put(key, value, root.left);
        } else if (compare > 0){
            root.right = put(key, value, root.right);
        }
        // Key is equal to root key, so do not add
        root.updateSize();
        return root;
    }

    // Print the in order traversal of the tree
    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(Node<Key, Value> n) {
        if (n == null) return;
        printInOrder(n.left);
        System.out.println(n);
        printInOrder(n.right);
    }

    // Print the level order traversal of the tree
    public void printLevelOrder() {
        Queue<Node<Key, Value>> queue = new ArrayDeque<Node<Key, Value>>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node<Key, Value> n = queue.remove();
            System.out.println(n);
            if (n.left != null) queue.add(n.left);
            if (n.right != null) queue.add(n.right);
        }
        System.out.println();
    }

    // Removes the entry with given key if it is in tree
    public void remove(Key key) {
        root = remove(key, root);
    }

    // Removes the entry with the specified key and returns the root
    private Node<Key, Value> remove(Key key, Node<Key, Value> root) {
        if (root == null) return null;
        int compare = key.compareTo(root.key);
        if (compare < 0) { // Key is less than root key, so remove from left subtree
            root.left = remove(key, root.left);
        } else if (compare > 0) {
            root.right = remove(key, root.right); // Key is greater than root key, so remove from right subtree
        } else {
            if (root.left == null) return root.right; // Case with leaf node or one child on right
            else if (root.right == null) return root.left; // One child on left
            Node<Key, Value> n = root.left; // Case with two children
            while (n.right != null) n = n.right; // Find predecessor
            root.key = n.key; // Replace key and value of root with that of predecessor
            root.value = n.value;
            root.left = remove(n.key, root.left); // Remove predecessor from left subtree
        }
        root.updateSize();
        return root;
    }

    private class Node<Key, Value> {
        Key key;
        Value value;
        Node<Key, Value> left = null;
        Node<Key, Value> right = null;
        int size = 1;

        public Node(Key key, Value value) {
            this.key = key;
            this.value = value;
        }

        void updateSize() {
            int leftSize = left == null ? 0 : left.size; // 0 if no left child, otherwise get its size
            int rightSize = right == null ? 0 : right.size; // 0 if no right child, otherwise get its size
            size = 1 + leftSize + rightSize;
        }

        @Override
        public String toString() {
            return "(Key: " + key + ", Value: " + value + ")";
        }
    }
}