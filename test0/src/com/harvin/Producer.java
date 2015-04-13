package com.harvin;

public class Producer implements Runnable {
	private final ProducerConsumerQueue sharedQueue;

    public Producer(ProducerConsumerQueue sharedQueue) {
        this.sharedQueue = sharedQueue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            try {
                sharedQueue.put(i);
            } catch (InterruptedException ex) {
                System.out.println(Thread.currentThread().getName() + " throw a interrupexception. ");
                //do something.
            }
        }
    }
}
