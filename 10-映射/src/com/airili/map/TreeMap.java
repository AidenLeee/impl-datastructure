package com.airili.map;


import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * @program: data_structure
 * @description: 用红黑树实现map
 * @author: Airili
 * @create: 2021-03-08 21:54
 **/
public class TreeMap<K, V> implements Map<K, V> {
    private static final boolean RED = false;
    private static final boolean BLACK = true;
    private int size;
    private Node<K, V> root;
    private Comparator<K> comparator;

    public TreeMap() {
        this(null);
    }

    public TreeMap(Comparator<K> comparator) {
        this.comparator = comparator;
    }


    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public V put(K key, V value) {
        keyNotNullCheck(key);

        //如果添加的是头节点
        if (size == 0) {
            root = createNode(key, value, null);
            size++;
            afterPut(root);
            return null;
        }

        //添加的不是第一个节点
        Node<K, V> node = root;    //从根节点开始向下找
        Node<K, V> parent = null;  //保存父节点
        int cmp = 0;            //用来最后一次比较的结果，为了能知道放到父节点的哪一边；
        while (node != null) {
            cmp = compare(key, node.key);
            parent = node;
            if (cmp > 0) {    //往右走
                node = node.right;
            } else if (cmp < 0) {    //往左走
                node = node.left;
            } else {    //覆盖
                node.key = key;
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
        }

        //放置新添加的元素
        node = createNode(key, value, parent);
        if (cmp > 0) {
            parent.right = node;
        } else {
            parent.left = node;
        }
        afterPut(node);
        size++;
        return null;
    }

    @Override
    public V get(K key) {
        Node<K, V> node = node(key);
        return node == null ? null : node.value;
    }

    @Override
    public V remove(K key) {
        return remove(node(key));
    }

    private V remove(Node<K, V> node) {
        if (node == null) return null;

        V oldValue = node.value;
        //如果被删除的节点是度为2的节点
        if (node.hasTwoChildren()) {
            Node<K, V> succ = successor(node);
            node.key = succ.key;
            node.value = succ.value;
            node = succ;
        }

        //如果被删除的节点是度为1或者0的节点
        Node<K, V> replacement = node.left != null ? node.left : node.right;
        if (replacement != null) { //删除的是度为1的节点
            //把要替换被删除的节点的子节点的父节点指向要被删除的节点的父节点
            replacement.parent = node.parent;
            if (node.parent == null) {   // //删除的是度为1的节点并且是根节点
                root = replacement;
            } else if (replacement == node.parent.left) {
                node.parent.left = replacement;
            } else {
                node.parent.right = replacement;
            }

            //在红黑树中这里要传入replacement，并且在AVL树中传入replacement也行，因为在第83行已经做了同作用的指向
            afterRemove(replacement);
        } else if (node.parent == null) { //  删除的是叶子节点（度为0）并且是根节点
            root = null;

            afterRemove(node);
        } else {  //  删除的是叶子节点（度为0）但不是根节点
            if (node == node.parent.left) {
                node.parent.left = null;
            } else {
                node.parent.right =null;
            }

            afterRemove(node);
        }

        size--;

        return oldValue;
    }

    @Override
    public boolean containsKey(K key) {
        return node(key) != null;
    }

    /**
     * 需要通过遍历来查找有没有对应的value
     * @param value
     * @return
     */
    @Override
    public boolean containsValue(V value) {
        if (root == null) return false;

        Queue<Node<K, V>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<K, V> node = queue.poll();

            if (valuesEqual(value, node.value)) return true;

            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);

        }
        return false;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (visitor == null) return;
        traversal(root, visitor);
    }

    private void traversal(Node<K, V> node, Visitor<K, V> visitor) {
        if (node == null || visitor.stop) return;

        traversal(node.left, visitor);
        //开始
        if (visitor.visit(node.key, node.value)) return;
        traversal(node.right, visitor);
    }

    private Node<K, V> color(Node<K, V> node, boolean color) {
        if (node == null) return node;
        node.color = color;
        return node;
    }

    private Node<K, V> red(Node<K, V> node) {
        color(node, RED);
        return node;
    }

    private Node<K, V> black(Node<K, V> node) {
        color(node, BLACK);
        return node;
    }

    private boolean colorOf(Node<K, V> node) {
        return node == null ? BLACK : node.color;
    }

    private boolean isRed(Node<K, V> node) {
        return colorOf(node) == RED;
    }

    private boolean isBlack(Node<K, V> node) {
        return colorOf(node) == BLACK;
    }

    private void afterPut(Node<K, V> node) {
        //父节点
        Node<K, V> parent = node.parent;
        //如果添加的是根节点
        if (parent == null) {
            black(node);
            return;
        }

        //添加节点的父节点是黑色的
        if (isBlack(parent)) return;

        //叔父节点
        Node<K, V> uncle = parent.sibling();
        //祖父节点
        Node<K, V> grand = red(parent.parent);
        if (isRed(uncle)) {  //如果uncle的颜色是红色，这种情况下会产生【上溢】
            black(parent);
            black(uncle);
            //将祖父节点当作新添加的节点，（使用递归）
            afterPut(red(parent.parent));
            return;

        }

        //uncle的颜色不是红色
        if (parent.isLeftChild()) {  //L
            if (node.isLeftChild()) {  //LL
                black(parent);
            } else {  //LR
                black(node);
                rotateLeft(parent);
            }
            rotateRight(grand);
        } else {  //R
            if (node.isRightChild()) { //RR
                black(parent);
            } else {  //RL
                black(node);
                rotateRight(parent);
            }
            rotateLeft(grand);
        }
    }

    protected void afterRemove(Node<K, V> node) {
        //被删除的节点是红色的
        //if (isRed(node)) return;

        /**
         * 被删除的节点是红色的
         *      或者
         * 被删除的节点是黑色而替代的节点是红色
         */
        if (isRed(node)) {
            black(node);
            return;
        }

        //被删除的节点是黑色叶子节点
        //被删除的是叶子节点且是根节点
        Node<K, V> parent = node.parent;
        if (parent == null) return;

        //判断被删除节点在父节点的左边还是右边
        boolean left = parent.left == null || node.isLeftChild();
        Node<K, V> sibling = left ? parent.right : parent.left;
        if (left) {  //被删除节点在左边，兄弟节点在右边，其实与另外一种情况是对称的
            if (isRed(sibling)) {  //兄弟节点是红的，则需转化成兄弟节点为黑
                black(sibling);
                red(parent);
                rotateLeft(parent);
                sibling = parent.right;
            }

            //兄弟节点为黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {  //兄弟节点没有红色子节点
                //判断父节节点是否为黑色
                boolean isParentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (isParentBlack) {
                    afterRemove(parent);
                }
            } else {  //兄弟节点字至少有一个红色子节点
                if (isBlack(sibling.right)) {  //兄弟节点的左边是黑色，需要先对sibling左旋转
                    rotateRight(sibling);
                    //旋转过后需要重新将sibling指向parent的左子节点
                    sibling = parent.right;
                }

                //兄弟节点的左边有红色节点
                color(sibling, colorOf(parent));
                black(parent);
                black(sibling.right);
                rotateLeft(parent);
            }

        } else {  //被删除节点在右边，兄弟节点在左边
            if (isRed(sibling)) {  //兄弟节点是红的，则需转化成兄弟节点为黑
                black(sibling);
                red(parent);
                rotateRight(parent);
                sibling = parent.left;
            }

            //兄弟节点为黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {  //兄弟节点没有红色子节点
                //判断父节节点是否为黑色
                boolean isParentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (isParentBlack) {
                    afterRemove(parent);
                }
            } else {  //兄弟节点字至少有一个红色子节点
                if (isBlack(sibling.left)) {  //兄弟节点的左边是黑色，需要先对sibling左旋转
                    rotateLeft(sibling);
                    //旋转过后需要重新将sibling指向parent的左子节点
                    sibling = parent.left;
                }

                //兄弟节点的左边有红色节点
                color(sibling, colorOf(parent));
                black(parent);
                black(sibling.left);
                rotateRight(parent);
            }
        }
    }

    /**
     * 返回值等于0，两元素相等
     * 返回值大于0，e1大于e2
     * 返回值小于0，e1小于e2
     * @param e1
     * @param e2
     * @return
     */
    private int compare(K e1, K e2) {
        if (comparator != null) {
            return comparator.compare(e1, e2);
        }
        return ((Comparable<K>)e1).compareTo(e2);
    }

    private void keyNotNullCheck(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null!");
        }
    }

    private Node<K, V> createNode(K key, V value, Node<K, V> parent) {
        return new Node<>(key, value, parent);
    }

    private boolean valuesEqual(V v1, V v2) {
        return Objects.equals(v1, v2);
    }

    /**
     * 找到一个节点的后继节点返回
     * @param node
     * @return
     */
    protected Node<K, V> successor(Node<K, V> node){
        if (node == null) return null;

        Node<K, V> pre = node.right;
        if (pre != null) {
            while (pre.left != null) {
                pre = pre.left;
            }
            return pre;
        }
        while (node.parent != null && node == node.parent.right) {
            node = node.parent;
        }
        return node.parent;
    }

    /**
     * 根据键找到其对应的节点
     * @param key
     * @return
     */
    private Node<K, V> node(K key) {
        Node<K, V> node = root;
        while (node != null) {
            int cmp = compare(key, node.key);
            if (cmp == 0) {
                return node;
            } else if (cmp < 0) {
                node = node.left;
            } else { //cmp > 0
                node = node.right;
            }
        }
        return null;   //个人感觉返回 node 也行
    }

    protected void rotateRight(Node<K, V> grand) {
        Node<K, V> parent = grand.left;
        Node<K, V> child = parent.right;
        grand.left = child;
        parent.right = grand;
        //维护变更节点的父节点 <!--
        afterRotate(grand, parent, child);
        //维护完成  -->
    }

    protected void rotateLeft(Node<K, V> grand) {
        Node<K, V> parent = grand.right;
        Node<K, V> child = parent.left;
        grand.right = child;
        parent.left = grand;
        //维护变更节点的父节点 <!--
        afterRotate(grand, parent, child);
        //维护完成  -->
    }

    protected void afterRotate(Node<K, V> grand, Node<K, V> parent, Node<K, V> child) {

        //让parent成为子树的根节点
        parent.parent = grand.parent;
        if (grand.isLeftChild()) {
            grand.parent.left = parent;
        } else if (grand.isRightChild()) {
            grand.parent.right = parent;
        } else {  // grand没有父节点，即grand是根节点
            root = parent;
        }

        //更新child的父节点 注意：可能存在空的情况
        if (child != null) {
            child.parent = grand;
        }

        //更新grand 的父节点
        grand.parent = parent;

    }


    private static class Node<K, V> {
        private K key;
        private V value;
        private boolean color = RED;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        public Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        public boolean hasTwoChildren() {
            return left != null && right != null;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public boolean isLeftChild() {
            return parent != null && this == parent.left;
        }

        public boolean isRightChild() {
            return parent != null && this == parent.right;
        }

        /**
         * 查找兄弟节点，并返回
         * @return
         */
        public Node<K, V> sibling() {
            if (isLeftChild()) return parent.right;
            if (isRightChild()) return parent.left;
            return null;
        }
    }
}
