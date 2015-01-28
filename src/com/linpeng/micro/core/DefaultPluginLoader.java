package com.linpeng.micro.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linpeng.micro.action.Action;

/**
 * Load plugin's environment into ctx
 * 
 * @author linpeng
 *
 */
public class DefaultPluginLoader implements PluginLoader {

	private DefaultFrameworkContext ctx;
	public static final File plugins = new File(PLUGIN_SOURCE);

	@Override
	public void load() {
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
	private void loadAction(String pluginName, File item) {
		Map<String, Object> pluginActions = getCtx().getActions();

		if (null == pluginActions) {
			pluginActions = new HashMap<String, Object>();
		}
		ClassLoader classLoader = this.getClass().getClassLoader();
		List<File> javaFiles = new ArrayList<File>();

		String basePath = plugins.getAbsolutePath() + File.separatorChar
				+ pluginName + File.separatorChar + "action"
				+ File.separatorChar;

		recursiveFile(item, javaFiles);
		for (File action : javaFiles) {

			String className = parsetPath2ClassName(action.getAbsolutePath(),
					basePath);
			try {
				Class<?> clz = classLoader.loadClass(className);
				if (clz.isAnnotationPresent(Action.class)) {
					String actionName = clz.getAnnotation(Action.class).value() == null ? clz
							.getSimpleName().toLowerCase() : clz.getAnnotation(
							Action.class).value();
					pluginActions.put(actionName, clz);
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	private String parsetPath2ClassName(String path, String basePath) {
		String className = path.replace(basePath, "").replace(".java", "")
				.replace(File.separator, ".");
		return className;
	}

	private void recursiveFile(File item, List<File> javaFiles) {
		if (item.isDirectory()) {
			for (File file : item.listFiles()) {
				recursiveFile(file, javaFiles);
			}
		} else {
			if (item.getName().endsWith(".java")) {
				javaFiles.add(item);
			}
		}
	}

	public DefaultFrameworkContext getCtx() {
		return ctx;
	}

	public void setCtx(DefaultFrameworkContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * TestCase
	 * 
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		DefaultFrameworkContext ctx = new DefaultFrameworkContext();
		DefaultPluginLoader loader = new DefaultPluginLoader();
		loader.setCtx(ctx);
		loader.load();
		// System.out.println(ctx);

		// TEST==========
		String req = "/";
		// step1 find action
		Map<String, Object> routers = ctx.getRouters();
		Router matchRouter = null;
		for (String key : routers.keySet()) {
			List<Router> list = (List<Router>) routers.get(key);
			for (Router router : list) {
				if (router.getPath().equalsIgnoreCase(req)) {
					matchRouter = router;
					break;
				}
			}
			if (matchRouter != null) {
				break;
			}
		}
		// step2 invoke action
		Object actionObj = null;
		if (matchRouter != null) {
			String action = matchRouter.getAction();

			Class<?> clz = (Class<?>) ctx.getActions()
					.get(action.split("/")[0]);
			try {
				if (null == clz) {
					throw new FileNotFoundException("404");
				}
				actionObj = clz.newInstance();
				String methodName = action.split("/")[1];
				Method method = clz.getMethod(methodName, null);
				method.invoke(actionObj, new Object[] {});
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	}
}
