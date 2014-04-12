/**
 * 
 */
package edu.sjsu.cmpe.dropbox.api.resources;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import edu.sjsu.cmpe.dropbox.domain.AmazonCredentials;
import edu.sjsu.cmpe.dropbox.domain.UserDetail;
import edu.sjsu.cmpe.dropbox.dto.Mongo;

import com.yammer.metrics.annotation.Timed;

/**
 * @author Team Projections
 * 
 */

@Path("v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

	private Mongo Mongo;

	private String bucketName;

	public UserResource(Mongo Mongo) {
		// TODO Auto-generated constructor stub
		this.Mongo = Mongo;
	}

	@POST
	@Path("/newuser")
	@Timed(name = "new-user-new-bucket")
	public Response newUser(@Valid UserDetail newUser) throws Exception {
		String newUserName = Mongo.registerUser(newUser.getUsername(),
				newUser.getPassword(), newUser.getEmailid());

		String newUserPassword = newUser.getPassword();
		String newUserEmailID = newUser.getEmailid();

		System.out.println("Creating new bucket for new user");
		this.bucketName = Mongo.getBucketName(newUserName);
		AmazonCredentials common = new AmazonCredentials();
		AWSCredentials credentials = common.getCredentials();
		AmazonS3Client client = common.getClient(credentials);
		Bucket bucketresult = common.createNewBucket(this.bucketName, client);
		if (newUserName != newUser.getUsername()) {
			String responseMessage = "Your username will be : " + newUserName;
			System.out.println(newUserName);
			sentEmail(credentials,newUserName, newUserPassword,newUserEmailID);
			return Response.status(200).entity(responseMessage).build();
		} else {
			String responceMessage = "Your username will be : " + newUserName;
			sentEmail(credentials,newUserName, newUserPassword, newUserEmailID);
			return Response.status(200).entity(responceMessage).build();
		}

	}

	@POST
	@Path("/olduser")
	@Timed(name = "Login")
	public Response loginUser(@Valid UserDetail newUser) {
		String existingUser = newUser.getUsername();
		System.out.println("usename:" + newUser.getUsername());
		System.out.println("password:" + newUser.getPassword());
		if (Mongo.isUserNameExist(existingUser) == true) {
			if (Mongo.authenticateUser(newUser.getUsername(),
					newUser.getPassword())) {
				return Response.ok(200).build();
			} else {
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Please verify your username and password")
						.build();
			}
		} else {
			return Response
					.status(Response.Status.UNAUTHORIZED)
					.entity("Sorry!! This username doesn't exists in our system. Please sign up as new user")
					.build();
		}
	}

	public void sentEmail(AWSCredentials credentials,String newUserName, String newUserPassword, String newUserEmailID){

		String FROM = "pooja.indi@gmail.com";
		String TO = newUserEmailID;
		String SUBJECT = "Welcome to Amazon S3";
		String BODY = "Email Confirmation.\n\nThank you for registering with us.\n\nYour credentials are:\n"
				+     "Username is :: "+newUserName+"\nPassword is :: " +newUserPassword;

		
		credentials.getAWSAccessKeyId();
		credentials.getAWSSecretKey();
		Destination destination = new Destination()
				.withToAddresses(new String[] { TO });
		Content subject = new Content().withData(SUBJECT);
		Content textBody = new Content().withData(BODY);
		Body body = new Body().withText(textBody);
		Message message = new Message().withSubject(subject).withBody(body);
		SendEmailRequest request = new SendEmailRequest().withSource(FROM)
				.withDestination(destination).withMessage(message);
		try {
			System.out
					.println("Attempting to send an email through AmazonSES...");
			AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(
					credentials);
			client.sendEmail(request);
			System.out.println("Email sent!");
		} catch (Exception ex) {
			System.out.println("The email was not sent.");
			System.out.println("Error message: " + ex.getMessage());
		}
	}
		
	}
