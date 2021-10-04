package fr.gipmds.dsn.services.auth;

import fr.gipmds.dsn.modeles.RequeteAuthentificationMSA;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;

public interface ServiceAuthentificationMSA {

    public Response authentifier(
            @HeaderParam("Content-Encoding") String headerContentEncoding,
            @HeaderParam("Accept-Encoding") String headerAcceptEncoding,
            RequeteAuthentificationMSA authentification);
}