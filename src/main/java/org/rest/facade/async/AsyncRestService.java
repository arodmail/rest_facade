package org.rest.facade.async;

import org.rest.facade.RestArg;
import org.rest.facade.ServiceException;
import org.rest.facade.SearchResult;

/**
 * A common interface for services that are invoked dynamically by the ServiceLocator.
 * Defines methods that are common for services that are exposed by the REST API and
 * provide an asynchronous interface through which a client may check progress and
 * status of long-running backend processes.
 *
 * @author A. Rodriguez
 */
public interface AsyncRestService {

    public final static String PROCESSING = "PROCESSING";
    public final static String COMPLETE = "COMPLETED";
    public final static String CANCELLED = "CANCELLED";
    public final static String UNKNOWN = "UNKNOWN";

    /**
     * Starts the async process. Returns an AsyncHandle
     */
    public AsyncHandle search(RestArg restArg) throws ServiceException;

    /**
     * Retrieves the results of async processing in a SearchResult.
     */
    public SearchResult<?> load(AsyncHandle asyncHandle) throws ServiceException;

    /**
     * Returns a String indicating the status of the asynchronous processing,
     * one of: RUNNING, CANCELLED, COMPLETE.
     *
     * @return String
     */
    public String getStatus(AsyncHandle asyncHandle) throws ServiceException;

    /**
     * Returns an integer between 0-100 to indicate progress complete for
     * asynchronous processing.
     *
     * @return integer between 0-100.
     */
    public int getProgress(AsyncHandle asyncHandle) throws ServiceException;

    /**
     * Cancels the asynchronous process. Subsequent calls to getStatus() should
     * return CANCELLED.
     */
    public void cancel(AsyncHandle asyncHandle) throws ServiceException;

}
