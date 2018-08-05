package org.rest.facade.plan;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controls ResponsePlan creation.
 *
 * @author A. Rodriguez
 */
public class ResponsePlanFactory {

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";

    private ResponsePlanFactory() {
    }

    /**
     * Creates and returns an implementation of ResponsePlan from the method
     * in the given HttpServletRequest.
     *
     * @param request  an HttpServletRequest object that contains the request the
     *                 client has made.
     * @param response an HttpServletResponse object that contains the response the
     *                 servlet sends to the client.
     */
    public static ResponsePlan createResponsePlan(HttpServletRequest request,
                                                  HttpServletResponse response) {
        String method = request.getMethod();

        if (method.equals(GET)) {

            return new GetPlan(request, response);

        } else if (method.equals(POST)) {

            return new PostPlan(request, response);

        } else if (method.equals(PUT)) {

            return new PutPlan(request, response);

        } else if (method.equals(DELETE)) {

            return new DeletePlan(request, response);

        } else {

            return null;

        }
    }

}
