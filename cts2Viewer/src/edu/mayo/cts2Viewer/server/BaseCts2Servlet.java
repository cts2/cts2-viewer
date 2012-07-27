package edu.mayo.cts2Viewer.server;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Base cts2 servlet that gets the absolute path to the data directory. This is
 * needed to run in production mode on different app servers.
 * 
 */
public class BaseCts2Servlet extends RemoteServiceServlet {

	private static final long serialVersionUID = 1L;

	public String getBasePath() {

		HttpSession httpSession = getThreadLocalRequest().getSession(true);
		ServletContext context = httpSession.getServletContext();

		String realContextPath = context.getRealPath(getThreadLocalRequest()
				.getContextPath());
		String dataPath = realContextPath + "/../";

		return dataPath;
	}

}
