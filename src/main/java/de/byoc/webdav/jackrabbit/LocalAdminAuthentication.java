package de.byoc.webdav.jackrabbit;

import org.apache.jackrabbit.core.security.authentication.Authentication;

import javax.jcr.Credentials;

public class LocalAdminAuthentication implements Authentication {
    @Override
    public boolean canHandle(final Credentials credentials) {
        return credentials instanceof LocalAdminCredentials;
    }

    @Override
    public boolean authenticate(final Credentials credentials) {
        return credentials instanceof LocalAdminCredentials;
    }
}
