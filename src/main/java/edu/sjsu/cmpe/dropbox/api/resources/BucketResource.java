package edu.sjsu.cmpe.dropbox.api.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.UUID;







import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

//import com.amazonaws.regions.Regions;
//import com.amazonaws.regions.Region;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;






import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
//import javax.ws.rs.core.CacheControl;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.sjsu.cmpe.dropbox.config.dropboxServiceConfiguration;
import edu.sjsu.cmpe.dropbox.domain.AmazonCredentials;
import edu.sjsu.cmpe.dropbox.domain.NewFile;

//import javax.ws.rs.core.Response.ResponseBuilder;
//import javax.ws.rs.core.Request;

//import javax.ws.rs.core.UriInfo;









//import com.sun.research.ws.wadl.Request;
import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;





//import edu.sjsu.cmpe.dropbox.domain.BucketDetails;
import edu.sjsu.cmpe.dropbox.dto.*;

import java.util.ArrayList;

   
    private String getFileNameFromPath(String filePath) {
		// TODO Auto-generated method stub
		String fileName=null;
		String newstr=filePath;
		System.out.println(filePath);
		String[] str=newstr.split("\\\\");
		for (String string : str) {
			fileName=string;
		//	System.out.println(string);
		}
		System.out.println("update for "+fileName);
		//compute the fileName
		return(fileName);
	}
    
    private long getFileSize(String filePath) {
		// TODO Auto-generated method stub
		File file = new File(filePath);
		long fileSize = file.length() / 1048786;
		System.out.println("File size is+"+fileSize);
		return fileSize;
	}

    @POST
    @Path("/old/{existinguser}/upload")
    @Timed(name = "upload-file")
       public Response uploadFile(@PathParam("existinguser") String existingUser, @QueryParam("filepath") String filePath) throws IOException{
    	AmazonCredentials myCredentials = new AmazonCredentials();
		AWSCredentials credentials = myCredentials.getCredentials();
    	System.out.println("Inside upload-file");
    	
		
    	AmazonS3 s3Client = new AmazonS3Client(credentials);

 
      	s3Client.setEndpoint("http://s3-us-west-1.amazonaws.com");
    //    String bucketName = existingUser;
      	String bucketName = mongo.getBucketName(existingUser);
      	String key = getFileNameFromPath(filePath);
      	
      	File file = new File("D:\\S3files\\" + key);
      	long fileSize = getFileSize(filePath);
      //	File file = new File(filePath);
      	System.out.println(key);
      	if(file.exists())
      	{
      	System.out.println("Yeayyyy file exists");
      		try {
    			s3Client.putObject(new PutObjectRequest(bucketName, key, file));
    			mongo.addNewFileDetails(existingUser, key, filePath, fileSize);
    			 return Response.status(200).entity("File Added").build();
    		} catch (AmazonServiceException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (AmazonClientException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 
      	}
      	else
      		System.out.println("No not found");
      	
    	
        System.out.println();
        return Response.status(400).entity("File could not be Added").build();
       
    	
   	
    }

    @DELETE
    @Path("/old/{existinguser}/delete")
    @Timed(name = "delete-file")
 //      public Response delFile(@PathParam("filename") String key) {
    public Response deleteFile(@PathParam("existinguser") String existingUser, @QueryParam("filename") String fileName){
    //	AmazonS3 s3Client = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
   
    AmazonCredentials myCredentials = new AmazonCredentials();
AWSCredentials credentials = myCredentials.getCredentials();
    System.out.println("Inside upload-file");
   
    AmazonS3 s3Client = new AmazonS3Client(credentials);

 
      s3Client.setEndpoint("http://s3-us-west-1.amazonaws.com");
    //    String bucketName = existingUser;
      String bucketName = mongo.getBucketName(existingUser);
      String key = fileName;
      System.out.println(key);
     
      try {
      System.out.println("Deleting an object\n");
      s3Client.deleteObject(bucketName, key);
      mongo.deleteFileDetails(existingUser, fileName);
      return Response.status(200).entity("File Deleted").build();
    } catch (AmazonServiceException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
    } catch (AmazonClientException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
    } 
      
        
   
        System.out.println();
        return Response.status(400).entity("File could not be deleted").build();
   
   	
    }
    
}

