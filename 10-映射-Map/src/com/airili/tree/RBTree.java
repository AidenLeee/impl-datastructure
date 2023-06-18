package com.airili.tree;

import java.util.Comparator;

/**
 * @program: data_structure
 * @description: 红黑树
 * @author: Airili
 * @create: 2021-03-08 12:18
 **/
public class RBTree<E> extends BBST<E> {
    private static final boolean RED = false;
    private static final boolean BLACK = true;


    public RBTree() {
        this(null);
    }

    @Override
    protected void afterAdd(Node<E> node) {
        //父节点
        Node<E> parent = node.parent;
        //如果添加的是根节点
        if (parent == null) {
            black(node);
            return;
        }

        //添加节点的父节点是黑色的
        if (isBlack(parent)) return;

        //叔父节点
        Node<E> uncle = parent.sibling();
        //祖父节点
        Node<E> grand = red(parent.parent);
        if (isRed(uncle)) {  //如果uncle的颜色是红色，这种情况下会产生【上溢】
            black(parent);
            black(uncle);
            //将祖父节点当作新添加的节点，（使用递归）
            afterAdd(red(parent.parent));
            return;

        }

        //uncle的颜色不是红色
        if (parent.isLeftChild()) {  //L
            if (node.isLeftChild()) {  //LL
                black(parent);
            } else {  //LR
                black(node);
                rotateLeft(parent);
            }
            rotateRight(grand);
        } else {  //R
            if (node.isRightChild()) { //RR
                black(parent);
            } else {  //RL
                black(node);
                rotateRight(parent);
            }
            rotateLeft(grand);
        }
    }

    @Override
    protected void afterRemove(Node<E> node) {
        //被删除的节点是红色的
        //if (isRed(node)) return;

        /**
         * 被删除的节点是红色的
         *      或者
         * 被删除的节点是黑色而替代的节点是红色
         */
        if (isRed(node)) {
            black(node);
            return;
        }

        //被删除的节点是黑色叶子节点
        //被删除的是叶子节点且是根节点
        Node<E> parent = node.parent;
        if (parent == null) return;

        //判断被删除节点在父节点的左边还是右边
        boolean left = parent.left == null || node.isLeftChild();
        Node<E> sibling = left ? parent.right : parent.left;
        if (left) {  //被删除节点在左边，兄弟节点在右边，其实与另外一种情况是对称的
            if (isRed(sibling)) {  //兄弟节点是红的，则需转化成兄弟节点为黑
                black(sibling);
                red(parent);
                rotateLeft(parent);
                sibling = parent.right;
            }

            //兄弟节点为黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {  //兄弟节点没有红色子节点
                //判断父节节点是否为黑色
                boolean isParentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (isParentBlack) {
                    afterRemove(parent);
                }
            } else {  //兄弟节点字至少有一个红色子节点
                if (isBlack(sibling.right)) {  //兄弟节点的左边是黑色，需要先对sibling左旋转
                    rotateRight(sibling);
                    //旋转过后需要重新将sibling指向parent的左子节点
                    sibling = parent.right;
                }

                //兄弟节点的左边有红色节点
                color(sibling, colorOf(parent));
                black(parent);
                black(sibling.right);
                rotateLeft(parent);
            }

        } else {  //被删除节点在右边，兄弟节点在左边
            if (isRed(sibling)) {  //兄弟节点是红的，则需转化成兄弟节点为黑
                black(sibling);
                red(parent);
                rotateRight(parent);
                sibling = parent.left;
            }

            //兄弟节点为黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {  //兄弟节点没有红色子节点
                //判断父节节点是否为黑色
                boolean isParentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (isParentBlack) {
                    afterRemove(parent);
                }
            } else {  //兄弟节点字至少有一个红色子节点
                if (isBlack(sibling.left)) {  //兄弟节点的左边是黑色，需要先对sibling左旋转
                    rotateLeft(sibling);
                    //旋转过后需要重新将sibling指向parent的左子节点
                    sibling = parent.left;
                }

                //兄弟节点的左边有红色节点
                color(sibling, colorOf(parent));
                black(parent);
                black(sibling.left);
                rotateRight(parent);
            }
        }
    }

