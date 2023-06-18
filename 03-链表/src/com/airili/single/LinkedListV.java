package com.airili.single;

import com.airili.AbstractList;

/**
 * @program: data_structure
 * @description: 带虚拟头节点的单向链表
 * @author: Airili
 * @create: 2021-02-28 11:34
 **/
public class LinkedListV<E> extends AbstractList<E> {
    //头节点
    private Node<E> first;

    public LinkedListV() {
        this.first = new Node<>(null, null);
    }

    @Override
    public void clear() {
        first = null;
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

        Node<E> pre = index == 0 ? first : node(index - 1);
        pre.next = new Node<>(element, pre.next);

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

        Node<E> pre = index == 0 ? first.next : node(index - 1);
        Node<E> node = pre.next;
        pre.next = node.next;
        size--;

        return node.element;
    }

    @Override
    public int indexOf(E element) {
        if (element == null) {
            Node<E> node = first.next;
            for (int i = 0; i < size; i++) {
                if (node.element == null) return i;
                node = node.next;
            }
        } else {
            Node<E> node = first.next;
            for (int i = 0; i < size; i++) {
                if (node.element.equals(element)) return i;
                node = node.next;
            }
        }

        return ELEMENT_NOT_FOUND;
    }


    private Node<E> node(int index) {
        indexOutOfBoundsCheck(index);
        Node<E> node = first.next;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }

        return node;
    }

    private class Node<E> {
        E element;
        Node<E> next;

        public Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("size=").append(size).append(", [");
        //如果没有元素，直接返回"[]",加这一步判断的原因在于，后一句的" Node<E> node = first.next; "又可能会报NullPointException
        if (size == 0) {
            string.append("]");
            return string.toString();
        }
        Node<E> node = first.next;
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
