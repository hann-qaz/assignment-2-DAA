import java.util.ArrayList;
import java.util.Random;

// Simple manual tests, no JUnit, just prints PASS/FAIL
public class Tests {

    static int passed = 0;
    static int failed = 0;

    static void check(boolean condition, String name) {
        if (condition) {
            passed++;
            System.out.println("PASS: " + name);
        } else {
            failed++;
            System.out.println("FAIL: " + name);
        }
    }

    public static void main(String[] args) {

        testDynamicArray();
        testLinkedList();
        testMinHeap();
        testAgainstJavaCollections();

        System.out.println();
        System.out.println("Total: " + passed + " passed, " + failed + " failed");
    }

    static void testDynamicArray() {
        System.out.println("--- DynamicArray tests ---");
        DynamicArray a = new DynamicArray();

        // empty structure
        check(a.size() == 0, "empty array size is 0");
        check(!a.contains(5), "empty array contains nothing");

        // one element
        a.add(10);
        check(a.size() == 1, "size 1 after one add");
        check(a.get(0) == 10, "get(0) returns 10");

        // multiple elements
        a.add(20);
        a.add(30);
        check(a.size() == 3, "size 3 after adds");

        // duplicate values
        a.add(10);
        check(a.contains(10), "contains duplicate value 10");

        // insert in the middle
        a.add(1, 99);
        check(a.get(1) == 99, "add(index,x) inserted correctly");

        // boundary indices
        check(a.get(a.size() - 1) == 10, "get last index works");

        // remove
        int removed = a.remove(0);
        check(removed == 10, "remove(0) returns old first value");

        // invalid index
        boolean threw = false;
        try {
            a.get(999);
        } catch (IndexOutOfBoundsException e) {
            threw = true;
        }
        check(threw, "get(invalid index) throws exception");

        // large input
        DynamicArray big = new DynamicArray();
        for (int i = 0; i < 50000; i++) {
            big.add(i);
        }
        check(big.size() == 50000, "large array has correct size");
        check(big.get(49999) == 49999, "large array last element correct");
    }

    static void testLinkedList() {
        System.out.println("--- LinkedList tests ---");
        LinkedList list = new LinkedList();

        check(list.size() == 0, "empty list size is 0");
        check(!list.contains(1), "empty list contains nothing");

        list.add(5);
        check(list.size() == 1 && list.get(0) == 5, "one element add/get");

        list.add(6);
        list.add(7);
        list.add(1, 100);
        check(list.get(1) == 100, "add(index,x) works");

        list.add(5); // duplicate
        check(list.contains(5), "contains duplicate value");

        int removed = list.remove(0);
        check(removed == 5, "remove(0) returns old head value");

        boolean threw = false;
        try {
            list.get(-1);
        } catch (IndexOutOfBoundsException e) {
            threw = true;
        }
        check(threw, "get(-1) throws exception");

        LinkedList big = new LinkedList();
        for (int i = 0; i < 50000; i++) {
            big.add(i);
        }
        check(big.size() == 50000, "large list has correct size");
        check(big.get(0) == 0 && big.get(49999) == 49999, "large list boundaries correct");
    }

    static void testMinHeap() {
        System.out.println("--- MinHeap tests ---");
        MinHeap heap = new MinHeap();

        check(heap.isEmpty(), "new heap is empty");

        heap.insert(5);
        check(heap.peekMin() == 5, "peekMin after one insert");

        heap.insert(3);
        heap.insert(8);
        heap.insert(1);
        heap.insert(3); // duplicate value
        check(heap.peekMin() == 1, "peekMin returns smallest");

        int last = Integer.MIN_VALUE;
        boolean nonDecreasing = true;
        int count = 0;
        while (!heap.isEmpty()) {
            int val = heap.extractMin();
            if (val < last) {
                nonDecreasing = false;
            }
            last = val;
            count++;
        }
        check(nonDecreasing, "extractMin returns non-decreasing sequence");
        check(count == 5, "extracted correct number of elements");

        // large input + heap property check via sorted comparison
        MinHeap big = new MinHeap();
        Random rnd = new Random(42);
        int n = 20000;
        int[] values = new int[n];
        for (int i = 0; i < n; i++) {
            values[i] = rnd.nextInt(1000000);
            big.insert(values[i]);
        }
        java.util.Arrays.sort(values);
        boolean ok = true;
        for (int i = 0; i < n; i++) {
            if (big.extractMin() != values[i]) {
                ok = false;
                break;
            }
        }
        check(ok, "large heap extracts elements in fully sorted order");
    }

    // compare our structures against java.util.ArrayList for correctness
    static void testAgainstJavaCollections() {
        System.out.println("--- Compare with java.util.ArrayList ---");
        Random rnd = new Random(42);
        DynamicArray mine = new DynamicArray();
        ArrayList<Integer> ref = new ArrayList<>();

        for (int i = 0; i < 2000; i++) {
            int x = rnd.nextInt(10000);
            mine.add(x);
            ref.add(x);
        }

        boolean same = true;
        for (int i = 0; i < ref.size(); i++) {
            if (mine.get(i) != ref.get(i)) {
                same = false;
                break;
            }
        }
        check(same, "DynamicArray matches ArrayList element by element");

        boolean containsSame = mine.contains(ref.get(500)) == ref.contains(ref.get(500));
        check(containsSame, "DynamicArray.contains matches ArrayList.contains");
    }
}
