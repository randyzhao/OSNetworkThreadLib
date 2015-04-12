package com.duke.nthread;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PseudoThreadServer implements ThreadServer {

	Map<String, Integer> keyValues = new HashMap<String, Integer>();
	Map<String, Object> locks = new HashMap<String, Object>();

	@Override
	public void createThread(Function func) {
		new Thread(func).start();
	}

	@Override
	public int get(String key) {
		return keyValues.get(key);
	}

	@Override
	public void set(String key, int val) {
		keyValues.put(key, val);
	}

	@Override
	public void lockInit(String name) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lock(String name, int port) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unLock(String name) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getWaitingLock(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void semInit(String name, int max) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void semUp(String name, int value) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void semDown(String name, int value, int port) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
