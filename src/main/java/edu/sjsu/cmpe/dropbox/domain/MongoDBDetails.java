/**
 * 
 */
package edu.sjsu.cmpe.dropbox.domain;

import java.io.File;
import java.nio.file.spi.FileTypeDetector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.jongo.Find;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Team_Projections This class contains every field related to database
 *         storage Purpose of this class: When we will use Jongo to store into
 *         MongoDB, obejct of this class will be created and then stored in the
 *         mongoDB
 */
public class MongoDBDetails {

	private String userId;

	// this should be unique
	private String userName;

	private String password;

	private List<String> listOfFiles = new ArrayList<String>();

	private List<Long> fileSize = new ArrayList<Long>();

	private List<String> listOfDate = new ArrayList<String>();

	private List<String> filePaths = new ArrayList<String>();

	private String bucketName;

	private String role;

	private long TotalStorage;

	private long TotalStorageLeft;

	private String emailid;

	@JsonIgnore
	private int random = 0;

	private List<String> sharedbucketNames = new ArrayList<String>();

	private List<String> sharedFileNames = new ArrayList<String>();

	private List<String> sharedArchiveIDs = new ArrayList<String>();

	private List<Long> sharedFileSizes = new ArrayList<Long>();

	private List<String> sharedFileDates = new ArrayList<String>();

	private List<String> trashListOfFiles = new ArrayList<String>();

	private List<String> trashbucketNames = new ArrayList<String>();

	private List<Long> trashFileSizes = new ArrayList<Long>();

	private List<String> trashListOfDates = new ArrayList<String>();

