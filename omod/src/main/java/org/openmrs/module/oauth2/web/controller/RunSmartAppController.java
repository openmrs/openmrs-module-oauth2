package org.openmrs.module.oauth2.web.controller;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.oauth2.Client;
import org.openmrs.module.oauth2.api.ClientRegistrationService;
import org.openmrs.module.oauth2.api.model.AuthorizedGrantType;
import org.openmrs.module.oauth2.api.model.RedirectURI;
import org.openmrs.module.oauth2.api.model.Scope;
import org.openmrs.module.oauth2.api.smart.SmartAppLaunchService;
import org.openmrs.module.oauth2.api.smart.SmartAppManagementService;
import org.openmrs.module.oauth2.api.smart.model.LaunchValue;
import org.openmrs.module.oauth2.api.smart.model.SmartApp;
import org.openmrs.module.oauth2.api.util.ClientSpringOAuthUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RunSmartAppController {

	protected final Log log = LogFactory.getLog(getClass());

	private static final String RUN_SMART_VIEW = "/module/oauth2/runSmart";

	public static final String RUN_SMART_CONTROLLER = "module/oauth2/runSmartApps/runSmart.form";

	public static final String REGISTER_MF = "module/oauth2/runSmartApps/registerMF.form";

	@RequestMapping(method = RequestMethod.GET, value = RUN_SMART_CONTROLLER)
	public String showList() {
		return RUN_SMART_VIEW;
	}

	@RequestMapping(method = RequestMethod.POST, value = RUN_SMART_CONTROLLER)
	public ModelAndView onSubmit(HttpServletRequest request) {

		String str = request.getParameter("run");
		Integer smartId = Integer.parseInt(str);

		SmartApp smartApp = getSmartService().getSmartApp(smartId);

		LaunchValue launchValue = getNewLaunchValue();
		launchValue.setSmartApp(smartApp);

		launchValue = getLaunchService().saveOrUpdateLaunchValue(launchValue);
		getLaunchService().generateAndPersistLaunchValue(launchValue);
		getLaunchService().saveOrUpdateLaunchValue(launchValue);

		String redirectURL =
				smartApp.getLaunchUrl() + "?iss=http://localhost:8080/openmrs/ws/oauth" + "&launch=" + launchValue
						.getLaunchValue();
		return new ModelAndView(new RedirectView(redirectURL));
	}

	@RequestMapping(method = RequestMethod.POST, value = REGISTER_MF)
	public String onSubmit(@RequestParam("mfFile") MultipartFile mfFile) {
		if (!mfFile.isEmpty()) {

			Manifest manifest = null;
			try {
				manifest = new Manifest(mfFile.getInputStream());
			}
			catch (IOException e) {
				e.printStackTrace();
			}

			Attributes attributes = manifest.getMainAttributes();

			String name = attributes.getValue("Name");
			String description = attributes.getValue("Description");
			String website = attributes.getValue("Website");
			String redirectionUri = attributes.getValue("Redirection-Uri");
			String clientType = attributes.getValue("Client-Type");
			List<String> scopes = Arrays.asList(attributes.getValue("Scopes").split("\\s*,\\s*"));
			List<String> grantTypes = Arrays.asList(attributes.getValue("Grant-Types").split("\\s*,\\s*"));
			String launchUrl = attributes.getValue("Launch-Url");

			Client client = getNewClient();
			client.setName(name);
			if (description != null)
				client.setDescription(description);
			if (website != null)
				client.setWebsite(website);

			Collection<RedirectURI> redirectURICollection = ClientSpringOAuthUtils
					.commaDelimitedStringToCollection(redirectionUri.trim(), client, RedirectURI.class);
			client.setClientType(Client.ClientType.valueOf(clientType));
			client.setRedirectUriCollection(redirectURICollection);

			String scopesCSV = StringUtils.join(scopes, ',');
			Collection<Scope> scopeCollection = ClientSpringOAuthUtils
					.commaDelimitedStringToCollection(scopesCSV, client, Scope.class);
			client.setScopeCollection(scopeCollection);

			String grantTypeCSV = StringUtils.join(grantTypes, ",");
			Collection<AuthorizedGrantType> grantTypeCollection = ClientSpringOAuthUtils
					.commaDelimitedStringToCollection(grantTypeCSV, client, AuthorizedGrantType.class);
			client.setGrantTypeCollection(grantTypeCollection);

			client.setCreator(Context.getAuthenticatedUser());
			client.setAccessTokenValiditySeconds(600);
			client.setRefreshTokenValiditySeconds(600);

			client = getService().saveOrUpdateClient(client);
			getService().generateAndPersistClientCredentials(client);
			getService().saveOrUpdateClient(client);

			SmartApp smartApp = getNewSmartApp();

			smartApp.setClient(client);
			smartApp.setLaunchUrl(launchUrl);
			getSmartService().saveOrUpdateSmartApp(smartApp);

			return "/module/oauth2/registrationSuccess";
		} else {
			return "/module/oauth2/mfError";
		}
	}

	@ModelAttribute("smartApps")
	public List<SmartApp> getRegisteredSmartApps() {
		List<SmartApp> smartApps = null;
		SmartAppManagementService service = Context.getService(SmartAppManagementService.class);
		User user = Context.getAuthenticatedUser();
		smartApps = service.loadSmartAppsForClientDeveloper(user);
		return smartApps;
	}

	public ClientRegistrationService getService() {
		return Context.getService(ClientRegistrationService.class);
	}

	private SmartAppLaunchService getLaunchService() {
		return Context.getService(SmartAppLaunchService.class);
	}

	private SmartAppManagementService getSmartService() {
		return Context.getService(SmartAppManagementService.class);
	}

	private LaunchValue getNewLaunchValue() {
		LaunchValue launchValue = new LaunchValue(null, null);
		return launchValue;
	}

	public Client getNewClient() {
		Client client = new Client(null, null, null, null, null, null, null);
		return client;
	}

	public SmartApp getNewSmartApp() {
		SmartApp smartApp = new SmartApp(null, null);
		return smartApp;
	}

}
