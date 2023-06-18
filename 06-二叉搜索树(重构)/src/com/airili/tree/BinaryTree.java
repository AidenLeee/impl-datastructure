package com.airili.tree;

import com.airili.utils.printer.BinaryTreeInfo;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * @program: data_structure
 * @description: 二叉树接口
 * @author: Airili
 * @create: 2021-03-02 11:25
 **/
public class BinaryTree<E> implements BinaryTreeInfo {
    protected int size;
    protected Node<E> root;


    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * 翻转二叉树
     */
    public void reverse() {
        if (root == null) return;

        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();

            Node<E> temp = node.left;
            node.left = node.right;
            node.right = temp;

            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }

    }

    public boolean isComplete() {
        if (root == null) return false;

        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        boolean leaf = false;
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();

            if (leaf && !node.isLeaf()) return false;
            if (node.left != null) {
                queue.offer(node.left);
            } else if (node.right != null) {
                return false;
            }
            if (node.right != null) {
                queue.offer(node.right);
            } else {
                leaf = true;
            }
        }

        return true;
    }

/*
    public boolean isComplete() {
        if (root == null) return false;

        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        boolean leaf = false;
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();

            if (leaf && !node.isLeaf()) {
                return false;
            }

            if (node.hasTwoChildren()) {
                queue.offer(node.left);
                queue.offer(node.right);
            } else if (node.left == null && node.right != null) {
                return false;
            } else { //后面遍历的节点都必须是叶子节点
                // node.left ！= null && node.right == null  或者  node.left == null && node.right == null
                leaf = true;
                if (node.left != null) {
                    queue.offer(node.left);
                }
            }
        }

        return true;
    }
*/

    public int height1(Node<E> node) {
        if (node == null) return 0;

        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        int height = 0;
        int levelSize = 1;
        while (!queue.isEmpty()) {
            node = queue.poll();
            levelSize--;
            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
            if (levelSize == 0) {
                height++;
                levelSize = queue.size();
            }
        }

        return height;
    }

    public int height() {
        return height1(root);
    }

    private int height(Node<E> node) {
        if (node.isLeaf()) return 1;
        return 1 + Math.max(height(node.left), height(node.right));
    }


    /**
     * 前序遍历
     */
    public void preorder(Visitor<E> visitor) {
        preorder(root, visitor);
    }

    private void preorder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) return;

        // 迭代实现
        Stack<Node<E>> stack = new Stack<>();
        stack.push(node);
        do {
            node = stack.pop();

            //<!--进行想要执行的操作
            if (visitor.stop) return;
            visitor.visit(node.element);
            //-->结束

            if (node.right != null) stack.push(node.right);
            if (node.left != null) stack.push(node.left);

        } while (!stack.isEmpty());


        /* 递归实现
        //<!--进行想要执行的操作
        System.out.println(node.element);
        //-->结束

        preorder(node.left);
        preorder(node.right);*/

    }

    /**
     * 中序遍历
     */
    public  void inorder(Visitor<E> visitor) {
        inorder(root, visitor);
    }


    private void inorder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) return;

        //迭代实现
        Stack<Node<E>> stack = new Stack<>();
        do {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }

            node = stack.pop();

            //<!--进行想要执行的操作
            if (visitor.stop);
            visitor.visit(node.element);
            //-->结束

            node = node.right;

        } while (!stack.isEmpty() || node != null);   //精华所在，就是到头节点时能往右走，while的判断条件要或上 node!=null

        /* 递归实现
        inorder(node.left);

        //<!--进行想要执行的操作
        System.out.println(node.element);
        //-->结束

        inorder(node.right);*/
    }

    /**
     * 后序遍历
     */
    public void postOrder(Visitor<E> visitor) {
        postOrder(root, visitor);
    }


    private void postOrder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) return;

        /*
        //迭代实现
        Stack<Node<E>> stack = new Stack<>();
        stack.push(node);
        do {
            if (node.left != null) stack.push(node.left);
            if (node.right != null) stack.push(node.right);
        }*/

        //递归实现
        postOrder(node.left, visitor);
        postOrder(node.right, visitor);

        //<!--进行想要执行的操作
        if (visitor.stop) return;
        visitor.visit(node.element);
        //-->结束

    }

    /**
     * 层序遍历
     */
    public void levelOrder(Visitor<E> visitor) {
        if (root == null || visitor.stop) return;

        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();

            //<!--进行想要执行的操作
            if (visitor.visit(node.element)) return;
            //-->结束

            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
    }

    public static abstract class Visitor<E> {
        //判断是否停止遍历
        boolean stop;
        public abstract boolean visit(E element);
    }


    /**
     * 找到一个节点的前驱节点返回
     * @param node
     * @return
     */
    protected Node<E> predecessor(Node<E> node){
        if (node == null) return null;

        Node<E> pre = node.left;
        if (pre != null) {
            while (pre.right != null) {
                pre = pre.right;
            }
            return pre;
        }
        while (node.parent != null && node == node.parent.left) {
            node = node.parent;
        }
        return node.parent;
    }

    /**
     * 找到一个节点的后继节点返回
     * @param node
     * @return
     */
    protected Node<E> successor(Node<E> node){
        if (node == null) return null;

        Node<E> pre = node.right;
        if (pre != null) {
            while (pre.left != null) {
                pre = pre.left;
            }
            return pre;
        }
        while (node.parent != null && node == node.parent.right) {
            node = node.parent;
        }
        return node.parent;
    }

    @Override
    public Object root() {
        return root;
    }

    @Override
    public Object left(Object node) {
        return ((Node<E>)node).left;
    }

    @Override
    public Object right(Object node) {
        return ((Node<E>)node).right;
    }

    @Override
    //返回要打印显示的东西
    public Object string(Object node) {
        Node<E> myNode = (Node<E>)node;
        String parentString = null;
        if (myNode.parent != null) {
            parentString = myNode.parent.element.toString();
        }
        return myNode.element + "_(" + parentString + ")";
    }


    protected static class Node<E> {
        E element;
        Node<E> left;
        Node<E> right;
        Node<E> parent; //对于红黑树特别有用

        public Node(E element, Node<E> parent) {
            this.element = element;
            this.parent = parent;
        }

        public boolean hasTwoChildren() {
            return left != null && right != null;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }
    }
}
