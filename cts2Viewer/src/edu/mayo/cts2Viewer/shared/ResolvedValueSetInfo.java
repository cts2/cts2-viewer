package edu.mayo.cts2Viewer.shared;

import java.io.Serializable;

public class ResolvedValueSetInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String X_PATH_BASE = "/cts2:IteratableResolvedValueSet/cts2:resolutionInfo/";

	public static final String X_PATH_VS_DEFINITION = X_PATH_BASE + "cts2:resolutionOf/core:valueSetDefinition";
	public static final String X_PATH_VS_DEFINITION_URI = X_PATH_BASE + "cts2:resolutionOf/core:valueSet/@uri";
	public static final String X_PATH_VS_DEFINITION_HREF = X_PATH_BASE + "cts2:resolutionOf/core:valueSet/@href";

	public static final String X_PATH_CODE_SYSTEM_VERSION = X_PATH_BASE + "cts2:resolvedUsingCodeSystem/core:version";
	public static final String X_PATH_CODE_SYSTEM_VERSION_HREF = X_PATH_BASE
	        + "cts2:resolvedUsingCodeSystem/core:version/@href";

	public static final String X_PATH_CODE_SYSTEM = X_PATH_BASE + "cts2:resolvedUsingCodeSystem/core:codeSystem";
	public static final String X_PATH_CODE_SYSTEM_URI = X_PATH_BASE
	        + "cts2:resolvedUsingCodeSystem/core:codeSystem/@uri";
	public static final String X_PATH_CODE_SYSTEM_HREF = X_PATH_BASE
	        + "cts2:resolvedUsingCodeSystem/core:codeSystem/@href";

	private String i_xml;

	private String i_valueSetDefinition;
	private String i_valueSetDefinitionUri;
	private String i_valueSetDefinitionHref;
	private String i_codeSystemVersion;
	private String i_codeSystemVersionHref;
	private String i_codeSystem;
	private String i_codeSystemUri;
	private String i_codeSystemHref;

	public ResolvedValueSetInfo() {
		super();

		i_valueSetDefinition = "";
		i_valueSetDefinitionUri = "";
		i_valueSetDefinitionHref = "";
		i_codeSystemVersion = "";
		i_codeSystemVersionHref = "";
		i_codeSystem = "";
		i_codeSystemUri = "";
		i_codeSystemHref = "";
	}

	public String getXml() {
		return i_xml;
	}

	public void setXml(String xml) {
		i_xml = xml;
	}

	public String getValueSetDefinition() {
		return i_valueSetDefinition;
	}

	public void setValueSetDefinition(String valueSetDefinition) {
		i_valueSetDefinition = valueSetDefinition;
	}

	public String getValueSetDefinitionUri() {
		return i_valueSetDefinitionUri;
	}

	public void setValueSetDefinitionUri(String valueSetDefinitionUri) {
		i_valueSetDefinitionUri = valueSetDefinitionUri;
	}

	public String getValueSetDefinitionHref() {
		return i_valueSetDefinitionHref;
	}

	public void setValueSetDefinitionHref(String valueSetDefinitionHref) {
		i_valueSetDefinitionHref = valueSetDefinitionHref;
	}

	public String getCodeSystemVersion() {
		return i_codeSystemVersion;
	}

	public void setCodeSystemVersion(String codeSystemVersion) {
		i_codeSystemVersion = codeSystemVersion;
	}

	public String getCodeSystemVersionHref() {
		return i_codeSystemVersionHref;
	}

	public void setCodeSystemVersionHref(String codeSystemVersionHref) {
		i_codeSystemVersionHref = codeSystemVersionHref;
	}

	public String getCodeSystem() {
		return i_codeSystem;
	}

	public void setCodeSystem(String codeSystem) {
		i_codeSystem = codeSystem;
	}

	public String getCodeSystemUri() {
		return i_codeSystemUri;
	}

	public void setCodeSystemUri(String codeSystemUri) {
		i_codeSystemUri = codeSystemUri;
	}

	public String getCodeSystemHref() {
		return i_codeSystemHref;
	}

	public void setCodeSystemHref(String codeSystemHref) {
		i_codeSystemHref = codeSystemHref;
	}

}
