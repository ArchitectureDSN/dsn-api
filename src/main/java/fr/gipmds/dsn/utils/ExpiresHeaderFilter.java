package fr.gipmds.dsn.utils;

import fr.gipmds.dsn.modeles.Config;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.Date;

public class ExpiresHeaderFilter implements ContainerResponseFilter {

    public static final String EXPIRES = "Expires";

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if (responseContext.getStatus() != 200) {
            // only apply filter to 200 OK
            return;
        }
        final MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        headers.remove(EXPIRES); // remove the header if it exists from somewhere else
        headers.add(EXPIRES, new Date(new Date().getTime() + Config.RATE_LIMITING_BACKOFF));
    }
}
