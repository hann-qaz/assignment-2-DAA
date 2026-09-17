// Simple singly linked list of ints, written by hand (not java.util.LinkedList)
public class LinkedList {

    private static class Node {
        int value;
        Node next;
        Node(int value) {
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public static long accessCount = 0;
    public static long comparisonCount = 0;

    public static void resetCounters() {
        accessCount = 0;
        comparisonCount = 0;
    }

    public int size() {
        return size;
    }

    // add at the end, O(1) because we keep a tail pointer
    public void add(int x) {
        Node node = new Node(x);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    // add at a given index, need to walk to the node before it
    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("bad index: " + index);
        }
        if (index == size) {
            add(x);
            return;
        }
        Node node = new Node(x);
        if (index == 0) {
            node.next = head;
            head = node;
            size++;
            return;
        }
        Node prev = head;
        for (int i = 0; i < index - 1; i++) {
            prev = prev.next;
            accessCount++;
        }
        node.next = prev.next;
        prev.next = node;
        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("bad index: " + index);
        }
        int removed;
        if (index == 0) {
            removed = head.value;
            head = head.next;
            if (head == null) {
                tail = null;
            }
        } else {
            Node prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
                accessCount++;
            }
            Node target = prev.next;
            removed = target.value;
            prev.next = target.next;
            if (target == tail) {
                tail = prev;
            }
        }
        size--;
        return removed;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("bad index: " + index);
        }
        Node cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
            accessCount++;
        }
        accessCount++;
        return cur.value;
    }

    public boolean contains(int x) {
        Node cur = head;
        while (cur != null) {
            comparisonCount++;
            if (cur.value == x) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }
}
