package org.rest.facade.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.facade.RestArg;
import org.rest.facade.RestResource;
import org.rest.facade.SearchResult;
import org.rest.facade.ServiceException;

import java.util.Date;

public class ResourceServiceImpl implements ResourceService {

    private final Logger logger = LogManager.getLogger(getClass().getName());

    /**
     * Returns a SearchResult of RestResource that match the criteria in the
     * given RestArg.
     *
     * @param arg an instance of an implementation of the RestArg interface.
     * @return a SearchResult of RestResource that match the criteria defined in
     * the given RestArg.
     * @throws ServiceException if an error occurs.
     */
    @Override
    public SearchResult<?> findAll(RestArg arg) throws ServiceException {
        SearchResult<Resource> searchResult = new SearchResult<>();
        for (int i=0; i < 10; i++) {
            Resource resource = new Resource();
            resource.setId(Integer.toString(i));
            resource.setName("name-" + i);
            resource.setLastModified(new Date());
            searchResult.getItems().add(resource);
        }
        int totalCount = searchResult.getItems().size();
        logger.trace("Return " + totalCount + " items from findAll()");
        searchResult.setTotalCount(totalCount);
        return searchResult;
    }

    /**
     * Returns a RestResource that matches the resource id on the RestArg
     *
     * @param arg an instance of an implementation of the RestArg interface.
     * @return RestResource that match the criteria defined in
     * the given RestArg.
     * @throws ServiceException if an error occurs.
     */
    @Override
    public RestResource find(RestArg arg) throws ServiceException {
        ResourceArg resourceArg = (ResourceArg)arg;
        Resource resource = new Resource();
        resource.setId(resourceArg.getId());
        resource.setName("name-" + resourceArg.getId());
        resource.setLastModified(new Date());
        logger.trace("Return find()");
        return resource;
    }

    /**
     * Saves (persists) or updates (commits) the given entity into the system.
     *
     * @param resource the RestResource to save or update.
     * @return the updated RestResource.
     * @throws ServiceException if an error occurs.
     */
    @Override
    public RestResource save(RestResource resource) throws ServiceException {
        Resource incomingResource = (Resource)resource;
        Resource savedResource = new Resource();
        savedResource.setId("1");
        savedResource.setName(incomingResource.getName());
        savedResource.setLastModified(new Date());
        logger.trace("Return save()");
        return savedResource;
    }

    /**
     * Removes the given RestResource from the system.
     *
     * @param resource the resource to remove.
     * @throws ServiceException if an error occurs.
     */
    @Override
    public void remove(RestResource resource) throws ServiceException {
        logger.trace("Start remove()");
    }

}
