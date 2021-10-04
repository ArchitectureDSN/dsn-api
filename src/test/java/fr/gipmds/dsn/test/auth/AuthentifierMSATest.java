package fr.gipmds.dsn.test.auth;

import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;
import fr.gipmds.dsn.modeles.RequeteAuthentificationMSA;
import fr.gipmds.dsn.test.BaseTestClass;
import fr.gipmds.dsn.test.resources.TestData;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(JunitServerExtension.class)
@DisplayName("Authentifier MSA Test")
class AuthentifierMSATest extends BaseTestClass {

    @Test
    @DisplayName("Authentifier 200 Test")
    void authentifier200Test() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/authentifier/2.0");
        RequeteAuthentificationMSA requete = new RequeteAuthentificationMSA();
        requete.setIdentifiant("wwallace");
        requete.setMotdepasse("azerty");
        // When
        Variant variant = new Variant(MediaType.valueOf(MediaType.APPLICATION_XML), Locale.FRANCE, "gzip");
        Response response = target.request().post(Entity.entity(requete, variant));
        String actual = response.readEntity(String.class);
        response.close();
        // Then
        assertEquals(200, response.getStatus());
        assertEquals(TestData.declarantInscrit.getFauxJeton(), actual);
    }

    @Test
    @DisplayName("Authentifier 406 Test")
    void authentifier406AcceptEncodingMauvaisTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/authentifier/2.0");
        RequeteAuthentificationMSA requete = new RequeteAuthentificationMSA();
        requete.setIdentifiant("wwallace");
        requete.setMotdepasse("azerty");
        // When
        Variant variant = new Variant(MediaType.valueOf(MediaType.APPLICATION_XML), Locale.FRANCE, "gzip");
        Response response = target.request()
                .acceptEncoding("deflate")  // doit contenir gzip ou ne rien préciser
                .post(Entity.entity(requete, variant));
        String actual = response.readEntity(String.class);
        response.close();
        // Then
        assertEquals(406, response.getStatus());
    }

    @Test
    @DisplayName("Authentifier 401 Erreur Authentification Test")
    void authentifier401ErreurAuthentificationTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/authentifier/2.0");
        RequeteAuthentificationMSA requete = new RequeteAuthentificationMSA();
        requete.setIdentifiant("wwallace");
        requete.setMotdepasse("qwerty");    // mot de passe non valide
        // When
        Variant variant = new Variant(MediaType.valueOf(MediaType.APPLICATION_XML), Locale.FRANCE, "gzip");
        Response response = target.request().post(Entity.entity(requete, variant));
        String actual = response.readEntity(String.class);
        response.close();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("Basic realm=\"msa.fr\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
        assertEquals(0, actual.length());
    }

    @Test
    @DisplayName("Authentifier 401 Non Inscrit Test")
    void authentifier401NonInscritTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/authentifier/2.0");
        RequeteAuthentificationMSA requete = new RequeteAuthentificationMSA();
        requete.setIdentifiant("sseagal");    // identifiant non valide
        requete.setMotdepasse("azerty");
        // When
        Variant variant = new Variant(MediaType.valueOf(MediaType.APPLICATION_XML), Locale.FRANCE, "gzip");
        Response response = target.request().post(Entity.entity(requete, variant));
        String actual = response.readEntity(String.class);
        response.close();
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("Basic realm=\"msa.fr\"", response.getHeaderString("WWW-Authenticate"));
        assertNull(response.getHeaderString("Content-Type"));
        assertEquals(0, actual.length());
    }

    @Test
    @DisplayName("Authentifier 422 Identifiant Null Test")
    void authentifier422IdentifiantNullTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/authentifier/2.0");
        RequeteAuthentificationMSA requete = new RequeteAuthentificationMSA();
        requete.setIdentifiant(null);  // identifiant non existant
        requete.setMotdepasse("azerty");
        // When
        Variant variant = new Variant(MediaType.valueOf(MediaType.APPLICATION_XML), Locale.FRANCE, "gzip");
        Response response = target.request().post(Entity.entity(requete, variant));
        String actual = response.readEntity(String.class);
        response.close();
        // Then
        assertEquals(422, response.getStatus());
        assertNull(response.getHeaderString("Content-Type"));
        assertEquals(0, actual.length());
    }

    @Test
    @DisplayName("Authentifier 422 Identifiant Trop Long Test")
    void authentifier422IdentifiantTropLongTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/authentifier/2.0");
        RequeteAuthentificationMSA requete = new RequeteAuthentificationMSA();
        requete.setIdentifiant("wwalz0123456789012345678901234567890");  // identifiant trop long
        requete.setMotdepasse("azerty");
        // When
        Variant variant = new Variant(MediaType.valueOf(MediaType.APPLICATION_XML), Locale.FRANCE, "gzip");
        Response response = target.request().post(Entity.entity(requete, variant));
        String actual = response.readEntity(String.class);
        response.close();
        // Then
        assertEquals(422, response.getStatus());
        assertNull(response.getHeaderString("Content-Type"));
        assertEquals(0, actual.length());
    }

    @Test
    @DisplayName("Authentifier 422 Motdepasse Null Test")
    void authentifier422MotdepasseNullTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/authentifier/2.0");
        RequeteAuthentificationMSA requete = new RequeteAuthentificationMSA();
        requete.setIdentifiant("wwallace");
        requete.setMotdepasse(null);   // mot de passe non existant
        // When
        Variant variant = new Variant(MediaType.valueOf(MediaType.APPLICATION_XML), Locale.FRANCE, "gzip");
        Response response = target.request().post(Entity.entity(requete, variant));
        String actual = response.readEntity(String.class);
        response.close();
        // Then
        assertEquals(422, response.getStatus());
        assertNull(response.getHeaderString("Content-Type"));
        assertEquals(0, actual.length());
    }

    @Test
    @DisplayName("Authentifier 422 Motdepasse Trop Long Test")
    void authentifier422MotdepasseTropLongTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/authentifier/2.0/");
        RequeteAuthentificationMSA requete = new RequeteAuthentificationMSA();
        requete.setIdentifiant("wwallace");
        requete.setMotdepasse("0123456789012345678901234567890");  // mot de passe trop long
        // When
        Variant variant = new Variant(MediaType.valueOf(MediaType.APPLICATION_XML), Locale.FRANCE, "gzip");
        Response response = target.request().post(Entity.entity(requete, variant));
        response.readEntity(String.class);
        response.close();
        // Then
        assertEquals(422, response.getStatus());
    }

    @Test
    @DisplayName("Authentifier 415 Content Type Mauvais Test")
    void authentifier415ContentTypeMauvaisTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/authentifier/2.0");
        RequeteAuthentificationMSA requete = new RequeteAuthentificationMSA();
        requete.setIdentifiant("wwallace");
        requete.setMotdepasse("azerty");
        Variant variant = new Variant(MediaType.valueOf(MediaType.TEXT_PLAIN /* ContentType non valide */), Locale.FRANCE, "gzip");
        Invocation invocation = target.request().acceptEncoding("gzip").buildPost(Entity.entity(requete, variant));
        // When
        Response response = invocation.invoke();
        response.close();
        // Then
        assertEquals(415, response.getStatus());
        assertNull(response.getHeaderString("Content-Type"));
        assertEquals(0, response.getLength());
    }

    @Test
    @DisplayName("Authentifier 500 Test")
    void authentifier500Test() {
        //pas d'erreur côté client , mais c'est une erreur du serveur
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl() + "/authentifier/2.0");
        RequeteAuthentificationMSA requete = new RequeteAuthentificationMSA();
        requete.setIdentifiant("kaboom");
        requete.setMotdepasse("kaboom");
        // When
        Variant variant = new Variant(MediaType.valueOf(MediaType.APPLICATION_XML), Locale.FRANCE, "gzip");
        Response response = target.request().post(Entity.entity(requete, variant));
        response.readEntity(String.class);
        response.close();
        // Then
        assertEquals(500, response.getStatus());
        assertNull(response.getHeaderString("Content-Type"));
        assertEquals(0, response.getLength());
    }
}
