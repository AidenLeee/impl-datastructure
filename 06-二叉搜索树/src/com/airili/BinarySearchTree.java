package com.airili;

import com.airili.utils.printer.BinaryTreeInfo;
import jdk.nashorn.internal.ir.IfNode;

import java.awt.print.Pageable;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * @program: data_structure
 * @description: 二叉搜素树
 * @author: Airili
 * @create: 2021-03-01 16:53
 **/
public class BinarySearchTree<E> implements BinaryTreeInfo {
    private int size;
    Node<E> root;
    Comparator<E> comparator;

    public BinarySearchTree() {
        this(null);
    }

    public BinarySearchTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {

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

        if (node.hasTwoChildren(node)) {
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
     * 前序遍历
     */
    public  void preorder(Visitor<E> visitor) {
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

    {

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

    /**
     * 找到一个节点的前驱节点返回
     * @param node
     * @return
     */
    private Node<E> predecessor(Node<E> node){
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
    private Node<E> successor(Node<E> node){
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


    private static class Node<E> {
        E element;
        Node<E> left;
        Node<E> right;
        Node<E> parent; //对于红黑树特别有用

        public Node(E element, Node<E> parent) {
            this.element = element;
            this.parent = parent;
        }

        public boolean hasTwoChildren(Node<E> node) {
            return node.left != null && node.right != null;
        }
    }


}
