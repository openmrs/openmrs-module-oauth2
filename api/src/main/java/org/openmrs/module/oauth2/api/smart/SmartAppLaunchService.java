package org.openmrs.module.oauth2.api.smart;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.oauth2.api.smart.model.LaunchValue;

public interface SmartAppLaunchService extends OpenmrsService {

	/**
	 * update details of the SMART app if exists, else create new SMART app
	 *
	 * @param launchValue
	 * @should
	 */
	public LaunchValue saveOrUpdateLaunchValue(LaunchValue launchValue);

	/**
	 * @param launchValue
	 */
	public LaunchValue updateLaunchValue(LaunchValue launchValue);

	/**
	 * @param smartId
	 * @return
	 */
	public LaunchValue getLaunchValue(Integer smartId);

	/**
	 * @param launchValue
	 * @return
	 * @should clear a launchValue footprint from database
	 */
	public LaunchValue clearLaunchValue(LaunchValue launchValue);

	/**
	 * @param launchValue
	 * @return
	 */
	public LaunchValue merge(LaunchValue launchValue);

	/**
	 * Generate {@link org.openmrs.module.oauth2.api.smart.model.LaunchValue#launchValue} and persist value in database.
	 *
	 * @param launchValue
	 */
	public void generateAndPersistLaunchValue(LaunchValue launchValue);

	/**
	 * @param launchValue
	 * @return
	 */

	public boolean verifyLaunchValue(LaunchValue launchValue, String receivedLaunchValue);

}
