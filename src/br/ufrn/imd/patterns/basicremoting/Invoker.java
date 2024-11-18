package br.ufrn.imd.patterns.basicremoting;

import org.json.JSONObject;
import java.lang.reflect.Method;

public class Invoker {
	public static JSONObject invoke(Method method, JSONObject instance, String requestBody) {
		JSONObject response = new JSONObject();
		try {
			if(instance == null) {
				response.put("error", true);
				response.put("code", "404");
				response.put("message", "Instance not provided for invocation.");
                return response;
			}
			
			JSONObject requestJson = new JSONObject(requestBody);
			Class<?> parameterType = method.getParameterTypes()[0];
			Object argument = Marshaller.unmarshall(requestJson.toString(), parameterType);
			
            Object result = method.invoke(instance.toMap(), argument);
            response = new JSONObject(Marshaller.marshall(result));
		} catch (Exception e) {
			response = new JSONObject(RemotingError.handleError(e));
		}
		return response;
	}
}
