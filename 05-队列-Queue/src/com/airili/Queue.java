package com.airili;

import com.airili.list.LinkedList;
import com.airili.list.List;

/**
 * @program: data_structure
 * @description: 队列，底层优先使用双向链表实现，因为队列常做的操作是往队头和队尾添加元素
 * @author: Airili
 * @create: 2021-03-01 08:49
 **/
public class Queue<E> {
    //双向链表实现
    private List<E> list = new LinkedList<>();

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void clear(){
        list.clear();
    }

    /**
     * 队尾入队
     * @param element
     */
    public void enQueue(E element) {
        list.add(element);
    }

    /**
     * 队头出队
     * @return
     */
    public E deQueue() {
        return list.remove(0);
    }

    public E front() {
        return list.get(0);
    }
}
