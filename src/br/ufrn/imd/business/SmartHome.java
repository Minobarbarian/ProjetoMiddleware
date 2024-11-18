package br.ufrn.imd.business;

import org.json.JSONObject;

import br.ufrn.imd.annotations.Get;
import br.ufrn.imd.annotations.Post;
import br.ufrn.imd.annotations.RequestMap;

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
	public void setThermostatTemp(JSONObject jsonObject) {
		this.thermostatTemp = jsonObject.getInt("var1");
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
