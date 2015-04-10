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
		Worker worker1 = new Worker(hts, 55555);
		Worker worker2 = new Worker(hts, 55556);
		Worker worker3 = new Worker(hts, 55557);
		worker1.start();
		worker2.start();
		worker3.start();
//		MyFunction func1 = new MyFunction();
//		func1.setParam(5);
//		MyFunction func2 = new MyFunction();
//		func2.setParam(7);
//		hts.createThread(func1);
//		hts.createThread(func2);
//		hts.createThread(new NotFoundFunction());
		hts.createThread(new TestLockFunction());
		hts.createThread(new TestLockFunction());
		hts.createThread(new TestLockFunction());
	}
	
	static int counter = 101;
	
	public static class TestLockFunction extends Function {

		@Override
		public void run() {
			try {
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
