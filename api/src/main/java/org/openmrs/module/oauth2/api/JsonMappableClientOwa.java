package org.openmrs.module.oauth2.api;

import java.io.Serializable;

import org.openmrs.module.oauth2.Client;

/**
 * JSON Mappable OAuth client for OWA initiated requests
 */
public class JsonMappableClientOwa implements Serializable {

	private String name;

	private String description;

	/*
	 * Change this constructor to control values returned as JSON response
	 */
	public JsonMappableClientOwa(Client client) {
		this.name = client.getName();
		this.description = client.getDescription();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

