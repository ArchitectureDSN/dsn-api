package fr.gipmds.dsn.services.recherche;

import fr.gipmds.dsn.modeles.ListeRetours2;
import fr.gipmds.dsn.test.resources.Concentrateur;
import fr.gipmds.dsn.test.resources.Declarant;
import fr.gipmds.dsn.test.resources.TestData;
import fr.gipmds.dsn.utils.Base64Utils;
import fr.gipmds.dsn.utils.SecurityUtils;
import org.apache.commons.codec.binary.Base64;
import org.jboss.resteasy.annotations.GZIP;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.annotations.providers.jaxb.Formatted;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.bind.JAXBContext;
import java.io.StringReader;
import java.util.Map;

@Path("/lister-retours-flux")
public class ServiceRechercheParFlux2 {

    @GET
    @Path("/2.0/{idflux}")
    @Produces(MediaType.APPLICATION_JSON)
    @GZIP
    @NoCache
    @Formatted
    public Response listerRetours(
            @HeaderParam("Accept-Encoding") String headerAcceptEncoding,
            @HeaderParam("Authorization") String headerAuthorization,
            @PathParam("idflux") String idFlux) {

        try {
            // 422
            if (idFlux.length() > 50)
                return Response.status(422).build();

            // 406 : Any response encoding != than gzip is not supported
            if (headerAcceptEncoding != null
                    && !headerAcceptEncoding.contains("gzip")) {
                return Response.status(406).build();
            }

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

            if ("abcdefghij".equals(idFlux)) {
                // 200
                final ListeRetours2 entity = (ListeRetours2) JAXBContext.newInstance(ListeRetours2.class)
                        .createUnmarshaller()
                        .unmarshal(new StringReader(TestData.reponseRecherche));

                ResponseBuilder builder = Response.ok().entity(entity);
                builder.header("Expires", "Thu, 01 Dec 1994 16:00:00 GMT");
                return builder.build();
            }
            if ("pasderetours".equals(idFlux)) {
                // 200 vide
                final ListeRetours2 entity = (ListeRetours2) JAXBContext.newInstance(ListeRetours2.class)
                        .createUnmarshaller()
                        .unmarshal(new StringReader(TestData.reponseRechercheVide));

                ResponseBuilder builder = Response.ok().entity(entity);
                builder.header("Expires", "Thu, 01 Dec 1994 16:00:00 GMT");
                return builder.build();
            } else if ("kaboom".equals(idFlux)) {
                // 500
                throw new RuntimeException();
            } else {
                // 404 Flux inconnu
                return Response.status(404).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).build();
        }
    }
}