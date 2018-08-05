package org.rest.facade.plan;

import org.rest.facade.*;
import org.rest.facade.async.AsyncHandle;
import org.rest.facade.async.AsyncRestService;
import org.rest.facade.http.RequestPath;
import org.rest.facade.response.CommonResponse;
import org.rest.facade.response.RestResponse;
import org.rest.facade.async.AsyncHandleFactory;
import org.rest.facade.support.RestArgFactory;
import org.rest.facade.support.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A ResponsePlan for DELETE requests.
 *
 * @author A. Rodriguez
 */
public class DeletePlan extends BasePlan {

    /**
     * Creates a new DeletePlan
     *
     * @param request  an HttpServletRequest that contains a request from
     *                 a client.
     * @param response an HttpServletResponse that contains a response from
     *                 the server.
     */
    public DeletePlan(HttpServletRequest request,
                      HttpServletResponse response) {

        super(request, response);

    }

    /**
     * Returns an RestResponse.
     *
     * @return RestResponse an instance of CommonResponse.
     */
    @Override
    public RestResponse execute() throws ServiceException {

        CommonResponse commonResponse = new CommonResponse();

        RequestPath requestPath =
                new RequestPath(super.request.getPathInfo());

        switch (requestPath.getFragmentCount()) {
            case RequestPath.ID: {

                String id = requestPath.getLastFragment();
                String path = requestPath.getFragment(1);
                RestService service = ServiceFactory.createService(path);

                // create a RestArg from the request path
                RestArg arg = RestArgFactory.createArg(path, "id=" + id);

                SearchResult<?> result = service.findAll(arg);

                if (result != null && result.getItems().size() > 0) {

                    service.remove((RestResource) result.getItems().get(0));
                    commonResponse.setCode(200);

                } else {

                    commonResponse.setCode(404);
                    commonResponse.setMsg("Resource not found.");

                }
                break;

            }
            case RequestPath.ASYNC: {

                String id = requestPath.getLastFragment();
                String path = requestPath.getFragment(2);  // resource type

                AsyncRestService service = ServiceFactory.createAsyncService(path);
                AsyncHandle asyncHandle = AsyncHandleFactory.create(path, id);

                // cancel asynchronous processing
                service.cancel(asyncHandle);

                commonResponse.setCode(200);

            }
            case RequestPath.FILE: {
                String path = requestPath.getFragment(1);
                RestService service = ServiceFactory.createService(path);

                // create a RestArg from the request path
                StringBuilder queryString = new StringBuilder();
                RestArg arg = RestArgFactory.createArg(path, queryString.toString());
                RestResource result = service.find(arg);

                if (result != null) {

                    service.remove(result);
                    commonResponse.setCode(200);

                } else {

                    commonResponse.setCode(404);
                    commonResponse.setMsg("Resource not found.");

                }

            }

            break;

        }

        return commonResponse;

    }
}