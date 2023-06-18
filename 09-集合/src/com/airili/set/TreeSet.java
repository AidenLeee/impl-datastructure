package com.airili.set;

import com.airili.tree.BinaryTree;
import com.airili.tree.RBTree;

import java.util.Comparator;

/**
 * @program: data_structure
 * @description: 用红黑树实现集合, （增加、删除、修改的复杂度都为O(logn)）, 但是有个局限性：添加的元素必须具备可比较性
 * @author: Airili
 * @create: 2021-03-08 21:32
 **/
public class TreeSet<E> implements Set<E> {
    private RBTree<E> tree;

    public TreeSet(){
        this(null);
    }

    public TreeSet(Comparator<E> comparator) {
        tree = new RBTree<>(comparator);
    }

    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    @Override
    public void clear() {
        tree.clear();
    }

    @Override
    public boolean contains(E element) {
        return tree.contains(element);
    }

    @Override
    public void add(E element) {
        tree.add(element);
    }

    @Override
    public void remove(E element) {
        tree.remove(element);
    }

    @Override
    public void traversal(Visitor<E> visitor) {
        tree.inorder(new BinaryTree.Visitor<E>() {
            @Override
            public boolean visit(E element) {
                return visitor.visit(element);
            }
        });
    }
}
