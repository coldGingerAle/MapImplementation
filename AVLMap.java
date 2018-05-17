import java.util.*;

class AVLMap<Key extends Comparable<Key>, Value> implements Map<Key, Value> {

    private AVLNode<Key, Value> root;

    // AVLMap constructor
    public AVLMap() {}

    // Put key value pair into map
    public void put(Key key, Value value) {
        root = put(key, value, root);
    }

    private AVLNode<Key, Value> put(Key key, Value value, AVLNode<Key, Value> root) {
        if (root == null) return new AVLNode<Key, Value>(key, value); // Add key since it was not found
        int compare = key.compareTo(root.key);
        if (compare < 0) {
            root.left = put(key, value, root.left); // Smaller, so add to left subtree
        } else if (compare > 0) {
            root.right = put(key, value, root.right); // Larger, so add to right subtree
        }
        root.updateEverything();
        return rebalance(root);
    }

    // Removes the entry with given key if it is in tree
    public void remove(Key key) { root = remove(key, root); }

    private AVLNode<Key, Value> remove(Key key, AVLNode<Key, Value> root) {
        if (root == null) return null;
        int compare = key.compareTo(root.key); // Key is less than root key, so remove from left subtree
        if (compare < 0) {
            root.left = remove(key, root.left);
        } else if (compare > 0) { // Key is greater than root key, so remove from right subtree
            root.right = remove(key, root.right);
        } else {
            if (root.left == null) return root.right; // Case with leaf node or one child on right
            else if (root.right == null) return root.left; // One child on left
            AVLNode<Key, Value> n = root.left; // Case with two children
            while (n.right != null) n = n.right; // Find predecessor
            root.key = n.key;
            root.value = n.value;
            root.left = remove(n.key, root.left);
        }
        root.updateEverything();
        return rebalance(root);
    }

    // Returns the number of elements in the map
    public int size() {
        return root == null ? 0 : root.size;
    }

    // Returns true if there are no elements in the map
    public boolean isEmpty() { return root == null; }

    // Retrieves value associated with Key
    public Value get(Key key) { return get(key, root); }

    public Value get(Key key, AVLNode<Key, Value> root) {
        if (root == null) return null;
        int compare = key.compareTo(root.key);
        if (compare < 0) {
            return get(key, root.left);
        } else if (compare > 0) {
            return get(key, root.right);
        }
        return root.value;
    }

    // Node for the AVL Map
    private static class AVLNode<Key, Value> {
        Key key;
        Value value;
        AVLNode<Key, Value> left;
        AVLNode<Key, Value> right;
        int height = 0, size = 1, balance = 0;

        AVLNode(Key key, Value value) {
            this.key = key;
            this.value = value;
        }

        // Update height, size, and balance factor
        void updateEverything() {
            int heightLeft = left == null ? -1 : left.height;
            int heightRight = right == null ? -1 : right.height;
            int sizeLeft = left == null ? 0 : left.size;
            int sizeRight = right == null ? 0 : right.size;
            height = 1 + Math.max(heightLeft, heightRight);
            size = 1 + sizeLeft + sizeRight;
            balance = heightRight - heightLeft;
        }

        @Override
        public String toString() { return "(Key: " + key + ", Value: " + value + ")"; }
    }

    // Right rotate, update size and balance, and return new root
    AVLNode<Key, Value> rightRotate(AVLNode<Key, Value> root) {
        AVLNode<Key, Value> pivot = root.left;
        AVLNode<Key, Value> pivotRightSubtree = pivot.right;

        // Perform right rotation
        pivot.right = root;
        root.left = pivotRightSubtree;

        // Update heights, size, balance factor
        root.updateEverything();
        pivot.updateEverything();

        // Return new root
        return pivot;
    }

    // Left rotate, update size and balance, and return new root
    AVLNode<Key, Value> leftRotate(AVLNode<Key, Value> root) {
        AVLNode<Key, Value> pivot = root.right;
        AVLNode<Key, Value> pivotLeftSubtree = pivot.left;

        // Perform left rotation
        pivot.left = root;
        root.right = pivotLeftSubtree;

        // Update height, size, balance factor
        root.updateEverything();
        pivot.updateEverything();

        // Return new root
        return pivot;
    }

    // Rebalance AVLMap
    private AVLNode<Key, Value> rebalance(AVLNode<Key, Value> root) {
        if (root.balance == -2) {
            if (root.left.balance == 1)
                root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        else if (root.balance == 2) {
            if (root.right.balance == -1)
                root.right = rightRotate(root.right);
            return leftRotate(root);
        }
        return root;
    }

    // Print the in order traversal of the tree
    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(AVLNode<Key, Value> n) {
        if (n == null) return;
        printInOrder(n.left);
        System.out.println(n);
        printInOrder(n.right);
    }

    // Print the level order traversal of the tree
    public void printLevelOrder() {
        Queue<AVLNode<Key, Value>> queue = new ArrayDeque<AVLNode<Key, Value>>();
        queue.add(root);
        while (!queue.isEmpty()) {
            AVLNode<Key, Value> n = queue.remove();
            System.out.println(n);
            if (n.left != null) queue.add(n.left);
            if (n.right != null) queue.add(n.right);
        }
        System.out.println();
    }

}