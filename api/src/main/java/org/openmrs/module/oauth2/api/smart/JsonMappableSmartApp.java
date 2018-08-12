package org.openmrs.module.oauth2.api.smart;

import java.io.Serializable;

import org.openmrs.module.oauth2.api.JsonMappableClient;
import org.openmrs.module.oauth2.api.smart.model.SmartApp;

/**
 * JSON mappable SMART App for externally made request.
 */
public class JsonMappableSmartApp extends JsonMappableClient implements Serializable {

	private Integer smartId;

	private String launchUrl;

	public JsonMappableSmartApp(SmartApp smartApp) {
		super(smartApp.getClient());
		this.smartId = smartApp.getSmartId();
		this.launchUrl = smartApp.getLaunchUrl();
	}

	public Integer getSmartId() {
		return smartId;
	}

	public void setSmartId(Integer smartId) {
		this.smartId = smartId;
	}

	public String getLaunchUrl() {
		return launchUrl;
	}

	public void setLaunchUrl(String launchUrl) {
		this.launchUrl = launchUrl;
	}
}
