package com.airili.tree;

import java.util.Comparator;

/**
 * @program: data_structure
 * @description: 平衡二叉搜索树
 * @author: Airili
 * @create: 2021-03-05 21:51
 **/
public class AVLTree<E> extends BST<E> {

    public AVLTree() {
        this(null);
    }

    public AVLTree(Comparator<E> comparator) {
        super(comparator);
    }

    private void rebalance(Node<E> grand) {
        Node<E> parent = ((AVLNode<E>)grand).higherChild();
        Node<E> node = ((AVLNode<E>)parent).higherChild();
        if (parent.isLeftChild()) { //L
            if (node.isLeftChild()) { //LL
                rotateRight(grand);
            } else { //LR
                rotateLeft(parent);
                rotateRight(grand);
            }
        } else { //R
            if (node.isRightChild()) { //RR
                rotateLeft(grand);
            } else { //RL
                rotateRight(parent);
                rotateLeft(grand);
            }
        }

    }

    private void rotateRight(Node<E> grand) {
        Node<E> parent = grand.left;
        Node<E> child = parent.right;
        grand.left = child;
        parent.right = grand;
        //维护变更节点的父节点 <!--
        afterRotate(grand, parent, child);
        //维护完成  -->
    }

    private void rotateLeft(Node<E> grand) {
        Node<E> parent = grand.right;
        Node<E> child = parent.left;
        grand.right = child;
        parent.left = grand;
        //维护变更节点的父节点 <!--
        afterRotate(grand, parent, child);
        //维护完成  -->
    }

    private void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {

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

        //更新高度
        ((AVLNode<E>)grand).updateHeight();
        ((AVLNode<E>)parent).updateHeight();
    }

    @Override
    protected void afterAdd(Node<E> node) {
        while ((node = node.parent) != null) {
            if (isBalance(node)) {
                //更新高度
                ((AVLNode<E>)node).updateHeight();
            } else {
                //恢复平衡
                rebalance(node);
                //恢复后就可以直接退出循环
                break;
            }
        }
    }

    @Override
    protected void afterRemove(Node<E> node) {
        while ((node = node.parent) != null) {
            if (isBalance(node)) {
                //更新高度
                ((AVLNode<E>)node).updateHeight();
            } else {
                //恢复高度
                rebalance(node);
                //由于恢复之后其祖父仍有可能不平衡，所以仍然要判断，不能退出循环
            }
        }
    }

    @Override
    protected Node createNode(Object element, Node parent) {
        return new AVLNode<>(element, parent);
    }

    private boolean isBalance(Node<E> node) {
        return Math.abs(((AVLNode<E>)node).balanceFactor()) < 2;
    }

    private static class AVLNode<E> extends Node<E>{
        int height = 1;
        public AVLNode(E element, Node<E> parent) {
            super(element, parent);
        }

        public int balanceFactor() {
            int leftHeight =  left == null ? 0 : ((AVLNode<E>)left).height;
            int rightHeight =  right == null ? 0 : ((AVLNode<E>)right).height;
            return leftHeight - rightHeight;
        }

        public void updateHeight() {
            int leftHeight =  left == null ? 0 : ((AVLNode<E>)left).height;
            int rightHeight =  right == null ? 0 : ((AVLNode<E>)right).height;
            height = 1 + Math.max(leftHeight, rightHeight);
        }

        public Node<E> higherChild() {
            int leftHeight =  left == null ? 0 : ((AVLNode<E>)left).height;
            int rightHeight =  right == null ? 0 : ((AVLNode<E>)right).height;
            if (leftHeight > rightHeight) return left;
            if (leftHeight < rightHeight) return right;
            //两树高度相等，则返回与父节点对于祖父节点的位置的节点
            return isLeftChild() ? left : right;
        }
    }
}
