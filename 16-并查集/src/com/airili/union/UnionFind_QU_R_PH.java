package com.airili.union;

/**
 * @program: data_structure
 * @description: 并查集 --- 基于快速查找实现 --- 根据rank（树高）优化 --- 路径减半(Path Halving)
 * @author: Airili
 * @create: 2021-04-06 14:02
 **/
public class UnionFind_QU_R_PH extends UnionFind_QU_R {

    public UnionFind_QU_R_PH(int capacity) {
        super(capacity);
    }

    @Override
    public int find(int v) {
        rangeCheck(v);

        while (parents[v] != v) {
            parents[v] = parents[parents[v]];
            v = parents[v];
        }
        return v;
    }

}
