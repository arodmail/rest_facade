package org.rest.facade.async;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.facade.support.ServiceConfig;

/**
 * Factory used to create an instance of an implementation of AsyncHandle.
 *
 * @author A. Rodriguez
 */
public class AsyncHandleFactory {

    private static final Logger logger = LogManager.getLogger(AsyncHandleFactory.class);

    private AsyncHandleFactory() {
    }

    public static AsyncHandle create(String path, String id) {
        AsyncHandle asyncHandleInstance = null;

        String handleClassName =
                ServiceConfig.gInstance().getAsyncHandleClassName(path);

        if (handleClassName != null) {
            try {

                Class clazz = Class.forName(handleClassName);
                asyncHandleInstance = (AsyncHandle) clazz.newInstance();

            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {

                logger.error(ex);

            }
        }

        if (asyncHandleInstance != null) asyncHandleInstance.setId(id);
        return asyncHandleInstance;
    }


}
