package fr.gipmds.dsn.test;

import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;

public class BaseTestClass {
    @TestServer
    private static EmbeddedJetty server;

    protected String getServerUrl() {
        String url = server.getUrl();
        // make sure the url does not end with /
        url = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
        // make sure where using HTTPS
        return url;
    }

    protected String getServerSecureUrl() {
        return getServerUrl().replace("http://", "https://");
    }
}
