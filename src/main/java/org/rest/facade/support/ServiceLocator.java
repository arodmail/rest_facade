package org.rest.facade.support;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * A Singleton used to scan Java packages in order to locate implementations of
 * the RestService interface, RestResource, as well as RestArg, using a class naming
 * convention in which the URI occurs within the class name. Returns matching class names.
 *
 * @see org.rest.facade.RestService
 * @author A. Rodriguez
 */
public class ServiceLocator {
    private static final Logger logger = LogManager.getLogger(ServiceLocator.class);

    /**
     * The Singleton instance of the locator.
     */
    private static ServiceLocator instance = null;

    /**
     * A class name post-fix used to map a URI to an arg class based on class
     * naming convention.
     */
    private static final String ARG = "Arg";

    /**
     * A class name post-fix used to map a URI to a service class based on class
     * naming convention.
     */
    private static final String SERVICE_IMPL = "ServiceImpl";

    /**
     * A class name post-fix used to map a URI to a service class based on class
     * naming convention.
     */
    private static final String SERVICE = "Service";

    /**
     * Primary Java package providing class definitions for implementations of RestArg.
     */
    public static final String ARG_PKG = "org.rest.facade.impl";

    /**
     * Secondary Java package providing class definitions for implementations of RestArg.
     */
    public static final String ARG_PKG_XFTAS = "org.rest.facade.impl";

    /**
     * Primary Java package providing class definitions for implementations of RestResource
     */
    public static final String ENTITY_PKG = "org.rest.facade.impl";

    /**
     * Secondary Java package providing class definitions for implementations of RestResource
     */
    public static final String ENTITY_PKG_XFTAS = "org.rest.facade.impl";

    /**
     * Primary Java package providing implementations of the RestService interface.
     */
    public static final String SERVICE_PKG = "org.rest.facade.impl";

    /**
     * Private no-arg constructor for the Singleton.
     */
    private ServiceLocator() {
    }

    /**
     * Returns the Singleton instance of the ServiceLocator.
     */
    public static synchronized ServiceLocator gInstance() {
        if (instance == null) {
            instance = new ServiceLocator();
        }
        return instance;
    }

