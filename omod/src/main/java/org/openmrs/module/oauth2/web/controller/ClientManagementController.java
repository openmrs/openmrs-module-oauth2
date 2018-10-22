package org.openmrs.module.oauth2.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ContextAuthenticationException;
import org.openmrs.module.oauth2.Client;
import org.openmrs.module.oauth2.api.ClientRegistrationService;
import org.openmrs.module.oauth2.api.JsonMappableClient;
import org.openmrs.module.oauth2.api.JsonMappableClientOwa;
import org.openmrs.module.oauth2.api.db.hibernate.ClientDAO;
import org.openmrs.module.oauth2.api.model.AuthorizedGrantType;
import org.openmrs.module.oauth2.api.model.RedirectURI;
import org.openmrs.module.oauth2.api.model.Scope;
import org.openmrs.module.oauth2.api.smart.JsonMappableSmartApp;
import org.openmrs.module.oauth2.api.smart.JsonMappableSmartAppOwa;
import org.openmrs.module.oauth2.api.smart.SmartAppManagementService;
import org.openmrs.module.oauth2.api.smart.db.hibernate.SmartAppDAO;
import org.openmrs.module.oauth2.api.smart.model.SmartApp;
import org.openmrs.module.oauth2.api.util.ClientSpringOAuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Sanatt on 23-07-2017 and modified by Prabodh during GSoC-2018.
 */
@RestController
public class ClientManagementController {

	@Autowired
	private ClientRegistrationService clientRegistrationService;

	@Autowired
	private ClientDAO dao;

	@Autowired
	private SmartAppDAO smartAppDAO;

