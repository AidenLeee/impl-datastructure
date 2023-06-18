package com.airili.tree;

import java.util.Comparator;

/**
 * @program: data_structure
 * @description: 平衡二叉搜索树
 * @author: Airili
 * @create: 2021-03-05 21:51
 **/
public class AVLTree<E> extends BBST<E> {

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

    @Override
    protected void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
        super.afterRotate(grand, parent, child);

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
    protected Node<E> createNode(E element, Node<E> parent) {
        return new AVLNode<>(element, parent);
    }

    private boolean isBalance(Node<E> node) {
        return Math.abs(((AVLNode<E>)node).balanceFactor()) < 2;
    }

    private static class AVLNode<E> extends Node<E> {
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
