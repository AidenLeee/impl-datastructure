package com.airili.union;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @program: data_structure
 * @description: 通用类型的并查集
 * @author: Airili
 * @create: 2021-04-07 12:13
 **/
public class GenericUnionFind<T> {
    private Map<T, Node<T>> nodes = new HashMap<>();

    public void makeSet(T v) {
        if (nodes.containsKey(v)) return;
        nodes.put(v, new Node<>(v));
    }

    /**
     * 找出v的根节点
     */
    private Node<T> findNode(T v) {
        Node<T> node = nodes.get(v);
        if (node == null) return null;
        while (!Objects.equals(node.value, node.parent.value)) {
            node.parent = node.parent.parent;
            node = node.parent;
        }
        return node;
    }

    public T find(T v) {
        Node<T> node = findNode(v);
        return node == null ? null : node.value;
    }

    public void union(T v1, T v2) {
        Node<T> p1 = findNode(v1);
        Node<T> p2 = findNode(v2);
        if (p1 == null || p2 == null) return;
        if (Objects.equals(p1.value, p2.value)) return;

        if (p1.rank < p2.rank) {
            p1.parent = p2;
        } else if (p1.rank > p2.rank) {
            p2.parent = p1;
        } else {
            p1.parent = p2;
            p2.rank += 1;
        }
    }

    public boolean isSame(T v1, T v2) {
        return Objects.equals(find(v1), find(v2));
    }

    private static class Node<T> {
        T value;
        Node<T> parent = this;
        int rank = 1;
        Node(T value) {
            this.value = value;
        }
    }
}
