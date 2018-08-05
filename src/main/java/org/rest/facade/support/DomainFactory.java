package org.rest.facade.support;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Provides factory methods to create an instance of a descendant of
 * BaseEntity + BaseArg.
 *
 * @author A. Rodriguez
 */
public class DomainFactory {
    private static final Logger logger = LogManager.getLogger(DomainFactory.class);

    private DomainFactory() {
    }

    public static Class getEntityClass(String path) {
        String entityClassName =
                ServiceConfig.gInstance().getEntityClassName(path);
        if (entityClassName != null) {
            try {
                Class<?> entityClass = Class.forName(entityClassName);
                return entityClass;
            } catch (ClassNotFoundException e) {
                logger.error(e);
            }
        }
        return null;
    }

}
