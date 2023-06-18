package com.airili.heap;

import com.airili.printer.BinaryTreeInfo;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import java.awt.print.Pageable;
import java.util.Comparator;

/**
 * @program: data_structure
 * @description: (二叉堆)最大堆，与二叉搜索树一样，堆中元素必须具备可比较性，其逻辑结构就是一颗完全二叉树，所以底层可以用数组实现
 *                  性质：父节点索引 --- floor((i - 1) / 2)
 *                       左子节点  --- 2i + 1
 *                       右子节点  --- 2i + 2
 *                       第一个叶子节点的索引 == 非叶子节点的个数
 *                       非叶子节点的个数 = floor(节点总个数 / 2)
 *                       叶子节点的个数 = floor((节点总个数 + 1) / 2)
 *                  获取最大值：O(1)、删除最大值：O(logn)、添加元素：O(logn)
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
    /**
     * 添加方法：直接将要添加的元素放到末尾，然后对末尾索引位置进行一次上滤
     */
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
    /**
     * 删除方法：直接拿最后一个元素替换掉index为0的元素，然后对index为0的元素进行一次下滤
     */
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
    /**
     * 替换方法：将输入的元素替换掉index为0位置的元素，然后进行一次下溢
     */
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

    /**
     * 上溢方法：根据完全二叉树的性质找到父节点的索引，
     *              如果比父节点元素小则直接退出循环，
     *              如果比父节点元素大则将父节点元素替换掉自身的值，用父节点重复操作直到索引越界，
     *              最后再index位置放上最初的值
     * @param index
     */
    private void siftUp(int index) {
        if (index == 0) return;

        E element = elements[index];
        while (index > 0) {
            int parentIndex = (index - 1) >> 1;
            if (compare(element, elements[parentIndex]) <= 0) break;
            //如果子节点元素比父节点元素大，则用子节点元素覆盖父节点
            elements[index] = elements[parentIndex];
            index = parentIndex;
        }
        //最后再再index位置把element放入
        elements[index] = element;
    }

    /**
     * 下溢方法：（index位置必须是非叶子节点）选取子节点中较大的一个元素与父节点进行比较，
     *              如果小于则直接退出循环，
     *              如果大于则父节点元素替换掉自身的值，用父节点重复操作直到索引已是叶子节点，
     *              最后再index位置放上最初的值
     * @param index
     */
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
            //如果子节点元素比父节点元素大，则用子节点元素覆盖父节点
            elements[index] = childElement;
            index = childIndex;
        }
        //最后再再index位置把element放入
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
        elements = (E[])new java.lang.Object[capacity];
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
    public java.lang.Object root() {
        return 0;
    }

    @Override
    public java.lang.Object left(java.lang.Object node) {
        int index = ((int)node << 1) + 1 ;
        return index < size ? index : null;
    }

    @Override
    public java.lang.Object right(java.lang.Object node) {
        int index = ((int)node << 1) + 2 ;
        return index < size ? index : null;
    }

    @Override
    public java.lang.Object string(java.lang.Object node) {
        return elements[(int)node];
    }
}
