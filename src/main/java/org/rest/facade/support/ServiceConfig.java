package org.rest.facade.support;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Parses and loads REST configuration elements as defined in rest-resources.xml.
 * The elements in rest-resources.xml are parsed into a List of ResourceConfig that
 * this loader provides access to.
 *
 * @author A. Rodriguez
 */
public class ServiceConfig {

    public static final String NAME = "name";
    public static final String URI = "uri";
    public static final String ARG_CLS = "arg-class";
    public static final String ENTITY_CLS = "entity-class";
    public static final String RESOURCE = "resource";
    public static final String SERVICE_STUB = "service-stub";
    public static final String ASYNC_HANDLE = "async-handle";
    public static final String IS_UNPUBLISHED = "is-unpublished";

    /**
     * The Singleton instance of the loader.
     */
    private static ServiceConfig instance = null;

    /**
     * A List of ResourceConfig, as defined in rest-resources.xml.old.
     */
    private List<ResourceConfig> configs = new ArrayList<ResourceConfig>();

    /**
     * Private no-arg constructor for the Singleton.
     */
    private ServiceConfig() {
    }

    /**
     * Returns the Singleton instance of the ServiceConfig.
     */
    public static synchronized ServiceConfig gInstance() {
        if (instance == null) {
            instance = new ServiceConfig();
        }
        return instance;
    }

    /**
     * Parses the given stream into a List<ResourceConfig>.
     */
    public void load(InputStream stream) {
        this.configs.clear();
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            NodeList nodes = builder.parse(new InputSource(stream)).getElementsByTagName(RESOURCE);
            for (int i = 0; i < nodes.getLength(); i++) {
                Node resource = nodes.item(i);
                String name = resource.getAttributes().getNamedItem(NAME).getNodeValue();
                NodeList elements = resource.getChildNodes();
                this.configs.add(createConfig(elements, name));
            }
        } catch (ParserConfigurationException | SAXException | IOException ignored) {
        }

    }

    /**
     * Creates a ResourceConfig from the given NodeList and resource name.
     */
    private ResourceConfig createConfig(NodeList elements, String name) {
        ResourceConfig config = new ResourceConfig();
        config.addElement(NAME, name);
        for (int n = 0; n < elements.getLength(); n++) {
            Node element = elements.item(n);
            config.addElement(element.getNodeName(), getNodeValue(element));
        }
        return config;
    }

    /**
     * Returns the text value of the given Node.
     */
    public String getNodeValue(Node node) {
        NodeList nodeList = node.getChildNodes();
        int length = nodeList.getLength();
        for (int i = 0; i < length; i++) {
            Node n = nodeList.item(i);
            if (n instanceof Text) {
                Text t = (Text) n;
                return t.getNodeValue();
            }
        }
        return "";
    }

    public String getServiceClassName(String path) {
        return getResourceConfigElement(URI, "/" + path, SERVICE_STUB);
    }

    public String getEntityClassName(String path) {
        return getResourceConfigElement(URI, "/" + path, ENTITY_CLS);
    }

    public String getArgClassName(String path) {
        return getResourceConfigElement(URI, "/" + path, ARG_CLS);
    }

    public String getAsyncHandleClassName(String path) {
        return getResourceConfigElement(URI, "/" + path, ASYNC_HANDLE);
    }

    public String getIsUnpublished(String path) {
        return getResourceConfigElement(URI, "/" + path, IS_UNPUBLISHED);
    }

    /**
     * Returns the list of ResourceConfig this ServiceConfig loads/exposes.
     */
    public Map<String, String> getMapping() {
        Map<String, String> result = new HashMap<String, String>(configs.size());
        for (ResourceConfig config : configs) {
            result.put(config.getElementValue(NAME), config.getElementValue(URI));
        }
        return result;
    }

    /**
     * Returns a value for a specific config element, given a match for related
     * element name and value.
     */
    public String getResourceConfigElement(String elementName, String elementValue,
                                           String returnElement) {
        for (ResourceConfig config : configs) {
            if (config.getElementValue(elementName).length() != 0
                    && config.getElementValue(elementName).equalsIgnoreCase(elementValue)) {
                return config.getElementValue(returnElement);
            }
        }
        return "";
    }

    /**
     * A holder for rest resource configuration values.
     */
    private class ResourceConfig {
        private Map<String, String> elements = new HashMap<String, String>();

        /**
         * Adds an element to this ResourceConfig.
         */
        public void addElement(String elementName, String elementValue) {
            this.elements.put(elementName, elementValue);
        }

        /**
         * Returns the element value for the given element or empty String if
         * the element is undefined.
         */
        public String getElementValue(String elementName) {
            if (elements.get(elementName) != null) {
                return elements.get(elementName);
            } else {
                return "";
            }
        }

        /**
         * Returns all the element names in this ResourceConfig.
         */
        public Set<String> getElementNames() {
            return this.elements.keySet();
        }

    }

}