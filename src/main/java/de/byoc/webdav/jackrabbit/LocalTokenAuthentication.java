package de.byoc.webdav.jackrabbit;

import org.apache.jackrabbit.api.security.authentication.token.TokenCredentials;
import org.apache.jackrabbit.core.security.authentication.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;

import javax.jcr.Credentials;
import javax.jcr.RepositoryException;

public class LocalTokenAuthentication implements Authentication {

    private static final Logger LOG = LoggerFactory.getLogger(LocalTokenAuthentication.class);

    private final Principal principal;
    private final Credentials credentials;

    public LocalTokenAuthentication(final Principal principal, final Credentials creds) {
        this.principal = principal;
        this.credentials = creds;
    }

    @Override
    public boolean canHandle(final Credentials credentials) {
        return credentials instanceof TokenCredentials;
    }

    @Override
    public boolean authenticate(final Credentials credentials) throws RepositoryException {
        // passt Token zum Principal
        LOG.info("Authenticate {}", credentials);
        LOG.info("We have: {} - {}", this.principal, this.credentials);

        return true;
    }
}
