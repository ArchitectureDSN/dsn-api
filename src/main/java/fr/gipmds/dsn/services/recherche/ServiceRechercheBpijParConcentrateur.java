package fr.gipmds.dsn.services.recherche;

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
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Path("/lister-bpij-concentrateur")
@Produces(MediaType.APPLICATION_XML)
@GZIP
@NoCache
@Formatted
public class ServiceRechercheBpijParConcentrateur {

    @GET
    @Path("1.0/{debut}")
    public Response listerRetours(
            @HeaderParam("Accept-Encoding") String headerAcceptEncoding,
            @HeaderParam("Authorization") String headerAuthorization,
            @PathParam("debut") String debut,
            @QueryParam("siret") String siret,
            @QueryParam("page") String page
    ) {
        return listerRetours(headerAcceptEncoding, headerAuthorization, debut, null, siret, page);
    }

    @GET
    @Path("1.0/{debut}/{fin}")
    public Response listerRetours(
            @HeaderParam("Accept-Encoding") String headerAcceptEncoding,
            @HeaderParam("Authorization") String headerAuthorization,
            @PathParam("debut") String debut,
            @PathParam("fin") String fin,
            @QueryParam("siret") String siret,
            @QueryParam("page") String page
    ) {
        System.out.println("headerAcceptEncoding = " + headerAcceptEncoding + ", headerAuthorization = " + headerAuthorization + ", debut = " + debut + ", fin = " + fin + ", siret = " + siret + ", page = " + page);

        Date dateDebut = null;
        Date dateFin = null;
        try {
            dateDebut = DateUtils.parseShort(debut);
            dateFin = fin == null ? new Date() : DateUtils.parseShort(fin);
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

        byte[] bytes;
        // 200
        if (isSameDay(dateDebut, new Date())) {
            // 200 réponse vide  // date du jour => réponse vide pour démonstration
            bytes = TestData.reponseRechercheListerBPIJVide.getBytes();
        } else {
            // 200 réponse normal
            bytes = TestData.reponseRechercheListerBPIJ.getBytes();
        }
        Response.ResponseBuilder builder = Response.ok().entity(bytes);
        builder.header("Expires", "Thu, 01 Dec 1994 16:00:00 GMT");
        return builder.build();
    }

    public static boolean isSameDay(Date date1, Date date2) {
        LocalDate localDate1 = date1.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate localDate2 = date2.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return localDate1.isEqual(localDate2);
    }
}
