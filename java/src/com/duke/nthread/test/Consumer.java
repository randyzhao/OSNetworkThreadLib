package com.duke.nthread.test;

public class Consumer extends Thread{
	
	public int id;
	public int totalnum=25;
	public Consumer(int IDN){
		id=IDN;
	}
	
	@Override
	public void run() {
		
		
		for (int i = 0; i < totalnum; i++) {
			super.run();
			try {
				consume();
			}
			catch(Exception e) {
				System.out.println("Consumer"+String.valueOf(id)+": I can't consume :(");
			}
		}
		

	}
	
	private synchronized void consume() {
		long cstartTime = System.nanoTime();
		Good g = null;
		
		try {
			ProdCon.full.acquire();
			ProdCon.mutex.acquire();
			
			System.out.println("Consumer"+String.valueOf(id)+": I'm getting in the basket");
			g = ProdCon.buffer.remove();
			System.out.println("Consumer"+String.valueOf(id)+": I got " + g.id);
			
			ProdCon.mutex.release();
			ProdCon.empty.release();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long cendTime = System.nanoTime();
		long ctotalTime = cendTime-cstartTime;
		
		System.out.println("Total Time of each consume is "+ctotalTime+" ns.");
		
		System.out.println("Consumer"+String.valueOf(id)+": I consumed " + g.id);
	}
}
