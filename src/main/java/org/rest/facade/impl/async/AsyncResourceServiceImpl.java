package org.rest.facade.impl.async;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.facade.RestArg;
import org.rest.facade.SearchResult;
import org.rest.facade.ServiceException;
import org.rest.facade.async.AsyncHandle;
import org.rest.facade.async.AsyncRestService;
import org.rest.facade.impl.Resource;

import java.util.Date;

public class AsyncResourceServiceImpl implements AsyncResourceService {

    private final Logger logger = LogManager.getLogger(getClass().getName());

    /**
     * Starts the async process. Returns an AsyncHandle
     *
     * @param restArg
     */
    @Override
    public AsyncHandle search(RestArg restArg) throws ServiceException {
        AsyncResourceHandle handle = new AsyncResourceHandle();
        handle.setId("123");
        logger.trace("Return AsyncHandle " + handle.getId());
        return handle;
    }

    /**
     * Retrieves the results of async processing in a SearchResult.
     *
     * @param asyncHandle
     */
    @Override
    public SearchResult<?> load(AsyncHandle asyncHandle) throws ServiceException {
        SearchResult<Resource> searchResult = new SearchResult<>();
        for (int i=0; i < 10; i++) {
            Resource resource = new Resource();
            resource.setName("asyncname-" + i);
            resource.setId(Integer.toString(i));
            resource.setLastModified(new Date());
            searchResult.getItems().add(resource);
        }
        int totalCount = searchResult.getItems().size();
        logger.trace("Return " + totalCount + " items from findAll()");
        searchResult.setTotalCount(totalCount);
        return searchResult;
    }

    /**
     * Returns a String indicating the status of the asynchronous processing,
     * one of: RUNNING, CANCELLED, COMPLETE.
     *
     * @param asyncHandle
     * @return String
     */
    @Override
    public String getStatus(AsyncHandle asyncHandle) throws ServiceException {
        logger.trace("Return status");
        return AsyncRestService.COMPLETE;
    }

    /**
     * Returns an integer between 0-100 to indicate progress complete for
     * asynchronous processing.
     *
     * @param asyncHandle
     * @return integer between 0-100.
     */
    @Override
    public int getProgress(AsyncHandle asyncHandle) throws ServiceException {
        logger.trace("Return progress");
        return 100;
    }

    /**
     * Cancels the asynchronous process. Subsequent calls to getStatus() should
     * return CANCELLED.
     *
     * @param asyncHandle
     */
    @Override
    public void cancel(AsyncHandle asyncHandle) throws ServiceException {
        logger.trace("Return cancel");
    }
}
