package org.openmrs.module.oauth2.web.controller;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.oauth2.api.smart.SmartAppLaunchService;
import org.openmrs.module.oauth2.api.smart.SmartAppManagementService;
import org.openmrs.module.oauth2.api.smart.model.LaunchValue;
import org.openmrs.module.oauth2.api.smart.model.SmartApp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = RunSmartAppController.RUN_SMART_CONTROLLER)
public class RunSmartAppController {

	protected final Log log = LogFactory.getLog(getClass());

	private static final String RUN_SMART_VIEW = "/module/oauth2/runSmart";

	public static final String RUN_SMART_CONTROLLER = "module/oauth2/runSmartApps/runSmart.form";

	@RequestMapping(method = RequestMethod.GET)
	public String showList() {
		return RUN_SMART_VIEW;
	}

	@RequestMapping(method = RequestMethod.POST)
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

	@ModelAttribute("smartApps")
	public List<SmartApp> getRegisteredSmartApps() {
		List<SmartApp> smartApps = null;
		SmartAppManagementService service = Context.getService(SmartAppManagementService.class);
		User user = Context.getAuthenticatedUser();
		smartApps = service.loadSmartAppsForClientDeveloper(user);
		return smartApps;
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

}
