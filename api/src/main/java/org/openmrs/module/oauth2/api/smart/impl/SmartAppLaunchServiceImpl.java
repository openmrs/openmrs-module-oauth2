package org.openmrs.module.oauth2.api.smart.impl;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.oauth2.api.smart.SmartAppLaunchService;
import org.openmrs.module.oauth2.api.smart.db.hibernate.LaunchValueDAO;
import org.openmrs.module.oauth2.api.smart.model.LaunchValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class SmartAppLaunchServiceImpl extends BaseOpenmrsService implements SmartAppLaunchService {

	@Autowired
	LaunchValueDAO launchValueDAO;

	@Transactional
	@Override
	public LaunchValue saveOrUpdateLaunchValue(LaunchValue launchValue) {
		return launchValueDAO.saveOrUpdate(launchValue);
	}

	@Transactional
	@Override
	public LaunchValue updateLaunchValue(LaunchValue launchValue) {
		return launchValueDAO.update(launchValue);
	}

	@Transactional(readOnly = true)
	@Override
	public LaunchValue getLaunchValue(Integer smartId) {
		return launchValueDAO.getLaunchValueBySmartId(smartId);
	}

	@Transactional
	@Override
	public LaunchValue clearLaunchValue(LaunchValue launchValue) {
		launchValueDAO.delete(launchValue);
		return launchValue;
	}

	@Transactional
	@Override
	public LaunchValue merge(LaunchValue launchValue) {
		return launchValueDAO.merge(launchValue);
	}

	@Override
	public void generateAndPersistLaunchValue(LaunchValue launchValue) {
		generateLaunchValue(launchValue);
		merge(launchValue);
		updateLaunchValue(launchValue);
	}

	private void generateLaunchValue(LaunchValue launchValue) {
		SecureRandom secureRandom = new SecureRandom();
		launchValue.setLaunchValue(new BigInteger(130, secureRandom).toString(32));
	}

	@Override
	public boolean verifyLaunchValue(LaunchValue launchValue, String receivedLaunchValue) {
		if (launchValue.getLaunchValue().equals(receivedLaunchValue))
			return true;
		return false;
	}
}
