package com.airili.tree;

import java.util.Comparator;

/**
 * @program: data_structure
 * @description: 平衡二叉搜索树
 * @author: Airili
 * @create: 2021-03-08 14:24
 **/
public class BBST<E> extends BST<E> {
    public BBST() {
        this(null);
    }

    public BBST(Comparator<E> comparator) {
        super(comparator);
    }

    protected void rotateRight(Node<E> grand) {
        Node<E> parent = grand.left;
        Node<E> child = parent.right;
        grand.left = child;
        parent.right = grand;
        //维护变更节点的父节点 <!--
        afterRotate(grand, parent, child);
        //维护完成  -->
    }

    protected void rotateLeft(Node<E> grand) {
        Node<E> parent = grand.right;
        Node<E> child = parent.left;
        grand.right = child;
        parent.left = grand;
        //维护变更节点的父节点 <!--
        afterRotate(grand, parent, child);
        //维护完成  -->
    }

    protected void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {

        //让parent成为子树的根节点
        parent.parent = grand.parent;
        if (grand.isLeftChild()) {
            grand.parent.left = parent;
        } else if (grand.isRightChild()) {
            grand.parent.right = parent;
        } else {  // grand没有父节点，即grand是根节点
            root = parent;
        }

        //更新child的父节点 注意：可能存在空的情况
        if (child != null) {
            child.parent = grand;
        }

        //更新grand 的父节点
        grand.parent = parent;

    }
}
