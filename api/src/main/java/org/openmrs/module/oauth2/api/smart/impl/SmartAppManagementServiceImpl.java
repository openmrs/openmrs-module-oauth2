package org.openmrs.module.oauth2.api.smart.impl;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.User;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.oauth2.Client;
import org.openmrs.module.oauth2.api.db.hibernate.ClientDAO;
import org.openmrs.module.oauth2.api.smart.SmartAppManagementService;
import org.openmrs.module.oauth2.api.smart.db.hibernate.SmartAppDAO;
import org.openmrs.module.oauth2.api.smart.model.SmartApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class SmartAppManagementServiceImpl extends BaseOpenmrsService implements SmartAppManagementService {


	protected ClientDAO clientDAO;

	public void setClientDAO(ClientDAO clientDAO) {
		this.clientDAO = clientDAO;
	}

	@Autowired
	SmartAppDAO smartAppDAO;

	@Transactional
	@Override
	public SmartApp saveOrUpdateSmartApp(SmartApp smartApp) {
		return smartAppDAO.saveOrUpdate(smartApp);
	}

	@Transactional
	@Override
	public SmartApp updateSmartApp(SmartApp smartApp) {
		return smartAppDAO.update(smartApp);
	}

	@Transactional(readOnly = true)
	@Override
	public SmartApp getSmartApp(Integer smartId) {
		return smartAppDAO.getById(smartId);
	}

	@Transactional(readOnly = true)
	@Override
	public SmartApp loadSmartAppByClientId(Integer clientId) {
		return smartAppDAO.loadSmartAppByClientId(clientId);
	}

	@Transactional(readOnly = true)
	@Override
	public List<SmartApp> loadSmartAppsForClientDeveloper(User clientDeveloper) {
		List<Client> clients = clientDAO.getAllClientsForClientDeveloper(clientDeveloper);
		List<SmartApp> smartApps = new ArrayList<>();
		for (Client client : clients) {
			if (smartAppDAO.loadSmartAppByClientId(client.getId()) != null)
				smartApps.add(smartAppDAO.loadSmartAppByClientId(client.getId()));
		}
		return smartApps;
	}

	@Transactional
	@Override
	public SmartApp unregisterSmartApp(SmartApp smartApp) {
		smartAppDAO.delete(smartApp);
		return smartApp;
	}

	@Transactional
	@Override
	public SmartApp merge(SmartApp smartApp) {
		return smartAppDAO.merge(smartApp);
	}
}
