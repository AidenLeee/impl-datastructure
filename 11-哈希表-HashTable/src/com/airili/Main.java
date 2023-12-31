package com.airili;


import com.airili.map.HashMap;
import com.airili.map.LinkedHashMap;
import com.airili.map.Map;
import com.airili.model.Key;
import com.airili.model.SubKey1;
import com.airili.model.SubKey2;


public class Main {

/*
    static void test1Map(Map<String, Integer> map, String[] words) {
        Times.test(map.getClass().getName(), new Task() {
            @Override
            public void execute() {
                for (String word : words) {
                    Integer count = map.get(word);
                    count = count == null ? 0 : count;
                    map.put(word, count + 1);
                }
                System.out.println(map.size()); // 17188

                int count = 0;
                for (String word : words) {
                    Integer i = map.get(word);
                    count += i == null ? 0 : i;
                    map.remove(word);
                }
                Asserts.test(count == words.length);
                Asserts.test(map.size() == 0);
            }
        });
    }

    static void test1() {
        String filepath = "C:\\Users\\MJ Lee\\Desktop\\src\\java\\util\\concurrent";
        FileInfo fileInfo = Files.read(filepath, null);
        String[] words = fileInfo.words();

        System.out.println("总行数：" + fileInfo.getLines());
        System.out.println("单词总数：" + words.length);
        System.out.println("-------------------------------------");

        test1Map(new TreeMap<>(), words);
        test1Map(new HashMap<>(), words);
        test1Map(new LinkedHashMap<>(), words);
    }

    static void test2(HashMap<Object, Integer> map) {
        for (int i = 1; i <= 20; i++) {
            map.put(new Key(i), i);
        }
        for (int i = 5; i <= 7; i++) {
            map.put(new Key(i), i + 5);
        }
        Asserts.test(map.size() == 20);
        Asserts.test(map.get(new Key(4)) == 4);
        Asserts.test(map.get(new Key(5)) == 10);
        Asserts.test(map.get(new Key(6)) == 11);
        Asserts.test(map.get(new Key(7)) == 12);
        Asserts.test(map.get(new Key(8)) == 8);
    }
*/

    static void test3(HashMap<Object, Integer> map) {
        map.put(null, 1); // 1
        map.put(new Object(), 2); // 2
        map.put("jack", 3); // 3
        map.put(10, 4); // 4
        map.put(new Object(), 5); // 5
        map.put("jack", 6);
        map.put(10, 7);
        map.put(null, 8);
        map.put(10, null);
        Asserts.test(map.size() == 5);
        Asserts.test(map.get(null) == 8);
        Asserts.test(map.get("jack") == 6);
        Asserts.test(map.get(10) == null);
        Asserts.test(map.get(new Object()) == null);
        Asserts.test(map.containsKey(10));
        Asserts.test(map.containsKey(null));
        Asserts.test(map.containsValue(null));
        Asserts.test(map.containsValue(1) == false);
    }

    static void test4(HashMap<Object, Integer> map) {
        map.put("jack", 1);
        map.put("rose", 2);
        map.put("jim", 3);
        map.put("jake", 4);
        map.remove("jack");
        map.remove("jim");
        for (int i = 1; i <= 10; i++) {
            map.put("test" + i, i);
            map.put(new Key(i), i);
        }
        System.out.println(map.size());
        for (int i = 5; i <= 7; i++) {
            map.remove(new Key(i));
            System.out.println(map.size());
        }
        System.out.println("----" + map.size());
        map.traversal(new Map.Visitor<Object, Integer>() {
            public boolean visit(Object key, Integer value) {
                System.out.println(key + "_" + value);
                return false;
            }
        });
        System.out.println("----" + map.size());
        for (int i = 1; i <= 3; i++) {
            map.put(new Key(i), i + 5);
        }

        System.out.println("----" + map.size());
        Asserts.test(map.size() == 19);
        Asserts.test(map.get(new Key(1)) == 6);
        Asserts.test(map.get(new Key(2)) == 7);
        Asserts.test(map.get(new Key(3)) == 8);
        Asserts.test(map.get(new Key(4)) == 4);
        Asserts.test(map.get(new Key(5)) == null);
        Asserts.test(map.get(new Key(6)) == null);
        Asserts.test(map.get(new Key(7)) == null);
        Asserts.test(map.get(new Key(8)) == 8);
        map.traversal(new Map.Visitor<Object, Integer>() {
            public boolean visit(Object key, Integer value) {
                System.out.println(key + "_" + value);
                return false;
            }
        });
    }

    static void test5(HashMap<Object, Integer> map) {
        for (int i = 1; i <= 20; i++) {
            map.put(new SubKey1(i), i);
        }
        map.put(new SubKey2(1), 5);
        Asserts.test(map.get(new SubKey1(1)) == 5);
        Asserts.test(map.get(new SubKey2(1)) == 5);
        Asserts.test(map.size() == 20);
    }

    public static void main(String[] args) {
//		test1();
//		test2(new HashMap<>());
        test3(new LinkedHashMap<>());
        test4(new LinkedHashMap<>());
        test5(new LinkedHashMap<>());
    }

/*        test1();
        test2(new LinkedHashMap<>());
        test3(new LinkedHashMap<>());
        test4(new LinkedHashMap<>());
        test5(new LinkedHashMap<>());*//*


        java.util.HashMap<String, String> map;
        java.util.LinkedHashMap<String, String> map2;
    }
*/

/*

    public static void main(String[] args) {
        Person p1 = new Person(2, 1.5f, "jack");
        Person p2 = new Person(2, 1.5f, "jack");

        Map<Object, Integer> map = new HashMap<>();
        map.put(p1, 1);
        map.put(p2, 2);
        map.put(333,3);
        map.put("rose",4);
        map.put(null, 5);

*/
/*        System.out.println(map.containsKey(p1));
        System.out.println(map.containsKey(333));
        System.out.println(map.containsKey("jack"));
        System.out.println(map.containsValue(1));
        System.out.println(map.containsValue(333));
        System.out.println(map.containsValue(5));*//*



*/
/*        map.traversal(new Map.Visitor<Object, Integer>() {
            @Override
            public boolean visit(Object key, Integer value) {
                System.out.println(key + "__" + value);
                return false;
            }
        });*//*


        System.out.println(map.size());
        System.out.println(map.remove(333));
        System.out.println(map.size());
        System.out.println(map.get(333));

*/
/*        System.out.println(map.get(p2));
        System.out.println(map.get(333));
        System.out.println(map.get("rose"));
        System.out.println(map.get(null));*//*



    }
*/

}
