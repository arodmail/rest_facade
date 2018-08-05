package org.rest.facade.plan;

import org.rest.facade.RestArg;
import org.rest.facade.RestResource;
import org.rest.facade.RestService;
import org.rest.facade.ServiceException;
import org.rest.facade.async.AsyncHandle;
import org.rest.facade.async.AsyncRestService;
import org.rest.facade.http.PayloadReader;
import org.rest.facade.http.RequestPath;
import org.rest.facade.response.AsyncResponse;
import org.rest.facade.response.CommonResponse;
import org.rest.facade.response.RestResponse;
import org.rest.facade.support.DomainFactory;
import org.rest.facade.support.RestArgFactory;
import org.rest.facade.support.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A ResponsePlan for POST requests.
 *
 * @author A. Rodriguez
 */
public class PostPlan extends BasePlan {

    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

    /**
     * Creates a new PostPlan
     *
     * @param request  an HttpServletRequest that contains a request from
     *                 a client.
     * @param response an HttpServletResponse that contains a response from
     *                 the server.
     */
    public PostPlan(HttpServletRequest request,
                    HttpServletResponse response) {

        super(request, response);

    }

    /**
     * Main entry point to handle a POST request.
     *
     * @return RestResponse object that holds a response code, a
     * formatted response body or an error message that are the result of
     * processing a client request.
     */
    @Override
    public RestResponse execute() throws ServiceException {
        RestResponse restResponse = new CommonResponse();
        RequestPath requestPath = new RequestPath(request.getPathInfo());

        if (requestPath.getFragmentCount() == RequestPath.TYPE) {
            restResponse = processPostRequest(requestPath.getLastFragment());
        } else {
            restResponse.setCode(500);
            restResponse.setMsg("Invalid request.");
        }

        return restResponse;
    }

    /**
     * Processes the POST request for the given content type and the
     * encoded contents of the given HttpServletRequest.
     */
    private CommonResponse processPostRequest(String resourceType) throws ServiceException {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse = saveResources(commonResponse, resourceType);
        return commonResponse;
    }

    /**
     * Persists a set of resources, read from the HTTP request body.
     */
    private CommonResponse saveResources(CommonResponse response, String resourceType) throws ServiceException {

        PayloadReader payloadReader = new PayloadReader();

        Class<?> clazz = DomainFactory.getEntityClass(resourceType);

        try {
            RestService service = ServiceFactory.createService(resourceType);

            if (service == null) {

                AsyncRestService asyncService = ServiceFactory.createAsyncService(resourceType);

                if (asyncService != null) {

                    clazz = RestArgFactory.getArgClass(resourceType);

                    RestArg restArg = payloadReader.readPayloadIntoArg(request, clazz);
                    AsyncHandle asyncHandle = asyncService.search(restArg);

                    response = new AsyncResponse();
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put(AsyncResponse.LOCATION, "/" + AsyncResponse.QUEUE + "/" + resourceType + "/" + asyncHandle.getId());
                    response.setHeaders(headers);
                    response.setCode(202); // Accepted

                } else {

                    response.setCode(404);

                }

            } else {
                // un-marshall one or more
                List<RestResource> resources = payloadReader.readPayload(request, clazz);

                for (RestResource resource : resources) {
                    RestResource savedResource = service.save(resource);
                    response.setResource(savedResource);
                }

                response.setCode(200);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }

}
