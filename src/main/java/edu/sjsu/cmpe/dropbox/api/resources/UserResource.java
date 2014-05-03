/**
 * 
 */
package edu.sjsu.cmpe.dropbox.api.resources;

import java.net.UnknownHostException;
import java.util.Properties;


//import javax.mail.Message;
/*import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;*/

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AWSJavaMailTransport;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.ListVerifiedEmailAddressesResult;
import com.amazonaws.services.simpleemail.model.VerifyEmailAddressRequest;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;




import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import edu.sjsu.cmpe.dropbox.domain.AmazonCredentials;
import edu.sjsu.cmpe.dropbox.domain.UserDetail;
import edu.sjsu.cmpe.dropbox.dto.MongoTest;

import com.yammer.metrics.annotation.Timed;

/**
 * @author Team Projections
 * 
 */

@Path("v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

	private MongoTest Mongo;

	private String bucketName;

	public UserResource(MongoTest Mongo) {
		// TODO Auto-generated constructor stub
		this.Mongo = Mongo;
	}

	@POST
	@Path("/newuser")
	@Timed(name = "new-user-new-bucket")
	public Response newUser(@Valid UserDetail newUser) throws Exception {
		
		System.out.println("Inside POST in UserResource");
		System.out.println("user - " + newUser.getUsername());
		System.out.println("Password - " + newUser.getPassword());
		System.out.println("Email - " + newUser.getEmailid());
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
	public Response loginUser(@Valid UserDetail newUser) throws UnknownHostException {
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

		String FROM = "cmpe273.maverick@gmail.com";
	//	String FROM = "success@simulator.amazonses.com";
		String TO = newUserEmailID;
		String SUBJECT = "Welcome to CMPE273 Dropbox project";
		String BODY = "Email Confirmation.\n\nThank you for registering with us.\n\nYour credentials are:\n"
				+     "Username is : "+newUserName+"\nPassword is : " +newUserPassword;

		
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
