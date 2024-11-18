package br.ufrn.imd.middleware;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;
import br.ufrn.imd.middleware.annotations.Get;
import br.ufrn.imd.middleware.annotations.Post;
import br.ufrn.imd.middleware.annotations.RequestMap;
import br.ufrn.imd.patterns.RemoteObject;
import br.ufrn.imd.patterns.ServerRequestHandler;

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
			if(method.isAnnotationPresent(Get.class)) {
				String route = baseRoute + method.getAnnotation(Get.class).router();
				RemoteObject.addMethodGet(route, method);
				registeredRoutes.append("Registered GET route: ").append(route).append("\n");
			} else if(method.isAnnotationPresent(Post.class)) {
				String route = baseRoute + method.getAnnotation(Post.class).router();
				RemoteObject.addMethodGet(route, method);
				registeredRoutes.append("Registered POST route: ").append(route).append("\n");
			}
		}
		return registeredRoutes.toString();
	}
	
	public void start(int port) throws IOException {
		server = HttpServer.create(new InetSocketAddress(port), 0);
		
		for(String route : RemoteObject.getRoutes()) {
			Method method = RemoteObject.getMethod(route);
			server.createContext(route, new ServerRequestHandler(method, RemoteObject.getInstance()));
		}
		
		server.start();	
		System.out.println("Server started on port " + port);
	}
}