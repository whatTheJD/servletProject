package de.openknowledge;


import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("")
@Produces("text/xml")
public class LoginResource {

    RepositoryInterface repository;

    public LoginResource(RepositoryInterface repository) {
        this.repository = repository;
    }

    @GET
    public List<Client> doGet() {
        return repository.findClient();
    }
}
