package com.duke.nthread;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.commons.codec.binary.Base64;

import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Worker {

	private ThreadServer ts;
	private HttpServer server;
	private int port;

	public Worker(ThreadServer ts, int port) {
		this.ts = ts;
		this.port = port;
	}

	public void start() throws IOException {
		server = HttpServer.create(new InetSocketAddress(port), 0);
		server.createContext("/assign_thread", new ThreadHandler());
		server.createContext("/get_lock", new GetLockHandler());
		server.createContext("/get_sem", new GetSemHandler());
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
	}
	
	class GetSemHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange t) throws IOException {
			System.out.println(t.getRequestMethod());
			System.out.println(t.getRequestHeaders().values());
			System.out.println(t.getRequestURI().getQuery());
			
			try {
				String name = t.getRequestURI().getQuery().split("=")[1];
				Object lock = ts.getWaitingLock(name);
				System.out.println("[Worker] get sem: " + name);
				synchronized (lock) {
					lock.notify();
				}
			} catch (Throwable th) {
				th.printStackTrace();
				throw new IOException(th);
			}
			
			String response = "Get sem done";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
		
	}
	
	@Deprecated
	class GetLockHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange t) throws IOException {
			System.out.println(t.getRequestMethod());
			System.out.println(t.getRequestHeaders().values());
			System.out.println(t.getRequestURI().getQuery());
			
			try {
				String name = t.getRequestURI().getQuery().split("=")[1];
				Object lock = ts.getWaitingLock(name);
				System.out.println("[Worker] get lock: " + name);
				synchronized (lock) {
					lock.notify();
				}
			} catch (Throwable th) {
				th.printStackTrace();
				throw new IOException(th);
			}
			
			String response = "Get lock done";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
		
	}

	class ThreadHandler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			System.out.println(t.getRequestMethod());
			System.out.println(t.getRequestHeaders().values());
			
			InputStream is = t.getRequestBody();
			StringBuilder sb = new StringBuilder();
			byte[] buffer = new byte[8192];
			while (is.read(buffer) != -1) {
				sb.append(new String(buffer));
			}
			System.out.println(sb);
			
			try {
				JSONObject jsonObj = new JSONObject(sb.toString());
				String encodedClass = jsonObj.getString("class");
				String encodedParam = jsonObj.getString("param");

				ByteArrayInputStream bis = new ByteArrayInputStream(
						Base64.decodeBase64(encodedClass));
				ObjectInput in = new ObjectInputStream(bis);
				Class<?> clazz = (Class<?>) in.readObject();
				in.close();
				
				Function func = (Function) clazz.newInstance();
				func.setThreadServer(ts);
				func.setParam(Integer.valueOf(encodedParam));
				func.setPort(port);
				func.run();
			} catch (JSONException je) {
				je.printStackTrace();
				throw new IOException(je);
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
				throw new IOException(cnfe);
			} catch (Throwable th) {
				th.printStackTrace();
				throw new IOException(th);
			}
			
			String response = "Thread created";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}

}
