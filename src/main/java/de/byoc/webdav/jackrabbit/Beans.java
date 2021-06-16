package de.byoc.webdav.jackrabbit;

import org.apache.jackrabbit.commons.JcrUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import java.util.Map;

import static org.apache.jackrabbit.core.RepositoryFactoryImpl.REPOSITORY_CONF;
import static org.apache.jackrabbit.core.RepositoryFactoryImpl.REPOSITORY_HOME;

public class Beans {

  @ApplicationScoped
  @Produces
  public Repository repo() throws RepositoryException {
    return JcrUtils.getRepository(Map.of(
            REPOSITORY_HOME, "schnuffel-repo",
            REPOSITORY_CONF, "classpath:my_repository.xml"
    ));
  }


}
