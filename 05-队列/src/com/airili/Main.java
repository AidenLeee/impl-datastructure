package com.airili;


import com.airili.circle.CircleDeque;
import com.airili.circle.CircleQueue;

public class Main {

	static void test1() {
/*		Queue<Integer> queue = new Queue<>();
		queue.enQueue(11);
		queue.enQueue(22);
		queue.enQueue(33);
		queue.enQueue(44);

		while (!queue.isEmpty()) {
			System.out.println(queue.deQueue());
		}*/

		Deque<Integer> queue = new Deque<>();
		queue.enQueueFront(11);
		queue.enQueueFront(22);
		queue.enQueueRear(33);
		queue.enQueueRear(44);

		/* 尾  44  33   11  22 头 */

		int length = queue.size();
		for (int i = 0; i < length; i++) {
			if ((i & 1) != 0) {
				System.out.println("尾出：" + i);
				System.out.println(queue.deQueueRear());
			} else {
				System.out.println("头出：" + i);
				System.out.println(queue.deQueueFront());
			}
		}
	}

/*	static void test2() {
		CircleQueue<Integer> queue = new CircleQueue<>();
		// 0 1 2 3 4 5 6 7 8 9
		for (int i = 0; i < 10; i++) {
			queue.enQueue(i);
		}
		System.out.println(queue);
		System.out.println("----------");
		// null null null null null 5 6 7 8 9
		for (int i = 0; i < 5; i++) {
			queue.deQueue();
		}
		System.out.println(queue);
		System.out.println("----------");
		// 15 16 17 18 19 5 6 7 8 9
		for (int i = 15; i < 20; i++) {
			queue.enQueue(i);
		}
		System.out.println(queue);
		System.out.println("----------");
		while (!queue.isEmpty()) {
			System.out.println(queue.deQueue());
		}
		System.out.println(queue);
	}*/

	static void test3() {
		CircleDeque<Integer> queue = new CircleDeque<>();
		// 头5 4 3 2 1  100 101 102 103 104 105 106 8 7 6 尾

		// 头 8 7 6  5 4 3 2 1  100 101 102 103 104 105 106 107 108 109 null null 10 9 尾
		for (int i = 0; i < 10; i++) {
			queue.enQueueFront(i + 1);
			queue.enQueueRear(i + 100);
		}
		System.out.println(queue);
		System.out.println("----------");
		// 头 null 7 6  5 4 3 2 1  100 101 102 103 104 105 106 null null null null null null null 尾
		for (int i = 0; i < 3; i++) {
			queue.deQueueFront();
			queue.deQueueRear();
		}
		System.out.println(queue);
		System.out.println("----------");
		// 头 11 7 6  5 4 3 2 1  100 101 102 103 104 105 106 null null null null null null 12 尾
		queue.enQueueFront(11);
		queue.enQueueFront(12);
		System.out.println(queue);
		while (!queue.isEmpty()) {
			System.out.println(queue.deQueueFront());
		}
		System.out.println(queue);
		System.out.println("----------");
	}

	public static void main(String[] args) {
		test3();
		//test3();

//		int n = 13;
//		int m = 7;
//
////		if (n >= m) {
////			System.out.println(n - m);
////		} else {
////			System.out.println(n);
////		}
//
//		// m > 0, n >= 0, n < 2m
//		System.out.println(n - (n >= m ? m : 0));
//
//		System.out.println(n % m);
	}

}
