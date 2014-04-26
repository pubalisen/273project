package edu.sjsu.cmpe.dropbox.ui.resources;

//import javax.ws.rs.GET;
import javax.ws.rs.GET;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import edu.sjsu.cmpe.dropbox.ui.views.LoginView;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class LoginResource {
	 //private final DocumentRepository documentRepository;

	    public LoginResource() {
		//this.documentRepository=documentRepository;
	    }

	    @GET
	    public LoginView getHome() {

	    	return new LoginView();

	    }
}


