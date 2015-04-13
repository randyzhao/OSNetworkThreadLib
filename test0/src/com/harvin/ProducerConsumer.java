package com.harvin;

public class ProducerConsumer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        ProducerConsumerQueue sharedQueue = new ProducerConsumerQueue(4);

        Thread prodThread = new Thread(new Producer(sharedQueue));
        Thread consThread1 = new Thread(new Consumer(sharedQueue));
        Thread consThread2 = new Thread(new Consumer(sharedQueue));

        prodThread.start();
        consThread1.start();
        consThread2.start();
	}

}
