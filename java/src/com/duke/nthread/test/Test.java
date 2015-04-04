package com.duke.nthread.test;

import com.duke.nthread.PseudoThreadServer;

public class Test {
	
	public static void main(String[] args) throws Exception {
		final PseudoThreadServer pts = new PseudoThreadServer();
		MyFunction mf1 = new MyFunction();
		MyFunction mf2 = new MyFunction();
		mf1.setThreadServer(pts);
		mf1.setParam(1);
		mf2.setThreadServer(pts);
		mf2.setParam(2);
		pts.createThread(mf1);
		pts.createThread(mf2);
	}

}
