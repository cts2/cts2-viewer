package edu.mayo.cts2Viewer.shared;

import java.io.Serializable;

public class ValueSetInfo implements Serializable {

	public static final String X_PATH_BASE = "/cts2:ValueSetCatalogEntryMsg/cts2:valueSetCatalogEntry";
	public static final String X_PATH_RESOURCE_STATE = X_PATH_BASE + "/@entryState";
	public static final String X_PATH_RESOURCE_ABOUT_URI = X_PATH_BASE + "/@about";
	public static final String X_PATH_RESOURCE_FORMAL_NAME = X_PATH_BASE + "/@formalName";
	public static final String X_PATH_RESOURCE_VS_NAME = X_PATH_BASE + "/@valueSetName";

	public static final String X_PATH_RESOURCE_SOURCE = X_PATH_BASE + "/core:sourceAndRole/core:source";
	public static final String X_PATH_RESOURCE_SOURCE_URI = X_PATH_BASE + "/core:sourceAndRole/core:source/@uri";
	public static final String X_PATH_RESOURCE_ROLE = X_PATH_BASE + "/core:sourceAndRole/core:role";
	public static final String X_PATH_RESOURCE_SYNOPSIS = X_PATH_BASE + "/core:resourceSynopsis/core:value";
	public static final String X_PATH_RESOURCE_DEFINITIONS = X_PATH_BASE + "/cts2:definitions";

	private String i_xml;

	private String i_stateValue;
	private String i_resourceNameValue;
	private String i_formalNameValue;
	private String i_descriptionValue;
	private String i_source;
	private String i_sourceUri;
	private String i_role;

	public String getXml() {
		return i_xml;
	}

	public void setXml(String xml) {
		i_xml = xml;
	}

	public String getState() {
		return i_stateValue;
	}

	public void setState(String stateValue) {
		i_stateValue = stateValue;
	}

	public String getResourceName() {
		return i_resourceNameValue;
	}

	public void setResourceName(String resourceNameValue) {
		i_resourceNameValue = resourceNameValue;
	}

	public String getFormalName() {
		return i_formalNameValue;
	}

	public void setFormalName(String formalNameValue) {
		i_formalNameValue = formalNameValue;
	}

	public String getDescriptionValue() {
		return i_descriptionValue;
	}

	public void setDescription(String descriptionValue) {
		i_descriptionValue = descriptionValue;
	}

	public String getRole() {
		return i_role;
	}

	public void setRole(String role) {
		i_role = role;
	}

	public String getSource() {
		return i_source;
	}

	public void setSource(String source) {
		i_source = source;
	}

	public String getSourceUri() {
		return i_sourceUri;
	}

	public void setSourceUri(String sourceUri) {
		i_sourceUri = sourceUri;
	}
}
