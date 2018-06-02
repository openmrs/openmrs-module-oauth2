package org.openmrs.module.oauth2.api.smart;

import java.util.List;

import org.openmrs.User;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.oauth2.api.smart.model.SmartApp;

public interface SmartAppManagementService extends OpenmrsService {

	/**
	 * update details of the SMART app if exists, else create new SMART app
	 *
	 * @param smartApp
	 * @should
	 */
	public SmartApp saveOrUpdateSmartApp(SmartApp smartApp);

	/**
	 * @param smartApp
	 */
	public SmartApp updateSmartApp(SmartApp smartApp);

	/**
	 * @param smartId
	 * @return
	 */
	public SmartApp getSmartApp(Integer smartId);

	/**
	 * @param clientId
	 * @return
	 */
	public SmartApp loadSmartAppByClientId(Integer clientId);

	/**
	 * @param clientDeveloper
	 * @return
	 */
	public List<SmartApp> loadSmartAppsForClientDeveloper(User clientDeveloper);

	/**
	 * @param smartApp
	 * @return
	 * @should unregister a smartApp and clear footprint from database
	 */
	public SmartApp unregisterSmartApp(SmartApp smartApp);

	/**
	 * @param smartApp
	 * @return
	 */
	public SmartApp merge(SmartApp smartApp);

}
