package org.rest.facade.response;

import org.rest.facade.marshall.JacksonWrapper;
import org.rest.facade.support.ResultList;
import org.rest.facade.support.XmlTag;

/**
 * A common RestResponse to generate JSON formatted responses
 * for arbitrary Objects.
 *
 * @author A. Rodriguez
 */
public class CommonResponse extends BaseResponse {

    /**
     * The Object (or resource) this RestResponse holds
     */
    private Object resource = null;

    /**
     * Creates an empty CommonResponse
     */
    public CommonResponse() {
    }

    /**
     * Creates an CommonResponse with the given Object (resource).
     *
     * @param resource the Object this HttpCommonResponse holds
     */
    public CommonResponse(Object resource) {
        this.resource = resource;
    }

    /**
     * Assigns the given Object (resource) to this CommonResponse.
     *
     * @param resource the Object this CommonResponse holds
     */
    public void setResource(Object resource) {
        this.resource = resource;
    }

    /**
     * Returns the given Object (resource) from this CommonResponse.
     *
     * @return resource the Object this CommonResponse holds
     */
    public Object getResource() {
        return this.resource;
    }

    /**
     * Returns a JSON representation of the RestResponse.
     */
    @Override
    public String toJSON() {
        if (this.resource != null) {
            return JacksonWrapper.toJSON(this.resource);
        }
        return "";
    }

    @Override
    public ResultList<?> getResultList() {
        return null;
    }

}