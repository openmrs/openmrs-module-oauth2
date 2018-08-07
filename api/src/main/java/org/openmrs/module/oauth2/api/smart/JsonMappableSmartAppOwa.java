package org.openmrs.module.oauth2.api.smart;

import java.io.Serializable;

import org.openmrs.module.oauth2.api.JsonMappableClientOwa;
import org.openmrs.module.oauth2.api.smart.model.SmartApp;

public class JsonMappableSmartAppOwa extends JsonMappableClientOwa implements Serializable {

	private Integer smartId;

	public JsonMappableSmartAppOwa(SmartApp smartApp) {
		super(smartApp.getClient());
		this.smartId = smartApp.getSmartId();
	}

	public Integer getSmartId() {
		return smartId;
	}

	public void setSmartId(Integer smartId) {
		this.smartId = smartId;
	}
}
