package kjry.ecommerce.datamodels;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DynamicList<T> implements Iterable<T> {

    private int capacity = 10;
    private int size = 0;
    private T[] arr;

    @SuppressWarnings("unchecked")
    public DynamicList() {
        arr = (T[]) new Object[capacity];
    }

    @SuppressWarnings("unchecked")
    public DynamicList(T[] x) {
        int arrSize = x.length;
        if (arrSize > capacity) {
            capacity = arrSize * 2;
        }
        arr = (T[]) new Object[capacity];
        System.arraycopy(x, 0, arr, 0, arrSize);
        size = arrSize;
    }

    @SuppressWarnings("unchecked")
    public void append(T x) {
        if (size >= capacity) {
            T[] newArr = (T[]) new Object[capacity * 2];
            System.arraycopy(arr, 0, newArr, 0, size);
            arr = newArr;
            capacity *= 2;
        }
        arr[size] = x;
        size++;
    }

    public T get(int idx) {
        if (idx < 0 || idx >= size) {
            return null;
        }
        return arr[idx];
    }

    public void remove(int idx) {
        if (idx < 0 || idx >= size) {
            return;
        }

        for (int i = idx; i < size - 1; i++) {
            arr[i] = arr[i + 1];
        }

        arr[size - 1] = null;
        size--;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new DynamicListIterator();
    }

    private class DynamicListIterator implements Iterator<T> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return arr[currentIndex++];
        }

        @Override
        public void remove() {
            if (currentIndex <= 0) {
                throw new IllegalStateException("next() has not been called yet");
            }
            DynamicList.this.remove(--currentIndex);
        }
    }
}
