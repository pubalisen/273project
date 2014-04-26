package edu.sjsu.cmpe.dropbox.ui.resources;



import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import edu.sjsu.cmpe.dropbox.domain.MongoDBDetails;
import edu.sjsu.cmpe.dropbox.dto.MongoTest;
import edu.sjsu.cmpe.dropbox.ui.views.FilesForMustach;
import edu.sjsu.cmpe.dropbox.ui.views.SharedFileView;

@Path("/home/{username}/sharedfiles")
@Produces(MediaType.TEXT_HTML)
public class SharedFileResource  {
	MongoTest Mongo;

	public SharedFileResource(MongoTest Mongo) {
		this.Mongo=Mongo;
	}

	@GET
	@Path("/")
	public SharedFileView getHome(@PathParam("username") String username) {
		ArrayList<FilesForMustach> listOfFiles = new ArrayList<FilesForMustach>();
		listOfFiles = getList(username);
		return new SharedFileView(listOfFiles);
		}

	private ArrayList<FilesForMustach> getList(String username) {
		// Auto-generated method stub
		ArrayList<FilesForMustach> listOfFiles = new ArrayList<FilesForMustach>();
		List<String> files = Mongo.getSharedFileNames(username);
		List<String> dates =  Mongo.getSharedFileDates(username);
		int index=0;
		for(String file: files){
			FilesForMustach element = new FilesForMustach();
			element.setFile(file);
			listOfFiles.add(index, element);
			index++;		
		}
		index=0;
		int index1=0;
		for(String date: dates){
			listOfFiles.get(index1).setDate(date);
			index1++;
			}
		return listOfFiles;
	}
}

