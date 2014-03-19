package edu.sjsu.cmpe.dropbox.api.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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



@Path("/v1/files")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BucketResource {
    @GET
    @Timed(name = "view-file")
       public Response getFile() {
    	System.out.println("in first");
    	//String access_key="AKIAJGHI3W5AFQQM2VJQ";
    	//String secret_key="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";
		//AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
		
    	//AmazonS3 s3Client = new AmazonS3Client(credentials);
    	
    	AmazonS3 s3Client = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
  //  	System.out.println("Credentials");
//    	Region usWest1 = Region.getRegion(Regions.US_WEST_1);
//    	s3Client.setRegion(usWest1);
   // 	System.out.println("Client");
    	s3Client.setEndpoint("http://s3-us-west-1.amazonaws.com");
    //	System.out.println("Regions");
    	String bucketName = "cmpe273project";
   // 	System.out.println("Listing objects");
        ObjectListing objectListing = s3Client.listObjects(new ListObjectsRequest()
                .withBucketName(bucketName));
               
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            System.out.println(" - " + objectSummary.getKey() + "  " +
                               "(size = " + objectSummary.getSize() + ")");
        }
        System.out.println();
    	
    	
    	
        return Response.status(200).entity("All files displayed").build();
    	
    }

    @POST
    @Timed(name = "add-file")
       public Response addFile(NewFile request) {
    	AmazonS3 s3Client = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
      	s3Client.setEndpoint("http://s3-us-west-1.amazonaws.com");
      	String bucketName = "cmpe273project";

      	String key = NewFile.getName();
      	File file = new File("/host/ubuntu/project273/" + key);
      	//File file = new File("D:\\SP14\\CMPE 273\\" + key);
      	if(file.exists())
      	{
      		System.out.println("Yeayyyy file exists");
      		try {
    			s3Client.putObject(new PutObjectRequest(bucketName, key, file));
    			 return Response.status(200).entity("File Added").build();
    		} catch (AmazonServiceException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (AmazonClientException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 
      	}
      	
    	
        System.out.println();
        return Response.status(400).entity("File could not be Added").build();
       
    	
   	
    }

    @DELETE
 //   @Path("/v1/files")
    @Timed(name = "delete-file")
 //      public Response delFile(@PathParam("filename") String key) {
    	public Response delFile(NewFile request) {
    	AmazonS3 s3Client = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
      	s3Client.setEndpoint("http://s3-us-west-1.amazonaws.com");
      	String bucketName = "cmpe273project";

      	String key = NewFile.getName();
      	//File file = new File("/host/ubuntu/project273/" + key);
      	//File file = new File("D:\\SP14\\CMPE 273\\" + key);
      	
      		try {
      			System.out.println("Deleting an object\n");
      			s3Client.deleteObject(bucketName, key);
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
  /*  @PUT
    @Timed(name = "Upload-file")
    
       public Response uploadFile(NewFile request) {
    	
   // 	System.out.println("in put");
    	AmazonS3 s3Client = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
  //  	System.out.println("put Credentials");
 //   	Region usWest1 = Region.getRegion(Regions.US_WEST_1);
//    	s3Client.setRegion(usWest1);
   // 	System.out.println("put Client");
    	s3Client.setEndpoint("http://s3-us-west-1.amazonaws.com");
   // 	System.out.println("put Regions");
    	String bucketName = "cmpe273project";
    //    System.out.println();
        String key = NewFile.getName();
        try {
			s3Client.putObject(new PutObjectRequest(bucketName, key, createSampleFile()));
		} catch (AmazonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AmazonClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    
		return null;
    	
    }
    
    private static File createSampleFile() throws IOException {
        File file = File.createTempFile("aws-java-sdk-", ".txt");
        //file.deleteOnExit();
        
        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write("This is a New File\n");
        writer.write("Upload Success\n");
        writer.close();

        return file;
    }
*/
    
}

