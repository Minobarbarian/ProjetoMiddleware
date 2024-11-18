package br.ufrn.imd.patterns.basicremoting;

import java.lang.reflect.Method;

public class Invoker {
	public static String invoke(Method method, Object instance, String requestBody) {
		try {
			Object argument = Marshaller.unmarshall(requestBody, method.getParameterTypes()[0]);
            Object result = method.invoke(instance, argument);
            return Marshaller.marshall(result);
		} catch (Exception e) {
			return RemotingError.handleError(e);
		}
	}
}
