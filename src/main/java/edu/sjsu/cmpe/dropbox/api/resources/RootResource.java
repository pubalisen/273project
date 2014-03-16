package edu.sjsu.cmpe.dropbox.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.dropbox.dto.LinkDto;
import edu.sjsu.cmpe.dropbox.dto.LinksDto;

@Path("/v1/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RootResource {

    public RootResource() {
	// do nothing
    }

    @GET
    @Timed(name = "get-root")
    public Response getRoot() {
	//LinksDto links = new LinksDto();
	System.out.print("You are in Root");

	return Response.status(200).build();
    }
}

