package com.airili.tree;


import java.util.Comparator;

/**
 * @program: data_structure
 * @description: 二叉搜素树
 * @author: Airili
 * @create: 2021-03-01 16:53
 **/
public class BST<E> extends BinaryTree {
    Comparator<E> comparator;

    public BST() {
        this(null);
    }

    public BST(Comparator<E> comparator) {
        this.comparator = comparator;
    }


    public void add(E element) {
        elementNotNullCheck(element);

        //如果添加的是头节点
        if (size == 0) {
            root = new Node<>(element, null);
            size++;
            return;
        }

        //添加的不是第一个节点
        Node<E> node = root;    //从根节点开始向下找
        Node<E> parent = null;  //保存父节点
        int cmp = 0;            //用来最后一次比较的结果，为了能知道放到父节点的哪一边；
        while (node != null) {
            cmp = compare(element, node.element);
            parent = node;
            if (cmp > 0) {    //往右走
                node = node.right;
            } else if (cmp < 0) {    //往左走
                node = node.left;
            } else {    //覆盖
                node.element = element;
                return;
            }
        }

        //放置新添加的元素
        node = new Node<>(element, parent);
        if (cmp > 0) {
            parent.right = node;
        } else {
            parent.left = node;
        }
        size++;

    }

    public void remove(E element) {
        remove(node(element));
    }

    private void remove(Node<E> node) {
        if (node == null) return;

        //如果被删除的节点是度为2的节点

        if (node.hasTwoChildren()) {
            Node<E> succ = successor(node);
            node.element = succ.element;
            node = succ;
        }

        //如果被删除的节点是度为1或者0的节点
        Node<E> replacement = node.left != null ? node.left : node.right;
        if (replacement != null) { //删除的是度为1的节点
            //把要替换被删除的节点的子节点的父节点指向要被删除的节点的父节点
            replacement.parent = node.parent;
            if (node.parent == null) {   // //删除的是度为1的节点并且是根节点
                root = replacement;
            } else if (replacement == node.parent.left) {
                node.parent.left = replacement;
            } else {
                node.parent.right = replacement;
            }
        } else if (node.parent == null) { //  删除的是叶子节点（度为0）并且是根节点
            root = null;
        } else {  //  删除的是叶子节点（度为0）但不是根节点
            if (node == node.parent.left) {
                node.parent.left = null;
            } else {
                node.parent.right =null;
            }
        }

        size--;

    }

    public boolean contains(E element) {
        return node(element) != null;
    }

    /**
     * 根据元素找到其对应的节点
     * @param element
     * @return
     */
    private Node<E> node(E element) {
        Node<E> node = root;
        while (node != null) {
            int cmp = compare(element, node.element);
            if (cmp == 0) {
                return node;
            } else if (cmp < 0) {
                node = node.left;
            } else { //cmp > 0
                node = node.right;
            }
        }
        return null;   //个人感觉返回 node 也行
    }

    /**
     * 返回值等于0，两元素相等
     * 返回值大于0，e1大于e2
     * 返回值小于0，e1小于e2
     * @param e1
     * @param e2
     * @return
     */
    private int compare(E e1, E e2) {
        if (comparator != null) {
            return comparator.compare(e1, e2);
        }
        return ((Comparable<E>)e1).compareTo(e2);
    }

    private void elementNotNullCheck(E element) {
        if (element == null) {
            throw new IllegalArgumentException("element must not be null!");
        }
    }


}
