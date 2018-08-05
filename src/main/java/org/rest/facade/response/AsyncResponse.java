package org.rest.facade.response;

import org.rest.facade.async.AsyncResponseBody;
import org.rest.facade.marshall.JacksonWrapper;
import org.rest.facade.support.*;

/**
 * A RestResponse for asynchronous requests.
 *
 * @author A. Rodriguez
 */
public class AsyncResponse extends CommonResponse {

    private AsyncResponseBody asyncResponseBody = new AsyncResponseBody();

    /**
     * Returns the status value.
     *
     * @return
     */
    public String getStatus() {
        return this.asyncResponseBody.getStatus();
    }

    /**
     * Sets the status value.
     *
     * @param status
     */
    public void setStatus(String status) {
        this.asyncResponseBody.setStatus(status);
    }

    /**
     * Gets the progress value.
     *
     * @return String progress
     */
    public String getProgress() {
        return this.asyncResponseBody.getProgress();
    }

    /**
     * Sets the progress value.
     *
     * @param progress
     */
    public void setProgress(String progress) {
        this.asyncResponseBody.setProgress(progress);
    }

    public String toJSON() {
        return JacksonWrapper.toJSON(this.asyncResponseBody);
    }

    @Override
    public ResultList<?> getResultList() {
        return null;
    }

}
