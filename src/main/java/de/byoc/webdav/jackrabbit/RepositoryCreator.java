package de.byoc.webdav.jackrabbit;

import org.apache.jackrabbit.api.JackrabbitRepository;
import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.RepositoryConfig;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import java.io.File;

public class RepositoryCreator {

  private static JackrabbitRepository repo;

  static JackrabbitRepository instance() {
    var lockme = "lock";
    if (repo == null) {
      try {
        synchronized (lockme) {
          if (repo == null)
            repo = RepositoryImpl.create(RepositoryConfig.create(
                    new File("/home/michael/IdeaProjects/quarkus-jackrabbit/jackrabbit")));
        }
      } catch (RepositoryException e) {
        throw new RuntimeException(e);
      }
    }
    return repo;
  }


}
