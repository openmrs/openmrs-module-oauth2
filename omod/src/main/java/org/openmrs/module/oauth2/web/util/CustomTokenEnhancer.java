package org.openmrs.module.oauth2.web.util;

import java.util.HashMap;
import java.util.Map;

import org.openmrs.api.context.Context;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

@Component
public class CustomTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
			OAuth2Authentication authentication) {
		// TODO Auto-generated method stub
		final Map<String, Object> additionalInfo = new HashMap<>();

		if(accessToken.getScope().isEmpty())
			System.out.println("Empty scope!");
		for(Object object:accessToken.getScope()){
			String scope = (String) object;
			System.out.println(scope);
			if(scope.contains("patient")){
				additionalInfo.put("patient", authentication.getName());
			}
		}
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

		return accessToken;
	}

}
