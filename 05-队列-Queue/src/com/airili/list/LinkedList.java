package com.airili.list;

/**
 * @program: data_structure
 * @description: 双向链表
 * @author: Airili
 * @create: 2021-02-28 14:24
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
            last  = new Node<>(element, last, null);
            if (oldLast == null) {
                first = last;
            } else {
                oldLast.next = last;
            }
        } else {
            Node<E> next = node(index);
            Node<E> prev = next.prev;
            Node<E> newNode = new Node<>(element, prev, next);
            next.prev = newNode;

            //如果在索引为0的位置添加元素
            if (prev == null) { //index == 0
                first = newNode;
            } else {
                prev.next = newNode;
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

        //可读性不足
/*        Node<E> node = null;
        if (index == 0){
            first = first.next;
            first.prev = null;
        } else if (index == size - 1) {
            last = last.prev;
            last.next = null;
        } else {
            Node<E> pre = node(index - 1);
            node = pre.next;
            pre.next = node.next;
            node.next.prev = pre;
        }*/

        Node<E> node = node(index);
        Node<E> prev = node.prev;
        Node<E> next = node.next;
        //不规范
/*        if (index == 0) { //prev == null
            first = next;
            next.prev = prev;
        } else if (index == size -1) {
            last = prev;
            prev.next = next;
        } else {
            prev.next = next;
            next.prev = prev;
        }*/
        if (prev == null) { //index == 0
            first = next;
        } else {
            prev.next = next;
        }
        if (next == null) { //index == size -1
            last = prev;
        } else {
            next.prev = prev;
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
