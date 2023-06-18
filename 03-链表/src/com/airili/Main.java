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
        //test2();

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

        List<Integer> ints  = new LinkedList<>();
        ints.add(10);
        ints.add(10);
        ints.add(22);
        ints.add(33);

        System.out.println(ints);
        System.out.println(ints.size());
        System.out.println(ints.remove(2));
        System.out.println(ints.contains(22));
        System.out.println(ints);
        System.out.println(ints.indexOf(33));
        ints.clear();
        System.out.println(ints);
    }

    /*static void test2() {
        java.util.LinkedList<Integer> ints = new LinkedList<>();
        ints.add(11);
        ints.add(22);
        ints.add(33);
        ints.add(44);

        System.out.println(ints);
        ints.clear();
        System.out.println(ints);
    }*/
}


