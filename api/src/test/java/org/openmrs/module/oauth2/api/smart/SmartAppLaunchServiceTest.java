package org.openmrs.module.oauth2.api.smart;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.oauth2.Client;
import org.openmrs.module.oauth2.api.ClientRegistrationService;
import org.openmrs.module.oauth2.api.smart.model.LaunchValue;
import org.openmrs.module.oauth2.api.smart.model.SmartApp;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class SmartAppLaunchServiceTest extends BaseModuleContextSensitiveTest {

	protected static final String SMART_APP_LAUNCH_INITIAL_DATA_XML = "SmartAppServicesTest-initialData.xml";

	protected static final String TEST_LAUNCH_VALUE = "testLaunchValue";

	public SmartAppLaunchService getService() {
		return Context.getService(SmartAppLaunchService.class);
	}

	public SmartAppManagementService getSmartService() {
		return Context.getService(SmartAppManagementService.class);
	}

	public ClientRegistrationService getClientService() {
		return Context.getService(ClientRegistrationService.class);
	}

	@Before
	public void runBeforeEachTest() throws Exception {
		executeDataSet(SMART_APP_LAUNCH_INITIAL_DATA_XML);
	}

	@Test
	public void saveOrUpdateLaunchValue_shouldSaveNewLaunchValueUpdateExistingLaunchValue() {
		LaunchValue launchValue = createSampleLaunchValue();
		getService().saveOrUpdateLaunchValue(launchValue);
		launchValue = getService().getLaunchValue(launchValue.getSmartApp().getSmartId());
		Assert.assertNotNull(launchValue);
	}

	@Test
	public void updateLaunchValue_shouldUpdateExistingLaunchValue() {
		LaunchValue launchValue = getService().getLaunchValue(1);
		launchValue.setLaunchValue(TEST_LAUNCH_VALUE);
		getService().updateLaunchValue(launchValue);
		launchValue = getService().getLaunchValue(1);
		Assert.assertEquals(TEST_LAUNCH_VALUE, launchValue.getLaunchValue());
	}

	@Test
	public void getLaunchValue_getLaunchValueBySmartId() {
		LaunchValue launchValue = getService().getLaunchValue(1);
		Assert.assertNotNull(launchValue);
	}

	@Test
	public void clearLaunchValue_shouldClearLaunchValue() {
		LaunchValue launchValue = getService().getLaunchValue(1);
		Assert.assertNotNull(launchValue);
		getService().clearLaunchValue(launchValue);
		launchValue = getService().getLaunchValue(1);
		Assert.assertNull(launchValue);
	}

	@Test
	public void merge_shouldMergeWithExistingLaunchValue() {
		LaunchValue launchValue = getService().getLaunchValue(1);
		launchValue.setLaunchValue(TEST_LAUNCH_VALUE);
		getService().merge(launchValue);
		LaunchValue newLaunchValue = getService().getLaunchValue(1);
		Assert.assertEquals(TEST_LAUNCH_VALUE, newLaunchValue.getLaunchValue());
	}

	@Test
	public void generateAndPersistLaunchValue_shouldGenerateAndPersistLaunchValue() {
		LaunchValue launchValue = getService().getLaunchValue(1);
		launchValue.setLaunchValue(null);
		getService().generateAndPersistLaunchValue(launchValue);
		Assert.assertNotNull(launchValue.getLaunchValue());
	}

	@Test
	public void verifyLaunchValue_shouldReturnTrueIfCorrectLaunchValue() {
		LaunchValue launchValue = getService().getLaunchValue(1);
		Assert.assertTrue(getService().verifyLaunchValue(launchValue, "someLaunchValue"));
	}

	@Test
	public void verifyLaunchValue_shouldReturnFalseIfIncorrectLaunchValue() {
		LaunchValue launchValue = getService().getLaunchValue(1);
		Assert.assertFalse(getService().verifyLaunchValue(launchValue, "IncorrectLaunchValue"));
	}

	private LaunchValue createSampleLaunchValue() {
		Client client = new Client("my-trusted-client-with-secret", "somesecret", "openmrs", "read,write",
				"authorization_code,refresh_token,implicit,client_credentials,password", "ROLE_CLIENT",
				"http://anywhere?key=value");
		client.setVoided(false);
		client.setDateCreated(new Date());
		client.setName("Demo Application");
		client.setClientType(Client.ClientType.WEB_APPLICATION);
		getClientService().saveOrUpdateClient(client);
		SmartApp smartApp = new SmartApp(client, "http://demoLaunchUrl.com");
		getSmartService().saveOrUpdateSmartApp(smartApp);
		LaunchValue launchValue = new LaunchValue(smartApp, TEST_LAUNCH_VALUE);
		return launchValue;
	}
}
