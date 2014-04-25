/**
 * 
 */
package edu.sjsu.cmpe.dropbox.dto;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import edu.sjsu.cmpe.dropbox.config.MongoConfig;
import edu.sjsu.cmpe.dropbox.domain.MongoDBDetails;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

/**
 * @author Team projections
 * 
 */
public class MongoTest {

	public static MongoCollection collection;
	

	public MongoTest() throws UnknownHostException {
		
		String textUri = "mongodb://cmpe273:123456@ds053658.mongolab.com:53658/dropbox";
	    MongoClientURI uri = new MongoClientURI(textUri);
		MongoClient m = new MongoClient(uri);
		System.out.println("Connected!!");
		DB db = m.getDB("dropbox");
		//this.collection = db.getCollection("dropboxdetails");
		// TODO Auto-generated constructor stub
		MongoClient client = null;
		try {
			System.out.println("Trying to create client try");
			client = new MongoClient(uri);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("Trying to create client try");
			e.printStackTrace();
			System.out.println("!!!!Error in connection!!!!");
		}
		System.out.println("Outside try catch");
		DB database = client.getDB(MongoConfig.getDbName());
	//	String username = "pawans55";
	//	String pwd = "_technical55";
	//	char[] password = pwd.toCharArray();
	//	boolean auth = database.authenticate(username, password);
		Jongo jongo = new Jongo(database);
		MongoTest.collection = jongo.getCollection(MongoConfig
				.getDbCollection());
		
		System.out.println("Pooja 1 - "+ MongoTest.collection);
	}

	/**
	 * @param dbDetails
	 *            pass the database object and it will directly save into
	 *            Database
	 */
	public void insert(MongoDBDetails dbDetails) {
		collection.insert(dbDetails);
	}

	/**
	 * @return the collection
	 */
	public MongoCollection getCollection() {
		return collection;
	}

	/**
	 * @param collection
	 *            the collection to set
	 */
	public void setCollection(MongoCollection collection) {
		this.collection = collection;
	}

	public Boolean isUserNameExist(String username) throws UnknownHostException {
		String query = "{userName:'" + username + "'}";
	//	BasicDBObject query = new BasicDBObject();
	//	query.put("name", username);
		System.out.println(query);
		System.out.println(collection);
	//	DBObject document = collection.findOne(query);
		MongoDBDetails dbDetails = collection.findOne(query).as(
				MongoDBDetails.class);
		System.out.println("In isUserNameExists");

		if (dbDetails == null) {
			System.out.println("Did not find");
			return false;
		} else {
			System.out.println("Found");
			return true;
		}
	}

	public Boolean checkFileSizeToUpload(String userName, long fileSize) {
		String query = "{userName:'" + userName + "'}";
		long storageLeft = MongoTest.collection.findOne(query)
				.as(MongoDBDetails.class).getTotalStorageLeft();
		if (storageLeft >= fileSize) {
			return true;
		} else {
			return false;
		}
	}

	public void deleteFileDetails(String userName, String fileName) {
		moveToTrash(userName, fileName, "N"); // N stands for normal file delete
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = MongoTest.collection.findOne(query).as(
				MongoDBDetails.class);
		int index = dbDetails.getFileIndex(fileName);
		dbDetails.removeFilePath(index);
		long fileSize = dbDetails.getFileSizeFile(index);
		dbDetails.setTotalStorageLeft(dbDetails.getTotalStorageLeft()
				+ fileSize);
		dbDetails.removefileSize(index);
		dbDetails.removeFileFromList(index);
		dbDetails.removeFileUploadingDate(index);
		collection.update(query).merge(dbDetails);
	}

	public void shareFile(String userName, String sharedWith, String fileName) {
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails userDbDetails = MongoTest.collection.findOne(query).as(
				MongoDBDetails.class);
		String query2 = "{userName:'" + sharedWith + "'}";
		MongoDBDetails sharedDbDetails = MongoTest.collection.findOne(query2)
				.as(MongoDBDetails.class);
		sharedDbDetails.addSharedFileName(fileName);
		int index = sharedDbDetails.getSharedFileIndex(fileName);
		sharedDbDetails.addSharedbucketName(index, userDbDetails.getbucketName());
		sharedDbDetails.addSharedFileSize(index,
		userDbDetails.getFileSizeByFileName(fileName));
		sharedDbDetails.addSharedFileDates(index, new Date().toGMTString());
		collection.update(query2).merge(sharedDbDetails);
		System.out.println("File shared");
	}

	// below 2 functions will be used for downloading and deleting the shared
	// file
	public String getSharedFilebucketName(String userName, String fileName) {
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = MongoTest.collection.findOne(query).as(
				MongoDBDetails.class);
		int index = dbDetails.getSharedFileIndex(fileName);
		return dbDetails.getSharedFilebucketName(index);
	}


