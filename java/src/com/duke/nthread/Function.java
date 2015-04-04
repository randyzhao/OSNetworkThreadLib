package com.duke.nthread;

import java.io.Serializable;

public abstract class Function implements Runnable, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected ThreadServer ts;
	protected int param;
	
	public void setParam(int param) {
		this.param = param;
	}
	
	public void setThreadServer(ThreadServer ts) {
		this.ts = ts;
	}

}
