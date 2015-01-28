package com.linpeng.micro.core;

/**
 * Router
 * 
 * @author linpeng
 *
 */
public class Router {
	/** request path */
	public String path;
	/** which action */
	public String action;
	/** which view */
	public String view;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

}
