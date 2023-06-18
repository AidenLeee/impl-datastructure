package com.airili;


import com.airili.list.LinkedList;
import com.airili.list.List;

/**
 * @program: data_structure
 * @description: 主函数
 * @author: Airili
 * @create: 2021-03-01 08:27
 **/
public class Main {

    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        stack.push(11);
        stack.push(22);
        stack.push(33);
        stack.push(44);

        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }
}


