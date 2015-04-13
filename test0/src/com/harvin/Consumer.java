package com.harvin;

public class Consumer implements Runnable {
	private final ProducerConsumerQueue sharedQueue;

    public Consumer(ProducerConsumerQueue sharedQueue) {
        this.sharedQueue = sharedQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sharedQueue.take();
            } catch (InterruptedException ex) {
                System.out.println(Thread.currentThread().getName() + " throw a interrupexception.");
                //do something.
            }
        }
    }
}
