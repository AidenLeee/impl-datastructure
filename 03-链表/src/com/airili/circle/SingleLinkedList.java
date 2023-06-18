package com.airili.circle;

import com.airili.AbstractList;

/**
 * @program: data_structure
 * @description: 循环单向链表
 * @author: Airili
 * @create: 2021-02-28 16:10
 **/
public class SingleLinkedList<E> extends AbstractList<E> {
    //头节点
    private Node<E> first;

    private class Node<E> {
        E element;
        Node<E> next;

        public Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }
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

        if (first == null) {
            first = new Node<>(element, first);
            //维护循环
            Node<E> last = (size == 0) ? first : node(size - 1);
            last.next = first;

        } else {
            Node<E> pre = node(index - 1);
            pre.next = new Node<>(element, pre.next);
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
        if (index == 0){
            if (size == 1) {
                first =null;
            } else {
                //这句一定要卸载前面，因为如果已经把first赋值给first.next,再去调用node函数会找不到对应的last;
                Node<E> last = node(index - 1);
                first = first.next;
                last.next = first;
            }
        } else {
            Node<E> pre = node(index - 1);
            node = pre.next;
            pre.next = node.next;
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
        Node<E> node = first;
        for (int i = 0; i < index; i++) {
            node = node.next;
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
