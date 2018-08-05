package org.rest.facade.response;

import org.rest.facade.marshall.JacksonWrapper;
import org.rest.facade.support.ResultList;
import org.rest.facade.support.XmlTag;

/**
 * A common RestResponse to generate JSON formatted responses for errors.
 *
 * @author A. Rodriguez
 */
public class ErrorResponse extends BaseResponse {

    /**
     * Returns a JSON representation of the RestResponse.
     */
    @Override
    public String toJSON() {
        return JacksonWrapper.toJSON(this);
    }

    @Override
    public ResultList<?> getResultList() {
        return null;
    }
}
