package pl.lodz.p.it.ssbd2022.ssbd02.util;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@SignatureValidatorFilter
public class SignatureValidator implements ContainerRequestFilter {

    private static final String HEADER_IF_MATCH = "If-match";

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String header = containerRequestContext.getHeaderString(HEADER_IF_MATCH);
        if (header == null || header.isEmpty()) {
            containerRequestContext.abortWith(Response.status(Response.Status.PRECONDITION_REQUIRED).build());
        } else if (!SignatureVerifier.validateEntitySignature(header)) {
            containerRequestContext.abortWith(Response.status(Response.Status.PRECONDITION_FAILED).build());
        }
    }
}
