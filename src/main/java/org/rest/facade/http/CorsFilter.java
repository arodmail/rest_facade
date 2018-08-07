package org.rest.facade.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * A servlet filter used to generate standard HTTP response headers to
 * pre-flight and other requests from web browser clients in different domains.
 * <p>
 * For details, see "Cross-Origin Resource Sharing": http://www.w3.org/TR/cors
 *
 * @author A. Rodriguez
 */
public class CorsFilter implements Filter {

    private final Logger logger = LogManager.getLogger(getClass().getName());

    /**
     * Provides a list of domains from which the resource can be accessed in a
     * cross-domain manner, or * as a wildcard.
     */
    private static final String ACCESS_CONTROL_ALLOW_ORIGIN
            = "Access-Control-Allow-Origin";

    /**
     * A comma-separated list of HTTP methods that are allowed to be sent
     * cross-domain.
     */
    private static final String ACCESS_CONTROL_ALLOW_METHODS
            = "Access-Control-Allow-Methods";

    /**
     * Holds a boolean value to indicate whether or not the response to the
     * request can be exposed when the credentials flag is true. When used as
     * part of a response to a pre-flight request. Indicates whether or not the
     * actual request can be made using credentials.
     */
    private static final String ACCESS_CONTROL_ALLOW_CREDENTIALS
            = "Access-Control-Allow-Credentials";

    /**
     * Used in response to a preflight request to indicate which HTTP headers
     * can be used when making the actual request.
     */
    private static final String ACCESS_CONTROL_ALLOW_HEADERS
            = "Access-Control-Allow-Headers";

    /**
     * Used in response to a preflight request to indicate to set the location header
     */
    private static final String ACCESS_CONTROL_EXPOSE_HEADERS
            = "Access-Control-Expose-Headers";

    /**
     * Basic Auth header name.
     */
    private static final String AUTHORIZATION = "Authorization";

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     *
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String origin = httpRequest.getHeader("Origin");
        verifyOrigin(origin);

        String referer = httpRequest.getHeader("Referer");
        if (referer != null && origin != null && !referer.startsWith(origin) && httpRequest.getPathInfo().contains("/Login")) {
            logger.warn(String.format("CorsFilter: Potential CSRF request from referer: %s origin: %s", referer, origin));
            throw new ServletException("Potential CSRF request");
        }

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN, origin);
        httpResponse.addHeader(ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, PATCH, DELETE, OPTIONS");
        httpResponse.addHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        httpResponse.addHeader(ACCESS_CONTROL_EXPOSE_HEADERS, "Location");

        Enumeration headers = httpRequest.getHeaderNames();

        StringBuilder allowedHeaders = new StringBuilder();
        while (headers.hasMoreElements()) {
            allowedHeaders.append(headers.nextElement()).append(",");
        }

        // but last ','
        String headersAllowed =
                allowedHeaders.toString().substring(0, allowedHeaders.toString().length() - 1);

        headersAllowed += ",x-requested-with";
        headersAllowed += ",Content-type";
        headersAllowed += ",Location";

        if (httpRequest.getHeader(AUTHORIZATION) == null) {
            headersAllowed += "," + AUTHORIZATION;
        }

        httpResponse.addHeader(ACCESS_CONTROL_ALLOW_HEADERS, headersAllowed);

        if (!httpRequest.getMethod().equals("OPTIONS")) {
            chain.doFilter(request, response);
        } else {
            if (origin == null) //On the preflight, make sure we have a valid origin
            {
                logger.warn("Origin header is required for OPTIONS request");
                throw new ServletException(request.getClass().getName() + "::" + response.getClass().getName());
            }
        }
    }

    public void destroy() {
    }

    public void verifyOrigin(String origin) throws ServletException {
        if (StringUtils.isEmpty(origin)) {
            return;  //nothing to validate
        }

        String[] domains = {""};
        if (domains != null) {
            for (String domain : domains) {
                if (origin.contains(domain.trim())) {
                    logger.debug(String.format("CorsFilter: allowing origin: %s", origin));
                    return;
                }
            }
            logger.warn(String.format("CorsFilter: rejecting origin: %s", origin));
            throw new ServletException("Origin not allowed.");
        }
    }

}