package br.ufrn.imd.patterns.basicremoting;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RemoteObject {
	private static final Map<String, Method> getRoutes = new HashMap<>();
	private static final Map<String, Method> postRoutes = new HashMap<>();
	private static Object instance;
	
	public static Object getInstance() {
		return instance;
	}

	public static void setInstance(Object instance) {
		RemoteObject.instance = instance;
	}

	public static void addMethodGet(String route, Method method) {
		getRoutes.put(route, method);
	}
	
	public static void addMethodPost(String route, Method method) {
		postRoutes.put(route, method);
	}
	
	public static Method getMethod(String route) {
		Method method = getRoutes.get(route);
		if(method == null) {
			method = postRoutes.get(route);
		}
		return method;
	}
	
	public static Iterable<String> getRoutes(){
		return getRoutes.keySet();
	}
}
