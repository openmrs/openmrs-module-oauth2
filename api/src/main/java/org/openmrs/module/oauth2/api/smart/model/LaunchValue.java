package org.openmrs.module.oauth2.api.smart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.openmrs.module.oauth2.Client;
import org.openmrs.module.oauth2.api.model.Parametrized;

/**
 * Model class for Launch url of SMART Apps.
 */
@Entity
@Table(name = "oauth2_smart_app_launch")
public class LaunchValue implements Parametrized {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@OneToOne
	@JoinColumn(name = "smart_id", nullable = false, updatable = false, insertable = true)
	private SmartApp smartApp;

	@Column(name = "launch_value")
	private String launchValue;

	public LaunchValue() {
	}

	public LaunchValue(String launchValue) {
		this.launchValue = launchValue;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SmartApp getSmartApp() {
		return smartApp;
	}

	public void setSmartApp(SmartApp smartApp) {
		this.smartApp = smartApp;
	}

	public String getLaunchValue() {
		return launchValue;
	}

	public void setLaunchValue(String launchValue) {
		this.launchValue = launchValue;
	}

	@Override
	public String getParameter() {
		return null;
	}

	@Override
	public void setParameter(String parameter) {

	}

	@Override
	public void setClient(Client client) {

	}

}
