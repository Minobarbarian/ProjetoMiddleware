package br.ufrn.imd.patterns.basicremoting;

import java.io.IOException;
import java.lang.reflect.Method;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ServerRequestHandler implements HttpHandler{
	private final Method method;
	private final Object instance;
	
	public ServerRequestHandler(Method method, Object instance) {
		this.method = method;
		this.instance = instance;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
			String requestBody = new String(exchange.getRequestBody().readAllBytes());
			String response = Invoker.invoke(method, exchange, requestBody);
			
			exchange.sendResponseHeaders(200, response.getBytes().length);
			exchange.getResponseBody().write(response.getBytes());
		} catch (Exception e) {
			String errorResponse = RemotingError.handleError(e);
			
			int errorCode = Integer.parseInt(new JSONObject(errorResponse).getString("code"));
			exchange.sendResponseHeaders(errorCode, errorResponse.getBytes().length);
			exchange.getResponseBody().write(errorResponse.getBytes());
		} finally {
			exchange.getResponseBody().close();
		}
	}
	
	
}
