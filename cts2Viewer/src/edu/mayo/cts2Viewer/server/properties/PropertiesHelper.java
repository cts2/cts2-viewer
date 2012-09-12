package edu.mayo.cts2Viewer.server.properties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to find the class path that the properties file resides in.
 */
public class PropertiesHelper {
	private static final String SERVER_PROPERTIES_FILE = "CTS2Profiles.properties";

	private static Logger logger = Logger.getLogger(PropertiesHelper.class.getName());
	private static PropertiesHelper i_propertiesHelper;

	public static PropertiesHelper getInstance() {
		if (i_propertiesHelper == null) {
			i_propertiesHelper = new PropertiesHelper();
		}
		return i_propertiesHelper;
	}

	private PropertiesHelper() {
		// private
	}

	public String getPropertiesDirectory() {

		logger.log(Level.INFO, "Getting path to properties file from the classpath...");

		String path = this.getClass().getPackage().getName();
		path = path.replaceAll("[.]", File.separator);
		path = path + File.separator + SERVER_PROPERTIES_FILE;
		logger.log(Level.INFO, "Path = " + path);

		return path;
	}

	private void getPropertiesFileTest(String path) {
		Properties properties = new Properties();
		logger.log(Level.INFO, "TESTING loading Properties...");
		try {
			// the properties file is in the same directory
			InputStream inputStream = this.getClass().getResourceAsStream(SERVER_PROPERTIES_FILE);

			properties.load(inputStream);
			logger.log(Level.INFO, "Properties loaded");

			String prop = properties.getProperty("profiles");
			logger.log(Level.INFO, "Profiles = " + prop);

			logger.log(Level.INFO, "TESTING loading Properties - SUCCESS");

		} catch (IOException e) {
			logger.log(Level.INFO, "TESTING loading Properties - FAILURE");
			logger.log(Level.INFO, "IOException = " + e.toString());
		}
	}
}
