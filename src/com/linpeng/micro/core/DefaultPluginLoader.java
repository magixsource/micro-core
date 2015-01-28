package com.linpeng.micro.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Load plugin's environment into ctx
 * 
 * @author linpeng
 *
 */
public class DefaultPluginLoader implements PluginLoader {

	private DefaultFrameworkContext ctx;

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
		String pluginName = plugin.getName();
		for (File item : plugin.listFiles()) {
			String fileName = item.getName();
			if (fileName.equalsIgnoreCase("action")) {
				loadAction(pluginName, item);
			} else if (fileName.equalsIgnoreCase("router")) {
				loadRouter(pluginName, item);
			} else if (fileName.equalsIgnoreCase("view")) {
				loadView(pluginName, item);
			}
		}
	}

	/**
	 * Load plugin view
	 * 
	 * @param pluginName
	 * 
	 * @param item
	 */
	@SuppressWarnings("unchecked")
	private void loadView(String pluginName, File item) {
		// index:index.html,other.html...
		List<String> pluginViews = (List<String>) getCtx().getViews().get(
				pluginName);
		boolean callback = false;
		if (null == pluginViews) {
			pluginViews = new ArrayList<String>();
			callback = true;
		}
		for (File view : item.listFiles()) {
			pluginViews.add(view.getName());
		}
		if (callback) {
			getCtx().getViews().put(pluginName, pluginViews);
		}
	}

	/**
	 * Load plugin router
	 * 
	 * @param pluginName
	 * 
	 * @param item
	 */
	@SuppressWarnings("unchecked")
	private void loadRouter(String pluginName, File item) {
		List<Router> pluginRouters = (List<Router>) getCtx().getRouters().get(
				pluginName);
		// Format: / index index.html
		boolean callback = false;
		if (null == pluginRouters) {
			pluginRouters = new ArrayList<Router>();
			callback = true;
		}
		BufferedReader reader = null;
		for (File router : item.listFiles()) {
			try {
				reader = new BufferedReader(new FileReader(router));
				String cfg = reader.readLine();
				String[] array = cfg.split("\t+");

				Router rt = new Router();
				rt.setPath(array[0]);
				rt.setAction(array[1]);
				rt.setView(array[2]);
				pluginRouters.add(rt);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (callback) {
			getCtx().getRouters().put(pluginName, pluginRouters);
		}
	}

	/**
	 * Load plugin action
	 * 
	 * @param pluginName
	 * 
	 * @param item
	 */
	@SuppressWarnings("unchecked")
	private void loadAction(String pluginName, File item) {
		List<String> pluginActions = (List<String>) getCtx().getActions().get(
				pluginName);
		boolean callback = false;
		if (null == pluginActions) {
			pluginActions = new ArrayList<String>();
			callback = true;
		}
		// ClassLoader classLoader = this.getClass().getClassLoader();
		for (File action : item.listFiles()) {
			pluginActions.add(action.getName());
		}
		if (callback) {
			getCtx().getActions().put(pluginName, pluginActions);
		}
	}

	/**
	 * TestCase
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		DefaultFrameworkContext ctx = new DefaultFrameworkContext();
		DefaultPluginLoader loader = new DefaultPluginLoader();
		loader.setCtx(ctx);
		loader.load();
		System.out.println(ctx);
	}

	public DefaultFrameworkContext getCtx() {
		return ctx;
	}

	public void setCtx(DefaultFrameworkContext ctx) {
		this.ctx = ctx;
	}
}
