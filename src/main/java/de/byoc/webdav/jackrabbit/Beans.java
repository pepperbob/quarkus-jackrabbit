package de.byoc.webdav.jackrabbit;

import org.apache.jackrabbit.commons.JcrUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;

public class Beans {

  @ApplicationScoped
  @Produces
  public Repository repo() throws RepositoryException {
    return JcrUtils.getRepository();
  }


}
