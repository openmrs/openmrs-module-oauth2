package org.openmrs.module.oauth2.api.smart.db.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.openmrs.User;
import org.openmrs.module.oauth2.api.smart.model.SmartApp;
import org.springframework.stereotype.Component;

@Component
public class SmartAppDAO extends HibernateSmartDAO<SmartApp> {

	/**
	 * You must call this before using any of the data access methods, since it's not actually
	 * possible to write them all with compile-time class information due to use of Generics.
	 *
	 * @param mappedClass
	 */
	protected SmartAppDAO(Class<SmartApp> mappedClass) {
		super(mappedClass);
	}

	private SmartAppDAO() {
		super(SmartApp.class);
	}

	public List<SmartApp> getAllSmartAppsForClientDeveloper(User clientDeveloper) {
		String queryString = "from org.openmrs.module.oauth2.api.smart.model.SmartApp where creator.userId = :client_developer_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(queryString);
		query.setParameter("client_developer_id", clientDeveloper.getId());
		List<SmartApp> smartApps = (List<SmartApp>) query.list();
		return smartApps;
	}

	public SmartApp loadSmartAppByClientId(Integer clientId) {
		String queryString = "from org.openmrs.module.oauth2.api.smart.model.SmartApp where clientId = :clientId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(queryString);
		query.setParameter("clientId", clientId);
		List<SmartApp> smartApps = (List<SmartApp>) query.list();
		if (smartApps.isEmpty())
			return null;
		return smartApps.get(0);
	}
}
