package org.rest.facade.support;

import org.rest.facade.RestArg;
import org.rest.facade.marshall.JAXBWrapper;
import org.rest.facade.response.RestResponse;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory used to create an instance of an implementation of the RestArg interface.
 * Converts a query string into an xml representation of a RestArg and uses JAXB to
 * go from xml to Java.
 *
 * @author A. Rodriguez
 */
public class RestArgFactory {

    private RestArgFactory() {
    }

    /**
     * Returns a populated instance of a RestArg that is mapped to the given
     * URI or URL path.
     */
    public static RestArg createArg(String path) {
        RestArg argInstance = null;
        try {
            Class argClass = getArgClass(path);
            argInstance = (RestArg) argClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return argInstance;
    }

    /**
     * Returns a populated instance of a RestArg that is mapped to the given
     * URI or URL path.
     */
    public static RestArg createArg(String path, String queryString) {
        if (queryString == null || queryString.equals("")) {
            return createArg(path);
        }
        RestArg argInstance = null;
        Class argClass = getArgClass(path);
        Map<String, List<String>> map = parseQueryString(queryString);

        // ignore special parameters
        if (map.get(RestResponse.FORMAT) != null) {
            boolean hasFormatArgument = false;
            Method[] methods = argClass.getMethods();
            for (Method method : methods) {
                if (method.getName().equals("setFormat")) {
                    hasFormatArgument = true;
                    break;
                }
            }

            if (hasFormatArgument == false) {
                map.remove(RestResponse.FORMAT);
            }
        }

        if (map.get(RestResponse.CALLBACK) != null) {
            map.remove(RestResponse.CALLBACK);
        }

        String argXML = buildXML(argClass, map).replaceAll("[^\\P{Cc}]", "");
        argInstance = createArg(argXML, argClass);
        return argInstance;
    }

    /**
     * Creates a RestArg from the given Arg json and class.
     */
    public static RestArg createArg(String argXML, Class argClass) {
        JAXBWrapper wrapper = new JAXBWrapper();
        RestArg argInstance = wrapper.unmarshallArg(argXML, argClass);
        return argInstance;
    }

    /**
     * Builds xml tags from a query string.
     */
    private static String buildXML(Class cls, Map<String, List<String>> map) {
        StringBuilder argXML = new StringBuilder();
        XmlTag xmlTag = new XmlTag();
        argXML.append(xmlTag.openElement(cls.getSimpleName()));
        for (String name : map.keySet()) {
            List<String> values = map.get(name);
            String tagName = resolveMethodName(cls, name);
            if (tagName.length() != 0) {
                for (String value : values) {
                    if (hasNestedElements(value)) {
                        argXML.append(xmlTag.openElement(tagName) + buildInnerTags(value) + xmlTag.closeElement(tagName));
                    } else if ((hasNestedArrayElements(value))) {
                        argXML.append(buildKeyValuePairInnerTags(tagName, value));
                    } else {
                        argXML.append(xmlTag.openElement(tagName) + value + xmlTag.closeElement(tagName));
                    }
                }
            }
        }
        argXML.append(xmlTag.closeElement(cls.getSimpleName()));
        return argXML.toString();
    }

    private static boolean hasNestedElements(String value) {
        return (value.startsWith("(") && value.endsWith(")"));
    }

    private static boolean hasNestedArrayElements(String value) {
        return (value.startsWith("[") && value.endsWith("]"));
    }

    /**
     * Nested elements of a RestArg are accepted on the query string inside
     * parenthesis. For example:
     * <p/>
     * (start,2012-06-05T16:56:20.278-07:00)(end,2012-06-05T16:56:20.278-07:00)
     * <p/>
     * Returns:
     * <p/>
     * <end>2012-06-05T21:40:55.762-07:00</end>
     * <start>2012-06-05T21:40:55.762-07:00</start>
     */
    private static String buildInnerTags(String value) {
        StringBuilder builder = new StringBuilder();
        String[] parts = value.split("\\)");
        for (String part : parts) {
            part = part.replaceAll("\\(", "");
            String[] nameVal = part.split(",");
            String name = nameVal[0];
            String val = nameVal[1];
            XmlTag tag = new XmlTag();
            builder.append(tag.openElement(name) + val + tag.closeElement(name));
        }
        return builder.toString();
    }

    protected static String buildKeyValuePairInnerTags(String tagName, String value) {
        StringBuilder builder = new StringBuilder();
        int end = value.indexOf("]");
        String trimmed = value.substring(1, end);
        String[] parts = trimmed.split("\\)");
        for (String part : parts) {
            part = part.replaceAll("\\(", "");
            String[] keyVal = part.split(",");
            String key = keyVal[0];
            String val = keyVal[1];
            XmlTag tag = new XmlTag();
            builder.append(tag.openElement(tagName));
            builder.append(tag.openElement("key") + key + tag.closeElement("key"));
            builder.append(tag.openElement("value") + val + tag.closeElement("value"));
            builder.append(tag.closeElement(tagName));
        }
        return builder.toString();
    }

    /**
     * Splits up the given query string (ampersand delimited name/value pairs) into Url-decoded
     * parameter names and values. Also accepts multi-valued parameters.
     */
    private static Map<String, List<String>> parseQueryString(String queryString) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        try {
            for (String parameter : queryString.split("&")) {
                String nameValuePair[] = parameter.split("=");
                String name = URLDecoder.decode(nameValuePair[0], "UTF-8");
                String value = "";
                if (nameValuePair.length > 1) {
                    value = URLDecoder.decode(nameValuePair[1], "UTF-8");
                }
                List<String> values = map.get(name);
                if (values == null) {
                    values = new ArrayList<String>();
                    if (!name.trim().equals("") && !value.trim().equals("")) {
                        map.put(name, values);
                    }
                }
                values.add(value);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Resolves and returns the true method name from the given class and
     * mixed case method name.
     */
    private static String resolveMethodName(Class cls, String mixedCaseMethodName) {
        Method[] methods = cls.getMethods();
        String result = "";
        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase("set" + mixedCaseMethodName)) {
                result = method.getName();
                result = result.substring("set".length(), result.length());
                result = result.substring(0, 1).toLowerCase() + result.substring(1, result.length());
                break;
            }
        }
        return result;
    }

    /**
     * Returns a RestArg implementation that is mapped to the given path.
     */
    public static Class getArgClass(String path) {
        String argClassName =
                ServiceConfig.gInstance().getArgClassName(path);
        if (argClassName.length() == 0) {
            argClassName =
                    ServiceLocator.gInstance().getArgClassName(path);
        }
        if (argClassName != null) {
            try {
                Class<?> argClass = Class.forName(argClassName);
                return argClass;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}