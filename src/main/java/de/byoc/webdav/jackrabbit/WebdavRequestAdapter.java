package de.byoc.webdav.jackrabbit;

import io.undertow.servlet.spec.HttpServletRequestImpl;
import io.undertow.vertx.VertxHttpExchange;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class WebdavRequestAdapter extends HttpServletRequestWrapper {

  private final String actualMethod;

  /**
   * Constructs a request object wrapping the given request.
   *
   * @param request the {@link HttpServletRequest} to be wrapped.
   * @throws IllegalArgumentException if the request is null
   */
  public WebdavRequestAdapter(HttpServletRequest request) {
    super(request);

    actualMethod = ((RoutingContext) ((VertxHttpExchange) ((HttpServletRequestImpl) request)
            .getExchange().getDelegate()).getContext()).request().rawMethod();
  }

  @Override
  public String getMethod() {
    return actualMethod;
  }

}
