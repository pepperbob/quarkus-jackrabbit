package de.byoc.webdav.jackrabbit;

import org.apache.jackrabbit.api.security.authentication.token.TokenCredentials;
import org.apache.jackrabbit.server.CredentialsProvider;

import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class SessionInPathCredentialsProvider implements CredentialsProvider {
    private CredentialsProvider fallback;

    public SessionInPathCredentialsProvider(final CredentialsProvider fallback) {
        this.fallback = fallback;
    }

    @Override
    public Credentials getCredentials(final HttpServletRequest request) throws ServletException, LoginException {
        if (request.getRequestURI().contains("/abc/")) {
            final TokenCredentials token = new TokenCredentials("token");
            token.setAttribute("UserId", "isabella"); // wo bekommen wir das her?
            return token;
        }
        return fallback.getCredentials(request);
    }
}
