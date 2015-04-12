package com.duke.nthread.test;

import java.io.IOException;

import com.duke.nthread.Function;
import com.duke.nthread.HttpThreadServer;
import com.duke.nthread.Worker;

public class Test {

	public static void main(String[] args) throws Exception {
//		final PseudoThreadServer pts = new PseudoThreadServer();
//		MyFunction mf1 = new MyFunction();
//		MyFunction mf2 = new MyFunction();
//		mf1.setThreadServer(pts);
//		mf1.setParam(1);
//		mf2.setThreadServer(pts);
//		mf2.setParam(2);
//		pts.createThread(mf1);
//		pts.createThread(mf2);
//		
//		HttpThreadServer hts = new HttpThreadServer("192.168.0.1:8080");
//		hts.createThread(new MyFunction());
		
//		HttpThreadServer hts = new HttpThreadServer("127.0.0.1:55555");
		HttpThreadServer hts = new HttpThreadServer("152.3.136.156:55555");
		Worker worker1 = new Worker(hts, 55556);
		Worker worker2 = new Worker(hts, 55557);
		Worker worker3 = new Worker(hts, 55558);
		Worker worker4 = new Worker(hts, 55559);
		Worker worker5 = new Worker(hts, 55560);
		Worker worker6 = new Worker(hts, 55561);
		Worker worker7 = new Worker(hts, 55562);
		Worker worker8 = new Worker(hts, 55563);
		worker1.start();
		worker2.start();
		worker3.start();
		worker4.start();
		worker5.start();
		worker6.start();
		worker7.start();
		worker8.start();
//		MyFunction func1 = new MyFunction();
//		func1.setParam(5);
//		MyFunction func2 = new MyFunction();
//		func2.setParam(7);
//		hts.createThread(func1);
//		hts.createThread(func2);
//		hts.createThread(new NotFoundFunction());
		hts.lockInit("a");
		hts.createThread(new TestLockFunction());
		hts.createThread(new TestLockFunction());
		hts.createThread(new TestLockFunction());
		
		hts.semInit("b", 3);
		hts.createThread(new TestSemUp());
		hts.createThread(new TestSemDown());
		hts.createThread(new TestSemDown());
		hts.createThread(new TestSemDown());
		hts.createThread(new TestSemDown());
		
	}
	
	static int counter = 101;
	
	public static class TestLockFunction extends Function {

		@Override
		public void run() {
			try {
				System.out.println("running");
				ts.lock("a", getPort());
				ts.set("a", counter++);
				System.out.println(ts.get("a"));
				ts.unLock("a");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static class TestSemUp extends Function {

		@Override
		public void run() {
			try {
				System.out.println("sem up 3");
				ts.semUp("b", 3);
				System.out.println("sem up 3 returned");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static class TestSemDown extends Function {

		@Override
		public void run() {
			try {
				System.out.println("sem down 1");
				ts.semDown("b", 1, getPort());
				System.out.println("sem down 1 returned");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static class NotFoundFunction extends Function {

		@Override
		public void run() {
			try {
				ts.get("nothiskey");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
