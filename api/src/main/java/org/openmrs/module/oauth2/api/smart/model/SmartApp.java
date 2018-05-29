package org.openmrs.module.oauth2.api.smart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.openmrs.module.oauth2.Client;
import org.openmrs.module.oauth2.api.model.Parametrized;

/**
 * Model (MVC) for Oauth Client.
 */
@Entity
@Table(name = "oauth2_smart_app")
public class SmartApp implements Parametrized {

	@Id
	@GeneratedValue
	@Column(name = "smart_id")
	private Integer smartId;

	@OneToOne
	@JoinColumn(name = "client_id", nullable = false, updatable = false, insertable = true)
	private Client client;

	@Column(name = "launch_url")
	private String launchUrl;

	//=============
	// Constructors
	//=============
	protected SmartApp() {
	}

	/**
	 * @param client
	 * @param launchUrl
	 */
	public SmartApp(Client client, String launchUrl) {
		this.client = client;
		this.launchUrl = launchUrl;
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

	public Client getClient() {
		return client;
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
		this.client = client;
	}

	//=======================
	// Object class overrides
	//=======================
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((client == null) ? 0 : client.hashCode());
		result = prime * result + ((launchUrl == null) ? 0 : launchUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SmartApp other = (SmartApp) obj;
		if (client == null) {
			if (other.client != null)
				return false;
		} else if (!client.equals(other.client))
			return false;
		if (launchUrl == null) {
			if (other.launchUrl != null)
				return false;
		} else if (!launchUrl.equals(other.launchUrl))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SmartApp [smartId=" + smartId + ", client=" + client + ", launchUrl=" + launchUrl + "]";
	}

}
