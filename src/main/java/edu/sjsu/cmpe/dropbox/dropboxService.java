package edu.sjsu.cmpe.dropbox;

import java.util.concurrent.ConcurrentHashMap;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;


import edu.sjsu.cmpe.dropbox.api.resources.BucketResource;
import edu.sjsu.cmpe.dropbox.api.resources.RootResource;
import edu.sjsu.cmpe.dropbox.config.dropboxServiceConfiguration;
//import edu.sjsu.cmpe.dropbox.domain.Bucket;
public class dropboxService extends Service<dropboxServiceConfiguration> {

    public static void main(String[] args) throws Exception {
	new dropboxService().run(args);
    }

    @Override
    public void initialize(Bootstrap<dropboxServiceConfiguration> bootstrap) {
	bootstrap.setName("dropbox-service");
    }

    @Override
    public void run(dropboxServiceConfiguration configuration,
	    Environment environment) throws Exception {
	/** Root API */
	environment.addResource(RootResource.class);
	/** Add new resources here */
	environment.addResource(new BucketResource());
    }
}
