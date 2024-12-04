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
				if(method.isAnnotationPresent(Get.class)) {
					Get getAnno = method.getAnnotation(Get.class);
					String route = baseRoute + getAnno.router();
					System.out.println("Registering GET route: " + route);
	                registerRouteForMethod(clazz, route, port);
				} else if (method.isAnnotationPresent(Post.class)) {
	                Post postAnno = method.getAnnotation(Post.class);
	                String route = baseRoute + postAnno.router();
	                System.out.println("Registering POST route: " + route);
	                registerRouteForMethod(clazz, route, port);
	            } else if (method.isAnnotationPresent(Put.class)) {
	            	Put putAnno = method.getAnnotation(Put.class);
	                String route = baseRoute + putAnno.router();
	                System.out.println("Registering PUT route: " + route);
	                registerRouteForMethod(clazz, route, port);
	            } else if (method.isAnnotationPresent(Delete.class)) {
	            	Delete deleteAnno = method.getAnnotation(Delete.class);
	                String route = baseRoute + deleteAnno.router();
	                System.out.println("Registering DELETE route: " + route);
	                registerRouteForMethod(clazz, route, port);
	            }
			}
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