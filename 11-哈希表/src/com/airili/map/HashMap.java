package com.airili.map;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * @program: data_structure
 * @description: 哈希map ，查找删除增加 的时间复杂度都为 O(1)，在无序遍历时使用
 * @author: Airili
 * @create: 2021-03-09 12:46
 **/
public class HashMap<K, V> implements Map<K, V> {
    private static final boolean RED = false;
    private static final boolean BLACK = true;
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public HashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        if (size == 0) return;
        size = 0;
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
    }

    @Override
    public V put(K key, V value) {
        //检查是否要扩容
        resize();
        int index = index(key);
        //取出table中index位置的红黑树的根节点
        Node<K, V> root = table[index];
        if (root == null) {  //如果在index位置尚未拥有元素
            root = createNode(key, value, null);
            table[index] = root;
            size++;

            afterPut(root);
            return null;
        }

        //添加的不是第一个节点
        Node<K, V> node = root;    //从根节点开始向下找
        Node<K, V> parent = null;  //保存父节点
        Node<K, V> result = null;  //用于存储结果
        int cmp = 0;            //用来最后一次比较的结果，为了能知道放到父节点的哪一边；
        K k1 = key;
        boolean searched = false;
        int h1 = hash(k1);
        do {
            parent = node;
            K k2 = node.key;
            int h2 = node.hash;
            if (h1 > h2) {
                cmp = 1;
            } else if (h1 < h2) {
                cmp = -1;
            } else if (Objects.equals(k1, k2)) {
                cmp = 0;
            } else if (k1 != null && k2 != null
                        && k1 instanceof Comparable
                        && k1.getClass() == k2.getClass()
                        && (cmp = ((Comparable)k1).compareTo(k2)) != 0) {
            } else if (searched) {  //已经全树扫描过一次
                cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
            } else {  //   searched == false; 还没有扫描，然后再根据内存地址大小决定左右
                if ((node.left != null && (result = node(node.left, k1)) != null)
                        || (node.right != null && (result = node(node.right, k1)) != null)) {
                    // 已经存在这个key
                    node = result;
                    cmp = 0;
                } else { // 不存在这个key
                    searched = true;
                    cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
                }
            }

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
        } while (node != null);

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
        return node != null ? node.value : null;
    }

    @Override
    public V remove(K key) {
        return remove(node(key));
    }

    protected V remove(Node<K, V> node) {
        if (node == null) return null;

        size--;
        //使得LinkedHashMap能正确得删除度为2的节点
        Node<K, V> real = node;
        V oldValue = node.value;
        //如果被删除的节点是度为2的节点
        if (node.hasTwoChildren()) {
            Node<K, V> succ = successor(node);
            node.key = succ.key;
            node.value = succ.value;
            node.hash = succ.hash;
            node = succ;
        }

        //如果被删除的节点是度为1或者0的节点
        Node<K, V> replacement = node.left != null ? node.left : node.right;
        if (replacement != null) { //删除的是度为1的节点
            //把要替换被删除的节点的子节点的父节点指向要被删除的节点的父节点
            replacement.parent = node.parent;
            if (node.parent == null) {   // //删除的是度为1的节点并且是根节点
                table[index(node)] = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else {
                node.parent.right = replacement;
            }

            //在红黑树中这里要传入replacement，并且在AVL树中传入replacement也行，因为在第83行已经做了同作用的指向
            fixRemove(replacement);
        } else if (node.parent == null) { //  删除的是叶子节点（度为0）并且是根节点
            table[index(node)] = null;

            fixRemove(node);
        } else {  //  删除的是叶子节点（度为0）但不是根节点
            if (node == node.parent.left) {
                node.parent.left = null;
            } else {
                node.parent.right = null;
            }

            fixRemove(node);
        }

        //交给子类去处理
        afterRemove(real, node);


        return oldValue;
    }

    @Override
    public boolean containsKey(K key) {
        return node(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        int length = table.length;
        if (size == 0);
        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < length; i++) {
            if (table[i] == null) continue;
            queue.offer(table[i]);
            while (!queue.isEmpty()) {
                Node<K, V> node = queue.poll();
                if (Objects.equals(value, node.value)) return true;

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
        }
        return false;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (size == 0 || visitor == null);
        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) continue;
            queue.offer(table[i]);
            while (!queue.isEmpty()) {
                Node<K, V> node = queue.poll();
                if (visitor.visit(node.key, node.value)) return;

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
        }
    }

    /**
     * 计算索引
     * @param key，支持key为null
     * @return
     */
    private int index(K key) {
        if (key == null) return 0;
        int hash = key.hashCode();
        //为了防止原来的hashCode没有经过处理，所以再将hashCode处理一遍
        hash = hash ^ (hash >>> 16);
        return hash & (table.length - 1);
    }

    private int index(Node<K, V> node) {
        return node.hash & (table.length - 1);
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

    protected void fixRemove(Node<K, V> node) {
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
                    fixRemove(parent);
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
                    fixRemove(parent);
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
     * 根据key找到相应的节点
     * @param k1
     * @return
     */
    private Node<K, V>  node(K k1) {
        int index = index(k1);
        Node<K, V> node = table[index];
        return node == null ? null : node(node, k1);
    }

    private Node<K, V> node(Node<K,V> node, K k1) {
        int h1 = hash(k1);
        //存储查找结果
        Node<K, V> result = null;
        int cmp = 0;
        while (node != null) {
            K k2 = node.key;
            int h2 = node.hash;
            if (h1 > h2) {
                node = node.right;
            } else if (h1 < h2) {
                node = node.left;
            } else if (Objects.equals(k1, k2)) {  //1.哈希值相等
                //2.判断两个key是否equals
                return node;
            } else if (k1 != null && k2 != null
                    && k1.getClass() == k2.getClass()
                    && k1 instanceof Comparable
                    && (cmp = ((Comparable)k1).compareTo(k2)) != 0) {
                //3.判断是否k1，k2是否实现Comparable接口
                node = cmp > 0 ? node.right : node.left;
            } else if (node.right != null && (result = node(node.right, k1)) != null){
                //4.扫描整颗右子树
                return result;
            } else {
                node = node.left;
            }
        }
        return null;
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

    protected void rotateLeft(Node<K, V> grand) {
        Node<K, V> parent = grand.right;
        Node<K, V> child = parent.left;
        grand.right = child;
        parent.left = grand;
        //维护变更节点的父节点 <!--
        afterRotate(grand, parent, child);
        //维护完成  -->
    }

    protected void afterRemove(Node<K, V> real, Node<K, V> node){}

    protected void afterRotate(Node<K, V> grand, Node<K, V> parent, Node<K, V> child) {

        //让parent成为子树的根节点
        parent.parent = grand.parent;
        if (grand.isLeftChild()) {
            grand.parent.left = parent;
        } else if (grand.isRightChild()) {
            grand.parent.right = parent;
        } else {  // grand没有父节点，即grand是根节点
            table[index(grand)] = parent;
        }

        //更新child的父节点 注意：可能存在空的情况
        if (child != null) {
            child.parent = grand;
        }

        //更新grand 的父节点
        grand.parent = parent;

    }

    protected Node<K, V> createNode(K key, V value, Node<K, V> parent) {
        return new Node<>(key, value, parent);
    }

    private int hash(K key) {
        int hash = key == null ? 0 : key.hashCode();
        return hash ^ (hash >>> 16);
    }


    private void resize() {
        if (size / table.length <= DEFAULT_LOAD_FACTOR) return;

        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length << 1];

        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] == null) continue;
            queue.offer(oldTable[i]);
            while (!queue.isEmpty()) {
                Node<K, V> node = queue.poll();

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);

                //放入节点
                moveNode(node);
            }
        }
    }

    private void moveNode(Node<K, V> newNode) {
        //初始化节点
        newNode.left = null;
        newNode.right = null;
        newNode.parent = null;
        newNode.color = RED;

        int index = index(newNode.key);
        //取出table中index位置的红黑树的根节点
        Node<K, V> root = table[index];
        if (root == null) {  //如果在index位置尚未拥有元素
            root = newNode;
            table[index] = root;

            //新添加节点后的处理
            afterPut(root);
            return;
        }

        //添加的不是第一个节点
        Node<K, V> node = root;    //从根节点开始向下找
        Node<K, V> parent = null;  //保存父节点
        Node<K, V> result = null;  //用于存储结果
        int cmp = 0;            //用来最后一次比较的结果，为了能知道放到父节点的哪一边；
        K k1 = newNode.key;
        int h1 = newNode.hash;
        do {
            parent = node;
            K k2 = node.key;
            int h2 = node.hash;
            if (h1 > h2) {
                cmp = 1;
            } else if (h1 < h2) {
                cmp = -1;
            } else if (k1 != null && k2 != null
                    && k1 instanceof Comparable
                    && k1.getClass() == k2.getClass()
                    && (cmp = ((Comparable)k1).compareTo(k2)) != 0) {
            } else {
                cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
            }

            if (cmp > 0) {    //往右走
                node = node.right;
            } else if (cmp < 0) {    //往左走
                node = node.left;
            }
        } while (node != null);

        //放置新添加的元素
       newNode.parent = parent;
        if (cmp > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }

        //新添加节点后的处理
        afterPut(newNode);
    }



    protected static class Node<K, V> {
        //成员变量不设置private修饰符的原因是为了后代能够直接访问
        K key;
        V value;
        int hash;
        boolean color = RED;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        public Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            int hash = key == null ? 0 : key.hashCode();
            this.hash = hash ^ (hash >>> 16);
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
