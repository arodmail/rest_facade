package org.rest.facade.support;

/**
 * A holder used to generate a response for HTTP GET requests for the root of the
 * REST API end-point, /rest. Holds a name and a default link for a REST resource.
 *
 * @author A. Rodriguez
 */
public class ResourceDescriptor implements Comparable {

    /**
     * The name of a resource as it appears to REST clients.
     */
    private String name = "";

    /**
     * A default link for a resource
     */
    private String link = "";

    /**
     * Creates an empty ResourceDescriptor.
     */
    public ResourceDescriptor() {
    }

    /**
     * Creates a ResourceDescriptor and assigns a name to it.
     *
     * @param name a resource name
     */
    public ResourceDescriptor(String name) {
        this.name = name;
    }

    /**
     * Creates a ResourceDescriptor and assigns a name to it.
     *
     * @param name a resource name
     * @param link a resource link
     */
    public ResourceDescriptor(String name, String link) {
        this.name = name;
        setLink(link);
    }

    /**
     * Returns the name assigned to the ResourceDescriptor
     *
     * @return name assigned to the ResourceDescriptor
     */
    public String getName() {
        return name;
    }

    /**
     * Assigns a name to the ResourceDescriptor
     *
     * @param name a name to assign to the ResourceDescriptor
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a link to a default page for the resource named by the ResourceDescriptor.
     *
     * @return a link to a default page for the resource
     */
    public String getLink() {
        return link;
    }

    /**
     * Assigns a link to this ResourceDescriptor.
     *
     * @param link a link for the resource named by the ResourceDescriptor
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Provides a comparison function to sort ResourceDescriptor by name
     *
     * @param info an instance of ResourceDescriptor
     * @return -1, or 0, or 1 if the given ResourceDescriptor is less than, equal to,
     * or greater than this one.
     */
    @Override
    public int compareTo(Object info) {
        if (info instanceof ResourceDescriptor) {
            ResourceDescriptor inf = (ResourceDescriptor) info;
            return getName().compareTo(inf.getName());
        }
        return -1;
    }
}
