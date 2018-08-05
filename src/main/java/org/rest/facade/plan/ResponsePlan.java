package org.rest.facade.plan;

import org.rest.facade.ServiceException;
import org.rest.facade.response.RestResponse;

/**
 * Defines a method to execute a REST response plan.
 *
 * @author A. Rodriguez
 */
public interface ResponsePlan {

    /**
     * Main entry point to handle a REST request. Implementations should
     * divide processing appropriately, and generate an concrete instance of
     * BaseResponse suitable for the type of request handled.
     *
     * @return HttpAbstractResponse object that holds a response code, a
     * formatted response body or an error message that are the result of
     * processing a client request.
     */
    public RestResponse execute() throws ServiceException;

}
