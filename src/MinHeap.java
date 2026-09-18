// Simple binary min-heap stored in an array
public class MinHeap {

    private int[] data;
    private int size;

    public static long comparisonCount = 0;

    public MinHeap() {
        data = new int[10];
        size = 0;
    }

    public static void resetCounters() {
        comparisonCount = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void grow() {
        int[] bigger = new int[data.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = data[i];
        }
        data = bigger;
    }

    private void swap(int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    public void insert(int x) {
        if (size == data.length) {
            grow();
        }
        data[size] = x;
        int i = size;
        size++;

        // sift up: while parent is bigger than child, swap
        while (i > 0) {
            int parent = (i - 1) / 2;
            comparisonCount++;
            if (data[parent] <= data[i]) {
                break;
            }
            swap(parent, i);
            i = parent;
        }
    }

    public int peekMin() {
        if (size == 0) {
            throw new RuntimeException("heap is empty");
        }
        return data[0];
    }

    public int extractMin() {
        if (size == 0) {
            throw new RuntimeException("heap is empty");
        }
        int min = data[0];
        size--;
        data[0] = data[size];

        // sift down: swap with the smaller child until heap property holds
        int i = 0;
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;

            if (left < size) {
                comparisonCount++;
                if (data[left] < data[smallest]) {
                    smallest = left;
                }
            }
            if (right < size) {
                comparisonCount++;
                if (data[right] < data[smallest]) {
                    smallest = right;
                }
            }
            if (smallest == i) {
                break;
            }
            swap(i, smallest);
            i = smallest;
        }
        return min;
    }
}
