package fr.gipmds.dsn.test.auth;

import fr.gipmds.dsn.modeles.RequeteAuthentificationNetE;
import fr.gipmds.dsn.services.auth.ServiceAuthentificationNetEImpl;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.core.SynchronousDispatcher;
import org.jboss.resteasy.core.SynchronousExecutionContext;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Authentifier Net E Test")
class AuthentifierNetETest {

    // public static String testUri = TestUtils.baseUriAuthentification;
    private static Dispatcher dispatcher;

    @BeforeAll
    public static void setup() {
        dispatcher = MockDispatcherFactory.createDispatcher();
        ServiceAuthentificationNetEImpl serviceAuthentificationNetE = new ServiceAuthentificationNetEImpl();
        dispatcher.getRegistry().addSingletonResource(serviceAuthentificationNetE);
    }

    @Test
    @DisplayName("Authentifier 200 Concentrateur Test")
    void authentifier200ConcentrateurTest() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret("18751249600022");
        requestBody.setNom("CTR2000");
        requestBody.setPrenom("concentrateur");
        requestBody.setMotdepasse("azerty");
        requestBody.setService("98");
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.APPLICATION_XML_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Tests
        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getOutputHeaders().getFirst("Content-Type").toString());
    }

    @Test
    @DisplayName("Authentifier 200 Concentrateur Test")
    void authentifier406ConcentrateurAcceptEncodingMauvaisTest() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret("18751249600022");
        requestBody.setNom("CTR2000");
        requestBody.setPrenom("concentrateur");
        requestBody.setMotdepasse("azerty");
        requestBody.setService("98");
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.APPLICATION_XML_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Tests
        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getOutputHeaders().getFirst("Content-Type").toString());
    }

    @Test
    @DisplayName("Authentifier 401 Concentrateur Erreur Authentification Test")
    void authentifier401ConcentrateurErreurAuthentificationTest() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret("99900000300015");
        requestBody.setNom("concentrateur");
        requestBody.setPrenom("concentrateur");
        requestBody.setMotdepasse("qwerty42");
        requestBody.setService("98");
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.APPLICATION_XML_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("Basic realm=\"net-entreprises.fr\"", response.getOutputHeaders().getFirst("WWW-Authenticate"));
        assertNull(response.getOutputHeaders().getFirst("Content-Type"));
        assertEquals(0, response.getContentAsString().length());
    }

    @Test
    @DisplayName("Authentifier 401 Concentrateur Non Inscrit Test")
    void authentifier401ConcentrateurNonInscritTest() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret("99900000300325");
        requestBody.setNom("concentrateur");
        requestBody.setPrenom("concentrateur");
        requestBody.setMotdepasse("Refonte01");
        requestBody.setService("98");
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.APPLICATION_XML_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("Basic realm=\"net-entreprises.fr\"", response.getOutputHeaders().getFirst("WWW-Authenticate"));
        assertNull(response.getOutputHeaders().getFirst("Content-Type"));
        assertEquals(0, response.getContentAsString().length());
    }

    @Test
    @DisplayName("Authentifier 200 Declarant RG Test")
    void authentifier200DeclarantRGTest() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret("99900000300015");
        requestBody.setNom("declarant");
        requestBody.setPrenom("declarant");
        requestBody.setMotdepasse("Refonte01");
        requestBody.setService("97");
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.APPLICATION_XML_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Then
        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getOutputHeaders().getFirst("Content-Type").toString());
    }

    @Test
    @DisplayName("Authentifier 200 Declarant RA Test")
    void authentifier200DeclarantRATest() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret("99900000300015");
        requestBody.setNom("declarant");
        requestBody.setPrenom("declarant");
        requestBody.setMotdepasse("Refonte01");
        requestBody.setService("94");
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.APPLICATION_XML_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Then
        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getOutputHeaders().getFirst("Content-Type").toString());
    }

    @Test
    @DisplayName("Authentifier 401 Declarant Erreur Authentification Test")
    void authentifier401DeclarantErreurAuthentificationTest() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret("99900000300325");
        requestBody.setNom("declarant");
        requestBody.setPrenom("declarant");
        requestBody.setMotdepasse("Refonte01");
        requestBody.setService("94");
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.APPLICATION_XML_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("Basic realm=\"net-entreprises.fr\"", response.getOutputHeaders().getFirst("WWW-Authenticate"));
        assertNull(response.getOutputHeaders().getFirst("Content-Type"));
        assertEquals(0, response.getContentAsString().length());
    }

    @Test
    @DisplayName("Authentifier 401 Declarant Non Inscrit Test")
    void authentifier401DeclarantNonInscritTest() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret("99900000300325");
        requestBody.setNom("declarant");
        requestBody.setPrenom("declarant");
        requestBody.setMotdepasse("Refonte01");
        requestBody.setService("97");
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.APPLICATION_XML_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Then
        assertEquals(401, response.getStatus());
        assertEquals("Basic realm=\"net-entreprises.fr\"", response.getOutputHeaders().getFirst("WWW-Authenticate"));
        assertNull(response.getOutputHeaders().getFirst("Content-Type"));
        assertEquals(0, response.getContentAsString().length());
    }

    @Test
    @DisplayName("Authentifier 422 Siret Null Test")
    void authentifier422SiretNullTest() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret(null);
        requestBody.setNom("declarant");
        requestBody.setPrenom("declarant");
        requestBody.setMotdepasse("Refonte01");
        requestBody.setService("97");
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.APPLICATION_XML_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Then
        assertEquals(422, response.getStatus());
        assertNull(response.getOutputHeaders().getFirst("Content-Type"));
        assertEquals(0, response.getContentAsString().length());
    }

    @Test
    @DisplayName("Authentifier 422 Siret Trop Court Test")
    void authentifier422SiretTropCourtTest() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret("99900000");
        requestBody.setNom("concentrateur");
        requestBody.setPrenom("concentrateur");
        requestBody.setMotdepasse("Refonte01");
        requestBody.setService("98");
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.APPLICATION_XML_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Then
        assertEquals(422, response.getStatus());
        assertNull(response.getOutputHeaders().getFirst("Content-Type"));
        assertEquals(0, response.getContentAsString().length());
    }

    @Test
    @DisplayName("Authentifier 422 Siret Trop Long Test")
    void authentifier422SiretTropLongTest() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret("999000003000154563258951");
        requestBody.setNom("declarant");
        requestBody.setPrenom("declarant");
        requestBody.setMotdepasse("Refonte01");
        requestBody.setService("94");
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.APPLICATION_XML_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Then
        assertEquals(422, response.getStatus());
        assertNull(response.getOutputHeaders().getFirst("Content-Type"));
        assertEquals(0, response.getContentAsString().length());
    }

    @Test
    @DisplayName("Authentifier 422 Nom Null Test")
    void authentifier422NomNullTest() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret("99900000300015");
        requestBody.setNom(null);
        requestBody.setPrenom("concentrateur");
        requestBody.setMotdepasse("Refonte01");
        requestBody.setService("98");
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.APPLICATION_XML_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Then
        assertEquals(422, response.getStatus());
        assertNull(response.getOutputHeaders().getFirst("Content-Type"));
        assertEquals(0, response.getContentAsString().length());
    }

    @Test
    @DisplayName("Authentifier 422 Nom Trop Long Test")
    void authentifier422NomTropLongTest() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret("99900000300015");
        requestBody.setNom("declarant123456789abcdefghijklklklklklklklklklklklklklklklklklklk");
        requestBody.setPrenom("declarant");
        requestBody.setMotdepasse("Refonte01");
        requestBody.setService("94");
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.APPLICATION_XML_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Then
        assertEquals(422, response.getStatus());
        assertNull(response.getOutputHeaders().getFirst("Content-Type"));
        assertEquals(0, response.getContentAsString().length());
    }

    @Test
    @DisplayName("Authentifier 422 Prenom Null Test")
    void authentifier422PrenomNullTest() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret("12345678901234");
        requestBody.setNom("CTR2000");
        requestBody.setPrenom(null);
        requestBody.setMotdepasse("azerty42");
        requestBody.setService("98");
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.APPLICATION_XML_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Then
        assertEquals(422, response.getStatus());
        assertNull(response.getOutputHeaders().getFirst("Content-Type"));
        assertEquals(0, response.getContentAsString().length());
    }

    @Test
    @DisplayName("Authentifier 422 Prenom Trop Long Test")
    void authentifier422PrenomTropLongTest() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret("99900000300015");
        requestBody.setNom("declarant");
        requestBody.setPrenom("declarant123456789lklklklklklklklklklklk");
        requestBody.setMotdepasse("Refonte01");
        requestBody.setService("97");
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.APPLICATION_XML_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Then
        assertEquals(422, response.getStatus());
        assertNull(response.getOutputHeaders().getFirst("Content-Type"));
        assertEquals(0, response.getContentAsString().length());
    }

    @Test
    @DisplayName("Authentifier 422 Motdepasse Null Test")
    void authentifier422MotdepasseNullTest() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret("12345678901234");
        requestBody.setNom("CTR2000");
        requestBody.setPrenom("concentrateur");
        requestBody.setMotdepasse(null);
        requestBody.setService("98");
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.APPLICATION_XML_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Then
        assertEquals(422, response.getStatus());
        assertNull(response.getOutputHeaders().getFirst("Content-Type"));
        assertEquals(0, response.getContentAsString().length());
    }

    @Test
    @DisplayName("Authentifier 422 Motdepasse Trop Long Test")
    void authentifier422MotdepasseTropLongTest() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret("99900000300015");
        requestBody.setNom("declarant");
        requestBody.setPrenom("declarant");
        requestBody.setMotdepasse("Refonte01457896sgggggggggggggggggggggggggg");
        requestBody.setService("97");
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.APPLICATION_XML_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Then
        assertEquals(422, response.getStatus());
        assertNull(response.getOutputHeaders().getFirst("Content-Type"));
        assertEquals(0, response.getContentAsString().length());
    }

    @Test
    @DisplayName("Authentifier 422 Service Null Test")
    void authentifier422ServiceNullTest() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret("12345678901234");
        requestBody.setNom("CTR2000");
        requestBody.setPrenom("concentrateur");
        requestBody.setMotdepasse("azerty42");
        requestBody.setService(null);
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.APPLICATION_XML_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Then
        assertEquals(422, response.getStatus());
        assertNull(response.getOutputHeaders().getFirst("Content-Type"));
        assertEquals(0, response.getContentAsString().length());
    }

    @Test
    @DisplayName("Authentifier 422 Service Trop Long Test")
    void authentifier422ServiceTropLongTest() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret("99900000300015");
        requestBody.setNom("concentrateur");
        requestBody.setPrenom("concentrateur");
        requestBody.setMotdepasse("Refonte01");
        requestBody.setService("007");
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.APPLICATION_XML_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Then
        assertEquals(422, response.getStatus());
        assertNull(response.getOutputHeaders().getFirst("Content-Type"));
        assertEquals(0, response.getContentAsString().length());
    }

    @Test
    @DisplayName("Authentifier 415 Content Type Mauvais Test")
    void authentifier415ContentTypeMauvaisTest() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret("12345678901234");
        requestBody.setNom("CTR2000");
        requestBody.setPrenom("concentrateur");
        requestBody.setMotdepasse("azerty42");
        requestBody.setService("98");
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.TEXT_PLAIN_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Then
        assertEquals(415, response.getStatus());
        assertNull(response.getOutputHeaders().getFirst("Content-Type"));
        assertEquals(0, response.getContentAsString().length());
    }

    @Test
    @DisplayName("Authentifier 500 Test")
    void authentifier500Test() throws Exception {
        // construction du body
        RequeteAuthentificationNetE requestBody = new RequeteAuthentificationNetE();
        requestBody.setSiret("12345677894563");
        requestBody.setNom("kaboom");
        requestBody.setPrenom("kaboom");
        requestBody.setMotdepasse("kaboom");
        requestBody.setService("17");
        // requête
        MockHttpRequest request = MockHttpRequest.post("/authentifier/1.0/");
        request.contentType(MediaType.APPLICATION_XML_TYPE);
        request.content(requestBody.toString().getBytes());
        MockHttpResponse response = new MockHttpResponse();
        SynchronousExecutionContext synchronousExecutionContext = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(synchronousExecutionContext);
        dispatcher.invoke(request, response);
        // Then
        assertEquals(500, response.getStatus());
        assertNull(response.getOutputHeaders().getFirst("Content-Type"));
        assertEquals(0, response.getContentAsString().length());
    }
}