	private List<String> trashCameFrom = new ArrayList<String>();

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
		this.bucketName = userName;
	}

	public void setUserNameWithNewbucketName(String username) {
		random++;
		this.userName = username + random;
		this.bucketName = username + random;
		// System.out.println("new user name is :"+);
	}

	/**
	 * @return the listOfFiles
	 */
	public List<String> getListOfFiles() {
		return listOfFiles;
	}

	/**
	 * @param listOfFiles
	 *            the listOfFiles to set
	 */
	public void setListOfFiles(List<String> listOfFiles) {
		this.listOfFiles = listOfFiles;
	}

	/**
	 * @return the bucketName
	 */
	public String getbucketName() {
		return bucketName;
	}

	/**
	 * @param bucketName
	 *            the bucketName to set
	 */
	public void setbucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(String role) {
		this.role = role;
		if (role.equalsIgnoreCase("manager")) {
			this.TotalStorage = 5120;
			this.TotalStorageLeft = 5120;
		} else {
			this.TotalStorage = 1024;
		}
	}

	/**
	 * @return the totalStorage
	 */
	public long getTotalStorage() {
		return TotalStorage;
	}

	/**
	 * @param totalStorage
	 *            the totalStorage to set
	 */
	public void setTotalStorage(long totalStorage) {
		TotalStorage = totalStorage;
	}

	/**
	 * @return the fileSize
	 */
	public List<Long> getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize
	 *            the fileSize to set
	 */
	public void setFileSize(List<Long> fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the filePaths
	 */
	public List<String> getFilePaths() {
		return filePaths;
	}

	/**
	 * @param filePaths
	 *            the filePaths to set
	 */
	public void setFilePaths(List<String> filePaths) {
		this.filePaths = filePaths;
	}

	/**
	 * @param fileName
	 *            add file to the list
	 */
	public void addFile(String fileName) {
		listOfFiles.add(fileName);
	}

	
	public List<String> getListOfDate() {
		return listOfDate;
	}

	public void setListOfDate(List<String> listOfDate) {
		this.listOfDate = listOfDate;
	}

	/**
	 * @param size
	 *            remove file size from the list
	 */
	public void removefileSize(int index) {
		fileSize.remove(index);
	}

		
	/* @param filePath
	 *            remove file path from the list
	 */
	public void removeFilePath(int index) {
		filePaths.remove(index);
	}

	/**
	 * @param index
	 *            remove file uploading date from the list
	 */
	public void removeFileUploadingDate(int index) {
		listOfDate.remove(index);
	}

	public void removeFileFromList(int index) {
		listOfFiles.remove(index);
	}

	public long getFileSizeFile(int index) {
		// TODO Auto-generated method stub
		return fileSize.get(index);
	}

	/**
	 * @return the sharedbucketNames
	 */
	public List<String> getSharedbucketNames() {
		return sharedbucketNames;
	}

	/**
	 * @param sharedbucketNames
	 *            the sharedbucketNames to set
	 */
	public void setSharedbucketNames(List<String> sharedbucketNames) {
		this.sharedbucketNames = sharedbucketNames;
	}

	/**
	 * @return the sharedFileNames
	 */
	public List<String> getSharedFileNames() {
		return sharedFileNames;
	}

	/**
	 * @param sharedFileNames
	 *            the sharedFileNames to set
	 */
	public void setSharedFileNames(List<String> sharedFileNames) {
		this.sharedFileNames = sharedFileNames;
	}

	/**
	 * @return the sharedArchiveIDs
	 */
	public List<String> getSharedArchiveIDs() {
		return sharedArchiveIDs;
	}

	/**
	 * @param sharedArchiveIDs
	 *            the sharedArchiveIDs to set
	 */
	public void setSharedArchiveIDs(List<String> sharedArchiveIDs) {
		this.sharedArchiveIDs = sharedArchiveIDs;
	}

	public void addSharedbucketName(int index, String bucketName) {
		sharedbucketNames.add(index, bucketName);
	}

	public void addSharedFileName(String fileName) {
		sharedFileNames.add(fileName);
	}

	public void addSharedArchiveId(int index, String ArchiveId) {
		sharedArchiveIDs.add(index, ArchiveId);
	}

	public String getSharedFilebucketName(int index) {
		return sharedbucketNames.get(index);
	}

	public String getSharedFileArchivalId(int index) {
		return sharedArchiveIDs.get(index);
	}

	public long getSharedFileSize(int index) {
		return sharedFileSizes.get(index);
	}

	/**
	 * @return the sharedFileSizes
	 */
	public List<Long> getSharedFileSizes() {
		return sharedFileSizes;
	}

	/**
	 * @param sharedFileSizes
	 *            the sharedFileSizes to set
	 */
	public void setSharedFileSizes(List<Long> sharedFileSizes) {
		this.sharedFileSizes = sharedFileSizes;
	}

	public void addSharedFileSize(int index, Long fileSize) {
		sharedFileSizes.add(index, fileSize);
	}

	public void addSharedFileDates(int index, String date) {
		sharedFileDates.add(index, date);
	}

	public long getFileSizeByFileName(String fileName) {
		int index = listOfFiles.indexOf(fileName);
		return fileSize.get(index);
	}

	public void removeSharedFileDetails(int index) {
		sharedArchiveIDs.remove(index);
		sharedFileNames.remove(index);
		sharedFileSizes.remove(index);
		sharedbucketNames.remove(index);
		sharedFileDates.remove(index);
	}

	/**
	 * @return the trashListOfFiles
	 */
	public List<String> getTrashListOfFiles() {
		return trashListOfFiles;
	}

	/**
	 * @param trashListOfFiles
	 *            the trashListOfFiles to set
	 */
	public void setTrashListOfFiles(List<String> trashListOfFiles) {
		this.trashListOfFiles = trashListOfFiles;
	}

	/**
	 * @return the trashFileSizes
	 */
	public List<Long> getTrashFileSizes() {
		return trashFileSizes;
	}

	/**
	 * @param trashFileSizes
	 *            the trashFileSizes to set
	 */
	public void setTrashFileSizes(List<Long> trashFileSizes) {
		this.trashFileSizes = trashFileSizes;
	}

	/**
	 * @return the trashListOfDates
	 */
	public List<String> getTrashListOfDates() {
		return trashListOfDates;
	}

	/**
	 * @param trashListOfDates
	 *            the trashListOfDates to set
	 */
	public void setTrashListOfDates(List<String> trashListOfDates) {
		this.trashListOfDates = trashListOfDates;
	}

	public void addTrashListOfFiles(String fileName) {
		trashListOfFiles.add(fileName);
	}

	public void addTrashFileSizes(int index, long fileSize) {
		trashFileSizes.add(index, fileSize);
	}

	public String getTrashFilebucketName(int index) {
		return trashbucketNames.get(index);
	}

	public int getTrashFileIndex(String fileName) {
		return trashListOfFiles.indexOf(fileName);
	}

	public String getTrashCameFrom(int index) {
		return trashCameFrom.get(index);
	}

	public void addTrashbucketName(int index, String bucketName) {
		trashbucketNames.add(index, bucketName);
	}

	public void addTrashFileDeletionDate(int index, String date) {
		trashListOfDates.add(index, date);
	}

	public void addTrashCameFrom(int index, String status) {
		trashCameFrom.add(index, status);
	}

	/**
	 * @return the trashbucketNames
	 */
	public List<String> getTrashbucketNames() {
		return trashbucketNames;
	}

	/**
	 * @param trashbucketNames
	 *            the trashbucketNames to set
	 */
	public void setTrashbucketNames(List<String> trashbucketNames) {
		this.trashbucketNames = trashbucketNames;
	}

	/**
	 * @return the trashCameFrom
	 */
	public List<String> getTrashCameFrom() {
		return trashCameFrom;
	}

	/**
	 * @param trashCameFrom
	 *            the trashCameFrom to set
	 */
	public void setTrashCameFrom(List<String> trashCameFrom) {
		this.trashCameFrom = trashCameFrom;
	}

	/**
	 * @return the sharedFileDates
	 */
	public List<String> getSharedFileDates() {
		return sharedFileDates;
	}

	/**
	 * @param sharedFileDates
	 *            the sharedFileDates to set
	 */
	public void setSharedFileDates(List<String> sharedFileDates) {
		this.sharedFileDates = sharedFileDates;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the emailid
	 */
	public String getEmailid() {
		return emailid;
	}

	/**
	 * @param emailid
	 *            the emailid to set
	 */
	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public int getFileIndex(String fileName) {
		int index = listOfFiles.indexOf(fileName);
		return index;
	}

	/**
	 * @param filePath
	 *            add file path to the list
	 */
	public void addFilePath(int index, String filePath) {
		filePaths.add(index, filePath);
	}

	/**
	 * @param size
	 *            add file size in the list
	 */
	public void addfileSize(int index, long size) {
		fileSize.add(index, size);
	}

	public void addFileUploadingDate(int index, String string) {
		listOfDate.add(index, string);
	}

	/**
	 * @return the totalStorageLeft
	 */
	public long getTotalStorageLeft() {
		return TotalStorageLeft;
	}

	/**
	 * @param totalStorageLeft
	 *            the totalStorageLeft to set
	 */
	public void setTotalStorageLeft(long totalStorageLeft) {
		TotalStorageLeft = totalStorageLeft;
	}

	public int getSharedFileIndex(String fileName) {
		return sharedFileNames.indexOf(fileName);
	}

	public void removeFromTrash(int index) {
		trashCameFrom.remove(index);
		trashFileSizes.remove(index);
		trashListOfDates.remove(index);
		trashListOfFiles.remove(index);
		trashbucketNames.remove(index);
	}

	public long getTrashFileSize(int index){
		return trashFileSizes.get(index);
	}
}