package com.airili.union;

/**
 * @program: data_structure
 * @description: 并查集 --- 基于快速查找实现 --- 根据rank（树高）优化
 * @author: Airili
 * @create: 2021-04-06 14:02
 **/
public class UnionFind_QU_R extends UnionFind_QU {
    protected int[] ranks;

    public UnionFind_QU_R(int capacity) {
        super(capacity);

        ranks = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            ranks[i] = 1;
        }
    }

    @Override
    public void union(int v1, int v2) {
        rangeCheck(v1);
        rangeCheck(v2);

        int p1 = find(v1);
        int p2 = find(v2);
        if (p1 == p2) return;

        if (ranks[p1] > ranks[p2]) {
            parents[p2] = p1;
        } else if (ranks[p1] < ranks[p2]) {
            parents[p1] = p2;
        } else {   //  ==
            parents[p1] = p2;
            ranks[p2]++;
        }

    }
}
