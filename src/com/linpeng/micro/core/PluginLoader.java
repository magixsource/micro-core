package com.linpeng.micro.core;

/**
 * Plugin loader:load all resources from plugins
 * 
 * @author linpeng
 *
 */
public interface PluginLoader {

	public static final String PLUGIN_SOURCE = "plugins/";

	/**
	 * Load all resources from plugin
	 */
	public void load();

}
