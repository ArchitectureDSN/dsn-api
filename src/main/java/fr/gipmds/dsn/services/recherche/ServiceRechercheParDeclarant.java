package fr.gipmds.dsn.services.recherche;

import fr.gipmds.dsn.modeles.Config;
import fr.gipmds.dsn.test.resources.Concentrateur;
import fr.gipmds.dsn.test.resources.Declarant;
import fr.gipmds.dsn.test.resources.TestData;
import fr.gipmds.dsn.utils.Base64Utils;
import fr.gipmds.dsn.utils.DateUtils;
import fr.gipmds.dsn.utils.SecurityUtils;
import org.apache.commons.codec.binary.Base64;
import org.jboss.resteasy.annotations.GZIP;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.annotations.providers.jaxb.Formatted;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Path("/lister-retours-declarant")
@GZIP
@NoCache
@Formatted
public class ServiceRechercheParDeclarant implements ServiceRecherche {

    @GET
    @Path("/1.0/{debut}")
    @Produces(MediaType.APPLICATION_XML)
    public Response listerRetours(
            @HeaderParam("Accept-Encoding") String headerAcceptEncoding,
            @HeaderParam("Authorization") String headerAuthorization,
            @PathParam("debut") String debut) {
        return listerRetours(headerAcceptEncoding, headerAuthorization, debut,
                null);
    }

    @GET
    @Path("1.0/{debut}/{fin}")
    @Produces(MediaType.APPLICATION_XML)
    public Response listerRetours(
            @HeaderParam("Accept-Encoding") String headerAcceptEncoding,
            @HeaderParam("Authorization") String headerAuthorization,
            @PathParam("debut") String debut, @PathParam("fin") String fin) {
        try {

            // Parse dates
            Date dateDebut = null;
            Date dateFin = null;
            try {
                dateDebut = DateUtils.parse(debut);
                dateFin = fin == null ? new Date() : DateUtils.parse(fin);
            } catch (ParseException e) {
                // 422 Bad date formats
                return Response.status(422).build();
            }

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
            else if (headerMap.get("concentrateur") != null
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

            // 429 Check dates range validity
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateDebut);
            cal.add(Calendar.MINUTE, Config.plageDeRechercheParDeclarant);
            if (dateFin.after(cal.getTime()))
                return Response.status(429).build();

            // 200
            byte[] bytes = TestData.reponseRecherche.getBytes();
            ResponseBuilder builder = Response.ok().entity(bytes);
            builder.header("Expires", "Thu, 01 Dec 1994 16:00:00 GMT");
            builder.header("Accept-Ranges", "minutes=0-"
                    + Config.plageDeRechercheParDeclarant);
            return builder.build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).build();
        }
    }
}