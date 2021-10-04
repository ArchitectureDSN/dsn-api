package fr.gipmds.dsn.test.recherche.depot;

import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;
import fr.gipmds.dsn.modeles.Config;
import fr.gipmds.dsn.services.depot.recherche.model.RechercherDepotParConcentrateurResponse;
import fr.gipmds.dsn.test.BaseTestClass;
import fr.gipmds.dsn.test.resources.TestData;
import fr.gipmds.dsn.utils.Base64Utils;
import fr.gipmds.dsn.utils.SecurityUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static fr.gipmds.dsn.test.TestUtils.getDateDebutFormatted;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(JunitServerExtension.class)
@DisplayName("Rechercher Depots Par Declarant Test")
class RechercherDepotsParDeclarantTest extends BaseTestClass {

    @Test
    @DisplayName("Rechercher 404 Pas De Plage Test")
    void rechercher404PasDePlageTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        // plage manquant
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/");
        Builder builder = target.request();
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(404, response.getStatus());
        assertNull(response.getHeaderString("Content-Type"));
    }

    @Test
    @DisplayName("Rechercher 422 Plage Mauvais Debut Test")
    void rechercher422PlageMauvaisDebutTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        // date debut mauvais format
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/120000");
        Builder builder = target.request();
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(422, response.getStatus());
        assertNull(response.getHeaderString("Content-Type"));
    }

    @Test
    @DisplayName("Rechercher 422 Plage Mauvaise Fin Test")
    void rechercher422PlageMauvaiseFinTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        // date fin mauvais format
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/20130101120000/20130101");
        Builder builder = target.request();
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(422, response.getStatus());
        assertNull(response.getHeaderString("Content-Type"));
    }

    @Test
    @DisplayName("Rechercher 200 Par Declarant Test")
    void rechercher200ParDeclarantTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        final String dateDebutFormatted = getDateDebutFormatted(Config.plageDeRechercheParDeclarant);
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/" + dateDebutFormatted);
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", TestData.declarantInscrit.getFauxJeton());
        builder.header("Authorization", SecurityUtils.buildAuthorizationHeader(map));
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(200, response.getStatus());
        assertEquals("no-cache", response.getHeaderString("Cache-Control"));
        assertEquals("minutes=0-" + Config.plageDeRechercheParDeclarant, response.getHeaderString("Accept-Ranges"));
        assertEquals("application/xml", response.getHeaderString("Content-Type"));
        RechercherDepotParConcentrateurResponse depots = response.readEntity(RechercherDepotParConcentrateurResponse.class);
        response.close();
        assertNotNull(depots);
        assertNotNull(depots.declarant);
        assertNotNull(depots.declarant.nom);
        assertNotNull(depots.declarant.prenom);
        assertNotNull(depots.declarant.siret);
        assertNotNull(depots.declarant.depot);
        assertEquals(2, depots.declarant.depot.size());
        assertEquals("0123456789", depots.declarant.depot.get(0).idflux);
    }

    @Test
    @DisplayName("Rechercher 429 Plage Trop Grande Test")
    void rechercher429PlageTropGrandeTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        // plage trop grande
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/20130101120000/20130101140000");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", TestData.declarantInscrit.getFauxJeton());
        builder.header("Authorization", SecurityUtils.buildAuthorizationHeader(map));
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(429, response.getStatus());
        assertNull(response.getHeaderString("Content-Type"));
    }

    @Test
    @DisplayName("Rechercher 200 Debut De Plage Seulement Test")
    void rechercher200DebutDePlageSeulementTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        String date = getDateDebutFormatted(Config.plageDeRechercheParDeclarant);
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/" + date);
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", TestData.declarantInscrit.getFauxJeton());
        builder.header("Authorization", SecurityUtils.buildAuthorizationHeader(map));
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(200, response.getStatus());
        assertEquals("no-cache", response.getHeaderString("Cache-Control"));
        assertNotNull(response.getHeaderString("Expires"), "Expires");
        assertEquals("minutes=0-" + Config.plageDeRechercheParDeclarant, response.getHeaderString("Accept-Ranges"));
        assertEquals("application/xml", response.getHeaderString("Content-Type"));
        RechercherDepotParConcentrateurResponse depots = response.readEntity(RechercherDepotParConcentrateurResponse.class);
        response.close();
        assertNotNull(depots);
        assertNotNull(depots.declarant);
        assertNotNull(depots.declarant.nom);
        assertNotNull(depots.declarant.prenom);
        assertNotNull(depots.declarant.siret);
        assertNotNull(depots.declarant.depot);
        assertEquals(2, depots.declarant.depot.size());
        assertEquals("0123456789", depots.declarant.depot.get(0).idflux);
    }

    @Test
    @DisplayName("Rechercher 401 AUTHORIZATION Mauvais Test")
    void rechercher401AUTHORIZATIONMauvaisTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/20130101120000/20130101130000");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("declarent", "SNP"); // valeur  declarant non valide
        builder.header("Authorization", SecurityUtils.buildAuthorizationHeader(map));
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Jeton manquant ou invalide\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
    }

    @Test
    @DisplayName("Rechercher 401 AUTHORIZATION Manquant Test")
    void rechercher401AUTHORIZATIONManquantTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/20130101120000/20130101130000");
        Builder builder = target.request();
        Invocation invocation = builder.buildGet();
        // Authorization manquant
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Jeton manquant ou invalide\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
    }

    @Test
    @DisplayName("Rechercher 401 JETON Mauvais Test")
    void rechercher401JETONMauvaisTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/20130101120000/20130101130000");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", Base64Utils.encode("Not a é valid token")); // jeton non valide
        String authorization = SecurityUtils.buildAuthorizationHeader(map);
        builder.header("Authorization", authorization);
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Jeton manquant ou invalide\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
    }

    @Test
    @DisplayName("Rechercher 401 CONCENTRATEUR Mauvais Test")
    void rechercher401CONCENTRATEURMauvaisTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/20130101120000/20130101130000");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("concentrateur", Base64Utils.encode("Not a é valid token")); // valeur  concentrateur non valide
        map.put("declarant", Base64Utils.encode(TestData.declarantInscrit.getSNP()));
        String authorization = SecurityUtils.buildAuthorizationHeader(map);
        builder.header("Authorization", authorization);
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Jeton manquant ou invalide\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
    }

    @Test
    @DisplayName("Rechercher 401 DECLARANT Mauvais Test")
    void rechercher401DECLARANTMauvaisTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/20130101120000/20130101130000");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("concentrateur", TestData.concentrateurInscrit.getFauxJeton());
        map.put("declarant", Base64Utils.encode("Not a é valid token")); // valeur  declarant non valide
        String authorization = SecurityUtils.buildAuthorizationHeader(map);
        builder.header("Authorization", authorization);
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Jeton manquant ou invalide\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
    }

    @Test
    void rechercher401JETONNullTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/20130101120000");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", null); // jeton non valide
        String authorization = SecurityUtils.buildAuthorizationHeader(map);
        builder.header("Authorization", authorization);
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Jeton manquant ou invalide\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
    }

    @Test
    @DisplayName("Rechercher 401 CONCENTRATEUR Null Test")
    void rechercher401CONCENTRATEURNullTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/20130101120000/20130101130000");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("concentrateur", null); // valeur  concentrateur non valide
        map.put("declarant", Base64Utils.encode(TestData.declarantInscrit.getSNP()));
        String authorization = SecurityUtils.buildAuthorizationHeader(map);
        builder.header("Authorization", authorization);
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Jeton manquant ou invalide\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
    }

    @Test
    @DisplayName("Rechercher 401 DECLARANT Null Test")
    void rechercher401DECLARANTNullTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/20130101120000/20130101130000");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("concentrateur", TestData.concentrateurInscrit.getFauxJeton());
        map.put("declarant", null); // valeur  declarant non valide
        String authorization = SecurityUtils.buildAuthorizationHeader(map);
        builder.header("Authorization", authorization);
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Jeton manquant ou invalide\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
    }

    @Test
    @DisplayName("Rechercher 401 JETON Non Inscrit Test")
    void rechercher401JETONNonInscritTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/20130101120000/20130101130000");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", TestData.declarantNonInscrit.getFauxJeton());  // valeur jeton non valide (non inscrit)
        String authorization = SecurityUtils.buildAuthorizationHeader(map);
        builder.header("Authorization", authorization);
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Utilisateur non-inscrit au service\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
    }

    @Test
    @DisplayName("Rechercher 401 DECLARANT Non Inscrit Test")
    void rechercher401DECLARANTNonInscritTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/20130101120000/20130101130000");
        Builder builder = target.request();
        Invocation invocation = builder.buildGet();
        Map<String, String> map = new HashMap<>();
        map.put("concentrateur", TestData.concentrateurInscrit.getFauxJeton());
        map.put("declarant", Base64Utils.encode(TestData.declarantNonInscrit.getSNP())); // valeur declarant non valide (non inscrit)
        String authorization = SecurityUtils.buildAuthorizationHeader(map);
        builder.header("Authorization", authorization);
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Utilisateur non-inscrit au service\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
    }

    @Test
    @DisplayName("Rechercher 401 CONCENTRATEUR Non Inscrit Test")
    void rechercher401CONCENTRATEURNonInscritTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/20130101120000/20130101130000");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("concentrateur", TestData.concentrateurNonInscrit.getFauxJeton()); // valeur concentrateur non valide  (non inscrit)
        map.put("declarant", Base64Utils.encode(TestData.declarantInscrit.getSNP()));
        String authorization = SecurityUtils.buildAuthorizationHeader(map);
        builder.header("Authorization", authorization);
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Utilisateur non-inscrit au service\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
    }

    @Test
    @DisplayName("Rechercher 406 ACCEPT _ ENCODING Mauvais Test")
    void rechercher406ACCEPT_ENCODINGMauvaisTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/20130101120000/20130101130000");
        Builder builder = target.request();
        Invocation invocation = builder
                .acceptEncoding("deflate") // doit contenir gzip
                .buildGet();
        // When
        Response response = invocation.invoke();
        String actual = response.readEntity(String.class);
        response.close();
        // Then
        assertEquals(406, response.getStatus());
        assertNull(response.getHeaderString("Content-Type"));
        assertEquals(0, actual.length());
    }

    @Test
    @DisplayName("Rechercher 401 JETON Pas En Base 64 Test")
    void rechercher401JETONPasEnBase64Test() throws Exception {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/20130101120000/20130101130000");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", URLEncoder.encode("Not a base 64 token", "UTF-8"));  // valeur jeton non valide (pas en base 64)
        builder.header("Authorization", SecurityUtils.buildAuthorizationHeader(map));
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Jeton manquant ou invalide\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
    }

    @Test
    @DisplayName("Rechercher 401 CONCENTRATEUR Pas En Base 64 Test")
    void rechercher401CONCENTRATEURPasEnBase64Test() throws Exception {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/20130101120000/20130101130000");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("concentrateur", URLEncoder.encode("Not a base 64 token", "UTF-8")); // valeur concentrateur non valide (pas en base 64)
        map.put("declarant", Base64Utils.encode(TestData.declarantInscrit.getSNP()));
        builder.header("Authorization", SecurityUtils.buildAuthorizationHeader(map));
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Jeton manquant ou invalide\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
    }

    @Test
    @DisplayName("Rechercher 401 DECLARANT Pas En Base 64 Test")
    void rechercher401DECLARANTPasEnBase64Test() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-depots/1.0/20130101120000/20130101130000");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("concentrateur", TestData.concentrateurInscrit.getFauxJeton());
        map.put("declarant", TestData.declarantInscrit.getSNP()); // valeur declarant non valide (pas en base 64)
        builder.header("Authorization", SecurityUtils.buildAuthorizationHeader(map));
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Jeton manquant ou invalide\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
    }
}
