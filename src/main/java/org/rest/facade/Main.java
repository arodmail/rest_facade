package org.rest.facade;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.rest.facade.http.CorsFilter;
import org.rest.facade.http.RestControlServlet;

import java.io.File;

/**
 * Main class to start embedded Tomcat and to deploy the RestControlServlet plus
 * any filters.
 *
 * @author A. Rodriguez
 */
public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        logger.debug("Starting Tomcat");
        Tomcat tomcat = new Tomcat();
        tomcat.getService().removeConnector(tomcat.getConnector());
        tomcat.getService().addConnector(getHttpConnector());

        logger.debug("Deploying RestControlServlet");
        Context ctx =
                tomcat.addContext("", new File(".").getAbsolutePath());
        Wrapper controlServlet =
                Tomcat.addServlet(ctx, "RestControlServlet", new RestControlServlet());
        controlServlet.addInitParameter("buffer-requests", "true");
        controlServlet.addInitParameter("configPath", "rest-resources.xml");
        ctx.addServletMappingDecoded("/rest/*", "RestControlServlet");

        logger.debug("Adding CorsFilter");
        FilterDef filterDef = new FilterDef();
        filterDef.setFilterClass(CorsFilter.class.getName());
        filterDef.setFilterName("CorsFilter");
        ctx.addFilterDef(filterDef);

        FilterMap filterMap = new FilterMap();
        filterMap.addURLPattern("/*");
        filterMap.setFilterName("CorsFilter");
        ctx.addFilterMap(filterMap);

        tomcat.start();
        logger.debug("Tomcat listening on Port " + tomcat.getService().findConnectors()[0].getPort());
        tomcat.getServer().await();
    }

    private static Connector getHttpConnector() throws Exception {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(4000);
        connector.setSecure(false);
        return connector;
    }

}
