package com.airili.union;

/**
 * @program: data_structure
 * @description: 并查集 --- 基于快速查找实现 --- 根据rank（树高）优化 --- 路径压缩(Path Compression)
 * @author: Airili
 * @create: 2021-04-06 14:02
 **/
public class UnionFind_QU_R_PC extends UnionFind_QU_R {

    public UnionFind_QU_R_PC(int capacity) {
        super(capacity);
    }

    @Override
    public int find(int v) {
        rangeCheck(v);

        if (parents[v] != v) {
            parents[v] = find(parents[v]);
        }
        return parents[v];
    }
}
