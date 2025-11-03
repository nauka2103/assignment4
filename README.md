# Graph Algorithms Benchmark — SCC, Topological Sort & DAG Paths

---

## 1. Project Overview

This project implements and benchmarks several core **graph algorithms** — from detecting cycles to finding optimal and critical paths — applied to datasets representing **Smart City / Smart Campus** task dependencies.

The workflow models interdependent operations such as maintenance, scheduling, or logistics using directed graphs and computes:

* **Strongly Connected Components (SCCs)** via *Tarjan’s Algorithm*
* **Condensation DAG** and **Topological Sort**
* **Shortest & Longest Paths** in the DAG
* Runtime metrics and operation counts exported to a `.csv` file

---

## 2. Algorithms Implemented

| **Algorithm**            | **Purpose**                                                    | **Class**                |
| :------------------------ | :------------------------------------------------------------- | :----------------------- |
| **Tarjan SCC**            | Detects all strongly connected components (recursive DFS).     | `TarjanSCC`              |
| **Condensation Graph**    | Compresses SCCs into a DAG structure.                          | `buildCondensation()`    |
| **Topological Sort**      | Orders DAG vertices for dependency resolution.                 | `KahnTopoSort`           |
| **DAG Shortest Path**     | Finds the minimum cumulative weight along DAG edges.           | `DAGShortestPath`        |
| **DAG Longest Path**      | Determines the critical path (max duration).                   | `DAGLongestPath`         |
| **Metrics Module**        | Tracks runtime (ns/ms), DFS calls, and relaxation operations.  | `Metrics`                |

---

## 3. Dataset Description

| **Dataset**     | **Size (Vertices)** | **Type**        | **Goal**                       |
| :-------------- | :------------------ | :--------------- | :------------------------------ |
| `small_1.json`  | ~6–8                | cyclic           | Validate SCC correctness        |
| `small_2.json`  | ~9                  | acyclic          | Test topological sorting        |
| `small_3.json`  | ~10                 | mixed            | SCC + DAG path validation       |
| `medium_1.json` | ~12                 | mixed            | Mid-size SCC performance        |
| `medium_2.json` | ~15                 | DAG              | Shortest path accuracy          |
| `medium_3.json` | ~18                 | cyclic           | SCC scaling validation          |
| `large_1.json`  | ~25                 | dense DAG        | Timing comparison               |
| `large_2.json`  | ~35                 | acyclic          | Path optimization test          |
| `large_3.json`  | ~45                 | dense + cyclic   | Full performance benchmark      |

---

## 4. Experimental Results (from `results/summary.csv`)

| **Dataset**     | **SCC Time (ns)** | **DFS Calls** | **Edges Processed** | **Topo Time (ns)** | **Shortest Path (ns)** | **Relaxations** |
| :-------------- | ----------------: | ------------: | ------------------: | -----------------: | ---------------------: | --------------: |
| medium_1.json   |         29,625 ns |            15 |                  25 |         13,750 ns  |               5,750 ns |               6 |
| large_1.json    |         21,292 ns |            30 |                  65 |          1,833 ns  |               1,417 ns |               2 |
| small_1.json    |          6,875 ns |             8 |                  10 |          2,917 ns  |               2,291 ns |              10 |
| medium_3.json   |         11,167 ns |            15 |                  25 |          3,125 ns  |               2,500 ns |              12 |
| small_2.json    |          7,334 ns |             8 |                  10 |          2,958 ns  |               2,459 ns |              10 |
| large_3.json    |         20,750 ns |            30 |                  60 |          1,625 ns  |               1,583 ns |               2 |
| large_2.json    |         21,042 ns |            30 |                  60 |          1,584 ns  |               1,333 ns |               2 |
| small_3.json    |          6,500 ns |             8 |                  11 |          3,041 ns  |               3,125 ns |              10 |
| medium_2.json   |         10,333 ns |            15 |                  27 |          3,209 ns  |               3,000 ns |              14 |

---

## 5. Interpretation

* SCC and Topological Sort times are small and generally scale with graph size, consistent with **O(V + E)** complexity.
* Shortest path durations are proportional to edge processing work; relaxations reflect how many edges were relevant to the source.
* Measurements were taken using `System.nanoTime()` and converted to nanoseconds in the summary. Values reflect single-run timings (suitable for small-medium graphs); for more stable numbers consider averaging multiple runs.

---

## 6. Algorithmic Analysis

### Tarjan’s Algorithm (SCC Detection)
Efficiently finds strongly connected components using low-link values and a stack. Complexity: `O(V + E)`.

### Condensation Graph
Compresses SCCs into nodes to form a DAG. Complexity: `O(V + E)`.

### Topological Sort (Kahn)
Processes in-degree-0 nodes iteratively to produce a valid execution order. Complexity: `O(V + E)`.

### Shortest Path in DAG
Edge-weight based DP processed in topological order: `dist[v] = min(dist[v], dist[u] + w)`. Complexity: `O(V + E)`.

### Longest Path in DAG
Max-DP for critical path detection: `dist[v] = max(dist[v], dist[u] + w)`. Complexity: `O(V + E)`.

---

## 7. Performance Overview

| **Algorithm**    | **Complexity** | **Trend** | **Key Factor**      |
| :--------------- | :------------- | :-------- | :------------------ |
| Tarjan SCC       | O(V + E)       | Linear    | Graph density       |
| Condensation DAG | O(V + E)       | Linear    | SCC count           |
| Topological Sort | O(V + E)       | Stable    | In-degree structure |
| Shortest Path    | O(V + E)       | Stable    | Edge relaxations    |
| Longest Path     | O(V + E)       | Stable    | Path depth          |

All algorithms demonstrated **linear scalability** and no exponential trends on the provided datasets.

---

## 8. Bottlenecks and Solutions

| **Issue**           | **Impact**           | **Solution**                            |
| :------------------ | :------------------- | :-------------------------------------- |
| Recursive DFS depth | Stack depth risk     | Use iterative variants where needed     |
| Dense edge sets     | More relaxations     | Still linear; prefilter irrelevant edges|
| Timer overhead      | Minor noise          | Average multiple runs if needed         |

---

## 9. Practical Recommendations

* Run SCC detection (Tarjan) when cycles or mutual dependencies are possible.
* Build condensation DAG and perform topological sort to derive safe execution order.
* Use DAG shortest-path DP to minimize total costs; use DAG longest-path DP to find critical (time-dominating) chains.
* For large real-world graphs, record averages across multiple runs and consider parallel preprocessing.

---


 **10. Author**
---
**By Nurkassymov Nauryzbay, SE-2423**
---