	/**
	 *See all registered clients for a particular OpenMRS user
	 */
	@RequestMapping(value = "/oauth/clientManagement", method = RequestMethod.GET,
			params = { "username", "password" })
	public ResponseEntity<List<JsonMappableClient>> listAllUsers(String username, String password,
			@RequestParam(value = "isSmart", required = false) boolean isSmart) {
		if (!verifyUserCredentials(username, password))
			return new ResponseEntity<List<JsonMappableClient>>((List<JsonMappableClient>) null, HttpStatus.UNAUTHORIZED);
		User openmrsUser = Context.getUserService().getUserByUsername(username);

		if (isSmart) {
			List<SmartApp> smartApps = getSmartService().loadSmartAppsForClientDeveloper(openmrsUser);
			if (smartApps.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			List<JsonMappableClient> jsonMappableSmartApps = new ArrayList<>();
			for (SmartApp smartApp : smartApps) {
				jsonMappableSmartApps.add(new JsonMappableSmartApp(smartApp));
			}
			return new ResponseEntity<>(jsonMappableSmartApps, HttpStatus.OK);
		} else {
			List<Client> clients = dao.getAllClientsForClientDeveloper(openmrsUser);
			if (clients.isEmpty()) {
				return new ResponseEntity<List<JsonMappableClient>>(HttpStatus.NO_CONTENT);
			}
			List<JsonMappableClient> jsonMappableClients = new ArrayList<>();
			for (Client c : clients) {
				jsonMappableClients.add(new JsonMappableClient(c));
			}
			return new ResponseEntity<List<JsonMappableClient>>(jsonMappableClients, HttpStatus.OK);
		}
	}

	/**
	 *See a registered client for a particular OpenMRS user
	 */
	@RequestMapping(value = "/oauth/clientManagement", method = RequestMethod.GET,
			params = { "username", "password", "client_id" })
	public ResponseEntity<JsonMappableClient> listAUsers(String client_id, String username, String password,
			@RequestParam(value = "isSmart", required = false) boolean isSmart) {
		if (!verifyUserCredentials(username, password))
			return new ResponseEntity<JsonMappableClient>((JsonMappableClient) null, HttpStatus.UNAUTHORIZED);

		Client client = (Client) dao.loadClientByClientId(client_id);
		User clientDeveloper = client.getCreator();
		User openmrsUser = Context.getUserService().getUserByUsername(username);
		if (clientDeveloper != openmrsUser)
			return new ResponseEntity<JsonMappableClient>((JsonMappableClient) null, HttpStatus.UNAUTHORIZED);
		if (isSmart) {
			SmartApp smartApp = smartAppDAO.loadSmartAppByClientId(client.getId());
			return new ResponseEntity<JsonMappableClient>(new JsonMappableSmartApp(smartApp), HttpStatus.OK);
		} else {
			return new ResponseEntity<JsonMappableClient>(new JsonMappableClient(client), HttpStatus.OK);
		}
	}

	/**
	 * Register a new client
	 */
	@RequestMapping(value = "/oauth/clientManagement", method = RequestMethod.POST,
			params = { "username", "password", "name", "redirectionUri", "clientType", "scopes", "grantTypes" })
	public ResponseEntity<JsonMappableClient> createNewUser(String username, String password, String name,
			String description,
			String website, String redirectionUri, String clientType,
			String[] scopes, String[] grantTypes, @RequestParam(value = "isSmart", required = false) boolean isSmart,
			@RequestParam(value = "launchUrl", required = false) String launchUrl) {


		if (!verifyUserCredentials(username, password))
			return new ResponseEntity<JsonMappableClient>((JsonMappableClient) null, HttpStatus.UNAUTHORIZED);
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

		client.setCreator(Context.getUserService().getUserByUsername(username));
		client.setAccessTokenValiditySeconds(600);
		client.setRefreshTokenValiditySeconds(600);

		client = getService().saveOrUpdateClient(client);
		getService().generateAndPersistClientCredentials(client);
		getService().saveOrUpdateClient(client);

		System.out.println("***Client Created");

		if (isSmart) {

			SmartApp smartApp = getNewSmartApp();

			smartApp.setClient(client);
			smartApp.setLaunchUrl(launchUrl);

			smartApp = getSmartService().saveOrUpdateSmartApp(smartApp);

			JsonMappableSmartApp jsonMappableSmartApp = new JsonMappableSmartApp(smartApp);
			return new ResponseEntity<JsonMappableClient>(jsonMappableSmartApp, HttpStatus.CREATED);
		} else {
			JsonMappableClient jsonMappableClient = new JsonMappableClient(client);
			return new ResponseEntity<JsonMappableClient>(jsonMappableClient, HttpStatus.CREATED);
		}

	}

	/**
	 * Delete or Unregister an oauth client
	 * @param client_id The client identifier
	 * @param client_secret The client secret
	 */
	@RequestMapping(value = "/oauth/clientManagement", method = RequestMethod.DELETE,
			params = { "client_id", "client_secret" })
	public ResponseEntity<String> deleteUser(String client_id, String client_secret,
			@RequestParam(value = "isSmart", required = false) boolean isSmart) {
		//verify the credentials
		Client tempClient = new Client(client_id, client_secret, null, null, null, null, null);
		List<String> encodedCredentials = clientRegistrationService.encodeCredentials(tempClient);
		Client client = (Client) dao.loadClientByClientId(client_id);
		if (!clientRegistrationService.verifyClientCredentials(client, encodedCredentials.get(0), encodedCredentials.get(1)))
			return new ResponseEntity<String>("Bad credentials", HttpStatus.UNAUTHORIZED);

		if (isSmart) {
			SmartApp smartApp = smartAppDAO.loadSmartAppByClientId(client.getId());
			getSmartService().unregisterSmartApp(smartApp);
			getService().unregisterClient(client);
			return new ResponseEntity<>("Smart Client deleted", HttpStatus.OK);
		} else {
			getService().unregisterClient(client);
			return new ResponseEntity<String>("Client deleted", HttpStatus.OK);
		}

	}

	/**
	 * Delete or Unregister an oauth client
	 * @param client_id The client identifier
	 */
	@RequestMapping(value = "/oauth/clientManagement", method = RequestMethod.DELETE,
			params = { "client_id", "username", "password" })
	public ResponseEntity<String> deleteUser(String client_id, String username, String password,
			@RequestParam(value = "isSmart", required = false) boolean isSmart) {
		if (!verifyUserCredentials(username, password))
			return new ResponseEntity<String>("Bad User Credentials", HttpStatus.UNAUTHORIZED);

		Client client = (Client) dao.loadClientByClientId(client_id);
		User clientDeveloper = client.getCreator();
		User openmrsUser = Context.getUserService().getUserByUsername(username);
		if (clientDeveloper != openmrsUser)
			return new ResponseEntity<String>("Invalid client developer", HttpStatus.UNAUTHORIZED);

		if (isSmart) {
			SmartApp smartApp = smartAppDAO.loadSmartAppByClientId(client.getId());
			getSmartService().unregisterSmartApp(smartApp);
			getService().unregisterClient(client);
			return new ResponseEntity<>("Smart Client deleted", HttpStatus.OK);
		} else {
			getService().unregisterClient(client);
			return new ResponseEntity<String>("Client deleted", HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/oauth/clientManagement", method = RequestMethod.GET)
	public ResponseEntity<List<JsonMappableClientOwa>> listAllUsers() {
		User openmrsUser = Context.getUserContext().getAuthenticatedUser();
		System.out.println("This user : "+openmrsUser);
		List<SmartApp> smartApps = getSmartService().loadSmartAppsForClientDeveloper(openmrsUser);
		if (smartApps.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		List<JsonMappableClientOwa> jsonMappableSmartApps = new ArrayList<>();
		for (SmartApp smartApp : smartApps) {
			jsonMappableSmartApps.add(new JsonMappableSmartAppOwa(smartApp));
		}
		return new ResponseEntity<>(jsonMappableSmartApps, HttpStatus.OK);

	}

	/**
	 * Update an oauth client
	 */
	@RequestMapping(value = "/oauth/clientManagement", method = RequestMethod.PUT,
			params = { "client_id", "client_secret", })

	public Client getNewClient() {
		Client client = new Client(null, null, null, null, null, null, null);
		return client;
	}

	public SmartApp getNewSmartApp() {
		SmartApp smartApp = new SmartApp(null, null);
		return smartApp;
	}

	public ClientRegistrationService getService() {
		return Context.getService(ClientRegistrationService.class);
	}

	public SmartAppManagementService getSmartService() {
		return Context.getService(SmartAppManagementService.class);
	}

	/**
	 * Verifies OpenMRS user credentials
	 */
	private boolean verifyUserCredentials(String username, String password) {
		try {
			Context.authenticate(username, password);
			return true;
		}
		catch (ContextAuthenticationException e) {
			return false;
		}
	}
}
