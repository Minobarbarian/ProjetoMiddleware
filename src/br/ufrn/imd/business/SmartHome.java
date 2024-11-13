package br.ufrn.imd.business;

import br.ufrn.imd.middleware.annotations.RequestMap;
import br.ufrn.imd.middleware.annotations.Post;
import br.ufrn.imd.middleware.annotations.Get;
import org.json.JSONObject;

@RequestMap(router = "/smart")
public class SmartHome {
	private String lightStat;
	private int thermostatTemp;
	
	public SmartHome(String lightStat, int thermostatTemp) {
		this.lightStat = lightStat;
		this.thermostatTemp = thermostatTemp;
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
	public void setThermostatTemp(int thermostatTemp) {
		this.thermostatTemp = thermostatTemp;
	}
	
	@Post(router = "/switch")
	public void lightSwitch() {
		switch(this.lightStat) {
		case "ON":
			this.lightStat = "OFF";
			break;
		case "OFF":
			this.lightStat = "ON";
			break;
		}
	}
}
