package eu.itech.caas;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configures a JAX-RS endpoint. Delete this class, if you are not exposing
 * JAX-RS resources in your application.
 * @author adam-bien.com
 */
@ApplicationPath("resources")
public class RESTConfiguration extends Application {

}
