package org.rest.facade.plan;

import org.rest.facade.*;
import org.rest.facade.async.AsyncHandle;
import org.rest.facade.async.AsyncHandleFactory;
import org.rest.facade.async.AsyncRestService;
import org.rest.facade.http.RequestPath;
import org.rest.facade.response.*;
import org.rest.facade.support.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * A ResponsePlan for GET requests.
 *
 * @author A. Rodriguez
 */
public class GetPlan extends BasePlan {

    /**
     * Creates a new GetPlan
     *
     * @param request  an HttpServletRequest that contains a request from
     *                 a client.
     * @param response an HttpServletResponse that contains a response from
     *                 the server.
     */
    public GetPlan(HttpServletRequest request,
                   HttpServletResponse response) {

        super(request, response);

    }

    /**
     * Main entry point to handle an HTTP GET request.
     *
     * @return RestResponse object that holds a response code, a
     * formatted response body or an error message that are the result of
     * processing a client request.
     */
    @Override
    public RestResponse execute() throws ServiceException {

        RestResponse restResponse = new CommonResponse();

        RequestPath requestPath =
                new RequestPath(request.getPathInfo());

        // /root/type/id/
        switch (requestPath.getFragmentCount()) {
            case RequestPath.ROOT: {

                String requestURL = request.getRequestURL().toString();
                if (!requestURL.endsWith(RequestPath.SLASH)) {
                    requestURL += RequestPath.SLASH;
                }

                List<ResourceDescriptor> resourceDescriptors =
                        ServiceLocator.gInstance().getResourceInfos();
                for (ResourceDescriptor descriptor : resourceDescriptors) {
                    descriptor.setLink(requestURL + descriptor.getName());
                }

                Map<String, String> mapping = ServiceConfig.gInstance().getMapping();
                for (String name : mapping.keySet()) {
                    String uri = mapping.get(name);
                    if (uri.startsWith(RequestPath.SLASH) && requestURL.endsWith(RequestPath.SLASH)) {
                        uri = uri.substring(1, uri.length());
                    }
                    resourceDescriptors.add(new ResourceDescriptor(name, requestURL + uri));
                }

                // de-dupe
                List<ResourceDescriptor> tmp = new ArrayList<ResourceDescriptor>();
                List<String> names = new ArrayList<String>();
                for (ResourceDescriptor rd : resourceDescriptors) {
                    String name = rd.getName();
                    if (!names.contains(name)) {
                        tmp.add(rd);
                        names.add(name);
                    }
                }
                resourceDescriptors.clear();
                resourceDescriptors = tmp;

                for (ResourceDescriptor descriptor : resourceDescriptors) {
                    descriptor.setLink(requestURL + descriptor.getName());
                }

                Collections.sort(resourceDescriptors);
                restResponse = new RootResponse(new ResourceList(resourceDescriptors));
                restResponse.setCode(200);

                break;

            }
            case RequestPath.TYPE: {
                String path = requestPath.getLastFragment();
                String queryString = request.getQueryString();

                // create RestService from the request path
                RestService service = ServiceFactory.createService(path);

                if (service == null) {

                    restResponse = new ErrorResponse();
                    restResponse.setCode(404);
                    restResponse.setMsg("The requested resource cannot be found.");

                    break;

                }
                // create a RestArg from the request path
                RestArg arg = RestArgFactory.createArg(path, queryString);
                SearchResult<?> result = service.findAll(arg);
                ResultList resultList = new ResultList(result.getItems());
                resultList.setTotalCount(result.getTotalCount());

                restResponse = new PagedResponse(resultList);
                restResponse.setCode(200);

                break;

            }
            case RequestPath.ID: {

                String id = requestPath.getLastFragment();
                String path = requestPath.getFragment(1);
                RestService service = ServiceFactory.createService(path);

                // create a RestArg from the request path
                StringBuilder queryString = new StringBuilder();
                if (request.getQueryString() != null) {
                    queryString.append(request.getQueryString());
                    queryString.append("&id=").append(id);
                } else {
                    queryString.append("id=").append(id);
                }
                RestArg arg = RestArgFactory.createArg(path, queryString.toString());

                RestResource result = service.find(arg);

                if (result != null) {
                    restResponse = new CommonResponse(result);
                    restResponse.setCode(200);

                } else {

                    restResponse = new ErrorResponse();
                    restResponse.setCode(404);
                    restResponse.setMsg("The requested resource cannot be found.");

                }

                break;

            }
            case RequestPath.ASYNC: {

                String id = requestPath.getLastFragment();
                String path = requestPath.getFragment(2);  // resource type

                AsyncRestService service = ServiceFactory.createAsyncService(path);
                AsyncHandle asyncHandle = AsyncHandleFactory.create(path, id);

                if (requestPath.getFragment(1).equals(AsyncResponse.ASYNC)) {

                    // load and return all results
                    SearchResult<?> result = service.load(asyncHandle);

                    ResultList resultList = new ResultList(result.getItems());
                    resultList.setTotalCount(result.getTotalCount());

                    restResponse = new PagedResponse(resultList);
                    restResponse.setCode(200);

                } else {

                    String status = service.getStatus(asyncHandle);
                    int progress = service.getProgress(asyncHandle);

                    AsyncResponse asyncResponse = new AsyncResponse();
                    asyncResponse.setStatus(status);
                    asyncResponse.setProgress(Integer.toString(progress));

                    if (status.equals(AsyncRestService.PROCESSING)) {

                        asyncResponse.setCode(200); // ok

                    } else if (status.equals(AsyncRestService.COMPLETE)) {

                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put(AsyncResponse.LOCATION, "/rest/" + AsyncResponse.ASYNC
                                + "/" + path + "/" + asyncHandle.getId());
                        asyncResponse.setHeaders(headers);

                        asyncResponse.setCode(200); // ok

                    } else if (status.equals(AsyncRestService.CANCELLED)) {

                        asyncResponse.setCode(200); // was deleted

                    } else if (status.equals(AsyncRestService.UNKNOWN)) {

                        asyncResponse.setCode(200); // cannot be found for some other reason

                    }

                    restResponse = asyncResponse;

                }

                break;

            }
            default: {

                restResponse = new ErrorResponse();
                restResponse.setCode(404);
                restResponse.setMsg("The requested resource cannot be found.");

                break;

            }
        }

        return restResponse;
    }

}
