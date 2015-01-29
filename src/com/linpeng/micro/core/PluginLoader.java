package com.linpeng.micro.core;

/**
 * Plugin loader:load all resources from plugins
 * 
 * @author linpeng
 *
 */
public interface PluginLoader {

	public static final String PLUGIN_SOURCE = "plugins/";
	public static final String ACTION_SOURCE = "action";
	public static final String ROUTER_SOURCE = "router";
	public static final String VIEW_SOURCE = "view";

	/**
	 * Load all resources from plugin
	 */
	public void load();

}
