package br.ufrn.imd.patterns.basicremoting;

import java.io.IOException;

import org.json.JSONObject;

import br.ufrn.imd.message.HttpMessage;

public class RemotingError {
	public static String handleError(Exception e) {
		e.printStackTrace();
		
		String errorCode = getErrorCode(e);
		String errorMessage = getErrorMessage(e);
		
		JSONObject errorResponse = new JSONObject();
		errorResponse.put("error", true);
		errorResponse.put("code", errorCode);
		errorResponse.put("message", errorMessage);
		
        return errorResponse.toString();
    }
	
	public static HttpMessage createErrorHttpMessage(String errorResponse, String method, String resource) {
		JSONObject body = new JSONObject(errorResponse);
		return new HttpMessage(method, resource, body);
	}
	
	private static String getErrorCode(Exception e) {
		if(e instanceof IllegalArgumentException) {
			return "400";
		} else if(
					e instanceof NullPointerException ||
					e instanceof IOException
				) {
			return "500";
		} else {
			return "500";
		}
	}
	
	private static String getErrorMessage(Exception e) {
		if(e instanceof IllegalArgumentException) {
			return "Invalid Argument.";
		} else if(e instanceof NullPointerException) {
			return "Server misse a value.";
		} else if(e instanceof IOException) {
			return "I/O error.";
		}else {
			return "Unexpected error.";
		}
	}
}
