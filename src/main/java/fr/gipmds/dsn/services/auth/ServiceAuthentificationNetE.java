package fr.gipmds.dsn.services.auth;

import fr.gipmds.dsn.modeles.RequeteAuthentificationNetE;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;

public interface ServiceAuthentificationNetE {

    public Response authentifier(
            @HeaderParam("Content-Encoding") String headerContentEncoding,
            @HeaderParam("Accept-Encoding") String headerAcceptEncoding,
            RequeteAuthentificationNetE authentification);
}