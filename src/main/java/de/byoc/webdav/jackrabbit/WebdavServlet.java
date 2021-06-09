package de.byoc.webdav.jackrabbit;

import org.apache.jackrabbit.webdav.simple.SimpleWebdavServlet;

import javax.inject.Inject;
import javax.jcr.Repository;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

}
