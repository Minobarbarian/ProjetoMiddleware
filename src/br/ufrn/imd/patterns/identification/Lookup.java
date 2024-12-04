package br.ufrn.imd.patterns.identification;

import java.util.HashMap;

import br.ufrn.imd.middleware.annotations.Delete;
import br.ufrn.imd.middleware.annotations.Get;
import br.ufrn.imd.middleware.annotations.Post;
import br.ufrn.imd.middleware.annotations.Put;
import br.ufrn.imd.middleware.annotations.RequestMap;

public class Lookup {
	private final HashMap<String, AbsoluteObjectReference> routes;
	
	public Lookup() {
		routes = new HashMap<>();
	}
	
	public void registerRoute(Class<?> clazz, int port) {
		if (clazz.isAnnotationPresent(RequestMap.class)) {
			RequestMap classAnno = clazz.getAnnotation(RequestMap.class);
			String baseRoute = classAnno.router();
			for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
				registerRouteForAnnotatedMethod(clazz, baseRoute, method, port);
			}
		}
	}
	
	private void registerRouteForAnnotatedMethod(Class<?> clazz, String baseRoute, java.lang.reflect.Method method, int port) {
		String route = null;
        if (method.isAnnotationPresent(Get.class)) {
            route = baseRoute + method.getAnnotation(Get.class).router();
            System.out.println("Registering GET route: " + route);
        } else if (method.isAnnotationPresent(Post.class)) {
            route = baseRoute + method.getAnnotation(Post.class).router();
            System.out.println("Registering POST route: " + route);
        } else if (method.isAnnotationPresent(Put.class)) {
            route = baseRoute + method.getAnnotation(Put.class).router();
            System.out.println("Registering PUT route: " + route);
        } else if (method.isAnnotationPresent(Delete.class)) {
            route = baseRoute + method.getAnnotation(Delete.class).router();
            System.out.println("Registering DELETE route: " + route);
        }
        
        if (route != null) {
            registerRouteForMethod(clazz, route, port);
        }
	}
	
	private void registerRouteForMethod(Class<?> clazz, String route, int port) {
	    ObjectId objectId = new ObjectId();
	    AbsoluteObjectReference reference = new AbsoluteObjectReference(objectId, clazz, "localhost", port);
	    routes.put(route, reference);
	}

    public AbsoluteObjectReference getRoute(String route) {
        return routes.get(route);
    }
}