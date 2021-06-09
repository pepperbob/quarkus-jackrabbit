package de.byoc.webdav.jackrabbit;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.apache.jackrabbit.spi.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

@ApplicationScoped
public class EventListenerThingy implements EventListener {

  private static final Logger LOG = LoggerFactory.getLogger(EventListenerThingy.class);

  @Inject
  Repository repo;

  Session session;

  void starup(@Observes StartupEvent event) throws RepositoryException {
    this.session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
    this.session.getWorkspace().getObservationManager()
            .addEventListener(this, Event.ALL_TYPES, "/", true, null, null, true);
  }

  void shutdown(@Observes ShutdownEvent event) {
    this.session.logout();
  }


  @Override
  public void onEvent(EventIterator events) {
    while (events.hasNext()) {
      var event = events.nextEvent();
      try {
        LOG.info("Type={}, {}", event.getType(), event.getPath());
      } catch (RepositoryException e) {
        LOG.warn("", e);
      }

    }
  }
}
