package fr.gipmds.dsn.services.telechargement;

import fr.gipmds.dsn.test.resources.Concentrateur;
import fr.gipmds.dsn.test.resources.Declarant;
import fr.gipmds.dsn.test.resources.TestData;
import fr.gipmds.dsn.utils.Base64Utils;
import fr.gipmds.dsn.utils.SecurityUtils;
import org.apache.commons.codec.binary.Base64;
import org.jboss.resteasy.annotations.GZIP;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Map;

@Path("/telecharger-retour")
@GZIP
@NoCache
public class ServiceTelechargement {

    @GET
    @Path("/1.0/{idFlux}/{idRetour}")
    public Response telechargerRetour(
            @HeaderParam("Accept-Encoding") String headerAcceptEncoding,
            @HeaderParam("Authorization") String headerAuthorization,
            @PathParam("idFlux") String idFlux,
            @PathParam("idRetour") String idRetour) {

        try {
            // 406 : Any response encoding != than gzip is not supported
            if (headerAcceptEncoding != null
                    && !headerAcceptEncoding.contains("gzip")) {
                return Response.status(406).build();
            }

            // 422
            if (idFlux.length() > 50 || idRetour.length() > 10)
                return Response.status(422).build();

            // Parse authorization header
            Map<String, String> headerMap = SecurityUtils
                    .parseAuthorizationHeader(headerAuthorization);

            // Traitement pour logiciel de paie
            if (headerMap.get("jeton") != null) {

                String jeton = headerMap.get("jeton");

                // 401 JETON not base64
                if (!Base64.isBase64(jeton))
                    return SecurityUtils.jetonManquantOuInvalide;

                jeton = Base64Utils.decode(jeton);
                Map<String, String> snpMap = SecurityUtils.parseToken(jeton);

                // 401 JETON bad header
                if (snpMap.size() != 3)
                    return SecurityUtils.jetonManquantOuInvalide;

                // 401 déclarant non-inscrit
                Declarant inscrit = TestData.declarantInscrit;
                if (!inscrit.siret.equals(snpMap.get("siret"))
                        || !inscrit.nom.equals(snpMap.get("nom"))
                        || !inscrit.prenom.equals(snpMap.get("prenom")))
                    return SecurityUtils.utilisateurNonInscrit;
            }
            // Traitement pour concentrateur
            else if (headerMap.get("concentrateur") != null) {

                String concentrateur = headerMap.get("concentrateur");

                // 401 JETON not base64
                if (!Base64.isBase64(concentrateur))
                    return SecurityUtils.jetonManquantOuInvalide;

                concentrateur = Base64Utils.decode(concentrateur);
                Map<String, String> ctrMap = SecurityUtils
                        .parseToken(concentrateur);

                // 401 CONCENTRATEUR bad header
                if (ctrMap.size() != 3)
                    return SecurityUtils.jetonManquantOuInvalide;

                // 401 concentrateur non-inscrit
                Concentrateur inscrit = TestData.concentrateurInscrit;
                if (!inscrit.siret.equals(ctrMap.get("siret"))
                        || !inscrit.nom.equals(ctrMap.get("nom")))
                    return SecurityUtils.utilisateurNonInscrit;
            } else {
                // Ni logiciel de paie, ni concentrateur
                return SecurityUtils.jetonManquantOuInvalide;
            }

            // 404
            if ("purge".equals(idRetour)) {
                return Response.status(Status.NOT_FOUND).build();
            }

            // 500
            if ("kaboom".equals(idRetour)) {
                throw new RuntimeException();
            }

            // 200
            if ("abcdefghij".equals(idFlux)) {

                if ("12345".equals(idRetour)) {
                    String retour = "Ceci est un retour TXT";
                    return Response.ok().entity(retour)
                            .type(MediaType.TEXT_PLAIN).build();
                }

                // 200
                if ("65432".equals(idRetour)) {
                    String retour = TestData.aee;

                    return Response.ok().entity(retour)
                            .type(MediaType.APPLICATION_XML).build();
                }

            }

            // 404
            return Response.status(Status.NOT_FOUND).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).build();
        }
    }
}