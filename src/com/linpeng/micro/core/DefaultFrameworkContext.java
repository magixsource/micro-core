package com.linpeng.micro.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Default Framework context: support useful tools
 * 
 * @author linpeng
 *
 */
public class DefaultFrameworkContext implements FrameworkContext {
	private Map<String, Object> actions = new HashMap<String, Object>();
	private Map<String, Object> routers = new HashMap<String, Object>();
	private Map<String, Object> views = new HashMap<String, Object>();
	
	public Map<String, Object> getViews() {
		return views;
	}

	public void setViews(Map<String, Object> views) {
		this.views = views;
	}

	public Map<String, Object> getRouters() {
		return routers;
	}

	public void setRouters(Map<String, Object> routers) {
		this.routers = routers;
	}

	public Map<String, Object> getActions() {
		return actions;
	}

	public void setActions(Map<String, Object> actions) {
		this.actions = actions;
	}
}
