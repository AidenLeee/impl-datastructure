package com.airili.set;

import com.airili.list.ArrayList;
import com.airili.list.List;
import com.airili.set.Set;

/**
 * @program: data_structure
 * @description: 用链表实现集合, （增加、删除、修改的复杂度都为O(n)）
 * @author: Airili
 * @create: 2021-03-08 21:23
 **/
public class ListSet<E> implements Set<E> {
    private List<E> list = new ArrayList<>();


    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean contains(E element) {
        return list.contains(element);
    }

    @Override
    public void add(E element) {
        int index = list.indexOf(element);
        if (index == List.ELEMENT_NOT_FOUND) {
            list.add(element);
        } else {
            list.set(index, element);
        }
    }

    @Override
    public void remove(E element) {
        int index = list.indexOf(element);
        if (index != List.ELEMENT_NOT_FOUND) {
            list.remove(index);
        }
    }

    @Override
    public void traversal(Visitor<E> visitor) {
        if (visitor == null) return;

        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (visitor.visit(list.get(i))) return;
        }
    }
}
