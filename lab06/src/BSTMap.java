import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private class Node {
        private K key;
        private V value;
        private Node left;
        private Node right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            left = null;
            right = null;
        }
    }

    private Node root;
    private int size;

    public BSTMap() {
        root = null;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    /** Maps {@code key} to {@code value} in a subtree rooted at {@code r}.
     * Increases {@code size} of this BST if the key is newly added.
     *
     * @return address of the subtree's root (possibly modified)
     */
    private Node putHelper(K key, V value, Node r) {
        if (r == null) {
            size += 1;
            return new Node(key, value);
        }
        int cmp = key.compareTo(r.key);
        if (cmp < 0) {
            r.left = putHelper(key, value, r.left);
        } else if (cmp > 0) {
            r.right = putHelper(key, value, r.right);
        } else {
            r.value = value;
        }
        return r;
    }

    @Override
    public V get(K key) {
        Node curr = root;
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                return curr.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        Node curr = root;
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // TODO: implement below methods

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
