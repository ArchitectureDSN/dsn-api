package fr.gipmds.dsn.services.depot;

import fr.gipmds.dsn.test.resources.Concentrateur;
import fr.gipmds.dsn.test.resources.Declarant;
import fr.gipmds.dsn.test.resources.TestData;
import fr.gipmds.dsn.utils.Base64Utils;
import fr.gipmds.dsn.utils.SecurityUtils;
import org.apache.commons.codec.binary.Base64;
import org.jboss.resteasy.annotations.GZIP;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/deposer-dsn")
public class ServiceDepot2 {

    @POST
    @Path("/2.0")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @GZIP
    public Response deposerDSN(
            @HeaderParam("Content-Encoding") String headerContentEncoding,
            @HeaderParam("Accept-Encoding") String headerAcceptEncoding,
            @HeaderParam("Authorization") String headerAuthorization,
            byte[] depot) {

        try {
            // 415 : Any request encoding != than gzip is not supported
            if (!"gzip".equals(headerContentEncoding)) {
                return Response.status(415).build();
            }

            // 406 : Any response encoding != than gzip is not supported
            if (headerAcceptEncoding != null
                    && !headerAcceptEncoding.contains("gzip")) {
                return Response.status(406).build();
            }

            // 422
            if (depot == null || depot.length == 0)
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
            } else if (headerMap.get("concentrateur") != null
                    && headerMap.get("declarant") != null) {
                String concentrateur = headerMap.get("concentrateur");
                String declarant = headerMap.get("declarant");

                // 401 JETON not base64
                if (!Base64.isBase64(concentrateur)
                        || !Base64.isBase64(declarant))
                    return SecurityUtils.jetonManquantOuInvalide;

                concentrateur = Base64Utils.decode(concentrateur);
                declarant = Base64Utils.decode(declarant);
                Map<String, String> ctrMap = SecurityUtils
                        .parseToken(concentrateur);
                Map<String, String> snpMap = SecurityUtils
                        .parseDeclarantSNP(declarant);

                // 401 CONCENTRATEUR bad header
                if (ctrMap.size() != 3)
                    return SecurityUtils.jetonManquantOuInvalide;

                // 401 concentrateur non-inscrit
                Concentrateur inscrit = TestData.concentrateurInscrit;
                if (!inscrit.siret.equals(ctrMap.get("siret"))
                        || !inscrit.nom.equals(ctrMap.get("nom")))
                    return SecurityUtils.utilisateurNonInscrit;

                // 401 DECLARANT bad header
                if (snpMap.size() != 3)
                    return SecurityUtils.jetonManquantOuInvalide;

                // 401 déclarant non-inscrit
                if (!TestData.declarantInscrit.siret
                        .equals(snpMap.get("siret"))
                        || !TestData.declarantInscrit.nom.equals(snpMap
                        .get("nom"))
                        || !TestData.declarantInscrit.prenom.equals(snpMap
                        .get("prenom")))
                    return SecurityUtils.utilisateurNonInscrit;
            } else {
                // Ni logiciel de paie, ni concentrateur
                return SecurityUtils.jetonManquantOuInvalide;
            }

            String depotStr = new String(depot);

            // 422 : Avis de rejet
            if (!depotStr.startsWith("S10")) {
                byte[] retour = TestData.are.getBytes();
                return Response.status(422).entity(retour).build();
            }

            // 500
            if (depotStr.contains("KABOOM")) {
                throw new RuntimeException();
            }

            // 200 : AEE
            byte[] retour = TestData.aee.getBytes();
            return Response.ok().header("Content-Encoding", "gzip")
                    .entity(retour).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).build();
        }
    }
}