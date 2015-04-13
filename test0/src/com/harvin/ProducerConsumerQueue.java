package com.harvin;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class ProducerConsumerQueue<E> {
	
	 private final Lock lock = new ReentrantLock();
	    private final Condition notEmpty = lock.newCondition();
	    private final Condition notFull = lock.newCondition();
	    private final int CAPACITY;
	    private Queue<E> list;

	    public ProducerConsumerQueue(int CAPACITY) {
	        this.CAPACITY = CAPACITY;
	        this.list = new LinkedList<E>();
	    }

	    //put the e into the queue.
	    public void put(E e) throws InterruptedException {
	        try {
	            lock.lock();

	            while (list.size() == this.CAPACITY) {
	                System.out.println("Producer " + Thread.currentThread().getName() + " is blocked.");
	                this.notFull.await();
	                System.out.println("Producer " + Thread.currentThread().getName() + " resumes.");
	            }
	            System.out.println("Producer " + Thread.currentThread().getName() + " put the element.");
	            list.offer(e);
	            this.notEmpty.signalAll();

	            System.out.println("After the Producer:" + Thread.currentThread().getName());
	            showQueue();

	        } finally {
	            //System.out.println("Producer "+Thread.currentThread().getName()+" unlock.");
	            lock.unlock();
	        }
	    }

	    //get e from queue.
	    public E take() throws InterruptedException {
	        try {
	            lock.lock();

	            E tmp;

	            while (list.size() == 0) {
	                System.out.println("Consumer " + Thread.currentThread().getName() + " is blocked.");
	                this.notEmpty.await();
	                System.out.println("Consumer " + Thread.currentThread().getName() + " resumes.");
	            }
	            System.out.println("Consumer " + Thread.currentThread().getName() + " get the element.");
	            tmp = list.poll();
	            notFull.signalAll();

	            System.out.println("After the Consumer:" + Thread.currentThread().getName());
	            showQueue();
	            return tmp;

	        } finally {
	            //System.out.println("Consumer "+Thread.currentThread().getName()+" unlock.");
	            lock.unlock();
	            //to make the consumer process more clear, you can cancel the comment of the follow line.
	            //Thread.sleep(5);
	        }
	    }


	    private void showQueue() {
	        System.out.print("\tThe queue now is :");
	        for (E e : list)
	            System.out.print("    " + e);
	        System.out.println("\n");
	    }
}
