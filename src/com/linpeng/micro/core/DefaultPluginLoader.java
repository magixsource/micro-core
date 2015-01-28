package com.linpeng.micro.core;

import java.io.File;

/**
 * Load plugin's environment into ctx
 * 
 * @author linpeng
 *
 */
public class DefaultPluginLoader implements PluginLoader {

	DefaultFrameworkContext ctx;

	@Override
	public void load() {
		File plugins = new File(PLUGIN_SOURCE);
		checkDirectory(plugins);
		for (File plugin : plugins.listFiles()) {
			loadPlugin(plugin);
		}
	}

	/**
	 * Check is file okay
	 * 
	 * @param plugins
	 */
	private void checkDirectory(File plugins) {
		if (plugins == null) {
			throw new IllegalArgumentException("File must be not null.");
		}
		if (!plugins.isDirectory()) {
			throw new RuntimeException("File must be created.");
		}
	}

	/**
	 * Load plug by file
	 * 
	 * @param plugin
	 */
	private void loadPlugin(File plugin) {
		checkDirectory(plugin);
		for (File item : plugin.listFiles()) {
			String fileName = item.getName();
			if (fileName.equalsIgnoreCase("action")) {
				loadAction(item);
			} else if (fileName.equalsIgnoreCase("router")) {
				loadRouter(item);
			} else if (fileName.equalsIgnoreCase("view")) {
				loadView(item);
			}
		}
	}

	/**
	 * Load plugin view
	 * 
	 * @param item
	 */
	private void loadView(File item) {

	}

	/**
	 * Load plugin router
	 * 
	 * @param item
	 */
	private void loadRouter(File item) {

	}

	/**
	 * Load plugin action
	 * 
	 * @param item
	 */
	private void loadAction(File item) {

	}

	/**
	 * TestCase
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		DefaultPluginLoader loader = new DefaultPluginLoader();
		loader.load();
	}
}
