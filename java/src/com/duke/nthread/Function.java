package com.duke.nthread;

public abstract class Function implements Runnable {

	protected ThreadServer ts;
	protected int param;
	protected int port;

	public void setParam(int param) {
		this.param = param;
	}
	
	public int getParam() {
		return param;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}

	public void setThreadServer(ThreadServer ts) {
		this.ts = ts;
	}


}
