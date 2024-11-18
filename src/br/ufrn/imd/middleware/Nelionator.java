package br.ufrn.imd.middleware;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import br.ufrn.imd.middleware.annotations.Delete;
import br.ufrn.imd.middleware.annotations.Get;
import br.ufrn.imd.middleware.annotations.Post;
import br.ufrn.imd.middleware.annotations.Put;
import br.ufrn.imd.middleware.annotations.RequestMap;
import br.ufrn.imd.patterns.basicremoting.RemoteObject;
import br.ufrn.imd.patterns.basicremoting.ServerRequestHandler;
import br.ufrn.imd.patterns.identification.AbsoluteObjectReference;
import br.ufrn.imd.patterns.identification.Lookup;
import br.ufrn.imd.patterns.identification.ObjectId;
import br.ufrn.imd.patterns.identification.RouteRegistry;

public class Nelionator {
	private HttpServer server;
	
	public String addComponents(Object object) {
		Class<?> clazz = object.getClass();
		
		String baseRoute = "";
		if(clazz.isAnnotationPresent(RequestMap.class)) {
			baseRoute = clazz.getAnnotation(RequestMap.class).router();
		}
		
		StringBuilder registeredRoutes = new StringBuilder();
		
		for(Method method : clazz.getDeclaredMethods()) {
			method.setAccessible(true);
			
			String route = null;
			if (method.isAnnotationPresent(Get.class)) {
                route = baseRoute + method.getAnnotation(Get.class).router();
            } else if (method.isAnnotationPresent(Post.class)) {
                route = baseRoute + method.getAnnotation(Post.class).router();
            }
			
			if(route != null) {
				RouteRegistry.registerRoute(route, method);
                Lookup.registerObject(route, object);
                
                registeredRoutes.append("Registered route: ").append(route).append("\n");
			}
		}
		return registeredRoutes.toString();
	}
	
	public void start(int port) throws IOException {
		server = HttpServer.create(new InetSocketAddress(port), 0);
		
		for(String route : RouteRegistry.getRoutes()) {
			Method method = RouteRegistry.getMethod(route);
            Object remoteObject = Lookup.getObject(route);
            server.createContext(route, new ServerRequestHandler(method, remoteObject));
		}
		
		server.start();	
		System.out.println("Server started on port " + port);
	}
}