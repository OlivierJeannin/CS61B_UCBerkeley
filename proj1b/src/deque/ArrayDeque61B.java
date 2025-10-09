package deque;

import java.util.ArrayList;
import java.util.List;

public class ArrayDeque61B<T> implements Deque61B<T> {
    private T[] arr;
    private int nextFirst;
    private int nextLast;
    private int size;

    public ArrayDeque61B() {
        arr = (T[]) new Object[8];
        nextFirst = 3;
        nextLast = 4;
        size = 0;
    }

    @Override
    public void addFirst(T x) {
        arr[nextFirst] = x;
        nextFirst = Math.floorMod(nextFirst - 1, arr.length);
        size++;
    }

    @Override
    public void addLast(T x) {
        arr[nextLast] = x;
        nextLast = Math.floorMod(nextLast + 1, arr.length);
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(get(i));
        }
        return list;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public T removeFirst() {
//        size--;
        return null;
    }

    @Override
    public T removeLast() {
//        size--;
        return null;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return arr[Math.floorMod(nextFirst + index + 1, arr.length)];
    }

    @Override
    public T getRecursive(int index) {
        throw new UnsupportedOperationException("No need to implement getRecursive for proj 1b");
    }
}
