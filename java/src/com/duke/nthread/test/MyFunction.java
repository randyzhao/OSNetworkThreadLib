package com.duke.nthread.test;

import java.io.IOException;

import com.duke.nthread.Function;

public class MyFunction extends Function {
	
	@Override
	public void run() {
		try {
			System.out.println("running my function");
			ts.set("a", param);
			System.out.println(ts.get("a"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
