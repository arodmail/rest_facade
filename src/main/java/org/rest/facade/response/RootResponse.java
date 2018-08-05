package org.rest.facade.response;

import org.rest.facade.marshall.JacksonWrapper;
import org.rest.facade.support.*;

/**
 * A RestResponse to generate an XML or JSON formatted response
 * for the REST API endpoint /rest.
 *
 * @author A. Rodriguez
 */
public class RootResponse extends BaseResponse {

    /**
     * The Object (or resource) this RestResponse holds
     */
    private ResourceList resourceList = null;


    /**
     * Creates an empty RootResponse
     */
    public RootResponse() {
    }

    /**
     * Creates an RootResponse with the given ResourceList.
     *
     * @param resourceList the ResourceList this RootResponse holds
     */
    public RootResponse(ResourceList resourceList) {
        this.resourceList = resourceList;
    }

    /**
     * Assigns the given Object (resource) to this CommonResponse.
     *
     * @param resourceList the Object this CommonResponse holds
     */
    public void setResource(ResourceList resourceList) {
        this.resourceList = resourceList;
    }

    /**
     * Returns a JSON representation of the RestResponse.
     */
    @Override
    public String toJSON() {
        if (this.resourceList != null) {
            return JacksonWrapper.toJSON(this.resourceList);
        }
        return "";
    }

    @Override
    public ResultList<?> getResultList() {
        return null;
    }

}