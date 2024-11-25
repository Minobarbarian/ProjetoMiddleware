package br.ufrn.imd.message;

import org.json.JSONObject;
import java.io.Serializable;

public record HttpMessage(
		String method,
		String resource,
		JSONObject body
) implements Serializable {}
