package org.rest.facade;

/**
 * A common interface for services that are invoked dynamically by the ServiceLocator
 * and exposed as REST APIs.
 *
 * @author A. Rodriguez
 */
public interface RestService {

    /**
     * Returns a SearchResult of RestResource that match the criteria in the
     * given RestArg.
     *
     * @param arg an instance of an implementation of the RestArg interface.
     * @return a SearchResult of RestResource that match the criteria defined in
     * the given RestArg.
     * @throws ServiceException if an error occurs.
     */
    public SearchResult<?> findAll(RestArg arg) throws ServiceException;

    /**
     * Returns a RestResource that matches the resource id on the RestArg
     *
     * @param arg an instance of an implementation of the RestArg interface.
     * @return RestResource that match the criteria defined in
     * the given RestArg.
     * @throws ServiceException if an error occurs.
     */
    public RestResource find(RestArg arg) throws ServiceException;

    /**
     * Saves (persists) or updates (commits) the given entity into the system.
     *
     * @param resource the RestResource to save or update.
     * @return the updated RestResource.
     * @throws ServiceException if an error occurs.
     */
    public RestResource save(RestResource resource) throws ServiceException;

    /**
     * Removes the given RestResource from the system.
     *
     * @param resource the resource to remove.
     * @throws ServiceException if an error occurs.
     */
    public void remove(RestResource resource) throws ServiceException;

}