package com.packtpub.wflydevelopment.chapter7.boundary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.packtpub.wflydevelopment.chapter7.controller.TheatreBooker;

@Path("/seat")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class SeatsResource {

    @Inject
    private TheatreBooker theatreBooker;

    @Inject
    private TheatreBox theatreBox;
   
    @PostConstruct
    void init(){
    	System.out.println(theatreBox==null); 
    }

    @GET
    public Collection<SeatDto> getSeatList() {
    	List<String> list = new ArrayList<String>();
    	list.add("d");
    	list.add("f");
    	list.add("h");
    	System.out.println(theatreBox==null);
    	//list;//
        return theatreBox.getSeats().stream().map(SeatDto::fromSeat).collect(Collectors.toList());
    }

    @POST
    @Path("{id}")
    public Response bookPlace(@PathParam("id") int id) {
        try {
            theatreBooker.bookSeat(id);
            return Response.ok(SeatDto.fromSeat(theatreBox.getSeat(id))).build();
        } catch (Exception e) {
            final Entity<String> errorMessage = Entity.json(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
        }
    }
}
