# HeTri: Multi-level Node Coloring for Efficient Triangle Enumeration on Heterogeneous Clusters

## Abstract

How can we efficiently enumerate triangles in enormous graphs using a heterogeneous cluster composed of machines with different CPUs and memory sizes? Triangle enumeration is a fundamental task for graph data analyses including detecting communities and finding anomalies like web spams or fake users in social media. Recently, several distributed methods have been proposed to enumerate triangles in billion-scale graphs, such as the Web and social networks, but they are all for homogeneous clusters. On heterogeneous clusters, they result in high network communication cost and long running time because of low memory utilization; they force all machines to use the same size of memory even if some machines have extra memory space.

In this paper, we propose `HeTri`, a heterogeneous triangle enumeration system that fully utilizes the memory of all machines in heterogeneous clusters. `HeTri` divides the problem into several tasks, and its performance depends on how the tasks are allocated and scheduled. Thus, we propose a parallel scheduling algorithm MLC, multi-level node coloring, that minimizes the network cost of `HeTri` by hierarchically grouping tasks so that tasks in each group share as many data as possible. Experimental results show that, on a heterogeneous cluster, `HeTri` with MLC achieves up to 1.91 times faster performance by reducing up to 5.02 times the amount of network communication compared to PTE, the state-of-the-art distributed triangle enumeration method. Even on a homogeneous cluster of 16 machines, `HeTri` with MLC consistently shows 1.32 to 1.71 times faster performance than PTE, with 1.46 to 4.13 times smaller network communication cost on real-world graphs.

## Datasets

All the datasets used in this paper are publicly available.

| Dataset                | Nodes            | Edges           | Source                                                     |
|------------------------|------------------|-----------------|------------------------------------------------------------|
| LiveJournal (LJ)       | 4.8M             | 69M             | http://snap.stanford.edu                                   |
| Twitter (TWT)          | 42M              | 1.2B            | http://an.kaist.ac.kr/trace/WWW2010.html                   |
| Friendster (FS)        | 66M              | 1.8B            | http://webdatacommons.org/hyperlinkgraph                   |
| SubDomain (SD)         | 101M             | 1.9B            | http://webscope.sandbox.yahoo.com                          |
| YahooWeb (YW)          | 1.4B             | 6.6B            | http://boston.lti.cs.cmu.edu/clueweb09                     |
| ClueWeb09 (CW09)       | 4.8B             | 7.9B            | http://www.lemurproject.org/clueweb09/webGraph.php         |
| ClueWeb12 (CW12)       | 6.3B             | 72B             | http://www.lemurproject.org/clueweb12/webGraph.php         |
| RMAT-k (k=23, ..., 29) | 2<sup>k</sup>    | 2<sup>k+4</sup> | RMAT with parameter (a,b,c,d) = (0.57, 0.19, 0.19, 0.05)   |
