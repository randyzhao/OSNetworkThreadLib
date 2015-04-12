package com.duke.nthread;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import static us.monoid.web.Resty.*;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

public class HttpThreadServer implements ThreadServer {

	private String addr;
	private Map<String, Object> waitingLocks = new HashMap<String, Object>();

	public HttpThreadServer(String addr) {
		this.addr = addr;
	}

	@Override
	public void createThread(Function func) throws IOException {
		Class<?> clazz = func.getClass();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(clazz);
			byte[] classBytes = bos.toByteArray();

			String encodedClass = Base64.encodeBase64String(classBytes);
			String encodedParam = String.valueOf(func.getParam());
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("class", encodedClass);
			jsonObj.put("param", encodedParam);
			new Resty().json("http://" + addr + "/create_thread", put(content(jsonObj)));
		} catch (JSONException je) {
			throw new IOException(je);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
				// ignore close exception
			}
			try {
				bos.close();
			} catch (IOException ex) {
				// ignore close exception
			}
		}
	}

	@Override
	public int get(String key) throws IOException {
		JSONResource jr = new Resty().json("http://" + addr + "/variable?key=" + key);
		try {
			return (int) jr.get("value");
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public void set(String key, int value) throws IOException {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("key", key);
			jsonObj.put("value", value);
		} catch (JSONException e) {
			throw new IOException(e);
		}
		new Resty().json("http://" + addr + "/variable", put(content(jsonObj)));
	}
	
	@Override
	public void lockInit(String name) throws IOException {
		semInit(name, 1);
	}

	@Override
	public void lock(String name, int port) throws IOException {
		semDown(name, 1, port);
	}
	
	@Override
	public void unLock(String name) throws IOException {
		semUp(name, 1);
	}
	
	@Override
	public Object getWaitingLock(String name) {
		synchronized (waitingLocks) {
			return waitingLocks.get(name);
		}
	}

	@Override
	public void semInit(String name, int max) throws IOException {
		new Resty().json("http://" + addr + "/sem_init?name=" + name + "&max=" + max);
	}

	@Override
	public void semUp(String name, int value) throws IOException {
		new Resty().json("http://" + addr + "/sem_up?name=" + name + "&value=" + value);
	}

	@Override
	public void semDown(String name, int value, int port) throws IOException {
		try {
			new Resty().json("http://" + addr + "/sem_down?name=" + name + "&value=" + value
					+ "&port=" + port);
		} catch (IOException ioe) {
			// need to wait
			System.out.println("[semDown()] waiting for sem: " + name + "," + port);
			if (ioe.getMessage().contains("404")) {
				Object lock;
				synchronized(waitingLocks) {
					lock = waitingLocks.get(name);
					if (lock == null) {
						lock = new Object();
						waitingLocks.put(name, lock);
					}
				}
				synchronized (lock) {
					try {
						lock.wait();
						System.out.println("[semDown()] wait return: " + name + "," + port);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// leak the memory here
//				synchronized (waitingLocks) {
//					waitingLocks.remove(name);
//				}
			} else {
				throw ioe;
			}
		}
	}

}
