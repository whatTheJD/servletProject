package de.openknowledge;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginResource {

    Repository repository;

    public LoginResource(Repository repository) {
        this.repository = repository;
    }

    //Todo Füge Exceptionhandling hinzu

    @GET
    @Path("clients")
    public List<Client> getClients() {
        List<Client> clientList = repository.readDb();
        return clientList;
    }

    @POST
    @Path("clients")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createClient(Client client){
        repository.writeDb(client);
        return Response.ok(client).build();
    }
}
