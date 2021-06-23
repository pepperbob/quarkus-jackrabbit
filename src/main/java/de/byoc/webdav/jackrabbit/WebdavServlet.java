package de.byoc.webdav.jackrabbit;

import org.apache.jackrabbit.server.CredentialsProvider;
import org.apache.jackrabbit.webdav.DavLocatorFactory;
import org.apache.jackrabbit.webdav.DavResourceLocator;
import org.apache.jackrabbit.webdav.simple.SimpleWebdavServlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.jcr.Repository;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebdavServlet extends SimpleWebdavServlet {

    @Inject
    Repository repo;

    @Override
    public Repository getRepository() {
        return repo;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(new WebdavRequestAdapter(req), resp);
    }

    @Override
    protected CredentialsProvider getCredentialsProvider() {
        return new SessionInPathCredentialsProvider(super.getCredentialsProvider());
    }

    @Override
    public DavLocatorFactory getLocatorFactory() {
        return new SecurityTokenAwareLocatorFactory(super.getLocatorFactory());
    }

    private class SecurityTokenAwareLocatorFactory implements DavLocatorFactory {

        private DavLocatorFactory delegate;

        public SecurityTokenAwareLocatorFactory(final DavLocatorFactory delegate) {
            this.delegate = delegate;
        }

        @Override
        public DavResourceLocator createResourceLocator(final String prefix, final String href) {
            // href contains token
            return delegate.createResourceLocator(prefix, href.replace("/abc", ""));
        }

        @Override
        public DavResourceLocator createResourceLocator(final String prefix, final String workspacePath, final String resourcePath) {
            return delegate.createResourceLocator(prefix, workspacePath, resourcePath);
        }

        @Override
        public DavResourceLocator createResourceLocator(final String prefix, final String workspacePath, final String path, final boolean isResourcePath) {
            return delegate.createResourceLocator(prefix, workspacePath, path, isResourcePath);
        }

    }
}
