import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

// Runs the 4 required workloads and writes CSV tables into results/tables
public class Benchmark {

    static final int[] SIZES = {100, 1000, 10000, 100000};
    static final int REPEATS = 5;
    static final long SEED = 42;

    public static void main(String[] args) throws IOException {
        workload1RandomAccess();
        workload2Search();
        workload3InsertRemove();
        workload4Heap();
        System.out.println("Done. CSV files written to results/tables/");
    }

    // ---------- Workload 1: Random Access ----------
    static void workload1RandomAccess() throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter("results/tables/workload1_random_access.csv"));
        out.println("structure,n,avg_time_ns,accesses");

        for (int n : SIZES) {
            long timeArr = 0, timeList = 0;
            for (int r = 0; r < REPEATS; r++) {
                Random rnd = new Random(SEED);
                DynamicArray arr = new DynamicArray();
                LinkedList list = new LinkedList();
                for (int i = 0; i < n; i++) {
                    int x = rnd.nextInt();
                    arr.add(x);
                    list.add(x);
                }
                int[] indices = new int[10000];
                for (int i = 0; i < 10000; i++) {
                    indices[i] = rnd.nextInt(n);
                }

                DynamicArray.resetCounters();
                long t1 = System.nanoTime();
                for (int idx : indices) {
                    arr.get(idx);
                }
                long t2 = System.nanoTime();
                timeArr += (t2 - t1);

                LinkedList.resetCounters();
                long t3 = System.nanoTime();
                for (int idx : indices) {
                    list.get(idx);
                }
                long t4 = System.nanoTime();
                timeList += (t4 - t3);
            }
            out.println("DynamicArray," + n + "," + (timeArr / REPEATS) + ",10000");
            out.println("LinkedList," + n + "," + (timeList / REPEATS) + ",10000");
        }
        out.close();
    }

    // ---------- Workload 2: Search ----------
    static void workload2Search() throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter("results/tables/workload2_search.csv"));
        out.println("structure,n,avg_time_ns,comparisons");

        for (int n : SIZES) {
            long timeArr = 0, timeList = 0;
            long compArr = 0, compList = 0;
            for (int r = 0; r < REPEATS; r++) {
                Random rnd = new Random(SEED);
                DynamicArray arr = new DynamicArray();
                LinkedList list = new LinkedList();
                for (int i = 0; i < n; i++) {
                    int x = rnd.nextInt();
                    arr.add(x);
                    list.add(x);
                }
                int[] values = new int[1000];
                for (int i = 0; i < 1000; i++) {
                    values[i] = rnd.nextInt();
                }

                DynamicArray.resetCounters();
                long t1 = System.nanoTime();
                for (int v : values) {
                    arr.contains(v);
                }
                long t2 = System.nanoTime();
                timeArr += (t2 - t1);
                compArr += DynamicArray.comparisonCount;

                LinkedList.resetCounters();
                long t3 = System.nanoTime();
                for (int v : values) {
                    list.contains(v);
                }
                long t4 = System.nanoTime();
                timeList += (t4 - t3);
                compList += LinkedList.comparisonCount;
            }
            out.println("DynamicArray," + n + "," + (timeArr / REPEATS) + "," + (compArr / REPEATS));
            out.println("LinkedList," + n + "," + (timeList / REPEATS) + "," + (compList / REPEATS));
        }
        out.close();
    }

    // ---------- Workload 3: Insertion and Removal ----------
    static void workload3InsertRemove() throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter("results/tables/workload3_insert_remove.csv"));
        out.println("structure,n,position,operation,avg_time_ns,movements");

        for (int n : SIZES) {
            runInsertRemove(out, n, 0, "begin");
            runInsertRemove(out, n, n / 2, "middle");
        }
        out.close();
    }

    static void runInsertRemove(PrintWriter out, int n, int position, String label) {
        long insTimeArr = 0, remTimeArr = 0, insTimeList = 0, remTimeList = 0;
        long insMoveArr = 0, remMoveArr = 0, insMoveList = 0, remMoveList = 0;

        for (int r = 0; r < REPEATS; r++) {
            Random rnd = new Random(SEED);
            DynamicArray arr = new DynamicArray();
            LinkedList list = new LinkedList();
            for (int i = 0; i < n; i++) {
                int x = rnd.nextInt();
                arr.add(x);
                list.add(x);
            }
            int[] toInsert = new int[1000];
            for (int i = 0; i < 1000; i++) {
                toInsert[i] = rnd.nextInt();
            }

            // A. insertion
            DynamicArray.resetCounters();
            long t1 = System.nanoTime();
            for (int x : toInsert) {
                arr.add(position, x);
            }
            long t2 = System.nanoTime();
            insTimeArr += (t2 - t1);
            insMoveArr += DynamicArray.accessCount;

            LinkedList.resetCounters();
            long t3 = System.nanoTime();
            for (int x : toInsert) {
                list.add(position, x);
            }
            long t4 = System.nanoTime();
            insTimeList += (t4 - t3);
            insMoveList += LinkedList.accessCount;

            // B. removal (restore by removing what we just inserted)
            DynamicArray.resetCounters();
            long t5 = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                arr.remove(position);
            }
            long t6 = System.nanoTime();
            remTimeArr += (t6 - t5);
            remMoveArr += DynamicArray.accessCount;

            LinkedList.resetCounters();
            long t7 = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                list.remove(position);
            }
            long t8 = System.nanoTime();
            remTimeList += (t8 - t7);
            remMoveList += LinkedList.accessCount;
        }

        out.println("DynamicArray," + n + "," + label + ",insert," + (insTimeArr / REPEATS) + "," + (insMoveArr / REPEATS));
        out.println("LinkedList," + n + "," + label + ",insert," + (insTimeList / REPEATS) + "," + (insMoveList / REPEATS));
        out.println("DynamicArray," + n + "," + label + ",remove," + (remTimeArr / REPEATS) + "," + (remMoveArr / REPEATS));
        out.println("LinkedList," + n + "," + label + ",remove," + (remTimeList / REPEATS) + "," + (remMoveList / REPEATS));
    }

    // ---------- Workload 4: Priority Processing (Min-Heap) ----------
    static void workload4Heap() throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter("results/tables/workload4_heap.csv"));
        out.println("n,avg_insert_time_ns,avg_extract_time_ns,avg_comparisons,sorted_correctly");

        for (int n : SIZES) {
            long insTime = 0, extTime = 0, comparisons = 0;
            boolean sortedOk = true;

            for (int r = 0; r < REPEATS; r++) {
                Random rnd = new Random(SEED);
                int[] values = new int[n];
                for (int i = 0; i < n; i++) {
                    values[i] = rnd.nextInt();
                }

                MinHeap heap = new MinHeap();
                MinHeap.resetCounters();
                long t1 = System.nanoTime();
                for (int v : values) {
                    heap.insert(v);
                }
                long t2 = System.nanoTime();
                insTime += (t2 - t1);

                long t3 = System.nanoTime();
                int last = Integer.MIN_VALUE;
                for (int i = 0; i < n; i++) {
                    int val = heap.extractMin();
                    if (val < last) {
                        sortedOk = false;
                    }
                    last = val;
                }
                long t4 = System.nanoTime();
                extTime += (t4 - t3);
                comparisons += MinHeap.comparisonCount;
            }
            out.println(n + "," + (insTime / REPEATS) + "," + (extTime / REPEATS) + "," + (comparisons / REPEATS) + "," + sortedOk);
        }
        out.close();
    }
}
