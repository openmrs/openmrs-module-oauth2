package org.openmrs.module.oauth2.api.smart;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.oauth2.Client;
import org.openmrs.module.oauth2.api.smart.model.SmartApp;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class SmartAppManagementServiceTest extends BaseModuleContextSensitiveTest {

	protected static final String SMART_APP_INITIAL_DATA_XML = "SmartAppServicesTest-initialData.xml";

	protected static final String TEST_LAUNCH_URL = "http://MyTestLaunchUrl.com";

	public SmartAppManagementService getService() {
		return Context.getService(SmartAppManagementService.class);
	}

	@Before
	public void runBeforeEachTest() throws Exception {
		executeDataSet(SMART_APP_INITIAL_DATA_XML);
	}

	@Test
	public void saveOrUpdateSmartApp_shouldSaveNewSmartAppUpdateExistingSmartApp() {
		SmartApp smartApp = createSampleSmartApp();
		getService().saveOrUpdateSmartApp(smartApp);
		smartApp = getService().getSmartApp(smartApp.getSmartId());
		Assert.assertNotNull(smartApp);
	}

	@Test
	public void updateSmartApp_shouldUpdateExistingSmartApp() {
		SmartApp smartApp = getService().getSmartApp(1);
		smartApp.setLaunchUrl(TEST_LAUNCH_URL);
		getService().updateSmartApp(smartApp);
		smartApp = getService().getSmartApp(1);
		Assert.assertEquals(TEST_LAUNCH_URL, smartApp.getLaunchUrl());
	}

	@Test
	public void getSmartApp_shouldReturnSmartAppBySmartId() {
		SmartApp smartApp = getService().getSmartApp(1);
		Assert.assertNotNull(smartApp);
	}

	@Test
	public void loadSmartAppByClientId_shouldReturnSmartAppByClientId() {
		SmartApp smartApp = getService().loadSmartAppByClientId(1);
		Assert.assertNotNull(smartApp);
	}

	@Test
	public void loadSmartAppsForClientDeveloper() {
		User clientDeveloper = Context.getUserService().getUser(4);
		List<SmartApp> smartApps = getService().loadSmartAppsForClientDeveloper(clientDeveloper);
		Assert.assertNotNull(smartApps);
	}

	@Test
	public void unregisterSmartApp_shouldUnregisterSmartApp() {
		SmartApp smartApp = getService().getSmartApp(1);
		Assert.assertNotNull(smartApp);
		getService().unregisterSmartApp(smartApp);
		smartApp = getService().getSmartApp(1);
		Assert.assertNull(smartApp);
	}

	@Test
	public void merge_shouldMergeWithExistingSmartApp() {
		SmartApp smartApp = getService().getSmartApp(1);
		smartApp.setLaunchUrl(TEST_LAUNCH_URL);
		getService().merge(smartApp);
		SmartApp newSmartApp = getService().getSmartApp(1);
		Assert.assertEquals(TEST_LAUNCH_URL, newSmartApp.getLaunchUrl());
	}

	private SmartApp createSampleSmartApp() {
		Client client = new Client("my-trusted-client-with-secret", "somesecret", "openmrs", "read,write",
				"authorization_code,refresh_token,implicit,client_credentials,password", "ROLE_CLIENT",
				"http://anywhere?key=value");
		client.setVoided(false);
		client.setDateCreated(new Date());
		client.setName("Demo Application");
		client.setClientType(Client.ClientType.WEB_APPLICATION);
		SmartApp smartApp = new SmartApp(client, "http://demoLaunchUrl.com");
		return smartApp;
	}

}
