package com.airili;


import java.util.Arrays;

/**
 * @program: data_structure
 * @description: 动态数组
 * @author: Airili
 * @create: 2021-02-27 23:36
 **/
public class ArrayList<E> {
    //元素的数量
    private int size;
    //所有元素
    private  E[] elements;

    private static final int DEFAULT_CAPACITY = 3;
    private static final int ELEMENT_NOT_FOUND  = -1;

    public ArrayList(int capacity) {
        capacity = (capacity < DEFAULT_CAPACITY) ? DEFAULT_CAPACITY : capacity;
        elements = (E[]) new Object[capacity];
    }

    public ArrayList() {
        this(DEFAULT_CAPACITY);
    }


    /**
     * 元素的数量
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * 数组是否为空
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 是否包含某个元素
     * @param element
     * @return
     */
    public boolean contains(E element) {
        return indexOf(element) != ELEMENT_NOT_FOUND;
    }

    /**
     * 添加元素到尾部
     * @param element
     */
    public void add(E element) {
        add(size, element);
    }

    /**
     * 获得index位置的元素
     * @param index
     * @return
     */
    public E get(int index) {
        indexOutOfBoundsCheck(index);
        return elements[index];
    }

    /**
     * 设置index位置的元素
     * @param index
     * @param element
     * @return
     */
    public E set(int index, E element) {
        indexOutOfBoundsCheck(index);
        E old = elements[index];
        elements[index] = element;

        return old;
    }

    /**
     * 在index位置插入一个元素
     * @param index
     * @param element
     */
    public void add(int index, E element) {
        indexOutOfBoundsCheckForAdd(index);
        capacityCheck(size);

        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    /**
     * 删除index位置的元素
     * @param index
     * @return
     */
    public E remove(int index) {
        indexOutOfBoundsCheck(index);

        E oldData = elements[index];
        for (int i = index; i < size - 1; i++) {
            elements[i] =elements[i + 1];
        }
        elements[--size] = null;

        return oldData;
    }

    /**
     * 获得某个元素在数组中的索引
     * @param element
     * @return
     */
    public int indexOf(E element) {
        //element为空判断
        if (element == null) {
            for (int i = 0; i < size; i++) {
                //找到第一个空值的索引，返回
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                //判断两元素是否相等，为了方便比较自定义类型，应该用equals方法比较
                if (element.equals(elements[i])) return i;
            }
        }

        //找不到
        return ELEMENT_NOT_FOUND;
    }

    /**
     * 清空数组
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    /**
     * 抛出索引越界异常
     * @param index
     */
    private void outOfBounds(int index) {
        throw new IndexOutOfBoundsException("Index:" + index + "  Size:" + size + "已越界！");
    }

    /**
     * 检查索引是否越界
     * @param index
     */
    private void indexOutOfBoundsCheck(int index) {
        if (index >= size || index < 0) {
            outOfBounds(index);
        }
    }

    /**
     * 在添加元素前检查索引是否越界
     * @param index
     */
    private void indexOutOfBoundsCheckForAdd(int index) {
        if (index > size || index < 0) {
            outOfBounds(index);
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

        System.out.println("动态数组扩容为" + capacity);
    }

    /**
     * 重写toString方法
     * @return
     */
    @Override
    public String toString() {
        return "ArrayList{" +
                "size=" + size +
                ", elements=" + Arrays.toString(elements) +
                '}';
    }
}
