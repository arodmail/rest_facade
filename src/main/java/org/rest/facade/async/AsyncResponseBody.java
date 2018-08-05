package org.rest.facade.async;

/**
 * Used to track progress on the response from an asynchronous request.
 *
 * @author A. Rodriguez
 */
public class AsyncResponseBody {

    private String status = "";
    private String progress = "";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

}

