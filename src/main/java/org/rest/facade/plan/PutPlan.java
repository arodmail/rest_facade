package org.rest.facade.plan;

import org.rest.facade.RestResource;
import org.rest.facade.RestService;
import org.rest.facade.ServiceException;
import org.rest.facade.http.PayloadReader;
import org.rest.facade.http.RequestPath;
import org.rest.facade.response.CommonResponse;
import org.rest.facade.response.RestResponse;
import org.rest.facade.support.DomainFactory;
import org.rest.facade.support.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * A ResponsePlan for PUT requests.
 *
 * @author A. Rodriguez
 */
public class PutPlan extends BasePlan {

    /**
     * Creates a new PutPlan
     *
     * @param request  an HttpServletRequest that contains a request from
     *                 a client.
     * @param response an HttpServletResponse that contains a response from
     *                 the server.
     */
    public PutPlan(HttpServletRequest request,
                   HttpServletResponse response) {

        super(request, response);

    }

    /**
     * Main entry point to handle a PUT request.
     *
     * @return HttpResponse object that holds a response code, a
     * formatted response body or an error message that are the result of
     * processing a client request.
     */
    @Override
    public RestResponse execute() throws ServiceException {

        CommonResponse commonResponse = new CommonResponse();

        RequestPath pathFragment =
                new RequestPath(super.request.getPathInfo());

        try {

            if (pathFragment.getFragmentCount() ==
                    RequestPath.ID) {

                String id = pathFragment.getLastFragment();
                String resourceType = pathFragment.getFragment(1);

                PayloadReader payloadReader = new PayloadReader();

                Class<?> clazz = DomainFactory.getEntityClass(resourceType);

                List<RestResource> resources = payloadReader.readPayload(request, clazz);

                RestService service = ServiceFactory.createService(resourceType);

                for (RestResource resource : resources) {
                    RestResource savedResource = service.save(resource);
                    commonResponse.setResource(savedResource);
                }

                commonResponse.setCode(200);

            } else if (pathFragment.getFragmentCount() ==
                    RequestPath.TYPE) {

                String resourceType = pathFragment.getFragment(1);

                PayloadReader payloadReader = new PayloadReader();

                Class<?> clazz = DomainFactory.getEntityClass(resourceType);

                List<RestResource> resources =
                        payloadReader.readPayload(request, clazz);

                RestService service = ServiceFactory.createService(resourceType);

                for (RestResource resource : resources) {
                    RestResource savedResource = service.save(resource);
                    commonResponse.setResource(savedResource);
                }

                commonResponse.setCode(200);

            } else {

                // invalid request
                commonResponse.setCode(500);
                commonResponse.setMsg("Invalid request.");

            }

        } catch (IOException e) {
            commonResponse.setCode(500);
            commonResponse.setMsg(e.getMessage());
        }
        return commonResponse;

    }


}
