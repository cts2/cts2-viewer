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

	}

	public String getPropertiesDirectory() {

		logger.log(Level.INFO, "Getting path to properties file from the classpath...");

		String path = this.getClass().getPackage().toString();
		path = path.replaceAll("[.]", File.separator);
		path = path + File.separator + SERVER_PROPERTIES_FILE;
		logger.log(Level.INFO, "Path = " + path);

		// try {
		// start from the "classes" directory -
		// webapps/cts2Viewer/WEB-INF/classes/
		// InputStream inputStream =
		// this.getClass().getResourceAsStream("/../../data/" +
		// SERVER_PROPERTIES_FILE);

		// String path = this.getClass().getResource("/../../data/" +
		// SERVER_PROPERTIES_FILE).getPath();
		// String path =
		// this.getClass().getResource(SERVER_PROPERTIES_FILE).getPath();

		// System.out.println("getName() = " + this.getClass().getName());
		// System.out.println("getCanonicalName() = " +
		// this.getClass().getCanonicalName());
		// System.out.println("getPackage() = " + this.getClass().getPackage());

		// getPropertiesFile(path);

		// properties.load(inputStream);
		// logger.log(Level.INFO, "Properties loaded");
		//
		// String prop = properties.getProperty("profiles");
		// logger.log(Level.INFO, "Profiles = " + prop);
		//
		// System.out.println("\nFile is created..........");
		// } catch (IOException e) {
		// logger.log(Level.INFO, "IOException = " + e.toString());
		// }

		return path;
	}

	public void getPropertiesFile(String path) {
		Properties properties = new Properties();

		try {
			// the properties file is in the same directory
			InputStream inputStream = this.getClass().getResourceAsStream(SERVER_PROPERTIES_FILE);

			properties.load(inputStream);
			logger.log(Level.INFO, "Properties loaded");

			String prop = properties.getProperty("profiles");
			logger.log(Level.INFO, "Profiles = " + prop);

		} catch (IOException e) {
			logger.log(Level.INFO, "IOException = " + e.toString());
		}
	}
}
