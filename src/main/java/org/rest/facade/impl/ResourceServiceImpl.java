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

    @Override
    public void remove(RestResource resource) throws ServiceException {
        logger.trace("Start remove()");
    }

}
