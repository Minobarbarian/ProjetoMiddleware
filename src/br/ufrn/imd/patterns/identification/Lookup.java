package br.ufrn.imd.patterns.identification;

import java.util.HashMap;

import br.ufrn.imd.middleware.annotations.RequestMap;

public class Lookup {
	private final HashMap<String, AbsoluteObjectReference> routes;
	
	public Lookup() {
		routes = new HashMap<>();
	}
	
	public void registerRoute(Class<?> clazz, int port) {
		if (clazz.isAnnotationPresent(RequestMap.class)) {
			RequestMap anno = clazz.getAnnotation(RequestMap.class);
			String route = anno.router();
			
			ObjectId objectId = new ObjectId();
			AbsoluteObjectReference reference = new AbsoluteObjectReference(objectId, "localhost", port);
			routes.put(route, reference);
		}
	}

    public AbsoluteObjectReference getRoute(String route) {
        return routes.get(route);
    }
}