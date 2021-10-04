package fr.gipmds.dsn.services.recherche;

import fr.gipmds.dsn.modeles.Config;
import fr.gipmds.dsn.test.resources.Concentrateur;
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


@Path("/lister-objets-concentrateur")
@Produces(MediaType.APPLICATION_XML)
@GZIP
@NoCache
@Formatted
public class ServiceRechercheObjetsParConcentrateur implements ServiceRecherche {


    @GET
    @Path("/1.0/{debut}")
    public Response listerRetours(
            @HeaderParam("Accept-Encoding") String headerAcceptEncoding,
            @HeaderParam("Authorization") String headerAuthorization,
            @PathParam("debut") String debut) {
        return listerRetours(headerAcceptEncoding, headerAuthorization, debut,
                null);
    }

    @GET
    @Path("/1.0/{debut}/{fin}")
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

            // 429 Check dates range validity
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateDebut);
            cal.add(Calendar.MINUTE, Config.plageDeRechercheParConcentrateur);
            if (dateFin.after(cal.getTime()))
                return Response.status(429).build();

            // 406 : Any response encoding != than gzip is not supported
            if (headerAcceptEncoding != null
                    && !headerAcceptEncoding.contains("gzip")) {
                return Response.status(406).build();
            }

            // Parse authorization header
            Map<String, String> headerMap = SecurityUtils
                    .parseAuthorizationHeader(headerAuthorization);

            // Traitement pour concentrateur
            if (headerMap.get("concentrateur") != null) {

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

                // 401 CONCENTRATEUR non-inscrit
                Concentrateur inscrit = TestData.concentrateurInscrit;
                if (!inscrit.siret.equals(ctrMap.get("siret"))
                        || !inscrit.nom.equals(ctrMap.get("nom")))
                    return SecurityUtils.utilisateurNonInscrit;

            } else {
                // Pas un concentrateur
                return SecurityUtils.jetonManquantOuInvalide;
            }

            // 200
            byte[] bytes = TestData.reponseRechercheObjets.getBytes();
            ResponseBuilder builder = Response.ok().entity(bytes);
            builder.header("Accept-Ranges", "minutes=0-"
                    + Config.plageDeRechercheParConcentrateur);
            return builder.build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).build();
        }
    }
}