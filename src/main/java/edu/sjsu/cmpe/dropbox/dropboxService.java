package edu.sjsu.cmpe.dropbox;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.views.ViewBundle;

import de.spinscale.dropwizard.jobs.JobsBundle;
import edu.sjsu.cmpe.dropbox.api.resources.BucketResource;
import edu.sjsu.cmpe.dropbox.api.resources.RootResource;
import edu.sjsu.cmpe.dropbox.api.resources.UserResource;
import edu.sjsu.cmpe.dropbox.config.MongoConfig;
import edu.sjsu.cmpe.dropbox.config.dropboxServiceConfiguration;
import edu.sjsu.cmpe.dropbox.domain.MongoDBDetails;
import edu.sjsu.cmpe.dropbox.dto.MongoTest;
import edu.sjsu.cmpe.dropbox.ui.resources.DeletedFileResource;
import edu.sjsu.cmpe.dropbox.ui.resources.HomeResource;
import edu.sjsu.cmpe.dropbox.ui.resources.LoginResource;
import edu.sjsu.cmpe.dropbox.ui.resources.RegisterResource;
import edu.sjsu.cmpe.dropbox.ui.resources.SharedFileResource;
//import edu.sjsu.cmpe.dropbox.domain.Bucket;
public class dropboxService extends Service<dropboxServiceConfiguration> {

    public static void main(String[] args) throws Exception {
	new dropboxService().run(args);
    }

    @Override
    public void initialize(Bootstrap<dropboxServiceConfiguration> bootstrap) {
	bootstrap.setName("dropbox-service");
	bootstrap.addBundle(new ViewBundle());
	bootstrap.addBundle(new AssetsBundle());
	bootstrap.addBundle(new JobsBundle("edu.sjsu.cmpe.dropbox"));
    }

    @Override
    public void run(dropboxServiceConfiguration configuration,
	    Environment environment) throws Exception {
	/** Root API */
    	
	environment.addResource(RootResource.class);
	/** Add new resources here */
	
	MongoConfig.setAmazonTimeZone(configuration.getAmazonTimeZone());
//	MongoConfig.setAmazonUsername("XXXXXXXXXXXXXXXXXXXXXXXx");
//	MongoConfig.setAmazonPassword("XXXXXXXXXXXXXXXXXXXXXXXX");
//	MongoConfig.setDatabaseAddress("XXXXXXXXXXXX");
//	MongoConfig.setDatabasePassword("XXXXXX");
//	MongoConfig.setDatabasePort(XXXXX);
	MongoConfig.setDatabaseUsername("cmpe273");
	MongoConfig.setDbCollection("dropboxdetails");
	MongoConfig.setDbName("dropbox");
	// Added root resource - kept nothing as of now.
	MongoTest Mongo = new MongoTest();
	environment.addResource(RootResource.class);
	MongoDBDetails MongoDBDetails=new MongoDBDetails();
	// Added Document resource API to handle file uploading
		environment.addResource(new UserResource(Mongo));
		environment.addResource(new BucketResource(Mongo));
	environment.addResource(new LoginResource());
	environment.addResource(new HomeResource(Mongo,MongoDBDetails));
	environment.addResource(new DeletedFileResource(Mongo));
	environment.addResource(new SharedFileResource(Mongo));
	environment.addResource(new RegisterResource());
    }
}
