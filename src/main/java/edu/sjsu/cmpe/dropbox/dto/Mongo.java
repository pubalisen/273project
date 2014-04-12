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
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

/**
 * @author Team projections
 * 
 */
public class Mongo {

	public static MongoCollection collection;

	public Mongo() {
		// TODO Auto-generated constructor stub
		MongoClient client = null;
		try {
			client = new MongoClient(new ServerAddress(
					MongoConfig.getDatabaseAddress(),
					MongoConfig.getDatabasePort()));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("!!!!Error in connection!!!!");
		}
		DB database = client.getDB(MongoConfig.getDbName());
		String username = "pawans55";
		String pwd = "_technical55";
		char[] password = pwd.toCharArray();
		boolean auth = database.authenticate(username, password);
		Jongo jongo = new Jongo(database);
		Mongo.collection = jongo.getCollection(MongoConfig
				.getDbCollection());
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

	public Boolean isUserNameExist(String username) {
		String query = "{userName:'" + username + "'}";
		MongoDBDetails dbDetails = collection.findOne(query).as(
				MongoDBDetails.class);

		if (dbDetails == null) {
			return false;
		} else {
			return true;
		}
	}

	public Boolean checkFileSizeToUpload(String userName, long fileSize) {
		String query = "{userName:'" + userName + "'}";
		long storageLeft = Mongo.collection.findOne(query)
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
		MongoDBDetails dbDetails = Mongo.collection.findOne(query).as(
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
		MongoDBDetails userDbDetails = Mongo.collection.findOne(query).as(
				MongoDBDetails.class);
		String query2 = "{userName:'" + sharedWith + "'}";
		MongoDBDetails sharedDbDetails = Mongo.collection.findOne(query2)
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
		MongoDBDetails dbDetails = Mongo.collection.findOne(query).as(
				MongoDBDetails.class);
		int index = dbDetails.getSharedFileIndex(fileName);
		return dbDetails.getSharedFilebucketName(index);
	}

	public String getSharedFileArchivalId(String userName, String fileName) {
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = Mongo.collection.findOne(query).as(
				MongoDBDetails.class);
		int index = dbDetails.getSharedFileIndex(fileName);
		return dbDetails.getSharedFileArchivalId(index);
	}

	public void deleteSharedFileDetails(String userName, String fileName) {
		moveToTrash(userName, fileName, "S"); // S stands for shared file
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = Mongo.collection.findOne(query).as(
				MongoDBDetails.class);
		int index = dbDetails.getSharedFileIndex(fileName);
		dbDetails.removeSharedFileDetails(index);
		collection.update(query).merge(dbDetails);
	}

	public void deleteFileFromTrash(String userName, String fileName) {
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = Mongo.collection.findOne(query).as(
				MongoDBDetails.class);
		int index = dbDetails.getTrashFileIndex(fileName);
		dbDetails.removeFromTrash(index);
		collection.update(query).merge(dbDetails);
	}

	/**
	 * @param userName
	 * @param fileName
	 * @param s
	 *            Move files to trash.. irrespective of whether file is deleted
	 *            from Normal list or from SharedFiles No need to maintain the
	 *            size of deleted shared files
	 */
	private void moveToTrash(String userName, String fileName, String s) {
		// TODO - Auto-generated method stub
		System.out.println("Inside Move to trash");
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = Mongo.collection.findOne(query).as(
				MongoDBDetails.class);

		if (s.equalsIgnoreCase("N")) {
			System.out.println("Normal Delete");
			int index1 = dbDetails.getFileIndex(fileName);
			dbDetails.addTrashListOfFiles(fileName);
			int index2 = dbDetails.getTrashFileIndex(fileName);
			dbDetails.addTrashbucketName(index2, dbDetails.getbucketName());
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
			dbDetails.addTrashbucketName(index2,
					dbDetails.getSharedFilebucketName(index1));
			
			dbDetails.addTrashFileSizes(index2,
					dbDetails.getSharedFileSize(index1));
			dbDetails
					.addTrashFileDeletionDate(index2, new Date().toGMTString());
			dbDetails.addTrashCameFrom(index2, s);
		}
		collection.update(query).merge(dbDetails);
		System.out.println("Moved to trash");
	}

	public Boolean authenticateUser(String userName, String password) {
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = Mongo.collection.findOne(query).as(
				MongoDBDetails.class);
		System.out.println("database pwd: " + dbDetails.getPassword());
		if (dbDetails.getPassword().equals(password)) {
			return true;
		} else {
			return false;
		}
	}

	public String getTrashFilebucketName(String userName, String fileName) {
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = Mongo.collection.findOne(query).as(
				MongoDBDetails.class);
		int index = dbDetails.getTrashFileIndex(fileName);
		return dbDetails.getTrashFilebucketName(index);
	}

	// Below methods will be used for Views

	public List<String> getFileNames(String userName) {
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = Mongo.collection.findOne(query).as(
				MongoDBDetails.class);

		return dbDetails.getListOfFiles();
	}

	public List<String> getSharedFileNames(String userName) {
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = Mongo.collection.findOne(query).as(
				MongoDBDetails.class);

		return dbDetails.getSharedFileNames();
	}

	public List<String> getTrashFileNames(String userName) {
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = Mongo.collection.findOne(query).as(
				MongoDBDetails.class);

		return dbDetails.getTrashListOfFiles();
	}

	public List<String> getTrashFileDates(String userName) {
		// TODO Auto-generated method stub
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = Mongo.collection.findOne(query).as(
				MongoDBDetails.class);
		return dbDetails.getTrashListOfDates();
	}
	
	public List<String> getSharedFileDates(String userName) {
		// TODO Auto-generated method stub
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = Mongo.collection.findOne(query).as(
				MongoDBDetails.class);
		return dbDetails.getSharedFileDates();
	}

	public List<String> getFileDates(String userName) {
		// TODO Auto-generated method stub
		String query = "{userName:'" + userName + "'}";
		MongoDBDetails dbDetails = Mongo.collection.findOne(query).as(
				MongoDBDetails.class);
		return dbDetails.getListOfDate();
	}
	public String getBucketName(String userName){
		String query = "{userName:'"+userName+"'}";
		String bucketName = Mongo.collection.findOne(query).as(MongoDBDetails.class).getbucketName();
		return (bucketName);
	}

	/**
	 * @param userName
	 * @param fileName
	 * @param filePath
	 * @param fileArchiveId
	 * @param fileZize
	 * This function will add new file details in the database
	 * 1. Automatically update the StorageLeft
	 * 2. add File name, File size archival ID, 
	 */
	public void addNewFileDetails(String userName, String fileName, String filePath, String fileArchiveId, long fileZize){
		String query = "{userName:'"+userName+"'}";
		MongoDBDetails dbDetails = Mongo.collection.findOne(query).as(MongoDBDetails.class);
		dbDetails.addFile(fileName);
		int index = dbDetails.getFileIndex(fileName);
		dbDetails.addFilePath(index, filePath);
		dbDetails.addfileSize(index, fileZize);
		dbDetails.addFileUploadingDate(index, new Date().toGMTString());
		dbDetails.setTotalStorageLeft(dbDetails.getTotalStorageLeft() - fileZize);
		collection.update(query).merge(dbDetails);
		System.out.println("updated fields");
	}

public void recoverFile(String userName, String fileName){
		System.out.println("Inside Move to trash");
		String query = "{userName:'"+userName+"'}";
		MongoDBDetails dbDetails = Mongo.collection.findOne(query).as(MongoDBDetails.class);
		
		int trashIndex = dbDetails.getTrashFileIndex(fileName);
		
		if(dbDetails.getTrashCameFrom(trashIndex).equalsIgnoreCase("N")){  // copy file details to normal file list
			dbDetails.addFile(fileName);
			int fileIndex = dbDetails.getFileIndex(fileName);
			dbDetails.addfileSize(fileIndex, dbDetails.getTrashFileSize(trashIndex));
			dbDetails.addFileUploadingDate(fileIndex, new Date().toGMTString());
			long fileSize = dbDetails.getTrashFileSize(trashIndex);
			dbDetails.setTotalStorageLeft(dbDetails.getTotalStorageLeft() - fileSize);
		}
		else{ // copy file details to shared File list
			dbDetails.addSharedFileName(fileName);
			int sharedFileIndex = dbDetails.getSharedFileIndex(fileName);
			dbDetails.addSharedFileSize(sharedFileIndex, dbDetails.getTrashFileSize(trashIndex));
			dbDetails.addSharedFileDates(sharedFileIndex, new Date().toGMTString());
		}
		dbDetails.removeFromTrash(trashIndex);
		collection.update(query).merge(dbDetails);
	}


	public String registerUser(String userName, String password, String emailid){
		MongoDBDetails dbDetails = new MongoDBDetails();
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
		return dbDetails.getUserName();
	}
}