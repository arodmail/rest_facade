package org.rest.facade.support;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.facade.async.AsyncRestService;
import org.rest.facade.RestService;

/**
 * Provides a factory method to create an instance of an implementation of
 * the RestService interface.
 *
 * @author A. Rodriguez
 */
public class ServiceFactory {

    private static final Logger logger = LogManager.getLogger(ServiceFactory.class);

    private ServiceFactory() {
    }

    /**
     * Returns an instance of an implementation of the RestService interface
     * that is mapped to the given URI string.
     */
    public static RestService createService(String path) {
        RestService serviceInstance = null;

        if (!path.endsWith("Async")) {

            String serviceClassName = ServiceConfig.gInstance().getServiceClassName(path);
            if (serviceClassName == null || serviceClassName.length() == 0) {
                serviceClassName =
                        ServiceLocator.gInstance().getServiceBeanName(path);
            }
            if (serviceClassName != null) {
                try {

                    // The object is expected to be an RMI stub (or some equivalent)
                    // A Spring Bean, or an EJB returned from a JNDI lookup
                    Object serviceObject = null;

                    if (serviceObject != null && serviceObject instanceof RestService) {

                        serviceInstance = (RestService) serviceObject;

                    } else {

                        // try to create an instance of the bean directly
                        try {

                            Class clazz = Class.forName(serviceClassName);
                            serviceInstance = (RestService) clazz.newInstance();

                        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {

                            logger.error(ex);

                        }

                    }

                } catch (Exception e) {
                    logger.error(e);
                }

            }

        }

        return serviceInstance;

    }

    /**
     * Returns an instance of an implementation of the AsyncRestService interface
     * that is mapped to the given URI string.
     */

    public static AsyncRestService createAsyncService(String path) {
        AsyncRestService serviceInstance = null;

        String serviceBeanName =
                ServiceConfig.gInstance().getServiceClassName(path);
        if (serviceBeanName == null || serviceBeanName.length() == 0) {
            serviceBeanName =
                    ServiceLocator.gInstance().getServiceBeanName(path);
        }
        if (serviceBeanName != null) {
            try {

                // serviceInstance = (AsyncRestService) SpringUtil.getBean(serviceBeanName);

            } catch (Exception e) {

                // try to create an instance of the bean directly
                try {

                    Class clazz = Class.forName(serviceBeanName);
                    serviceInstance = (AsyncRestService) clazz.newInstance();

                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {

                    logger.error(ex);

                }

            }
            return serviceInstance;
        } else {
            return null;
        }
    }

}
