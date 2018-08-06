package org.rest.facade.impl.async;

import org.rest.facade.async.AsyncHandle;

public class AsyncResourceHandle implements AsyncHandle {

    public String id;

    /**
     * Assigns an id to the handle.
     *
     * @param handleId
     */
    @Override
    public void setId(String handleId) {
        this.id = handleId;
    }

    /**
     * Returns the handle id.
     *
     * @return String a handle id.
     */
    @Override
    public String getId() {
        return this.id;
    }
}
