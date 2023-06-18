package com.airili;

import sun.security.mscapi.CKeyPairGenerator;

import java.util.HashMap;

/**
 * @program: data_structure
 * @description: 字典树，前缀树，单词查找树
 * @author: Airili
 * @create: 2021-03-11 22:46
 **/
public class Trie<V> {
    private int size;
    private Node<V> root;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        size = 0;
        root = null;
    }

    public V get(String key) {
        Node<V> node = node(key);
        return node == null && node.word ? null : node.value;
    }

    public boolean contains(String key) {
        Node<V> node = node(key);
        return node == null && node.word;
    }

    public V add(String key, V value) {
        keyCheck(key);

        //如果根节点为空，创建根节点
        if (root == null) {
            root = new Node<>(null);
        }
        Node<V> node = root;
        for (int i = 0; i < key.length(); i++) {
            Character c = key.charAt(i);
            boolean isChildrenEmpty = node.children == null;
            Node<V> childNode = isChildrenEmpty ? null : node.children.get(c);
            if (childNode == null) { //后面没有子节点，要创建节点
                childNode = new Node<>(node);
                childNode.character = c;
                node.children = isChildrenEmpty ? new HashMap<>() : node.children;
                node.children.put(c, childNode);
            }
            node = childNode;
        }

        if (node.word) {  //这个单词已经存在，则替换值
            V oldValue = node.value;
            node.value = value;
            return oldValue;
        } else {  //这个单词之前不存在
            node.value = value;
            node.word = true;
            size++;
            return null;
        }
    }

    public V remove(String key) {
        //找到最后一个节点
        Node<V> node = node(key);
        //如果不是以单词结尾，不用作任何处理
        if (node == null || !node.word) return null;
        size--;
        V oldValue = node.value;

        //找到了这个单词，分情况讨论如何删除
        //1.如果还有子节点
        if (node.children != null && !node.children.isEmpty()) {
            node.value = null;
            node.word =false;
            return oldValue;
        }
        //2.如果没有子节点
        Node<V> parent = null;
        while ((parent = node.parent) != null) {
            parent.children.remove(node.character);
            if (parent.word || !parent.children.isEmpty()) break;
            node = parent;
        }

        return oldValue;
    }

    public boolean startsWith(String prefix) {
        return node(prefix) != null;
    }

    private Node<V> node(String key) {
        keyCheck(key);

        Node<V> node = root;
        for (int i = 0; i < key.length(); i++) {
            if (node == null || node.children == null || node.children.isEmpty()) return null;
            Character c = key.charAt(i);
            node = node.children.get(c);
        }
        //找到了这个字符串，但是不确定是一个单词
        return node;
    }

    private void keyCheck(String key) {
        if (key == null || key.length() == 0) {
            throw new IllegalArgumentException("key must not be empty!");
        }
    }

    private static class Node<V> {
        Node<V> parent;
        HashMap<Character, Node<V>> children;
        Character character;
        V value;
        boolean word; // 是否为单词的结尾（是否为一个完整的单词）
        public Node(Node<V> parent) {
            this.parent = parent;
        }
    }
}
