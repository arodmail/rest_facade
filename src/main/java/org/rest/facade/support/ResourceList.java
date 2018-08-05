package org.rest.facade.support;

import java.util.ArrayList;
import java.util.List;

/**
 * A List of ResourceDescriptor
 *
 * @author A. Rodriguez
 */
public class ResourceList {

    private List<ResourceDescriptor> resources;

    public ResourceList() {
        resources = new ArrayList<ResourceDescriptor>();
    }

    public ResourceList(List<ResourceDescriptor> resourceDescriptors) {
        this.resources = resourceDescriptors;
    }

    public List<ResourceDescriptor> getResources() {
        return this.resources;
    }

    public void addResources(ResourceDescriptor resourceInfos) {
        this.resources.add(resourceInfos);
    }

}


