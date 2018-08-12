package org.openmrs.module.oauth2.api.smart;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.oauth2.api.smart.model.LaunchValue;

public interface SmartAppLaunchService extends OpenmrsService {

	/**
	 * @param launchValue
	 * @should update details of the Launch Value if exists, else create new Launch Value.
	 */
	public LaunchValue saveOrUpdateLaunchValue(LaunchValue launchValue);

	/**
	 * @param launchValue
	 * @should update details of the Launch Value
	 */
	public LaunchValue updateLaunchValue(LaunchValue launchValue);

	/**
	 * @param smartId
	 * @return Launch Value
	 */
	public LaunchValue getLaunchValue(Integer smartId);

	/**
	 * @param launchValue
	 * @should clear a launchValue footprint from database
	 */
	public LaunchValue clearLaunchValue(LaunchValue launchValue);

	/**
	 * @param launchValue
	 * @should merge Launch Value
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
	 * @should verify Launch Value
	 */

	public boolean verifyLaunchValue(LaunchValue launchValue, String receivedLaunchValue);

}
