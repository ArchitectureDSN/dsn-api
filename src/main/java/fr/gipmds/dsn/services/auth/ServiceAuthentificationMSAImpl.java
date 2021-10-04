package fr.gipmds.dsn.services.auth;

import fr.gipmds.dsn.modeles.RequeteAuthentificationMSA;
import fr.gipmds.dsn.test.resources.TestData;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/authentifier/2.0")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_OCTET_STREAM)
public class ServiceAuthentificationMSAImpl implements ServiceAuthentificationMSA {

    @Override
    @POST
    @Path("")
    public Response authentifier(
            @HeaderParam("Content-Encoding") String headerContentEncoding,
            @HeaderParam("Accept-Encoding") String headerAcceptEncoding,
            RequeteAuthentificationMSA authentification) {

        try {

            // 406 : Any response encoding != than gzip is not supported
            if (headerAcceptEncoding != null
                    && !headerAcceptEncoding.contains("gzip")) {
                return Response.status(406).build();
            }

            // 422 null
            if (authentification.getIdentifiant() == null
                    || authentification.getMotdepasse() == null)
                return Response.status(422).build();

            // 422 identifiant
            if (authentification.getIdentifiant().length() > 30)
                return Response.status(422).build();

            // 422 mot de passe
            if (authentification.getMotdepasse().length() > 30)
                return Response.status(422).build();

            // 200 Déclarant
            if ("wwallace".equals(authentification.getIdentifiant())
                    && "azerty".equals(authentification.getMotdepasse())) {
                String jeton = TestData.declarantInscrit.getFauxJeton();
                return Response.status(Status.OK).entity(jeton).build();
            }

            // 401 Déclarant non-inscrit
            if ("sseagal".equals(authentification.getIdentifiant())) {
                return Response.status(Status.UNAUTHORIZED)
                        .header("WWW-Authenticate", "Basic realm=\"msa.fr\"")
                        .build();
            }

            // 500
            if ("kaboom".equals(authentification.getIdentifiant())) {
                throw new RuntimeException();
            }

            // 401 Erreur d'authentification
            return Response.status(Status.UNAUTHORIZED)
                    .header("WWW-Authenticate", "Basic realm=\"msa.fr\"")
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).build();
        }
    }
}