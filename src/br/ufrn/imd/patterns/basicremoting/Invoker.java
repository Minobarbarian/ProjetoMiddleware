package br.ufrn.imd.patterns.basicremoting;

import java.lang.reflect.Method;

import org.json.JSONObject;

import br.ufrn.imd.message.HttpMessage;
import br.ufrn.imd.middleware.annotations.Delete;
import br.ufrn.imd.middleware.annotations.Get;
import br.ufrn.imd.middleware.annotations.Post;
import br.ufrn.imd.middleware.annotations.Put;
import br.ufrn.imd.middleware.annotations.RequestMap;
import br.ufrn.imd.patterns.identification.Lookup;
import br.ufrn.imd.patterns.identification.AbsoluteObjectReference;

public class Invoker {
	private final Lookup lookup;
	
	public Invoker(Lookup lookup) {
		this.lookup = lookup;
	}
	
	public HttpMessage invoke(HttpMessage request) {
		String route = request.resource();
		String httpMethod = request.method();
		
		AbsoluteObjectReference remoteReference = lookup.getRoute(route);
		
		if(remoteReference == null) {
			String errorResponse = RemotingError.handleError(new IllegalArgumentException("No matching object found for route"));
            return RemotingError.createErrorHttpMessage(errorResponse, httpMethod, route);
		}
		
		try {
			Class<?> clazz = remoteReference.getClazz();
			Method target = findMethod(clazz, httpMethod, route);
			
			if(target == null) {
				String errorResponse = RemotingError.handleError(new IllegalArgumentException("No matching method found"));
                return RemotingError.createErrorHttpMessage(errorResponse, httpMethod, route);
			}
			
			Object remoteObject = clazz.getDeclaredConstructor().newInstance();
			Object result;
			
			if(target.getParameterCount() == 0) {
				result = target.invoke(remoteObject);
			} else {
				result = (JSONObject) target.invoke(remoteObject, request.body());
			}
			return new HttpMessage(request.method(), request.resource(), (JSONObject) result);
		} catch (Exception e) {
			String errorResponse =  RemotingError.handleError(e);
			return RemotingError.createErrorHttpMessage(errorResponse, httpMethod, route);
		}
	}
	
	private Method findMethod(Class<?> clazz, String httpMethod, String route) {
		for(Method method : clazz.getDeclaredMethods()) {
			if(matchesAnnotation(method, httpMethod, route)) {
				return method;
			}
		}
		return null;
	}
	
	private boolean matchesAnnotation(Method method, String httpMethod, String route) {
		RequestMap classAnnotation = method.getDeclaringClass().getAnnotation(RequestMap.class);
	    String basePath = (classAnnotation != null) ? classAnnotation.router() : "";
	    String fullRoute = basePath + getRouteForMethod(method, httpMethod);
		return fullRoute.equals(route);
	}
	
	private String getRouteForMethod(Method method, String httpMethod) {
		switch(httpMethod) {
        case "GET":
            if (method.isAnnotationPresent(Get.class)) {
                return method.getAnnotation(Get.class).router();
            }
            break;
        case "PUT":
            if (method.isAnnotationPresent(Put.class)) {
                return method.getAnnotation(Put.class).router();
            }
            break;
        case "POST":
            if (method.isAnnotationPresent(Post.class)) {
                return method.getAnnotation(Post.class).router();
            }
            break;
        case "DELETE":
            if (method.isAnnotationPresent(Delete.class)) {
                return method.getAnnotation(Delete.class).router();
            }
            break;
		}
		return "";
	}
}