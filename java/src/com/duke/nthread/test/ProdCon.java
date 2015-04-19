package com.duke.nthread.test;

import java.util.concurrent.Semaphore;

public class ProdCon {
	
	public static Basket buffer;
	public static final Semaphore mutex = new Semaphore(1);
	public static final Semaphore full = new Semaphore(0);
	public static final Semaphore empty = new Semaphore(1);
	
	public static void main(String[] args) {

		
		buffer = new Basket();

		Producer prod = new Producer();
		Consumer con1 = new Consumer(1);
		Consumer con2 = new Consumer(2);
		
		prod.start();
		con1.start();
		con2.start();
		
	}

	
}




