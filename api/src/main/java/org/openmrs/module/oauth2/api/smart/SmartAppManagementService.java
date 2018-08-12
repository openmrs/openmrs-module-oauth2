package org.openmrs.module.oauth2.api.smart;

import java.util.List;

import org.openmrs.User;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.oauth2.api.smart.model.SmartApp;

public interface SmartAppManagementService extends OpenmrsService {

	/**
	 * @param smartApp
	 * @should update details of the SMART app if exists, else create new SMART app
	 */
	public SmartApp saveOrUpdateSmartApp(SmartApp smartApp);

	/**
	 * @param smartApp
	 * @should update details of the SMART app
	 */
	public SmartApp updateSmartApp(SmartApp smartApp);

	/**
	 * @param smartId
	 * @return SMART App
	 */
	public SmartApp getSmartApp(Integer smartId);

	/**
	 * @param clientId
	 * @return SMART App
	 * @should update details of the SMART app
	 */
	public SmartApp loadSmartAppByClientId(Integer clientId);

	/**
	 * @param clientDeveloper
	 * @return all SMART  apps for a given client developer
	 */
	public List<SmartApp> loadSmartAppsForClientDeveloper(User clientDeveloper);

	/**
	 * @param smartApp
	 * @should unregister a smartApp and clear footprint from database
	 */
	public SmartApp unregisterSmartApp(SmartApp smartApp);

	/**
	 * @param smartApp
	 * @should merge SMART app
	 */
	public SmartApp merge(SmartApp smartApp);

}
