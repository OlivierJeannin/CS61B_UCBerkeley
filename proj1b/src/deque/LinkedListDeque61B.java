package deque;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LinkedListDeque61B<T> implements Deque61B<T> {
    private class Node {
        T value;
        Node prev;
        Node next;

        Node(T value) {
            this.value = value;
            this.prev = null;
            this.next = null;
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque61B() {
        sentinel = new Node(null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;

        this.size = 0;
    }

    @Override
    public void addFirst(T x) {
        Node node = new Node(x);
        Node oldFirst = sentinel.next;

        node.prev = sentinel;
        sentinel.next = node;

        oldFirst.prev = node;
        node.next = oldFirst;

        this.size++;
    }

    @Override
    public void addLast(T x) {
        Node node = new Node(x);
        Node oldLast = sentinel.prev;

        node.prev = oldLast;
        oldLast.next = node;

        sentinel.prev = node;
        node.next = sentinel;

        this.size++;
    }

    @Override
    public List<T> toList() {
        List<T> list = new ArrayList<>();

        Node n = sentinel.next;
        while (n != sentinel) {
            list.add(n.value);
            n = n.next;
        }
        return list;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        Node node = sentinel.next;

        sentinel.next = node.next;
        node.next.prev = sentinel;

        node.prev = null;
        node.next = null;

        size--;

        return node.value;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }

        Node node = sentinel.prev;

        node.prev.next = sentinel;
        sentinel.prev = node.prev;

        node.prev = null;
        node.next = null;

        size--;

        return node.value;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        Node node = sentinel.next;
        while (index > 0) {
            node = node.next;
            index--;
        }
        return node.value;
    }

    @Override
    public T getRecursive(int index) {
        return getRecursiveHelper(sentinel.next, size, index);
    }

    /**
     * Get the element in a sub-queue of Deque61B.
     *
     * @param first the first node of this sub-queue (excluding the sentinel node)
     * @param subSize number of nodes in this sub-queue
     * @param index the index in this sub-queue of the element to get
     * @return element at {@code index} in the sub-queue
     */
    private T getRecursiveHelper(Node first, int subSize, int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        if (index == 0) {
            return first.value;
        }

        return getRecursiveHelper(first.next, subSize - 1, index - 1);
    }

    private class LLDequeIterator implements Iterator<T> {
        private Node curPos;

        public LLDequeIterator() {
            curPos = sentinel;
        }

        @Override
        public boolean hasNext() {
            return curPos.next != sentinel;
        }

        @Override
        public T next() {
            curPos = curPos.next;
            return curPos.value;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new LLDequeIterator();
    }

    @Override
    public boolean equals(Object obj) {
        return equalsHelper(obj);
    }

    @Override
    public String toString() {
        return toStringHelper();
    }
}
