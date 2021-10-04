package fr.gipmds.dsn.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class SecurityUtils {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

    private SecurityUtils() {
        throw new IllegalStateException("No instances");
    }

    public static final Response jetonManquantOuInvalide = Response
            .status(401)
            .header("WWW-Authenticate",
                    "DSNLogin realm=\"Jeton manquant ou invalide\"").build();

    public static final Response utilisateurNonInscrit = Response
            .status(401)
            .header("WWW-Authenticate",
                    "DSNLogin realm=\"Utilisateur non-inscrit au service\"")
            .build();

    public static Map<String, String> parseAuthorizationHeader(String header) {

        Map<String, String> result = new HashMap<>();

        if (header == null)
            return result;

        header = header.replace("DSNLogin", "");
        String[] elements = header.split(",");

        for (String element : elements) {
            element = element.trim();
            String[] kvp = element.split("=");

            if (kvp.length == 2 && kvp[0] != null && !kvp[0].isEmpty()
                    && kvp[1] != null && !kvp[1].isEmpty())
                result.put(kvp[0], kvp[1]);
        }

        return result;
    }

    public static String buildAuthorizationHeader(Map<String, String> headers) {
        if (headers == null || headers.size() == 0)
            return null;

        StringBuilder result = new StringBuilder("DSNLogin ");
        boolean firstRun = true;

        for (String key : headers.keySet()) {
            if (!firstRun) {
                result.append(",");
            }

            result
                    .append(key)
                    .append("=")
                    .append(headers.get(key));
            firstRun = false;
        }

        return result.toString();
    }

    public static Map<String, String> parseDeclarantSNP(String snp) {

        Map<String, String> result = new HashMap<>();

        String[] elements = snp.split(";");

        if (elements.length == 3) {
            if (elements[0] != null && !elements[0].isEmpty())
                result.put("siret", elements[0]);
            if (elements[1] != null && !elements[1].isEmpty())
                result.put("nom", elements[1]);
            if (elements[2] != null && !elements[2].isEmpty())
                result.put("prenom", elements[2]);
        }

        return result;
    }

    public static Map<String, String> parseToken(String jeton) {

        Map<String, String> result = new HashMap<>();

        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(jeton));
            Document doc = docBuilder.parse(inputSource);

            Node rootNode = doc.getChildNodes().item(0);
            NodeList nodes = rootNode.getChildNodes();

            Node n4 = nodes.item(4);
            String siret = n4.getTextContent();
            result.put("siret", siret);

            Node n7 = nodes.item(7);
            String nom = n7.getTextContent();
            result.put("nom", nom);

            Node n6 = nodes.item(6);
            String prenom = n6.getTextContent();
            result.put("prenom", prenom);
        } catch (Exception e) {
            logger.error("Error Parsing", e);
        }

        return result;
    }
}