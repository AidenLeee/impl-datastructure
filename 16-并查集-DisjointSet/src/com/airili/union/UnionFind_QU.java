package com.airili.union;

/**
 * @program: data_structure
 * @description: 并查集 --- 基于快速查找实现
 * @author: Airili
 * @create: 2021-04-06 14:02
 **/
public class UnionFind_QU extends UnionFind {

    public UnionFind_QU(int capacity) {
        super(capacity);
    }

    @Override
    public int find(int v) {
        rangeCheck(v);

        while (v != parents[v]) {
            v = parents[v];
        }

        return v;
    }

    @Override
    public void union(int v1, int v2) {
        rangeCheck(v1);
        rangeCheck(v2);

        int p1 = find(v1);
        int p2 = find(v2);
        if (p1 == p2) return;

        parents[p1] = p2;

    }
}
