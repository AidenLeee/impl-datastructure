package com.airili;

/**
 * @program: data_structure
 * @description: 主函数
 * @author: Airili
 * @create: 2021-02-27 23:32
 **/
public class Main {

    public static void main(String[] args) {
        test();

    }

    static void test() {
        // int -> Integer

        // 所有的类，最终都继承java.lang.Object

        // new是向堆空间申请内存
/*        ArrayList<Person> persons  = new ArrayList<>();
        persons.add(new Person(10, "Jack"));
        persons.add(new Person(12, "James"));
        persons.add(new Person(15, "Rose"));
        persons.clear();
        persons.add(new Person(22, "abc"));

        System.out.println(persons);*/

        ArrayList<Integer> ints  = new ArrayList<>();
        ints.add(10);
        ints.add(10);
        ints.add(22);
        ints.add(33);
        System.out.println(ints);
        ints.clear();
        System.out.println(ints);
    }
}


