//ImageService REST endpoint for the monolith to access the image service
package com.ibm.websphere.samples.pbw.ms.image;
 
import java.io.IOException;
import java.io.OutputStream;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
  
@Path("/inventory")
public class ImageService {
         
        private EntityManager getEntityManager() {
                try {
                        Context ctx = new InitialContext();
                        return (EntityManager)ctx.lookup("java:comp/env/images/em"); 
                } catch (NamingException e) {
                }
                return null;
        }
         
        @GET
        @Produces({"image/jpeg"})
        @Path("{inventoryID}")
        public Response getMessage(@PathParam("inventoryID") String inventoryID) {
                Inventory inv = getInv(inventoryID);
                if (inv != null) {
                        final byte[] retval = inv.getImgbytes();
                        StreamingOutput stream = new StreamingOutput() {
                                public void write(OutputStream output) throws IOException, WebApplicationException {
                                        output.write(retval);
                                }
                        };
                        return Response.ok(stream).build();
                }
                return Response.status(Response.Status.NOT_FOUND).build();
        }
  
        private Inventory getInv(String inventoryID) {
                EntityManager em = getEntityManager();
                if (em == null) {
                        return null;
                }
                return (Inventory)em.find(Inventory.class, inventoryID);
        }
}