import pandas as pd
import matplotlib.pyplot as plt

# Plot 1: Random Access - time vs n
df1 = pd.read_csv("tables/workload1_random_access.csv")
plt.figure()
for name, g in df1.groupby("structure"):
    plt.plot(g["n"], g["avg_time_ns"] / 1e6, marker="o", label=name)
plt.xscale("log")
plt.yscale("log")
plt.xlabel("n (initial size)")
plt.ylabel("time, ms (10000 get calls)")
plt.title("Workload 1: Random Access - time vs n")
plt.legend()
plt.grid(True, which="both", alpha=0.3)
plt.savefig("plots/plot1_random_access_time.png", dpi=120, bbox_inches="tight")

# Plot 2: Search - time vs n
df2 = pd.read_csv("tables/workload2_search.csv")
plt.figure()
for name, g in df2.groupby("structure"):
    plt.plot(g["n"], g["avg_time_ns"] / 1e6, marker="o", label=name)
plt.xscale("log")
plt.yscale("log")
plt.xlabel("n (initial size)")
plt.ylabel("time, ms (1000 contains calls)")
plt.title("Workload 2: Search - time vs n")
plt.legend()
plt.grid(True, which="both", alpha=0.3)
plt.savefig("plots/plot2_search_time.png", dpi=120, bbox_inches="tight")

# Plot 3: Search - comparisons vs n
plt.figure()
for name, g in df2.groupby("structure"):
    plt.plot(g["n"], g["comparisons"], marker="o", label=name)
plt.xscale("log")
plt.yscale("log")
plt.xlabel("n (initial size)")
plt.ylabel("total comparisons (1000 contains calls)")
plt.title("Workload 2: Search - comparisons vs n")
plt.legend()
plt.grid(True, which="both", alpha=0.3)
plt.savefig("plots/plot3_search_comparisons.png", dpi=120, bbox_inches="tight")

# Plot 4: Insert/Remove at beginning - time vs n
df3 = pd.read_csv("tables/workload3_insert_remove.csv")
begin_insert = df3[(df3.position == "begin") & (df3.operation == "insert")]
plt.figure()
for name, g in begin_insert.groupby("structure"):
    plt.plot(g["n"], g["avg_time_ns"] / 1e6, marker="o", label=name)
plt.xscale("log")
plt.yscale("log")
plt.xlabel("n (initial size)")
plt.ylabel("time, ms (1000 inserts at index 0)")
plt.title("Workload 3: Insertion at the beginning - time vs n")
plt.legend()
plt.grid(True, which="both", alpha=0.3)
plt.savefig("plots/plot4_insert_begin_time.png", dpi=120, bbox_inches="tight")

# Plot 5: Heap - insert vs extract time vs n
df4 = pd.read_csv("tables/workload4_heap.csv")
plt.figure()
plt.plot(df4["n"], df4["avg_insert_time_ns"] / 1e6, marker="o", label="insert (n times)")
plt.plot(df4["n"], df4["avg_extract_time_ns"] / 1e6, marker="o", label="extractMin (n times)")
plt.xscale("log")
plt.yscale("log")
plt.xlabel("n")
plt.ylabel("time, ms")
plt.title("Workload 4: MinHeap - time vs n")
plt.legend()
plt.grid(True, which="both", alpha=0.3)
plt.savefig("plots/plot5_heap_time.png", dpi=120, bbox_inches="tight")

print("plots saved")
