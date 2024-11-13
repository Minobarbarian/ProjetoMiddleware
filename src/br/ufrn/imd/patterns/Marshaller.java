package br.ufrn.imd.patterns;

import org.json.JSONObject;

public class Marshaller {
	public static String marshall(Object result) {
		if(result instanceof JSONObject) {
			return ((JSONObject) result).toString();
		} else {
			return result.toString();
		}
	}
	public static Object unmarshall(String requestBody, Class<?> parameterType) {
		if(parameterType == Integer.class) {
			return Integer.valueOf(requestBody);
		}
		return requestBody;
	}
}
