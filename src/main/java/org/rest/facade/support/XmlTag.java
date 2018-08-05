package org.rest.facade.support;

import java.util.Map;
import java.util.TreeMap;

/**
 * Defines string constants used in xml element names for request / response
 * envelopes, and utility methods to create tags.
 *
 * @author A. Rodriguez
 */
public class XmlTag {

    public static final String EQ = "=";
    public static final String LT = "<";
    public static final String GT = ">";
    public static final String LTL = "</";

    public static final String RESOURCE = "resource";

    private Map<String, String> attributes = new TreeMap<String, String>();

    /**
     * Returns an open tag from the given tag name.
     */
    public String openElement(String elementName) {

        StringBuilder openTag = new StringBuilder();

        openTag.append(LT).append(elementName);

        if (attributes.size() > 0) {
            for (String name : attributes.keySet()) {
                String value = attributes.get(name);
                openTag.append(" ").append(name).append(EQ + "\"").append(value).append("\"");
            }
            attributes.clear();
        }
        openTag.append(GT);
        return openTag.toString();
    }

    /**
     * Returns an close tag from the given tag name.
     */
    public String closeElement(String elementName) {
        return LTL + elementName + GT;
    }
}