	public void deleteSharedFileDetails(String userName, String fileName) {
		moveToTrash(userName, fileName, "S"); // S stands for shared file
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = MongoTest.collection.findOne(query).as(
				MongoDBDetails.class);
		int index = dbDetails.getSharedFileIndex(fileName);
		dbDetails.removeSharedFileDetails(index);
		collection.update(query).merge(dbDetails);
	}

	private void moveToTrash(String userName, String fileName, String s) {
		// TODO - Auto-generated method stub
		System.out.println("Inside Move to trash");
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = MongoTest.collection.findOne(query).as(
				MongoDBDetails.class);

		if (s.equalsIgnoreCase("N")) {
			System.out.println("Normal Delete");
			int index1 = dbDetails.getFileIndex(fileName);
/*			dbDetails.addTrashListOfFiles(fileName);
			int index2 = dbDetails.getTrashFileIndex(fileName);
			dbDetails.addTrashVaultName(index2, dbDetails.getVaultName());
			dbDetails.addtrashFileArichiveIds(index2,
					dbDetails.getArchiveId(index1));
			dbDetails.addTrashFileSizes(index2,
					dbDetails.getFileSizeFile(index1));
			dbDetails
					.addTrashFileDeletionDate(index2, new Date().toGMTString());
			dbDetails.addTrashCameFrom(index2, s);
		} else {
			System.out.println("shared Delete");
			int index1 = dbDetails.getSharedFileIndex(fileName);
			dbDetails.addTrashListOfFiles(fileName);
			int index2 = dbDetails.getTrashFileIndex(fileName);
			dbDetails.addTrashVaultName(index2,
					dbDetails.getSharedFileVaultName(index1));
			dbDetails.addtrashFileArichiveIds(index2,
					dbDetails.getSharedFileArchivalId(index1));
			dbDetails.addTrashFileSizes(index2,
					dbDetails.getSharedFileSize(index1));
			dbDetails
					.addTrashFileDeletionDate(index2, new Date().toGMTString());
			dbDetails.addTrashCameFrom(index2, s);*/
		}
		collection.update(query).merge(dbDetails);
		System.out.println("Moved to trash");
	}

	
	public Boolean authenticateUser(String userName, String password) {
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = MongoTest.collection.findOne(query).as(
				MongoDBDetails.class);
		System.out.println("database pwd: " + dbDetails.getPassword());
		if (dbDetails.getPassword().equals(password)) {
			return true;
		} else {
			return false;
		}
	}

	

	// Below methods will be used for Views

	public List<String> getFileNames(String userName) {
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = MongoTest.collection.findOne(query).as(
				MongoDBDetails.class);

		return dbDetails.getListOfFiles();
	}

	public List<String> getSharedFileNames(String userName) {
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = MongoTest.collection.findOne(query).as(
				MongoDBDetails.class);

		return dbDetails.getSharedFileNames();
	}

	

	
	public List<String> getSharedFileDates(String userName) {
		// TODO Auto-generated method stub
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = MongoTest.collection.findOne(query).as(
				MongoDBDetails.class);
		return dbDetails.getSharedFileDates();
	}

	public List<String> getFileDates(String userName) {
		// TODO Auto-generated method stub
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = MongoTest.collection.findOne(query).as(
				MongoDBDetails.class);
		return dbDetails.getListOfDate();
	}
	public String getBucketName(String userName){
		String query = "{userName:'"+userName+"'}";
		String bucketName = MongoTest.collection.findOne(query).as(MongoDBDetails.class).getbucketName();
		return (bucketName);
	}

	/**
	 * @param userName
	 * @param fileName
	 * @param filePath
	 * @param fileZize
	 * This function will add new file details in the database
	 * 1. Automatically update the StorageLeft
	 */
	public void addNewFileDetails(String userName, String fileName, String filePath, long fileSize){
		String query = "{userName:'"+userName+"'}";
		MongoDBDetails dbDetails = MongoTest.collection.findOne(query).as(MongoDBDetails.class);
		dbDetails.addFile(fileName);
		int index = dbDetails.getFileIndex(fileName);
		dbDetails.addFilePath(index, filePath);
		dbDetails.addfileSize(index, fileSize);
		dbDetails.addFileUploadingDate(index, new Date().toGMTString());
		dbDetails.setTotalStorageLeft(dbDetails.getTotalStorageLeft() - fileSize);
		collection.update(query).merge(dbDetails);
		System.out.println("updated fields");
	}


	public String registerUser(String userName, String password, String emailid) throws UnknownHostException{
		MongoDBDetails dbDetails = new MongoDBDetails();
		System.out.println("I am in registerUser MongoTest.java");
		if(isUserNameExist(userName) == false){
			dbDetails.setUserName(userName);
		}
		else{
			System.out.println("This is existing username");
			dbDetails.setUserNameWithNewbucketName(userName); //Created unique username and bucketname also
		}
		dbDetails.setPassword(password);
		dbDetails.setEmailid(emailid);
		insert(dbDetails);
		System.out.println("Pooja 2 - " + dbDetails);
		return dbDetails.getUserName();
	}
}