package com.airili.heap;

import com.airili.printer.BinaryTreeInfo;

import java.util.Comparator;

/**
 * @program: data_structure
 * @description: 最大堆
 * @author: Airili
 * @create: 2021-03-11 12:36
 **/
public class BinaryHeap<E> extends AbstractHeap<E> implements BinaryTreeInfo {
    private E[] elements;
    private static final int DEFAULT_CAPACITY = 10;

    public BinaryHeap() {
        this(null, null);
    }

    public BinaryHeap(E[] elements, Comparator<E> comparator) {
        super(comparator);

        if (elements == null || elements.length == 0) {
            this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        } else {
            size = elements.length;
            int capacity = Math.max(DEFAULT_CAPACITY, elements.length);
            this.elements = (E[]) new Object[capacity];
            for (int i = 0; i < size; i++) {
                this.elements[i] = elements[i];
            }
            heapify();
        }
    }

    public BinaryHeap(E[] elements) {
        this(elements, null);
    }

    public BinaryHeap(Comparator<E> comparator) {
        this(null, comparator);
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public void add(E element) {
        elementNotNullCheck(element);
        capacityCheck(size);

        elements[size++] = element;
        siftUp(size - 1);
    }

    @Override
    public E get() {
        emptyCheck();
        return elements[0];
    }

    @Override
    public E remove() {
        emptyCheck();

        E oldData =  elements[0];
        int index = --size;
        elements[0] = elements[index];
        elements[index] = null;
        siftDown(0);


        return oldData;
    }

    @Override
    public E replace(E element) {
        elementNotNullCheck(element);

        E oldData = null;
        if (size == 0) {
            elements[0] = element;
            size++;
        } else {
            oldData = elements[0];
            elements[0] = element;
            siftDown(0);
        }
        return oldData;
/*
        E remove = remove();
        add(element);
        return remove;*/
    }

    private void siftUp(int index) {
        if (index == 0) return;

        E element = elements[index];
        while (index > 0) {
            int parentIndex = (index - 1) >> 1;
            if (compare(element, elements[parentIndex]) <= 0) break;
            //如果新加入的元素比父元素大，则交换
            elements[index] = elements[parentIndex];
            index = parentIndex;
        }
        elements[index] = element;
    }

    private void siftDown(int index) {
        //第一个叶子节点的索引 == 非叶子节点的个数
        int half = size >> 1;
        E element = elements[index];

        // index < 第一个叶子节点的索引
        // 必须保证index位置是非叶子节点
        while (index < half) {
            // index的节点有2种情况
            // 1.只有左子节点
            // 2.同时有左右子节点

            // 默认为左子节点跟它进行比较
            int childIndex = (index << 1) + 1;
            E childElement = elements[childIndex];
            // 右子节点
            int rightIndex = childIndex + 1;

            //选出左右子节点中较大的那个
            if (rightIndex < size
                && compare(childElement, elements[rightIndex]) < 0) {
                childElement = elements[childIndex = rightIndex];
            }

            if (compare(element, childElement) >= 0) break;
            //交换两元素
            elements[index] = childElement;
            index = childIndex;
        }
        elements[index] = element;
    }

    private void heapify() {
        //自上而下的上溢，时间复杂度O(nlogn)
/*        for (int i = 1; i < elements.length; i++) {
            siftUp(i);
        }*/

        //自下而上的下溢，时间复杂度O(n)
        for (int i = (size >> 1) - 1; i > -1; i--) {
            siftDown(i);
        }
    }

    /**
     * 在添加元素前检查数组是否有足够容量
     * @param size
     */
    private void capacityCheck(int size) {
        int capacity = elements.length;
        if ((size + 1) <= capacity) return;
        //扩容为原来的1.5倍
        capacity += capacity >> 1;
        E[] old = elements;
        elements = (E[])new Object[capacity];
        //搬运数据
        for (int i = 0; i < size; i++) {
            elements[i] = old[i];
        }

        System.out.println("堆扩容为" + capacity);
    }

    private void elementNotNullCheck(E element) {
        if (element == null) {
            throw new IllegalArgumentException("元素不能为空！");
        }
    }

    private void emptyCheck() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("堆为空");
        }
    }

    @Override
    public Object root() {
        return 0;
    }

    @Override
    public Object left(Object node) {
        int index = ((int)node << 1) + 1 ;
        return index < size ? index : null;
    }

    @Override
    public Object right(Object node) {
        int index = ((int)node << 1) + 2 ;
        return index < size ? index : null;
    }

    @Override
    public Object string(Object node) {
        return elements[(int)node];
    }
}
