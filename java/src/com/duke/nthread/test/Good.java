package com.duke.nthread.test;

public class Good {
	private static int _id = 0;
	public int id;
	
	public Good() {
		id = _id++;
	}
}
