package com.duke.nthread.test;

public class Producer extends Thread{
	@Override
	public void run() {

		for (int i = 0; i < 50; i++) {
			super.run();
			produce();
		}

	}
	
	private synchronized void produce() {
		long pstartTime = System.nanoTime();
		Good g = new Good();
		System.out.println("Producer: I made " + g.id);
		
		try {
			ProdCon.empty.acquire();
			ProdCon.mutex.acquire();
			
			ProdCon.buffer.add(g);
			System.out.println("Producer: " + g.id + " is in the basket!");
			
			ProdCon.mutex.release();
			ProdCon.full.release();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long pendTime = System.nanoTime();
		long ptotalTime = pendTime-pstartTime;
		
		System.out.println("Total Time of each produce is "+ptotalTime+" ns.");
		
	}
}
