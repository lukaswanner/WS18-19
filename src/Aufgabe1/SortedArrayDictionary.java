package Aufgabe1;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SortedArrayDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {

    private static final int DEF_CAPACITY = 16;
    private int size;
    private Entry<K, V>[] data;

    public SortedArrayDictionary() {
        size = 0;
        data = new Entry[DEF_CAPACITY];
    }

    public void ensureCapacity(int newCapacity) {
        if (newCapacity < size) {
            return;
        }
        Entry[] old = data;
        data = new Entry[newCapacity];
        System.arraycopy(old, 0, data, 0, size);
    }

    private int searchKey(K key) {
        int li = 0;
        int re = size - 1;

        while (re >= li) {
            int m = (li + re) / 2;
            if (key.compareTo(data[m].getKey()) < 0) {
                re = m - 1;
            } else if (key.compareTo(data[m].getKey()) > 0) {
                li = m + 1;
            } else {
                return m;
            }
        }
        return -1;
    }

    //most simple search con : takes too long with big dataset
    private int simplesearchKey(K key) {
        for (int i = 0; i < size; i++) {
            if (data[i].getKey().equals(key)) {
                return i;
            }
        }
        return -1; //not found
    }

    @Override
    public V search(K key) {
        int i = searchKey(key);
        if (i >= 0) {
            return data[i].getValue();
        } else {
            return null;
        }
    }

    @Override
    public V insert(K key, V value) {
        int i = searchKey(key);
        //if key is already in the data set
        if (i >= 0) {
            V oldvalue = data[i].getValue();
            data[i].setValue(value);
            return oldvalue;
        }
        //if key isn't already in the data set
        if (data.length == size) {
            ensureCapacity(2 * size);
        }
        int j = size - 1;
        //shift everything starting from the back one position forward
        while (j >= 0 && key.compareTo(data[j].getKey()) < 0) {
            data[j + 1] = data[j];
            j--;
        }
        //insert new key at the created space for it
        data[j + 1] = new Entry<K, V>(key, value);
        size++;
        return null;

    }


    @Override
    public V remove(K key) {
        int i = searchKey(key);
        if (i == -1)
            return null;
        V r = data[i].getValue();
        for (int j = i; j < size - 1; j++)
            data[j] = data[j + 1];
        data[--size] = null;
        return r;

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() { return new SortedIterator();}

    private class SortedIterator implements Iterator {
        int current = 0;

        @Override
        public boolean hasNext() { return current < size && data[current] != null; }

        @Override
        public Entry<K,V> next() {
            if(!hasNext())
                throw new NoSuchElementException();
            return data[current++];
        }
    }
}
