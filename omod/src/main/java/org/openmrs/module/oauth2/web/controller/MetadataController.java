package org.openmrs.module.oauth2.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetadataController {

	/**
	 * OAuth module metadata endpoint for SMART apps.
	 * @return metadata in standard Conformance Statement format
	 */
	@RequestMapping(value = "/oauth/metadata", method = RequestMethod.GET)
	public ResponseEntity<HashMap<String, Object>> getMetadata() {
		String fhirUri = "http://localhost:8080/openmrs/ws/fhir";
		String authUri = "http://localhost:8080/openmrs/ws/oauth/authorize";
		String tokenUri = "http://localhost:8080/openmrs/ws/oauth/token";

		HashMap<String, Object> codingMap = new HashMap<>();
		codingMap.put("system","http://hl7.org/fhir/restful-security-service");
		codingMap.put("code","SMART-on-FHIR");

		List<HashMap<String,Object>> codingList = new ArrayList<>();
		codingList.add(codingMap);

		HashMap<String,Object> serviceMap = new HashMap<>();
		serviceMap.put("coding",codingList);
		serviceMap.put("text","OAuth2 using SMART-on-FHIR profile (see http://docs.smarthealthit.org)");

		List<HashMap<String,Object>> serviceList = new ArrayList<>();
		serviceList.add(serviceMap);

		HashMap<String,Object> tokenMap = new HashMap<>();
		tokenMap.put("url","token");
		tokenMap.put("valueUri",tokenUri);

		HashMap<String,Object> authorizeMap = new HashMap<>();
		authorizeMap.put("url","authorize");
		authorizeMap.put("valueUri",authUri);

		HashMap<String,Object> fhirMap = new HashMap<>();
		fhirMap.put("url","fhir");
		fhirMap.put("valueUri",fhirUri);

		List<HashMap<String,Object>> innerExtentionList = new ArrayList<>();
		innerExtentionList.add(tokenMap);
		innerExtentionList.add(authorizeMap);
		innerExtentionList.add(fhirMap);

		HashMap<String,Object> extentionMap = new HashMap<>();
		extentionMap.put("url","http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris");
		extentionMap.put("extension",innerExtentionList);

		List<HashMap<String,Object>> extentionList = new ArrayList<>();
		extentionList.add(extentionMap);

		HashMap<String,Object> securityMap = new HashMap<>();
		securityMap.put("service",serviceList);
		securityMap.put("extension",extentionList);

		HashMap<String,Object> restMap = new HashMap<>();
		restMap.put("security",securityMap);

		List<HashMap<String,Object>> restList = new ArrayList<>();
		restList.add(restMap);

		HashMap<String,Object> metadataMap = new HashMap<>();
		metadataMap.put("resourceType","Conformance");
		metadataMap.put("rest",restList);

		return new ResponseEntity<HashMap<String, Object>>(metadataMap, HttpStatus.OK);
	}
}
