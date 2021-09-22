package de.byoc.webdav.jackrabbit;

import org.apache.jackrabbit.api.security.authentication.token.TokenCredentials;
import org.apache.jackrabbit.core.security.UserPrincipal;
import org.apache.jackrabbit.core.security.authentication.Authentication;
import org.apache.jackrabbit.core.security.authentication.DefaultLoginModule;
import org.apache.jackrabbit.value.StringValue;

import java.security.Principal;
import java.util.Map;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

public class LocalLoginModule extends DefaultLoginModule {

    private Session session;

    @Override
    protected void doInit(final CallbackHandler callbackHandler, final Session session, final Map options) throws LoginException {
        super.doInit(callbackHandler, session, options);
        this.session = session;
    }

    @Override
    protected Authentication getAuthentication(final Principal principal, final Credentials creds) throws RepositoryException {
        if (creds instanceof LocalAdminCredentials) {
            return new LocalAdminAuthentication();
        }

        if (creds instanceof TokenCredentials) {
            return new LocalTokenAuthentication(principal, creds);
        }

        return super.getAuthentication(principal, creds);
    }

    @Override
    protected Principal getPrincipal(final Credentials credentials) {
        if (credentials instanceof LocalAdminCredentials) {
            return new UserPrincipal("LocalAdmin");
        }
        if (credentials instanceof TokenCredentials) {
            return mapPrincipal((TokenCredentials) credentials);
        }
        return super.getPrincipal(credentials);
    }

    private Principal mapPrincipal(final TokenCredentials credentials) {
        try {
            // Löse auf: Token -> Nutzer
            final Query query = session.getWorkspace().getQueryManager()
                .createQuery("select * from [nt:unstructured] where [token]=$token", Query.JCR_SQL2);
            query.bindValue("token", new StringValue(credentials.getToken()));
            query.execute().getRows().forEachRemaining(x -> System.out.println(x));

            var x = session.getRepository().login(new LocalAdminCredentials());
            final Node secrets = x.getRootNode().getNode("secrets");
            if (secrets.hasNode(credentials.getToken())) {
                var node = secrets.getNode(credentials.getToken());
                return new UserPrincipal(node.getProperty("username").getString());
            }
            x.logout();

            return new UserPrincipal(credentials.getAttribute("UserId"));
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean supportsCredentials(final Credentials creds) {
        return creds instanceof TokenCredentials
            || creds instanceof LocalAdminCredentials
            || super.supportsCredentials(creds);
    }

    @Override
    protected String getUserID(final Credentials credentials) {
        if (credentials instanceof LocalAdminCredentials) {
            return "admin";
        }

        if (credentials instanceof TokenCredentials) {
            return ((TokenCredentials) credentials).getAttribute("UserId");
        }
        return super.getUserID(credentials);
    }
}
