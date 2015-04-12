package com.duke.nthread;

import java.io.IOException;

public interface ThreadServer {

	public void createThread(Function func) throws IOException;

	public int get(String key) throws IOException;

	public void set(String key, int val) throws IOException;
	
	public void lockInit(String name) throws IOException;
	
	public void lock(String name, int port) throws IOException;
	
	public void unLock(String name) throws IOException;
	
	public void semInit(String name, int max) throws IOException;
	
	public void semUp(String name, int value) throws IOException;
	
	public void semDown(String name, int value, int port) throws IOException;
	
	public Object getWaitingLock(String name);

}
