// Enables JAX-RS

package com.ibm.websphere.samples.pbw.ms.image;
 
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
 
@ApplicationPath("/product/*")
public class ImageApplication extends Application
{

}