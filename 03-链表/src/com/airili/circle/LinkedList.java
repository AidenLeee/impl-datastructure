package com.airili.circle;

import com.airili.AbstractList;

/**
 * @program: data_structure
 * @description: 循环双向链表
 * @author: Airili
 * @create: 2021-02-28 16:44
 **/
public class LinkedList<E> extends AbstractList<E> {
    //头节点
    private Node<E> first;
    //尾节点
    private Node<E> last;

    private class Node<E> {
        E element;
        Node<E> next;
        Node<E> prev;

        public Node(E element, Node<E> prev, Node<E> next) {
            this.element = element;
            this.next = next;
            this.prev = prev;
        }
    }


    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
    }

    @Override
    /**
     * 最好：O(1)
     * 最坏：O(n)
     * 平均：O(n)
     */
    public E get(int index) {
        return node(index).element;
    }

    @Override
    /**
     * 最好：O(1)
     * 最坏：O(n)
     * 平均：O(n)
     */
    public E set(int index, E element) {
        indexOutOfBoundsCheck(index);

        Node<E> node = node(index);
        E oldData = node.element;
        node.element = element;

        return oldData;
    }

    @Override
    /**
     * 最好：O(1)
     * 最坏：O(n)
     * 平均：O(n)
     */
    public void add(int index, E element) {
        indexOutOfBoundsCheckForAdd(index);

        //往链表最后的位置添加元素
        if (index == size) {
            Node oldLast = last;
            last = new Node<>(element, oldLast, first);
            if (oldLast == null) { //这是链表添加的第一个元素
                first = last;
                first.next = first;
                first.prev =first;
            } else {
                oldLast.next = last;
                first.prev = last;
            }
        } else {
            Node<E> next = node(index);
            Node<E> prev = next.prev;
            Node<E> newNode = new Node<>(element, prev, next);
            next.prev = newNode;
            prev.next = newNode;
            //如果在索引为0的位置添加元素
            if (next == first) { //index == 0
                first = newNode;
            }
        }
        size++;
    }

    @Override
    /**
     * 最好：O(1)
     * 最坏：O(n)
     * 平均：O(n)
     */
    public E remove(int index) {
        indexOutOfBoundsCheck(index);

        Node<E> node = first;
        if (size == 1) {
            first = null;
            last = null;
        } else {
            node = node(index);
            Node<E> prev = node.prev;
            Node<E> next = node.next;
            prev.next = next;
            next.prev = prev;

            if (node == first) { //index == 0
                first = next;
            }
            if (node == last) { //index == size -1
                last = prev;
            }
        }

        size--;

        return node.element;
    }

    @Override
    public int indexOf(E element) {
        if (element == null) {
            Node<E> node = first;
            for (int i = 0; i < size; i++) {
                if (node.element == null) return i;
                node = node.next;
            }
        } else {
            Node<E> node = first;
            for (int i = 0; i < size; i++) {
                if (node.element.equals(element)) return i;
                node = node.next;
            }
        }

        return ELEMENT_NOT_FOUND;
    }


    private Node<E> node(int index) {
        indexOutOfBoundsCheck(index);
        Node<E> node;
        if (index < (size >> 1)) {
            node = first;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
        } else {
            node = last;
            for (int i = size - 1; i > index; i--) {
                node = node.prev;
            }
        }

        return node;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("size=").append(size).append(", [");
        Node<E> node = first;
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                string.append(", ");
            }

            string.append(node.element);

            node = node.next;
        }
        string.append("]");
        return string.toString();
    }

}