/*

    @Override
    protected void afterRemove(Node<E> node, Node<E> replacement) {
        //被删除的节点是红色的
        if (isRed(node)) return;

        //被删除的节点是黑色而替代的节点是红色
        if (replacement != null && isRed(replacement)) {
            black(replacement);
            return;
        }

        //被删除的节点是黑色叶子节点
        //被删除的是叶子节点且是根节点
        Node<E> parent = node.parent;
        if (parent == null) return;

        //判断被删除节点在父节点的左边还是右边
        boolean left = parent.left == null || node.isLeftChild();
        Node<E> sibling = left ? parent.right : parent.left;
        if (left) {  //被删除节点在左边，兄弟节点在右边
            if (isRed(sibling)) {  //兄弟节点是红的，则需转化成兄弟节点为黑
                black(sibling);
                red(parent);
                rotateLeft(parent);
                sibling = parent.right;
            }

            //兄弟节点为黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {  //兄弟节点没有红色子节点
                //判断父节节点是否为黑色
                boolean isParentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (isParentBlack) {
                    afterRemove(parent, null);
                }
            } else {  //兄弟节点字至少有一个红色子节点
                if (isBlack(sibling.right)) {  //兄弟节点的左边是黑色，需要先对sibling左旋转
                    rotateRight(sibling);
                    //旋转过后需要重新将sibling指向parent的左子节点
                    sibling = parent.right;
                }

                //兄弟节点的左边有红色节点
                color(sibling, colorOf(parent));
                black(parent);
                black(sibling.right);
                rotateLeft(parent);
            }

        } else {  //被删除节点在右边，兄弟节点在左边
            if (isRed(sibling)) {  //兄弟节点是红的，则需转化成兄弟节点为黑
                black(sibling);
                red(parent);
                rotateRight(parent);
                sibling = parent.left;
            }

            //兄弟节点为黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {  //兄弟节点没有红色子节点
                //判断父节节点是否为黑色
                boolean isParentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (isParentBlack) {
                    afterRemove(parent, null);
                }
            } else {  //兄弟节点字至少有一个红色子节点
                if (isBlack(sibling.left)) {  //兄弟节点的左边是黑色，需要先对sibling左旋转
                    rotateLeft(sibling);
                    //旋转过后需要重新将sibling指向parent的左子节点
                    sibling = parent.left;
                }

                //兄弟节点的左边有红色节点
                color(sibling, colorOf(parent));
                black(parent);
                black(sibling.left);
                rotateRight(parent);
            }
        }
    }
*/

    private Node<E> color(Node<E> node, boolean color) {
        if (node == null) return node;
        ((RBNode<E>)node).color = color;
        return node;
    }

    private Node<E> red(Node<E> node) {
        color(node, RED);
        return node;
    }

    private Node<E> black(Node<E> node) {
        color(node, BLACK);
        return node;
    }

    private boolean colorOf(Node<E> node) {
        return node == null ? BLACK : ((RBNode<E>)node).color;
    }

    private boolean isRed(Node<E> node) {
        return colorOf(node) == RED;
    }

    private boolean isBlack(Node<E> node) {
        return colorOf(node) == BLACK;
    }

    @Override
    protected Node<E> createNode(E element, Node<E> parent) {
        return new RBNode<>(element, parent);
    }


    public RBTree(Comparator<E> comparator) {
        super(comparator);
    }

    private static class RBNode<E> extends Node<E> {
        private boolean color = RED;

        public RBNode(E element, Node<E> parent) {
            super(element, parent);
        }

        @Override
        public String toString() {
            String str = "";
            if (color == RED) {
                str = "R_";
            }
            return str + element.toString();
        }
    }
}
