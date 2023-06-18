package com.airili.union;

/**
 * @program: data_structure
 * @description: 并查集 --- 基于快速查找实现 --- 根据rank（树高）优化 --- 路径分裂(Path Spliting)
 * @author: Airili
 * @create: 2021-04-06 14:02
 **/
public class UnionFind_QU_R_PS extends UnionFind_QU_R {

    public UnionFind_QU_R_PS(int capacity) {
        super(capacity);
    }

    @Override
    public int find(int v) {
        rangeCheck(v);

        while (parents[v] != v) {
            int parent = parents[v];
            parents[v] = parents[parents[v]];
            v = parent;
        }
        return v;
    }

}
