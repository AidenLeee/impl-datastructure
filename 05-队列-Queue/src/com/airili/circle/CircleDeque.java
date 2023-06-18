package com.airili.circle;

/**
 * @program: data_structure
 * @description: 循环双端队列
 * @author: Airili
 * @create: 2021-03-01 10:15
 **/
public class CircleDeque<E> {
    //队头索引，没有必要设置一个rear成员来存储队尾索引，因为可以通过front算出来
    int front;
    int size;
    E[] elements;

    private static final int DEFAULT_CAPACITY = 10;

    public CircleDeque() {
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
     * 队尾入队
     * @param element
     */
    public void enQueueRear(E element) {
        capacityCheck();

        elements[realIndex(size)] = element;
        size++;
    }

    /**
     * 队头出队
     * @return
     */
    public E deQueueFront() {
        E oldData = elements[front];
        elements[front] = null;
        front = realIndex(1);
        size--;
        return oldData;
    }

    /**
     * 队头入队
     * @param element
     */
    public void enQueueFront(E element) {
        capacityCheck();

        front = realIndex(-1);
        elements[front] = element;
        size++;
    }

    /**
     * 队尾出队
     * @return
     */
    public E deQueueRear() {
        E oldData = elements[realIndex(size - 1)];
        elements[realIndex(size - 1)] = null;
        size--;

        return oldData;
    }

    public E front() {
        return elements[front];
    }

    public E rear() {
        return elements[realIndex(size - 1)];
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
        index += front;
        if (index < 0) return index + elements.length;
        return index < elements.length ? index : (index - elements.length);
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
