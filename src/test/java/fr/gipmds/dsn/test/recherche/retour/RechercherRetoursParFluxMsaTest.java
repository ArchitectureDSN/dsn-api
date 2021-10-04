package fr.gipmds.dsn.test.recherche.retour;

import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;
import fr.gipmds.dsn.modeles.ListeRetours;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(JunitServerExtension.class)
@DisplayName("Rechercher Retours Par Flux Msa Test")
class RechercherRetoursParFluxMsaTest extends BaseTestClass {

    @Test
    @DisplayName("Rechercher 200 Par Declarant Test")
    void rechercher200ParDeclarantTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-retours-flux/1.0/abcdefghij");
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
        assertEquals("application/xml", response.getHeaderString("Content-Type"));
        ListeRetours actual = response.readEntity(ListeRetours.class);
        response.close();
        assertNotNull(actual.flux);
        assertEquals("abcdefghij", actual.flux.get(0).id);
        assertTrue(actual.flux.get(0).retours.size() > 0);
    }

    @Test
    @DisplayName("Rechercher 200 Vide Test")
    void rechercher200VideTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-retours-flux/1.0/pasderetours");
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
        assertEquals("application/xml", response.getHeaderString("Content-Type"));
        ListeRetours actual = response.readEntity(ListeRetours.class);
        response.close();
        assertNotNull(actual.flux);
        assertEquals(0, actual.flux.size());
    }

    @Test
    @DisplayName("Rechercher 200 Par Concentrateur Test")
    void rechercher200ParConcentrateurTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-retours-flux/1.0/abcdefghij");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("concentrateur", TestData.concentrateurInscrit.getFauxJeton());
        builder.header("Authorization", SecurityUtils.buildAuthorizationHeader(map));
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(200, response.getStatus());
        assertEquals("no-cache", response.getHeaderString("Cache-Control"));
        assertNotNull(response.getHeaderString("Expires"), "Expires");
        assertEquals("application/xml", response.getHeaderString("Content-Type"));
        ListeRetours actual = response.readEntity(ListeRetours.class);
        response.close();
        assertNotNull(actual.flux);
        assertNotNull(actual.flux.get(0));
        assertEquals("abcdefghij", actual.flux.get(0).id);
        assertTrue(actual.flux.get(0).retours.size() > 0);
    }

    @Test
    @DisplayName("Rechercher 200 Accept Encoding GZIP Test")
    void rechercher200AcceptEncodingGZIPTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-retours-flux/1.0/abcdefghij");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", TestData.declarantInscrit.getFauxJeton());
        builder.header("Authorization", SecurityUtils.buildAuthorizationHeader(map));
        Invocation invocation = builder.acceptEncoding("gzip").buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(200, response.getStatus());
        assertEquals("no-cache", response.getHeaderString("Cache-Control"));
        assertNotNull(response.getHeaderString("Expires"), "Expires");
        assertEquals("application/xml", response.getHeaderString("Content-Type"));
        ListeRetours actual = response.readEntity(ListeRetours.class);
        response.close();
        assertNotNull(actual.flux);
        assertEquals("abcdefghij", actual.flux.get(0).id);
        assertTrue(actual.flux.get(0).retours.size() > 0);
    }

    @Test
    @DisplayName("Rechercher 406 Accept Encoding Mauvais Test")
    void rechercher406AcceptEncodingMauvaisTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-retours-flux/1.0/abcdefghij");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", TestData.declarantInscrit.getFauxJeton());
        builder.header("Authorization", SecurityUtils.buildAuthorizationHeader(map));
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
    @DisplayName("Rechercher 422 Id Flux Trop Long Test")
    void rechercher422IdFluxTropLongTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        // id flux trop long
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-retours-flux/1.0/012345678901234567890123456789012345678901234567890");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", TestData.declarantInscrit.getFauxJeton());
        builder.header("Authorization", SecurityUtils.buildAuthorizationHeader(map));
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        String actual = response.readEntity(String.class);
        response.close();
        // Then
        assertEquals(422, response.getStatus());
        assertNull(response.getHeaderString("Content-Type"));
        assertEquals(0, actual.length());
    }

    @Test
    @DisplayName("Rechercher 404 Id Flux Inconnu Test")
    void rechercher404IdFluxInconnuTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        // flux non existant
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-retours-flux/1.0/23456");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", TestData.declarantInscrit.getFauxJeton());
        builder.header("Authorization", SecurityUtils.buildAuthorizationHeader(map));
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        String actual = response.readEntity(String.class);
        response.close();
        // Then
        assertEquals(404, response.getStatus());
        assertNull(response.getHeaderString("Content-Type"));
        assertEquals(0, actual.length());
    }

    @Test
    @DisplayName("Rechercher 401 CONCENTRATEUR Mauvais Test")
    void rechercher401CONCENTRATEURMauvaisTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-retours-flux/1.0/abcdefghij");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("concentrateur", Base64Utils.encode("Not a é valid token"));// valeur concentrateur non valide
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
    @DisplayName("Rechercher 401 JETON Mauvais Test")
    void rechercher401JETONMauvaisTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-retours-flux/1.0/abcdefghij");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", Base64Utils.encode("Not a é valid token"));// jeton non valide
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
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-retours-flux/1.0/abcdefghij");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", null);// jeton non valide
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
    @DisplayName("Rechercher 401 JETON Pas En Base 64 Test")
    void rechercher401JETONPasEnBase64Test() throws Exception {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-retours-flux/1.0/abcdefghij");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", URLEncoder.encode("Not a base64 toekn!", "UTF-8")); // valeur jeton non valide (pas en base 64)
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
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-retours-flux/1.0/abcdefghij");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("concentrateur", URLEncoder.encode("Not a base 64 token!", "UTF-8"));// valeur concentrateur non valide (pas en base 64)
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
    @DisplayName("Rechercher 401 CONCENTRATEUR Non Inscrit Test")
    void rechercher401CONCENTRATEURNonInscritTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-retours-flux/1.0/abcdefghij");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("concentrateur", TestData.concentrateurNonInscrit.getFauxJeton());// valeur concentrateur non valide  (non inscrit)
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
    @DisplayName("Rechercher 401 CONCENTRATEUR Null Test")
    void rechercher401CONCENTRATEURNullTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-retours-flux/1.0/abcdefghij");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("concentrateur", null);// valeur concentrateur non valide
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
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-retours-flux/1.0/abcdefghij");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", TestData.declarantNonInscrit.getFauxJeton()); // valeur jeton non valide (non inscrit)
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
    @DisplayName("Rechercher 401 AUTHORIZATION Entete Manquant Test")
    void rechercher401AUTHORIZATIONEnteteManquantTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-retours-flux/1.0/abcdefghij");
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
    @DisplayName("Rechercher 401 AUTHORIZATION Mauvais Entete Test")
    void rechercher401AUTHORIZATIONMauvaisEnteteTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-retours-flux/1.0/abcdefghij");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("declarent", "SNP");// valeur  declarant non valide
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
    @DisplayName("Rechercher 500 Test")
    void rechercher500Test() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/lister-retours-flux/1.0/kaboom");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", TestData.declarantInscrit.getFauxJeton());
        builder.header("Authorization", SecurityUtils.buildAuthorizationHeader(map));
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(500, response.getStatus());
        assertNull(response.getHeaderString("Content-Type"));
    }
}
