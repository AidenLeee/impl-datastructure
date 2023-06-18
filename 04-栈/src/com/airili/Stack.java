package com.airili;

import com.airili.list.ArrayList;
import com.airili.list.List;

/**
 * @program: data_structure
 * @description: 栈，用动态数组和链表实现都可以，因为都是经常访问和取出最后一个元素
 * @author: Airili
 * @create: 2021-03-01 08:30
 **/
public class Stack<E> {
    //用动态数组实现
    private List<E> list = new ArrayList<>();


    public void clear() {
        list.clear();
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void push(E element) {
        list.add(element);
    }


    public E pop() {
        return list.remove(list.size() - 1);
    }


    public E top() {
        return list.get(list.size() - 1);
    }
}
