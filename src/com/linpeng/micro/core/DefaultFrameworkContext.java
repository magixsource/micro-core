package com.linpeng.micro.core;

import java.util.Map;

/**
 * Default Framework context: support useful tools
 * 
 * @author linpeng
 *
 */
public class DefaultFrameworkContext implements FrameworkContext {
	public Map<String, Object> actions;
	public Map<String, Object> routers;
	public Map<String, Object> views;
}
