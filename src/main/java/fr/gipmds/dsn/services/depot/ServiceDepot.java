package fr.gipmds.dsn.services.depot;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;

public interface ServiceDepot {

    public Response deposerDSN(
            @HeaderParam("Content-Encoding") String headerContentEncoding,
            @HeaderParam("Accept-Encoding") String headerAcceptEncoding,
            @HeaderParam("Authorization") String headerAuthorization,
            byte[] depot);
}