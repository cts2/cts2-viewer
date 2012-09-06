package edu.mayo.cts2Viewer.shared;

import java.io.Serializable;

public class Credentials implements Serializable {

	private static final long serialVersionUID = 1L;

	private String i_user;
	private String i_password;
	private String i_server;

	public Credentials() {
		super();
	}

	public String getPassword() {
		return i_password;
	}

	public void setPassword(String password) {
		i_password = password;
	}

	public String getUser() {
		return i_user;
	}

	public void setUser(String user) {
		i_user = user;
	}

	public String getServer() {
		return i_server;
	}

	public void setServer(String server) {
		i_server = server;
	}

}
