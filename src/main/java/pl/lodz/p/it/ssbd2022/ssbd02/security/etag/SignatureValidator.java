package pl.lodz.p.it.ssbd2022.ssbd02.security.etag;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;


/**
 * Filter sprawdzający podpisy ETag na przychodzących żądaniach z odpowiednią adnotacją
 */
@Provider
@SignatureValidatorFilter
public class SignatureValidator implements ContainerRequestFilter {

    @Inject
    SignatureVerifier signatureVerifier;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        String header = containerRequestContext.getHeaderString("if-Match");
        if (header == null || header.isEmpty()) {
            containerRequestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).build());
        } else if (!signatureVerifier.validateEntitySignature(header)) {
            containerRequestContext.abortWith(Response.status(Response.Status.PRECONDITION_FAILED).build());
        }
    }
}
