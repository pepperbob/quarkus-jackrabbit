package de.byoc.webdav.jackrabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.UUID;

@Path("/hello")
public class MyApp {

  private static final Logger LOG = LoggerFactory.getLogger(MyApp.class);

  @Inject
  Repository repo;

  @POST
  @Path("/{filename: .+}")
  public Response hello(byte[] body, @PathParam("filename") String filename,
                        @HeaderParam("Content-Type") String contentType) throws RepositoryException {
    Session session = repo.login(
            new SimpleCredentials("admin", "admin".toCharArray()));
    String fileId = UUID.randomUUID().toString();
    try {
      Binary binary = session.getValueFactory().createBinary(new ByteArrayInputStream(body));
      Node folderNode = session.getRootNode();
      Node fileNode = folderNode.addNode (filename, "nt:file");

      //create the mandatory child node - jcr:content
      Node resNode = fileNode.addNode ("jcr:content", "nt:resource");
      resNode.setProperty ("jcr:mimeType", contentType);
      resNode.setProperty ("jcr:encoding", "utf-8");
      resNode.setProperty ("jcr:data", binary);
      Calendar lastModified = Calendar.getInstance ();
      resNode.setProperty ("jcr:lastModified", lastModified);
      session.save();
      return Response.accepted().header("FileId", fileId).build();
    } finally {
      session.logout();
    }
  }

  @GET
  @Path("/{fileId: .+}")
  @Produces(MediaType.TEXT_PLAIN)
  public Response getFile(@PathParam("fileId") String fileId) throws RepositoryException {
    Session session = repo.login(
            new SimpleCredentials("admin", "admin".toCharArray()));
    try {
      Node folder = session.getRootNode();

      Node node = folder.getNode(fileId + ".txt");
      Node contentNode = node.getNode("jcr:content");
      LOG.info("File found: {}", node.getProperties());
      InputStream content = contentNode.getProperty("jcr:data").getBinary().getStream();
      String contentType = contentNode.getProperty("jcr:mimeType").getString();
      return Response.ok(content).header("Content-Type", contentType).build();
    } finally {
      session.logout();
    }
  }

}