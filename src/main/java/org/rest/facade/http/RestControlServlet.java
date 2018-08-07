package org.rest.facade.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.facade.ServiceException;
import org.rest.facade.plan.ResponsePlan;
import org.rest.facade.plan.ResponsePlanFactory;
import org.rest.facade.response.*;
import org.rest.facade.support.ServiceConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * RestControlServlet is the front controller that services HTTP requests
 * to manage XPS resources. The following HTTP methods and URI-pattern(s)
 * are supported:
 * <p/>
 * <pre>
 * GET  /rest/
 * GET  /rest/{ResourceType}
 * GET  /rest/{ResourceType}/{ID}
 * POST /rest/{ResourceType}
 * PUT  /rest/{ResourceType}/{ID}
 * DELETE /rest/{ResourceType}/{ID}
 * </pre>
 * <p/>
 * HTTP POST requests are used by clients to add resources into the system and
 * are expected to include in the body of the HTTP request a JSON
 * formatted representation of the resource or resources to add.
 * <p/>
 * HTTP GET requests are used by clients to retrieve JSON representations
 * of different resource types, the type is specified in the last fragment of the
 * request URI.
 * <p/>
 * HTTP PUT requests are used to modify existing resources and, just as in POST
 * requests, the modified resource is expected to be included in the body of the
 * request. The ID of the resource to modify is expected to be specified
 * on the request URI.
 * <p/>
 * The responses from HTTP requests processed by the RestControlServlet are standard
 * HTTP 200 OK if the processing is successful, or 500 Internal Server Error if
 * the request cannot be processed without errors, for example, in cases where an
 * HTTP POST request in which the body of the request is found to be invalid or
 * not well-formed.
 *
 * @author A. Rodriguez
 */
public class RestControlServlet extends HttpServlet {

    /**
     * Logger.
     */
    private final Logger logger = LogManager.getLogger(getClass().getName());

    /**
     * String constant for config path
     */
    public static final String REST_RESOURCES = "rest-resources.xml";

    /**
     * Initializes the RestControlServlet by loading rest-resources.xml
     */
    @Override
    public void init() throws ServletException {
        try {
            ServiceConfig.gInstance().load(ClassLoader.getSystemResourceAsStream(REST_RESOURCES));
        } catch (Throwable t) {
            logger.error(t);
        }
    }

    /**
     * Handles GET requests.
     *
     * @param request  an HttpServletRequest object that contains the request the
     *                 client has made.
     * @param response an HttpServletResponse object that contains the response the
     *                 servlet sends to the client.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    /**
     * Handles POST requests.
     *
     * @param request  an HttpServletRequest object that contains the request the
     *                 client has made.
     * @param response an HttpServletResponse object that contains the response the
     *                 servlet sends to the client.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    /**
     * Handles PUT requests.
     *
     * @param request  an HttpServletRequest object that contains the request the
     *                 client has made.
     * @param response an HttpServletResponse object that contains the response the
     *                 servlet sends to the client.
     */
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    /**
     * Handles DELETE requests.
     *
     * @param request  an HttpServletRequest object that contains the request the
     *                 client has made.
     * @param response an HttpServletResponse object that contains the response the
     *                 servlet sends to the client.
     */
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    /**
     * Creates a ResponsePlan from the given ServletRequest, executes it, and writes a
     * RestResponse.
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        ResponsePlan responsePlan =
                ResponsePlanFactory.createResponsePlan(request, response);

        RestResponse restResponse = null;

        try {

            restResponse = responsePlan.execute();

        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);

            restResponse = new ErrorResponse();
            restResponse.setCode(500);
            restResponse.setMsg(e.getMessage());

        }

        writeResponse(request, response, restResponse);

    }

    /**
     * Writes a response.
     */
    private void writeResponse(HttpServletRequest request, HttpServletResponse response,
                               RestResponse restResponse) {

        try {
            response.setHeader(RestResponse.CACHE_CONTROL, RestResponse.NO_CACHE);

            String responseContent = negotiateContentType(request, response, restResponse);

            if (acceptsCompression(request)) {
                response.setHeader(RestResponse.CONTENT_ENCODING, RestResponse.GZIP);
                response = GzipResponse.wrap(response);
            } else {
                response.setContentLength(responseContent.length());
            }

            PrintWriter writer = response.getWriter();

            if (restResponse.getCode() != 200) {
                response.setStatus(restResponse.getCode());
            } else {
                response.setStatus(200);
            }
            writer.write(responseContent);
            writer.close();

        } catch (Throwable t) {
            logger.error(t);
        }
    }

    private boolean acceptsCompression(HttpServletRequest request) {
        if (request.getHeader(RestResponse.ACCEPT_ENCODING) != null
                && request.getHeader(RestResponse.ACCEPT_ENCODING).contains(RestResponse.GZIP)) {
            return true;
        }
        return false;
    }

    /**
     * Generates a response format, given what is specified in the request.
     */
    private String negotiateContentType(HttpServletRequest request, HttpServletResponse response,
                                        RestResponse restResponse) {
        String responseContent = "";

        // default to JSON
        responseContent = restResponse.toJSON();
        response.setContentType(RestResponse.JSON_MIME);

        // check for custom callback and wrap response if present
        if (request.getParameter(RestResponse.CALLBACK) != null) {
            String callback = request.getParameter(RestResponse.CALLBACK);
            responseContent = callback + "(" + responseContent + ")";
        }

        // add response headers
        Map<String, String> headers = restResponse.getHeaders();
        for (String name : restResponse.getHeaders().keySet()) {
            response.addHeader(name, headers.get(name));
        }

        return responseContent + "\n";
    }

}
