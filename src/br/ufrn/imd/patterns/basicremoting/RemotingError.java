package br.ufrn.imd.patterns.basicremoting;

import java.io.IOException;

import org.json.JSONObject;

import br.ufrn.imd.message.HttpMessage;

public class RemotingError {
	public static String handleError(Exception e) {
		e.printStackTrace();
		
		ErrorInfo errorInfo = getErrorInfo(e);
		
		JSONObject errorResponse = new JSONObject();
		errorResponse.put("error", true);
		errorResponse.put("code", errorInfo.getCode());
		errorResponse.put("message", errorInfo.getMessage());
		
        return errorResponse.toString();
    }
	
	public static HttpMessage createErrorHttpMessage(String errorResponse, String method, String resource) {
		JSONObject body = new JSONObject(errorResponse);
		return new HttpMessage(method, resource, body);
	}
	
	private static ErrorInfo getErrorInfo(Exception e) {
		if (e instanceof IllegalArgumentException) {
            return new ErrorInfo("400", "Invalid Argument.");
        } else if (e instanceof NullPointerException) {
            return new ErrorInfo("500", "Server missed a value.");
        } else if (e instanceof IOException) {
            return new ErrorInfo("500", "I/O error.");
        } else {
            return new ErrorInfo("500", "Unexpected error.");
        }
	}
	
	private static class ErrorInfo {
        private final String code;
        private final String message;
        
        public ErrorInfo(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
