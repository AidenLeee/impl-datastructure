package com.airili.union;

/**
 * @program: data_structure
 * @description: 并查集 --- 基于快速查找实现
 * @author: Airili
 * @create: 2021-04-06 14:02
 **/
public class UnionFind_QF extends UnionFind {

    public UnionFind_QF(int capacity) {
        super(capacity);
    }

    @Override
    public int find(int v) {
        rangeCheck(v);

        return parents[v];
    }

    @Override
    public void union(int v1, int v2) {
        rangeCheck(v1);
        rangeCheck(v2);

        int p1 = find(v1);
        int p2 = find(v2);
        if (p1 == p2) return;

        for (int i = 0; i < parents.length; i++) {
            if (parents[i] == p1) {
                parents[i] = p2;
            }
        }

    }
}
