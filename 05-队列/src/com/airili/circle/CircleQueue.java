package com.airili.circle;


/**
 * @program: data_structure
 * @description: 循环队列,可以理解为：为了使得队列底层能够使数组实现的情况下也有O(1)的复杂度，所以增加了一个队头成员变量
 * @author: Airili
 * @create: 2021-03-01 09:52
 **/
public class CircleQueue<E> {
    //队头索引
    int front;
    int size;
    E[] elements;

    private static final int DEFAULT_CAPACITY = 6;

    public CircleQueue() {
        elements = (E[])new Object[DEFAULT_CAPACITY];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear(){
        for (int i = 0; i < size; i++) {
            elements[realIndex(i)] = null;
        }
        front = 0;
        size = 0;
    }

    /**
     * 尾部入队
     * @param element
     */
    public void enQueue(E element) {
        capacityCheck();

        elements[realIndex(size)] = element;
        size++;
    }

    /**
     * 头部出队
     * @return
     */
    public E deQueue() {
        E oldData = elements[front];
        elements[front] = null;
        front = realIndex(1);
        size--;
        return oldData;
    }

    public E front() {
        return elements[front];
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("capcacity=").append(elements.length)
                .append(" size=").append(size)
                .append(" front=").append(front)
                .append(", [");
        for (int i = 0; i < elements.length; i++) {
            if (i != 0) {
                string.append(", ");
            }

            string.append(elements[i]);
        }
        string.append("]");
        return string.toString();
    }

    private int realIndex(int index) {
        //return (front + index) % elements.length;
        //因为 (front + index) 一定比 2*elements.length 小，所以可以优化模运算
        index += front;
        return index < elements.length ? index : index - elements.length;
    }

    /**
     * 在添加元素前检查数组是否有足够容量
     */
    private void capacityCheck() {
        int oldCapacity = elements.length;
        if (oldCapacity >= (size + 1)) return;

        // 新容量为旧容量的1.5倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        E[] newElements = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[realIndex(i)];
        }
        elements = newElements;

        front = 0;
        System.out.println("动态数组扩容为" + newElements.length);
    }
}