    /**
     * Returns the name of a class that implements the RestService interface and that is
     * mapped to the given URI (or URL path fragment) based on a class naming convention
     * in which the URI occurs within the class name.
     *
     * @param path a URI or URL path fragment for which to resolve a service class name.
     * @return the name of a class that is mapped to the given URI or URL path fragment.
     */
    public String getServiceClassName(String path) {
        try {
            List<Class<?>> classes = getClasses(SERVICE_PKG);
            for (Class<?> clazz : classes) {
                if (isRestService(clazz)) {
                    String name = clazz.getSimpleName();
                    if (name.indexOf(SERVICE) != -1) {
                        String partialName = name.substring(0, name.indexOf(SERVICE));
                        if (path.equalsIgnoreCase(partialName)) {
                            return SERVICE_PKG + "." + name;
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            logger.error(e);
        }
        return null;
    }

    /**
     * Returns the name of a bean that implements the RestService interface and that is
     * mapped to the given URI (or URL path fragment) based on a class naming convention
     * in which the URI occurs within the class name.
     *
     * @param path a URI or URL path fragment for which to resolve a service class name.
     * @return the name of a bean that is mapped to the given URI or URL path fragment.
     */
    public String getServiceBeanName(String path) {
        try {
            List<Class<?>> classes = getClasses(SERVICE_PKG);
            for (Class<?> clazz : classes) {
                if (isRestService(clazz)) {
                    String name = clazz.getSimpleName();
                    if (name.indexOf(SERVICE) != -1) {
                        String partialName = name.substring(0, name.indexOf(SERVICE));
                        if (path.equalsIgnoreCase(partialName)) {
                            return toBeanName(name);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            logger.error(e);
        }
        return null;
    }

    private String toBeanName(String serviceClass) {
        String name = serviceClass.substring(0, 1).toLowerCase() +
                serviceClass.substring(1, serviceClass.length());
        name = name.substring(0, serviceClass.length() - 4);
        return name;
    }

    /**
     * Returns the name of an arg class that is mapped to the given URI (or URL path fragment).
     *
     * @param path a URI or URL path fragment for which to resolve an arg class name.
     * @return the name of a class that is mapped to the given URI or URL path fragment.
     */
    public String getArgClassName(String path) {
        try {
            List<Class<?>> classes = getClasses(ARG_PKG);
            for (Class<?> clazz : classes) {
                String name = clazz.getSimpleName();
                if (name.indexOf(ARG) != -1) {
                    if (name.equalsIgnoreCase(path + ARG)) {
                        return ARG_PKG + "." + name;
                    }
                }
            }

            // seek xftas package
            classes = getClasses(ARG_PKG_XFTAS);
            for (Class<?> clazz : classes) {
                String name = clazz.getSimpleName();
                if (name.indexOf(ARG) != -1) {
                    String partialName = name.substring(0, name.indexOf(ARG));
                    if (path.equalsIgnoreCase(partialName)) {
                        return ARG_PKG_XFTAS + "." + name;
                    }
                }
            }

        } catch (ClassNotFoundException | IOException e) {
            logger.error(e);
        }

        // not found
        return null;
    }

    /**
     * Returns the name of an entity class that is mapped to the given URI (or URL path fragment).
     *
     * @param path a URI or URL path fragment for which to resolve an entity class name.
     * @return the name of a class that is mapped to the given URI or URL path fragment.
     */
    public String getEntityClassName(String path) {
        try {
            List<Class<?>> classes = getClasses(ENTITY_PKG);
            for (Class<?> clazz : classes) {
                String name = clazz.getSimpleName();
                if (path.equalsIgnoreCase(name)) {
                    return ENTITY_PKG + "." + name;
                }
            }

            // seek in xftas package
            classes = getClasses(ENTITY_PKG_XFTAS);
            for (Class<?> clazz : classes) {
                String name = clazz.getSimpleName();
                if (path.equalsIgnoreCase(name)) {
                    return ENTITY_PKG_XFTAS + "." + name;
                }
            }

        } catch (ClassNotFoundException | IOException e) {
            logger.error(e);
        }
        // not found
        return null;
    }

    /**
     * Scans the core.services package, creates ResourceDescriptor objects from the interface class names.
     */
    public List<ResourceDescriptor> getResourceInfos() {
        List<ResourceDescriptor> descriptors = new ArrayList<ResourceDescriptor>();
        try {
            List<Class<?>> classes = getClasses(SERVICE_PKG);
            for (Class<?> clazz : classes) {
                if (isRestService(clazz)) {
                    String name = clazz.getSimpleName();
                    if (name.indexOf(SERVICE) != -1) {
                        name = name.substring(0, name.indexOf(SERVICE));
                    }
                    descriptors.add(new ResourceDescriptor(name));
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            logger.error(e);
        }

        // Sort by resource name
        Collections.sort(descriptors);
        return descriptors;
    }

    /**
     * Returns true if the given Class implements the RestService interface.
     */
    private boolean isRestService(Class clazz) {
        for (Class iface : clazz.getInterfaces()) {
            if (iface.getName().endsWith("RestService")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Scans a package and returns a List of Class that belong to it. Also scans
     * sub-packages, if any.
     */
    public static ArrayList<Class<?>> getClasses(String packageName)
            throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

        packageName = packageName.replace(".", "/");
        Enumeration<URL> packageURLs = classLoader.getResources(packageName);

        while (packageURLs.hasMoreElements()) {
            URL packageURL = packageURLs.nextElement();
            if (packageURL.getProtocol().equals("jar")) {
                String entryName = "";

                // scan the jar file's entries
                String jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
                jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
                JarFile jf = new JarFile(jarFileName);

                Enumeration<JarEntry> jarEntries = jf.entries();
                while (jarEntries.hasMoreElements()) {
                    entryName = jarEntries.nextElement().getName();
                    if (entryName.startsWith(packageName) && entryName.endsWith(".class")) {
                        entryName = entryName.substring(packageName.length(), entryName.lastIndexOf('.'));
                        String fullName = (packageName + entryName).replaceAll("/", ".");
                        classes.add(Class.forName(fullName));
                    }
                }
            } else {
                File folder = new File(packageURL.getFile());
                File[] files = folder.listFiles();
                for (File file : files) {
                    String entryName = file.getName();
                    if (entryName.endsWith(".class")) {
                        entryName = entryName.substring(0, entryName.lastIndexOf('.'));
                        classes.add(Class.forName(packageName.replaceAll("/", ".") + '.' + entryName));
                    }
                }
            }
        }

        return classes;
    }

}
