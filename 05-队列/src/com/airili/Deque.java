package com.airili;

import com.airili.list.LinkedList;
import com.airili.list.List;

/**
 * @program: data_structure
 * @description: 双端队列
 * @author: Airili
 * @create: 2021-03-01 09:02
 **/
public class Deque<E> {
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
    public void enQueueRear(E element) {
        list.add(element);
    }

    /**
     * 队头出队
     * @return
     */
    public E deQueueFront() {
        return list.remove(0);
    }

    /**
     * 队头入队
     * @param element
     */
    public void enQueueFront(E element) {
        list.add(0, element);
    }

    /**
     * 队尾出队
     * @return
     */
    public E deQueueRear() {
        return list.remove(list.size() - 1);
    }

    public E front() {
        return list.get(0);
    }

    public E rear() {
        return list.get(list.size() - 1);
    }

}
