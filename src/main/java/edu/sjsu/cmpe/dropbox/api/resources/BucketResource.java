package edu.sjsu.cmpe.dropbox.api.resources;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.CopyPartRequest;
import com.amazonaws.services.s3.model.CopyPartResult;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.ProgressEvent;
import com.amazonaws.services.s3.model.ProgressListener;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.ListVerifiedEmailAddressesResult;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.VerifyEmailAddressRequest;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.regions.Region;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.sjsu.cmpe.dropbox.config.dropboxServiceConfiguration;
import edu.sjsu.cmpe.dropbox.domain.AmazonCredentials;
import edu.sjsu.cmpe.dropbox.domain.NewFile;

import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.dropbox.dto.*;

import java.util.ArrayList;



@Path("/v1/files")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BucketResource {
	private MongoTest mongo;
	public BucketResource(MongoTest mongo){
		this.mongo = mongo;		
	}
	

    @GET
    @Path("/old/{existinguser}/download")
    @Timed(name = "download-file")
       public Response downloadFile(@QueryParam("filepath") String filePath, @QueryParam("fileName") String fileName,
   			@PathParam("existinguser") String existingUser) throws IOException {
    	AmazonCredentials myCredentials = new AmazonCredentials();
		AWSCredentials credentials = myCredentials.getCredentials();
		
    	System.out.println("Inside Download-file");
    	
		//test
    	AmazonS3 s3Client = new AmazonS3Client(credentials);
    	
    	String key = fileName;
      	s3Client.setEndpoint("http://s3-us-west-1.amazonaws.com");
      	String bucketName = mongo.getBucketName(existingUser);
      	 	S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, key));
        InputStream reader = new BufferedInputStream(object.getObjectContent());
      	File file = new File(filePath);      
      	OutputStream writer = new BufferedOutputStream(new FileOutputStream(file));

      		int read = -1;

      		while ( ( read = reader.read() ) != -1 ) {
      		    writer.write(read);
      		}

      		writer.flush();
      		writer.close();
      		reader.close();

        return Response.status(200).entity("All files displayed").build();
    	
    }
    
    @GET
	@Path("/old/{existinguser}/share/download")
	@Timed(name = "download-shared-file")
    public Response downloadSharedFile(@QueryParam("filePath") String filePath, @QueryParam("fileName") String fileName,
   			@PathParam("existinguser") String existingUser) throws IOException {
    	AmazonCredentials myCredentials = new AmazonCredentials();
		AWSCredentials credentials = myCredentials.getCredentials();
    	System.out.println("Inside download-share-file");
    	
		//test
    	AmazonS3 s3Client = new AmazonS3Client(credentials);
    	System.out.println("FILEpATH : "+ filePath);
    	
    	System.out.println("fileName : "+ fileName);
    	String key = fileName;
    	  System.out.println("key : "+ key);
      	s3Client.setEndpoint("http://s3-us-west-1.amazonaws.com");
      	String bucketName = mongo.getBucketName(existingUser);
      	S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, key));
        InputStream reader = new BufferedInputStream(object.getObjectContent());
        System.out.println("filepath : " + filePath);
      	File file = new File(filePath);      
      	OutputStream writer = new BufferedOutputStream(new FileOutputStream(file));

      		int read = -1;

      		while ( ( read = reader.read() ) != -1 ) {
      		    writer.write(read);
      		}

      		writer.flush();
      		writer.close();
      		reader.close();

        return Response.status(200).entity("File Downloaded").build();
    	
    }
    
    
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
       public Response uploadFile(@PathParam("existinguser") String existingUser) throws Throwable{
    	System.out.print("User" +existingUser);
    	AmazonCredentials myCredentials = new AmazonCredentials();
		AWSCredentials credentials = myCredentials.getCredentials();
    	System.out.println("Inside upload-file");

    	AmazonS3 s3Client = new AmazonS3Client(credentials);

 
      	s3Client.setEndpoint("http://s3-us-west-1.amazonaws.com");
     
      	     
      	String bucketName = mongo.getBucketName(existingUser);
      	
      	
      	String Name = existingUser;
    	System.out.print("Name is in resource.java is" + Name);
    	System.out.print("\n");
      	
      	String bucketName1 = mongo.getBucketName(existingUser);
      
      	String Key= null;
      	//String key = getFileNameFromPath(filePath);

      	//long fileSize = getFileSize(filePath);

      		try {
      			long storageBefore = mongo.getUserStorage(existingUser);
      			System.out.println("Storage before "+storageBefore);
    			S3TransferProgress uploader = new S3TransferProgress(Name, s3Client, mongo);
    			Key = uploader.getfileName();
    			 Long fileSize = uploader.getfileSize();
    			 long storageAfter = mongo.getUserStorage(existingUser);
    			 System.out.println("Storage After "+storageAfter);
    		//	 if(storageBefore == storageAfter )
    		//		 return Response.status(400).entity("File could not be added due to size restriction").build();
    			 System.out.println("Hellllloooooooooo Key " + Key + "size" + fileSize);
    			 return Response.status(200).entity("File Added").build();
    			 
    		} catch (AmazonServiceException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (AmazonClientException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 

      	
    	
        System.out.println();
        return Response.status(400).entity("File could not be Added").build();
   	
    }
   //This method copies file from one bucket to other 
    void copyFileFromBucket(String existinguser, String sharewith, String fileName){
    	AmazonCredentials myCredentials = new AmazonCredentials();
		AWSCredentials credentials = myCredentials.getCredentials();
    	System.out.println("Inside copy-file");
    	
		
    	AmazonS3 s3Client = new AmazonS3Client(credentials);

 
      	s3Client.setEndpoint("http://s3-us-west-1.amazonaws.com");
         
         // List to store copy part responses.
      	String targetObjectKey = fileName;
      	String targetBucketName = sharewith;
      	String sourceBucketName = existinguser;
      	String sourceObjectKey  = fileName;

         List<CopyPartResult> copyResponses =
                   new ArrayList<CopyPartResult>();
                           
         InitiateMultipartUploadRequest initiateRequest = 
         	new InitiateMultipartUploadRequest(targetBucketName, targetObjectKey);
         
         InitiateMultipartUploadResult initResult = 
         	s3Client.initiateMultipartUpload(initiateRequest);

         try {
             // Get object size.
             GetObjectMetadataRequest metadataRequest = 
             	new GetObjectMetadataRequest(sourceBucketName, sourceObjectKey);

             ObjectMetadata metadataResult = s3Client.getObjectMetadata(metadataRequest);
             long objectSize = metadataResult.getContentLength(); // in bytes

             // Copy parts.
             long partSize = 5 * (long)Math.pow(2.0, 20.0); // 5 MB

             long bytePosition = 0;
             for (int i = 1; bytePosition < objectSize; i++)
             {
             	CopyPartRequest copyRequest = new CopyPartRequest()
                    .withDestinationBucketName(targetBucketName)
                    .withDestinationKey(targetObjectKey)
                    .withSourceBucketName(sourceBucketName)
                    .withSourceKey(sourceObjectKey)
                    .withUploadId(initResult.getUploadId())
                    .withFirstByte(bytePosition)
                    .withLastByte(bytePosition + partSize -1 >= objectSize ? objectSize - 1 : bytePosition + partSize - 1) 
                    .withPartNumber(i);

                 copyResponses.add(s3Client.copyPart(copyRequest));
                 bytePosition += partSize;

             }
             CompleteMultipartUploadRequest completeRequest = new 
             	CompleteMultipartUploadRequest(
             			targetBucketName,
             			targetObjectKey,
             			initResult.getUploadId(),
             			GetETags(copyResponses));

             CompleteMultipartUploadResult completeUploadResponse =
                 s3Client.completeMultipartUpload(completeRequest);
         } catch (Exception e) {
         	System.out.println(e.getMessage());
         }
      }
      
  
    

// Helper function that constructs ETags.
static List<PartETag> GetETags(List<CopyPartResult> responses)
{
    List<PartETag> etags = new ArrayList<PartETag>();
    for (CopyPartResult response : responses)
    {
        etags.add(new PartETag(response.getPartNumber(), response.getETag()));
    }
    return etags;
} 
    @POST
	@Path("/old/{existinguser}/share")
	@Timed(name = "share-file")
	public Response shareFile(@PathParam("existinguser") String existingUser,
			@QueryParam("filename") String fileName,
			@QueryParam("sharewith") String sharewith) throws UnknownHostException {
    	AmazonCredentials myCredentials = new AmazonCredentials();
		AWSCredentials credentials = myCredentials.getCredentials();
		if (mongo.isUserNameExist(sharewith)) {
			mongo.shareFile(existingUser, sharewith, fileName);
			copyFileFromBucket(existingUser, sharewith, fileName);
			/*started changing here*/
			String emailId = mongo.getUserEmail(sharewith);
			sentEmail(credentials, existingUser, sharewith,emailId,fileName);
			/*end my changes*/
			return Response.status(200).build();
		} else {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity("Sorry we couldn't find the username you have provided")
					.build();
		}
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
    
    @DELETE
	@Path("/old/{existinguser}/share/delete")
	@Timed(name = "delete-shared-file")
	public Response deleteSharedFile(
			@PathParam("existinguser") String existingUser,
			@QueryParam("fileName") String fileName) {
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
          mongo.deleteSharedFileDetails(existingUser, fileName);
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
    
    public void sentEmail(AWSCredentials credentials,String UserName, String shareUser, String newUserEmailID, String filename){

		String FROM = "cmpe273.maverick@gmail.com";
	//	String FROM = "success@simulator.amazonses.com";
		String TO = newUserEmailID;
		String SUBJECT = "Welcome to CMPE273 Dropbox project";
		String BODY = "Your friend : " + UserName+ " has shared a file : " +filename+" with you:\n";
	
	//	credentials.getAWSAccessKeyId();
	//	credentials.getAWSSecretKey();
		Destination destination = new Destination()
				.withToAddresses(new String[] { TO });
		Content subject = new Content().withData(SUBJECT);
		Content textBody = new Content().withData(BODY);
		Body body = new Body().withText(textBody);
		Message message = new Message().withSubject(subject).withBody(body);
		SendEmailRequest request = new SendEmailRequest().withSource(FROM)
				.withDestination(destination).withMessage(message);
	//	SendEmailRequest request = new SendEmailRequest(FROM, TO, message);
		try {
			System.out
					.println("Attempting to send an email through AmazonSES...");
			System.out.println(credentials);
			System.out.println(request);
			
			
			AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(
					credentials);
			 Region REGION = Region.getRegion(Regions.US_EAST_1);
	            client.setRegion(REGION);
	            verifyEmailAddress(client, FROM);
			client.sendEmail(request);
			System.out.println("Email sent!");
		} catch (Exception ex) {
			System.out.println("The email was not sent.");
			ex.printStackTrace();
			System.out.println("Error message: " + ex.getMessage());
		}
		
	   
	}
		
/**
 * Sends a request to Amazon Simple Email Service to verify the specified
 * email address. This triggers a verification email, which will contain a
 * link that you can click on to complete the verification process.
 *
 * @param ses
 *            The Amazon Simple Email Service client to use when making
 *            requests to Amazon SES.
 * @param address
 *            The email address to verify.
 */
private static void verifyEmailAddress(AmazonSimpleEmailService ses, String address) {
    ListVerifiedEmailAddressesResult verifiedEmails = ses.listVerifiedEmailAddresses();
    System.out.println("List of verified address " + verifiedEmails);
    if (verifiedEmails.getVerifiedEmailAddresses().contains(address)) return;

    ses.verifyEmailAddress(new VerifyEmailAddressRequest().withEmailAddress(address));
    System.out.println("Please check the email address " + address + " to verify it");
    System.exit(0);
}
    
    
}

