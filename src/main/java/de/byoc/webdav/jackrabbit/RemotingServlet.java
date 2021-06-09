package de.byoc.webdav.jackrabbit;

import org.apache.jackrabbit.server.remoting.davex.JcrRemotingServlet;
import org.apache.jackrabbit.webdav.simple.SimpleWebdavServlet;

import javax.inject.Inject;
import javax.jcr.Repository;

public class RemotingServlet extends JcrRemotingServlet {

  @Inject
  Repository repo;

  @Override
  public Repository getRepository() {
    return repo;
  }
}
