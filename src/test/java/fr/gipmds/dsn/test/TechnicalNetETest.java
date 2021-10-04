package fr.gipmds.dsn.test;

import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;

@ExtendWith(JunitServerExtension.class)
@DisplayName("Technical Net E Test")
class TechnicalNetETest extends BaseTestClass {

    @Disabled("POC : certificates and https not enabled on embedded Server")
    @Test
    @DisplayName("No SSL Test")
    void noSSLTest() {
        // Given
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(getServerUrl().replace("https", "http") + "/telecharger-retour/1.0/abcdefghij/12345");
        Builder builder = target.request();
        Invocation invocation = builder.buildGet();
        Assertions.assertThrows(ProcessingException.class, () -> {
            // When
            invocation.invoke();
            Assertions.fail("expected ProcessingException to be thrown");
        });
    }
}
