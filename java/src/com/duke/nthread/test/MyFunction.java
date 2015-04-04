package com.duke.nthread.test;

import com.duke.nthread.Function;

public class MyFunction extends Function {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void run() {
		ts.set("a", param);
		System.out.println(ts.get("a"));
	}

}
