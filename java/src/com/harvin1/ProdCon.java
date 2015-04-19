package com.harvin1;

import java.io.IOException;
import java.util.ArrayList;

//import java.util.concurrent.Semaphore;

import com.duke.nthread.Function;
import com.duke.nthread.HttpThreadServer;
//import com.duke.nthread.ThreadServer;
import com.duke.nthread.Worker;
//import com.duke.nthread.test.Test.TestLockFunction;

public class ProdCon {

//	public static Basket buffer;
	public static ArrayList<Good> buffer = new ArrayList<Good>();

	// public static final Semaphore mutex = new Semaphore(1);
	// public static final Semaphore full = new Semaphore(0);
	// public static final Semaphore empty = new Semaphore(1);

	public static void main(String[] args) throws Exception {
		//buffer = new Basket();

		// Producer prod = new Producer();
		// Consumer con1 = new Consumer(1);
		// Consumer con2 = new Consumer(2);
		HttpThreadServer hts = new HttpThreadServer("127.0.0.1:55555");
		Worker prod = new Worker(hts, 55551);
		Worker con1 = new Worker(hts, 55552);
		Worker con2 = new Worker(hts, 55553);

		prod.start();
		con1.start();
		con2.start();
		
		hts.semInit("mutex", 1);

		hts.semInit("full", 10);
		hts.semDown("full", 10, 1);
		
		hts.semInit("empty", 10);
		
		hts.createThread(new Producer());
		hts.createThread(new Consumer());
		hts.createThread(new Consumer());
		

		
		
	}

	static int totalnum = 500;
	
	public static class Consumer extends Function {

		public int count = 0;

		@Override
		public void run() {
			for (int i = 0; i < totalnum; i++) {
				// super.run();
				try {
					consume();
				} catch (Exception e) {
					System.out.println("Consumer: I can't consume :(");
				}
			}

		}

		private synchronized void consume() {
			Good g = null;

			try {
				// ProdCon.full.acquire();
				// ProdCon.mutex.acquire();
				ts.semDown("full", 1, getPort());
				ts.semDown("mutex", 1, getPort());

				System.out.println("Consumer: I'm getting in the basket");
				g = ProdCon.buffer.remove(0);
				System.out.println("Consumer: I got " + g.id );

				// ProdCon.mutex.release();
				// ProdCon.empty.release();
				ts.semUp("mutex", 1);
				ts.semUp("empty", 1);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("Consumer: I consumed " + g.id);
		}
	}

	public static class Producer extends Function {
		@Override
		public void run() {
			for (int i = 0; i < totalnum; i++) {
				// super.run();
				produce();
			}
		}

		private synchronized void produce() {
			Good g = new Good();
			System.out.println("Producer: I made " + g.id);

			try {
				// ProdCon.empty.acquire();
				// ProdCon.mutex.acquire();
				ts.semDown("empty", 1, getPort());
				ts.semDown("mutex", 1, getPort());

				ProdCon.buffer.add(g);
				System.out.println("Producer: " + g.id + " is in the basket!");

				// ProdCon.mutex.release();
				// ProdCon.full.release();
				ts.semUp("mutex", 1);
				ts.semUp("full", 1);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}