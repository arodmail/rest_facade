package org.rest.facade.async;

import org.rest.facade.RestResource;

/**
 * A common interface implemented by entity classes to be recognized by the
 * REST processing layer while processing asynchronous requests.
 *
 * @author A. Rodriguez
 */
public interface AsyncHandle extends RestResource {

    /**
     * Assigns an id to the handle.
     *
     * @param handleId
     */
    public void setId(String handleId);

    /**
     * Returns the handle id.
     *
     * @return String a handle id.
     */
    public String getId();

}
