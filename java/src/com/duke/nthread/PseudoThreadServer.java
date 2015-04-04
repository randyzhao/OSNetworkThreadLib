package com.duke.nthread;

import java.util.HashMap;
import java.util.Map;

public class PseudoThreadServer implements ThreadServer {
	
	Map<String, Integer> keyValues = new HashMap<String, Integer>();

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

}
