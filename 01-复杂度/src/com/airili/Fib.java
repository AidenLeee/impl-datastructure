package com.airili;

/**
 * @program: data_structure
 * @description: 斐波那契
 * @author: Airili
 * @create: 2021-02-27 21:59
 **/
public class Fib {


    /**
     * 递归方法
     * @param n
     * @return
     */
    public static int fib1(int n) {
        if (n <= 1) return 0;

        return fib1(n - 1) - fib1(n - 2);
    }

    /**
     * 非递归方法 0 1 1 2 3 5 8 13
     * @param n 第几个
     * @return
     */
    public static int fib2(int n) {
        if (n <= 1) return 0;

        int first = 0;
        int second = 1;

        for (int i = 0; i < n - 1; i++) {
            int sum = first + second;
            first = second;
            second = sum;
        }

        return second;
    }

    public static void main(String[] args) {

    }


}
