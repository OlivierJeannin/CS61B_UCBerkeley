package hashmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 *  A hash table-backed Map implementation.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!

    private final int initialCapacity;
    private final double maxLoadFactor;

    private int capacity;
    private int size;

    /** Constructors */
    public MyHashMap() {
        this(16, 0.75);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, 0.75);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor maximum load factor
     */
    public MyHashMap(int initialCapacity, double loadFactor) {
        this.initialCapacity = initialCapacity;
        this.maxLoadFactor = loadFactor;

        capacity = initialCapacity;
        size = 0;

        buckets = new Collection[initialCapacity];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *  Note that that this is referring to the hash table bucket itself,
     *  not the hash map itself.
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    // Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    /**
     * Associates the specified value with the specified key in this map.
     * If the map already contains the specified key, replaces the key's mapping
     * with the value specified.
     */
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException();
        }

        Node node = getNode(key);
        if (node != null) {
            // key was found present in this map; replace value
            node.value = value;
            return;
        }

        // key not found in this map; add a new mapping
        node = new Node(key, value);
        getBucket(buckets, key).add(node);
        size++;

        if (currentLoadFactor() > maxLoadFactor) {
            resize();
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        Node node = getNode(key);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    /**
     * Returns whether this map contains a mapping for the specified key.
     */
    @Override
    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    /**
     * Returns the number of key-value mappings in this map.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Removes every mapping from this map.
     */
    @Override
    public void clear() {
        buckets = new Collection[initialCapacity];  // use initialCapacity as length of empty array
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = createBucket();
        }

        capacity = initialCapacity;
        size = 0;
    }

    /* TODO optional tasks */

    /**
     * Returns a Set view of the keys contained in this map. Not required for this lab.
     * If you don't implement this, throw an UnsupportedOperationException.
     */
    @Override
    public Set<K> keySet() {
        return null;
    }

    /**
     * Removes the mapping for the specified key from this map if present,
     * or null if there is no such mapping.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException.
     *
     * @param key
     */
    @Override
    public V remove(K key) {
        return null;
    }

    /**
     * Returns an iterator over elements of type {@code K}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<K> iterator() {
        return null;
    }

    /* Private Helpers */

    /**
     * Returns the current load factor (N / M) of this hash map.
     */
    private double currentLoadFactor() {
        return size * 1.0 / capacity;
    }

    /**
     * Returns the address of the bucket which the given key should fall into,
     * according to the key's hashcode.
     *
     * @param buckets the array of all available buckets
     */
    private Collection<Node> getBucket(Collection<Node>[] buckets, K key) {
        int index = Math.floorMod(key.hashCode(), buckets.length);
        return buckets[index];
    }

    /**
     * Returns the node containing the given key, or null if no such node was found.
     */
    private Node getNode(K key) {
        Collection<Node> bucket = getBucket(buckets, key);
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Doubles the capacity of {@code buckets} array, and re-hashes all the elements.
     * Assumes N / M is larger than the maximum load factor.
     */
    private void resize() {
        capacity *= 2;
        Collection<Node>[] newBuckets = new Collection[capacity];
        for (int i = 0; i < newBuckets.length; i++) {
            newBuckets[i] = createBucket();
        }

        // re-hash all elements
        for (Collection<Node> bucket : buckets) {
            for (Node node : bucket) {
                getBucket(newBuckets, node.key).add(node);
            }
        }

        buckets = newBuckets;
    }
}
