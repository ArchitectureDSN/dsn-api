package fr.gipmds.dsn.services.auth;

import fr.gipmds.dsn.modeles.RequeteAuthentificationNetE;
import fr.gipmds.dsn.test.resources.TestData;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/authentifier/1.0")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_OCTET_STREAM)
public class ServiceAuthentificationNetEImpl implements ServiceAuthentificationNetE {

    @Override
    @POST
    public Response authentifier(
            @HeaderParam("Content-Encoding") String headerContentEncoding,
            @HeaderParam("Accept-Encoding") String headerAcceptEncoding,
            RequeteAuthentificationNetE authentification) {

        try {
            // 406 : Any response encoding != than gzip is not supported
            if (headerAcceptEncoding != null
                    && !headerAcceptEncoding.contains("gzip")) {
                return Response.status(406).build();
            }

            // 422 null
            if (authentification.getService() == null
                    || authentification.getNom() == null
                    || authentification.getPrenom() == null
                    || authentification.getSiret() == null
                    || authentification.getMotdepasse() == null)
                return Response.status(422).build();

            // 422 code service
            if (authentification.getService().length() > 2)
                return Response.status(422).build();

            // 422 siret
            if (authentification.getSiret().length() != 14)
                return Response.status(422).build();

            // 422 nom
            if (authentification.getNom().length() > 64)
                return Response.status(422).build();

            // 422 prenom
            if (authentification.getPrenom().length() > 39)
                return Response.status(422).build();

            // 422 mot de passe
            if (authentification.getMotdepasse().length() > 30)
                return Response.status(422).build();

            if ("99900000300015".equals(authentification.getSiret())
                    && "declarant".equals(authentification.getNom())
                    && "declarant".equals(authentification.getPrenom())
                    && "Refonte01".equals(authentification.getMotdepasse())) {
                // 200 Déclarant
                if ("97".equals(authentification.getService())
                        || "94".equals(authentification.getService())) {
                    String jeton = TestData.declarantInscrit.getFauxJeton();
                    return Response.status(Status.OK).entity(jeton).build();
                }
                // 401 Déclarant non inscrit
                return Response
                        .status(Status.UNAUTHORIZED)
                        .header("WWW-Authenticate",
                                "Basic realm=\"net-entreprises.fr\"").build();
            }

            if ("18751249600022".equals(authentification.getSiret())
                    && "CTR2000".equals(authentification.getNom())
                    && "concentrateur".equals(authentification.getPrenom())
                    && "azerty".equals(authentification.getMotdepasse())) {
                // 200 Concentrateur
                if ("98".equals(authentification.getService())) {
                    String jeton = TestData.concentrateurInscrit
                            .getFauxJeton();
                    return Response.status(Status.OK).entity(jeton).build();
                }
                // 401 Concentrateur non inscrit
                return Response
                        .status(Status.UNAUTHORIZED)
                        .header("WWW-Authenticate",
                                "Basic realm=\"net-entreprises.fr\"").build();
            }

            // 500
            if ("kaboom".equals(authentification.getNom())) {
                throw new RuntimeException();
            }

            // 401 erreur d'authentification
            return Response
                    .status(Status.UNAUTHORIZED)
                    .header("WWW-Authenticate",
                            "Basic realm=\"net-entreprises.fr\"").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).build();
        }
    }
}