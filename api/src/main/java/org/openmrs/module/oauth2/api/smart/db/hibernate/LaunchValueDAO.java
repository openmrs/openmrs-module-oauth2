package org.openmrs.module.oauth2.api.smart.db.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.openmrs.module.oauth2.api.smart.model.LaunchValue;
import org.openmrs.module.oauth2.api.smart.model.SmartApp;
import org.springframework.stereotype.Component;

@Component
public class LaunchValueDAO extends HibernateSmartDAO<LaunchValue> {

	/**
	 * You must call this before using any of the data access methods, since it's not actually
	 * possible to write them all with compile-time class information due to use of Generics.
	 *
	 * @param mappedClass
	 */
	protected LaunchValueDAO(Class<LaunchValue> mappedClass) {
		super(mappedClass);
	}

	private LaunchValueDAO() {
		super(LaunchValue.class);
	}

	public LaunchValue getLaunchValueForSmartApp(SmartApp smartApp) {
		String queryString = "from org.openmrs.module.oauth2.api.smart.model.LaunchValue where smartId = :smartId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(queryString);
		query.setParameter("smartId", smartApp.getSmartId());
		List<LaunchValue> launchValues = (List<LaunchValue>) query.list();
		if (launchValues.isEmpty())
			return null;
		return launchValues.get(0);
	}

	public LaunchValue getLaunchValueBySmartId(Integer smartId) {
		String queryString = "from org.openmrs.module.oauth2.api.smart.model.SmartApp where smartId = :smartId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(queryString);
		query.setParameter("smartId", smartId);
		List<LaunchValue> launchValues = (List<LaunchValue>) query.list();
		if (launchValues.isEmpty())
			return null;
		return launchValues.get(0);
	}
}
