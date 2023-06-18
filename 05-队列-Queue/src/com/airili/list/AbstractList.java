package com.airili.list;

/**
 * @program: data_structure
 * @description: 抽象类
 * @author: Airili
 * @create: 2021-02-28 11:17
 **/
public abstract class AbstractList<E> implements List<E> {
    /**
     * 元素的数量
     */
    protected int size;

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
     * 抛出索引越界异常
     * @param index
     */
    protected void outOfBounds(int index) {
        throw new IndexOutOfBoundsException("Index:" + index + "  Size:" + size + "已越界！");
    }

    /**
     * 检查索引是否越界
     * @param index
     */
    protected void indexOutOfBoundsCheck(int index) {
        if (index >= size || index < 0) {
            outOfBounds(index);
        }
    }

    /**
     * 在添加元素前检查索引是否越界
     * @param index
     */
    protected void indexOutOfBoundsCheckForAdd(int index) {
        if (index > size || index < 0) {
            outOfBounds(index);
        }
    }
}
