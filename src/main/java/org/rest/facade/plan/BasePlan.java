package org.rest.facade.plan;

import org.rest.facade.ServiceException;
import org.rest.facade.response.RestResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Abstract base class for implementations of ResponsePlan. Holds an
 * HttpServletRequest and an HttpServletResponse. Subclasses of BasePlan
 * are expected to provide an implementation of execute() that is specific for
 * the HTTP method being serviced.
 *
 * @author A. Rodriguez
 */
public abstract class BasePlan implements ResponsePlan {

    // contains a request from a client
    public HttpServletRequest request;

    // contains a response from the server
    public HttpServletResponse response;

    /**
     * Creates a new BasePlan
     *
     * @param request  an HttpServletRequest that contains a request from
     *                 a client.
     * @param response an HttpServletResponse that contains a response from
     *                 the server.
     */
    public BasePlan(HttpServletRequest request,
                    HttpServletResponse response) {

        this.request = request;
        this.response = response;

    }

    /**
     * Main entry point to handle a REST API request. Subclasses are expected to
     * provide an implementation that divides processing appropriately, and generates
     * an instance of an implementation of RestResponse suitable for the type of
     * request handled.
     *
     * @return HttpResponse object that holds a response code, a
     * formatted response body, or an error message that are the result of
     * processing a client request.
     */
    @Override
    public RestResponse execute() throws ServiceException {

        // implemented by a concrete subclass
        return null;

    }

}
