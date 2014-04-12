/**
 * 
 */
package edu.sjsu.cmpe.dropbox.config;

/**
 * @author Team Projections
 *
 */
public class MongoConfig {
	
	private static String amazonTimeZone;
	
	private static String amazonUsername;
	
	private static String amazonPassword;
	
	private static String dbName="dropbox";
	
    private static String dbCollection="dropboxdetails";
    
    private static String databaseUsername="pawans55";
    
    private static String databasePassword="_technical55";

	private static String databaseAddress="ds053658.mongolab.com";

	private static int databasePort=53658;

	/**
	 * @return the amazonTimeZone
	 */
	public static String getAmazonTimeZone() {
		return amazonTimeZone;
	}

	/**
	 * @param amazonTimeZone the amazonTimeZone to set
	 */
	public static void setAmazonTimeZone(String amazonTimeZone) {
		MongoConfig.amazonTimeZone = amazonTimeZone;
	}

	/**
	 * @return the amazonUsername
	 */
	public static String getAmazonUsername() {
		return amazonUsername;
	}

	/**
	 * @param amazonUsername the amazonUsername to set
	 */
	public static void setAmazonUsername(String amazonUsername) {
		MongoConfig.amazonUsername = amazonUsername;
	}

	/**
	 * @return the amazonPassword
	 */
	public static String getAmazonPassword() {
		return amazonPassword;
	}

	/**
	 * @param amazonPassword the amazonPassword to set
	 */
	public static void setAmazonPassword(String amazonPassword) {
		MongoConfig.amazonPassword = amazonPassword;
	}

	/**
	 * @return the dbName
	 */
	public static String getDbName() {
		return dbName;
	}

	/**
	 * @param dbName the dbName to set
	 */
	public static void setDbName(String dbName) {
		MongoConfig.dbName = dbName;
	}

	/**
	 * @return the dbCollection
	 */
	public static String getDbCollection() {
		return dbCollection;
	}

	/**
	 * @param dbCollection the dbCollection to set
	 */
	public static void setDbCollection(String dbCollection) {
		MongoConfig.dbCollection = dbCollection;
	}

	/**
	 * @return the databaseUsername
	 */
	public static String getDatabaseUsername() {
		return databaseUsername;
	}

	/**
	 * @param databaseUsername the databaseUsername to set
	 */
	public static void setDatabaseUsername(String databaseUsername) {
		MongoConfig.databaseUsername = databaseUsername;
	}

	/**
	 * @return the databasePassword
	 */
	public static String getDatabasePassword() {
		return databasePassword;
	}

	/**
	 * @param databasePassword the databasePassword to set
	 */
	public static void setDatabasePassword(String databasePassword) {
		MongoConfig.databasePassword = databasePassword;
	}

	/**
	 * @return the databaseAddress
	 */
	public static String getDatabaseAddress() {
		return databaseAddress;
	}

	/**
	 * @param databaseAddress the databaseAddress to set
	 */
	public static void setDatabaseAddress(String databaseAddress) {
		MongoConfig.databaseAddress = databaseAddress;
	}

	/**
	 * @return the databasePort
	 */
	public static int getDatabasePort() {
		return databasePort;
	}

	/**
	 * @param databasePort the databasePort to set
	 */
	public static void setDatabasePort(int databasePort) {
		MongoConfig.databasePort = databasePort;
	}
}
