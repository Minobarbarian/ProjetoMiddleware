package br.ufrn.imd.patterns.identification;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RouteRegistry {
	private static final Map<String, Method> routeMethods = new HashMap<>();

    // Register a method for a specific route
    public static void registerRoute(String route, Method method) {
        routeMethods.put(route, method);
    }

    // Retrieve the method associated with a route
    public static Method getMethod(String route) {
        return routeMethods.get(route);
    }

    // Get all registered routes
    public static Set<String> getRoutes() {
        return routeMethods.keySet();
    }
}