package br.ufrn.imd.patterns;

import org.json.JSONObject;

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
	
	private static String getErrorCode(Exception e) {
		if(e instanceof IllegalArgumentException) {
			return "400";
		} else if(e instanceof NullPointerException) {
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
		} else {
			return "Unexpected error.";
		}
	}
}
