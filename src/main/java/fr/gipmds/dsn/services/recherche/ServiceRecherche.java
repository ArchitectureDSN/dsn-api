package fr.gipmds.dsn.services.recherche;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

public interface ServiceRecherche {

    public Response listerRetours(
            @HeaderParam("Accept-Encoding") String headerAcceptEncoding,
            @HeaderParam("Authorization") String headerAuthorization,
            @PathParam("idflux") String idFlux);
}