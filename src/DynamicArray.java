// Simple dynamic array of ints, like a mini ArrayList
public class DynamicArray {

    private int[] data;
    private int size;

    // counters used only for benchmarking (comparisons / moves)
    public static long accessCount = 0;
    public static long comparisonCount = 0;

    public DynamicArray() {
        data = new int[10];
        size = 0;
    }

    public static void resetCounters() {
        accessCount = 0;
        comparisonCount = 0;
    }

    public int size() {
        return size;
    }

    private void grow() {
        int[] bigger = new int[data.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = data[i];
        }
        data = bigger;
    }

    // add at the end
    public void add(int x) {
        if (size == data.length) {
            grow();
        }
        data[size] = x;
        size++;
    }

    // add at a given index, shift everything after it to the right
    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("bad index: " + index);
        }
        if (size == data.length) {
            grow();
        }
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
            accessCount++;
        }
        data[index] = x;
        size++;
    }

    // remove element at index, shift everything after it to the left
    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("bad index: " + index);
        }
        int removed = data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
            accessCount++;
        }
        size--;
        return removed;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("bad index: " + index);
        }
        accessCount++;
        return data[index];
    }

    public boolean contains(int x) {
        for (int i = 0; i < size; i++) {
            comparisonCount++;
            if (data[i] == x) {
                return true;
            }
        }
        return false;
    }
}
