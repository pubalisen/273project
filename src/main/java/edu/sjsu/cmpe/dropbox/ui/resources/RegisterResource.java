package edu.sjsu.cmpe.dropbox.ui.resources;



//import javax.ws.rs.GET;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.sjsu.cmpe.dropbox.ui.views.LoginView;
import edu.sjsu.cmpe.dropbox.ui.views.RegisterView;



@Path("/register")
@Produces(MediaType.TEXT_HTML)
public class RegisterResource {
	

	    public RegisterResource() {
		
	    }

	    @GET

	    public RegisterView getRegisteration() {

	    	return new RegisterView();

	    }
	
}


