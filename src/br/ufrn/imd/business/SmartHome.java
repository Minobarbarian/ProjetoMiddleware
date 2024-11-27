package br.ufrn.imd.business;

import br.ufrn.imd.middleware.annotations.RequestMap;
import br.ufrn.imd.middleware.annotations.Post;
import br.ufrn.imd.middleware.annotations.Get;
import org.json.JSONObject;

@RequestMap(router = "/smart")
public class SmartHome {
	private String lightStat;
	private int thermostatTemp;
	
	public SmartHome() {
		this.lightStat = "OFF";
		this.thermostatTemp = 22;
	}
	
	public String getLightStat() {
		return lightStat;
	}
	public int getThermostatTemp() {
		return thermostatTemp;
	}
	
	@Get(router = "/state")
	public JSONObject stateString() {
		JSONObject result = new JSONObject();
		result.put("Light", lightStat);
		result.put("Temperature", thermostatTemp);
		return result;
	}
	
	@Post(router = "/regulate")
	public JSONObject setThermostatTemp(JSONObject jsonObject) {
		this.thermostatTemp = jsonObject.getInt("var1");
		JSONObject result = new JSONObject();
		result.put("status", "success");
		result.put("Temperature", thermostatTemp);
		return result;
	}
	
	@Post(router = "/switch")
	public JSONObject lightSwitch() {
		switch(this.lightStat) {
		case "ON":
			this.lightStat = "OFF";
			break;
		case "OFF":
			this.lightStat = "ON";
			break;
		}
		JSONObject result = new JSONObject();
		result.put("status", "success");
		result.put("Light", lightStat);
		return result;
	}
}
