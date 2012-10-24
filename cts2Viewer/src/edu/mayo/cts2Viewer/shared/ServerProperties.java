package edu.mayo.cts2Viewer.shared;

import java.io.Serializable;

/**
 * Class to hold server properties that are retrieved from the server properties
 * file based on the selected server.
 * 
 */
public class ServerProperties implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean i_requireCredentials;
	private boolean i_showFilters;
	private String entityTransformService;

	public String getEntityTransformService() {
		return entityTransformService;
	}

	public void setEntityTransformService(String entityTransformService) {
		this.entityTransformService = entityTransformService;
	}

	public boolean isRequireCredentials() {
		return i_requireCredentials;
	}

	public void setRequireCredentials(boolean isSecure) {
		this.i_requireCredentials = isSecure;
	}

	public boolean isShowFilters() {
		return i_showFilters;
	}

	public void setShowFilters(boolean showFilters) {
		this.i_showFilters = showFilters;
	}

}
