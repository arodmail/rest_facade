package org.rest.facade.response;

import org.rest.facade.marshall.JacksonWrapper;
import org.rest.facade.support.ResultList;
import org.rest.facade.support.XmlTag;

/**
 * Provides a common response for multi-paged search requests.
 *
 * @author A. Rodriguez
 */
public class PagedResponse extends BaseResponse {

    /**
     * The data structure that holds a search result.
     */
    private ResultList<?> resultList = null;

    /**
     * Creates an empty PagedResponse.
     */
    public PagedResponse() {
    }

    /**
     * Creates an PagedResponse with the given ResultList.
     */
    public PagedResponse(ResultList<?> resultList) {
        this.resultList = resultList;
    }

    /**
     * Returns a JSON representation of the RestResponse.
     */
    @Override
    public String toJSON() {
        if (this.resultList != null) {
            return JacksonWrapper.toJSON(this.resultList);
        }
        return "";
    }

    public ResultList<?> getResultList() {
        return resultList;
    }
}
