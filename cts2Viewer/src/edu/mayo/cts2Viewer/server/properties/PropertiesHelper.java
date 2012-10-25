package edu.mayo.cts2Viewer.server.properties;

import java.io.File;
import java.util.logging.Logger;

/**
 * Class to find the class path that the properties file resides in.
 */
public class PropertiesHelper {
	private static final String SERVER_PROPERTIES_FILE = "CTS2Profiles.properties";
	private static final String NQF_NUMBERS_FILE = "nqfNumbers";
	private static final String EMEASURE_IDS_FILE = "eMeasureIds";

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

		//logger.log(Level.INFO, "Getting path to properties file from the classpath...");

		String path = this.getClass().getPackage().getName();
		path = path.replaceAll("[.]", File.separator);
		path = path + File.separator + SERVER_PROPERTIES_FILE;
		//logger.log(Level.INFO, "Path = " + path);

		return path;
	}

	public String getNqfNumbersPath() {
		String path = this.getClass().getPackage().getName();
		path = path.replaceAll("[.]", File.separator);
		path = path + File.separator + NQF_NUMBERS_FILE;
		//logger.log(Level.INFO, "Path = " + path);

		return path;
	}

	public String getEmeasureIdsPath() {
		String path = this.getClass().getPackage().getName();
		path = path.replaceAll("[.]", File.separator);
		path = path + File.separator + EMEASURE_IDS_FILE;
		//logger.log(Level.INFO, "Path = " + path);

		return path;
	}
}
