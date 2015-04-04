package com.duke.nthread;

public interface ThreadServer {
	
	public void createThread(Function func);
	
	public int get(String key);
	
	public void set(String key, int val);

}
