package com.airili.queue;

import com.airili.heap.BinaryHeap;
import com.airili.heap.Heap;

import java.util.Comparator;

/**
 * @program: data_structure
 * @description: 优先级队列，底层使用堆来实现
 * @author: Airili
 * @create: 2021-03-11 22:32
 **/
public class PriorityQueue<E> {
    private Heap<E> heap;

    public PriorityQueue() {
        this(null);
    }

    public PriorityQueue(Comparator<E> comparator) {
        this.heap = new BinaryHeap<>(comparator);
    }

    public int size() {
        return heap.size();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public void clear() {
        heap.clear();
    }

    public void enQueue(E element) {
        heap.add(element);
    }

    public E deQueue() {
        return heap.remove();
    }

    public E front() {
        return heap.get();
    }
}
