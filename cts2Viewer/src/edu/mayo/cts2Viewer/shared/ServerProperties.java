package edu.mayo.cts2Viewer.shared;

import java.io.Serializable;

/**
 * Class to hold server properties that are retrieved from the server properties
 * file based on the selected server.
 * 
 */
public class ServerProperties implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean i_isSecure;
	private boolean i_showFilters;

	public boolean isSecure() {
		return i_isSecure;
	}

	public void setSecure(boolean isSecure) {
		this.i_isSecure = isSecure;
	}

	public boolean isShowFilters() {
		return i_showFilters;
	}

	public void setShowFilters(boolean showFilters) {
		this.i_showFilters = showFilters;
	}

}
