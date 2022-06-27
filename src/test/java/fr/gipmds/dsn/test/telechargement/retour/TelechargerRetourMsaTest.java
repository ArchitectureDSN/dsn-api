package fr.gipmds.dsn.test.telechargement.retour;

import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;
import fr.gipmds.dsn.test.BaseTestClass;
import fr.gipmds.dsn.test.resources.TestData;
import fr.gipmds.dsn.utils.Base64Utils;
import fr.gipmds.dsn.utils.SecurityUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.jupiter.api.Disabled;
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
@DisplayName("Telecharger Retour Msa Test")
class TelechargerRetourMsaTest extends BaseTestClass {

    @Test
    @DisplayName("Telecharger 200 Text Plain Test")
    void telecharger200TextPlainTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/abcdefghij/12345");
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
        assertEquals(200, response.getStatus());
        assertEquals("no-cache", response.getHeaderString("Cache-Control"));
        assertEquals("text/plain", response.getHeaderString("Content-Type"));
        assertEquals("Ceci est un retour TXT", actual);
    }

    @Test
    @DisplayName("Telecharger 200 Application Xml Test")
    void telecharger200ApplicationXmlTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        String test = getServerUrl();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/abcdefghij/65432");
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
        assertEquals(200, response.getStatus());
        assertEquals("application/xml", response.getHeaderString("Content-Type"));
        assertTrue(actual.length() < 8192);
        assertNull(response.getHeaderString("Transfer-Encoding"));
        assertEquals(TestData.aee, actual);
    }

    @Test
    @DisplayName("Telecharger 200 Accept Encoding GZIP Test")
    void telecharger200AcceptEncodingGZIPTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/abcdefghij/12345");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", TestData.declarantInscrit.getFauxJeton());
        builder.header("Authorization", SecurityUtils.buildAuthorizationHeader(map));
        Invocation invocation = builder.acceptEncoding("gzip").buildGet();
        // When
        Response response = invocation.invoke();
        String actual = response.readEntity(String.class);
        response.close();
        // Then
        assertEquals(200, response.getStatus());
        assertEquals("no-cache", response.getHeaderString("Cache-Control"));
        assertEquals("text/plain", response.getHeaderString("Content-Type"));
        assertEquals("Ceci est un retour TXT", actual);
    }

    @Test
    @DisplayName("Telecharger 406 Accept Encoding Mauvais Test")
    void telecharger406AcceptEncodingMauvaisTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/abcdefghij/12345");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", TestData.declarantInscrit.getFauxJeton());
        builder.header("Authorization", SecurityUtils.buildAuthorizationHeader(map));
        Invocation invocation = builder.acceptEncoding("deflate").buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(406, response.getStatus());
    }

    @Test
    @Disabled(value = "PoC seulement : dépend du serveur et de sa configuration")
    @DisplayName("Telecharger 200 Forced Chunked Test")
    void telecharger200ForcedChunkedTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/abcdefghij/65432");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", TestData.declarantInscrit.getFauxJeton());
        builder.header("Authorization", SecurityUtils.buildAuthorizationHeader(map));
        Invocation invocation = builder.header("TE", "chunked").buildGet();
        // When
        Response response = invocation.invoke();
        String actual = response.readEntity(String.class);
        response.close();
        // Then
        assertEquals(200, response.getStatus());
        assertEquals("application/xml", response.getHeaderString("Content-Type"));
        assertEquals("chunked", response.getHeaderString("Transfer-Encoding"));
        assertEquals(TestData.retour, actual);
    }

    @Test
    @DisplayName("Telecharger 422 Id Flux Trop Long Test")
    void telecharger422IdFluxTropLongTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/012345678901234567890123456789012345678901234567890/purge");
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
    @DisplayName("Telecharger 422 Id Retour Trop Long Test")
    void telecharger422IdRetourTropLongTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/0123456789/01234567891");
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
    @DisplayName("Telecharger 404 Id Flux Inconnu Test")
    void telecharger404IdFluxInconnuTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/inconnu/23456");
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
    @DisplayName("Telecharger 404 Id Retour Inconnu Test")
    void telecharger404IdRetourInconnuTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/0123456789/inconnu");
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
    @DisplayName("Telecharger 404 Retour Purge Test")
    void telecharger404RetourPurgeTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/abcdefghij/purge");
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
    @DisplayName("Telecharger 404 Parametre Manquant Test")
    void telecharger404ParametreManquantTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/0123456789");
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
    @DisplayName("Telecharger 401 AUTHORIZATION Mauvais Test")
    void telecharger401AUTHORIZATIONMauvaisTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/abcdefghij/65432");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("declarent", "SNP");
        builder.header("Authorization", SecurityUtils.buildAuthorizationHeader(map));
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Jeton manquant ou invalide\"", response.getHeaderString("WWW-Authenticate"));
    }

    @Test
    @DisplayName("Telecharger 401 AUTHORIZATION Manquant Test")
    void telecharger401AUTHORIZATIONManquantTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/abcdefghij/65432");
        Builder builder = target.request();
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Jeton manquant ou invalide\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
        assertEquals(0, response.getLength());
    }

    @Test
    @DisplayName("Telecharger 401 JETON Mauvais Test")
    void telecharger401JETONMauvaisTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/abcdefghij/65432");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", Base64Utils.encode("Not a é valid token"));
        String authorization = SecurityUtils.buildAuthorizationHeader(map);
        builder.header("Authorization", authorization);
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Jeton manquant ou invalide\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
        assertEquals(0, response.getLength());
    }

    @Test
    @DisplayName("Telecharger 401 CONCENTRATEUR Mauvais Test")
    void telecharger401CONCENTRATEURMauvaisTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/abcdefghij/65432");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("concentrateur", Base64Utils.encode("Not a é valid token"));
        String authorization = SecurityUtils.buildAuthorizationHeader(map);
        builder.header("Authorization", authorization);
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Jeton manquant ou invalide\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
        assertEquals(0, response.getLength());
    }

    @Test
    void telecharger401JETONNullTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/abcdefghij/65432");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", null);
        String authorization = SecurityUtils.buildAuthorizationHeader(map);
        builder.header("Authorization", authorization);
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Jeton manquant ou invalide\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
        assertEquals(0, response.getLength());
    }

    @Test
    @DisplayName("Telecharger 401 CONCENTRATEUR Null Test")
    void telecharger401CONCENTRATEURNullTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/abcdefghij/65432");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("concentrateur", null);
        String authorization = SecurityUtils.buildAuthorizationHeader(map);
        builder.header("Authorization", authorization);
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Jeton manquant ou invalide\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
        assertEquals(0, response.getLength());
    }

    @Test
    @DisplayName("Telecharger 401 JETON Non Inscrit Test")
    void telecharger401JETONNonInscritTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/abcdefghij/65432");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", TestData.declarantNonInscrit.getFauxJeton());
        String authorization = SecurityUtils.buildAuthorizationHeader(map);
        builder.header("Authorization", authorization);
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Utilisateur non-inscrit au service\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
        assertEquals(0, response.getLength());
    }

    @Test
    @DisplayName("Telecharger 401 CONCENTRATEUR Non Inscrit Test")
    void telecharger401CONCENTRATEURNonInscritTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/abcdefghij/65432");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("concentrateur", TestData.concentrateurNonInscrit.getFauxJeton());
        String authorization = SecurityUtils.buildAuthorizationHeader(map);
        builder.header("Authorization", authorization);
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Utilisateur non-inscrit au service\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
        assertEquals(0, response.getLength());
    }

    @Test
    @DisplayName("Telecharger 401 JETON Pas En Base 64 Test")
    void telecharger401JETONPasEnBase64Test() throws Exception {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/abcdefghij/65432");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", URLEncoder.encode("Not a base 64 token!", "UTF-8"));
        builder.header("Authorization", SecurityUtils.buildAuthorizationHeader(map));
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Jeton manquant ou invalide\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
        assertEquals(0, response.getLength());
    }

    @Test
    @DisplayName("Telecharger 401 CONCENTRATEUR Pas En Base 64 Test")
    void telecharger401CONCENTRATEURPasEnBase64Test() throws Exception {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/abcdefghij/65432");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("concentrateur", URLEncoder.encode("Not a base 64 token!", "UTF-8"));
        builder.header("Authorization", SecurityUtils.buildAuthorizationHeader(map));
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("DSNLogin realm=\"Jeton manquant ou invalide\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
        assertEquals(0, response.getLength());
    }

    @Test
    @DisplayName("Telecharger 500 Test")
    void telecharger500Test() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/telecharger-retour/1.0/kaboom/kaboom");
        Builder builder = target.request();
        Map<String, String> map = new HashMap<>();
        map.put("jeton", TestData.declarantInscrit.getFauxJeton());
        builder.header("Authorization", SecurityUtils.buildAuthorizationHeader(map));
        Invocation invocation = builder.buildGet();
        // When
        Response response = invocation.invoke();
        response.close();
        // Then
        assertEquals(500, response.getStatus());
        assertNull(response.getHeaderString("Content-Type"));
        assertEquals(0, response.getLength());
    }
}
