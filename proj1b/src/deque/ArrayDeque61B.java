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
        nextFirst = arr.length - 1;
        nextLast = 0;
        size = 0;
    }

    /** Assumes that the requirements for a resizing are met. */
    private void resize(int length) {
        T[] newArr = (T[]) new Object[length];
        for (int i = 0; i < size; i++) {
            newArr[i] = arr[Math.floorMod(nextFirst + i + 1, arr.length)];
        }
        arr = newArr;
        nextFirst = newArr.length - 1;
        nextLast = size;
    }

    @Override
    public void addFirst(T x) {
        if (size >= arr.length) {
            resize(2 * arr.length);
        }
        arr[nextFirst] = x;
        nextFirst = Math.floorMod(nextFirst - 1, arr.length);
        size++;
    }

    @Override
    public void addLast(T x) {
        if (size >= arr.length) {
            resize(2 * arr.length);
        }
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
        if (size == 0) {
            return null;
        }
        if (arr.length >= 16 && size <= arr.length / 4) {
            resize(arr.length / 2);
        }
        nextFirst = Math.floorMod(nextFirst + 1, arr.length);
        size--;
        return arr[nextFirst];
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        if (arr.length >= 16 && size <= arr.length / 4) {
            resize(arr.length / 2);
        }
        nextLast = Math.floorMod(nextLast - 1, arr.length);
        size--;
        return arr[nextLast];
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
