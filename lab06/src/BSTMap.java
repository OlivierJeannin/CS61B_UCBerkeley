import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

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
        root = putHelperRecursive(key, value, root);
        // putHelperIterative(key, value);
    }

    /** Maps {@code key} to {@code value} in a subtree rooted at {@code r}.
     *
     * @return address of the subtree's root (possibly modified)
     */
    private Node putHelperRecursive(K key, V value, Node r) {
        if (r == null) {
            size += 1;
            return new Node(key, value);
        }
        int cmp = key.compareTo(r.key);
        if (cmp < 0) {
            r.left = putHelperRecursive(key, value, r.left);
        } else if (cmp > 0) {
            r.right = putHelperRecursive(key, value, r.right);
        } else {
            r.value = value;
        }
        return r;
    }

    /** An iterative approach to put(). */
    private void putHelperIterative(K key, V value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return;
        }
        Node curr = root;
        while (true) {
            int cmp = key.compareTo(curr.key);
            if (cmp == 0) {
                curr.value = value;
                return;
            }
            if (cmp < 0) {
                if (curr.left == null) {
                    curr.left = new Node(key, value);
                    size++;
                    return;
                }
                curr = curr.left;
            } else {
                if (curr.right == null) {
                    curr.right = new Node(key, value);
                    size++;
                    return;
                }
                curr = curr.right;
            }
        }
    }

    /** Iteratively traverse this BST for the node with the mapping of the given key.
     *
     * @return address of that node if the mapping is found in this BST, or null if not
     */
    private Node findIteratively(K key) {
        Node curr = root;
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                return curr;
            }
        }
        return null;
    }

    @Override
    public V get(K key) {
        Node n = findIteratively(key);
        if (n == null) {
            return null;
        } else {
            return n.value;
        }
    }

    @Override
    public boolean containsKey(K key) {
        return findIteratively(key) != null;
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

    @Override
    public Set<K> keySet() {
        Set<K> set = new TreeSet<>();
        keySetHelper(set, root);
        return set;
    }

    private void keySetHelper(Set<K> set, Node r) {
        if (r == null) {
            return;
        }
        keySetHelper(set, r.left);
        set.add(r.key);
        keySetHelper(set, r.right);
    }

    @Override
    public V remove(K key) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
