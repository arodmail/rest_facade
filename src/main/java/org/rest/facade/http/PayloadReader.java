package org.rest.facade.http;

import com.google.gson.*;
import org.rest.facade.RestArg;
import org.rest.facade.RestResource;
import org.rest.facade.marshall.JacksonWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the body of an HTTP request into a List of RestResource.
 *
 * @author A. Rodriguez
 */
public class PayloadReader {

    /**
     * Reads the HTTP payload from the given InputStream. Returns a List<RestResource>
     */
    public List<RestResource> readPayload(HttpServletRequest request, Class clazz)
            throws IOException {
        List<RestResource> result;
        String payLoad = removeWS(streamToString(request.getInputStream()));
        result = readJsonPayload(payLoad, clazz);
        return result;
    }

    private <E extends RestResource> List<E> readJsonPayload(String payLoad, Class<E> clazz) {
        List<E> resources = new ArrayList<E>();
        if (!payLoad.startsWith("[") && !payLoad.endsWith("]")) {
            E resource = JacksonWrapper.fromJSON(payLoad, clazz);
            resources.add(resource);
        } else {
            JsonParser parser = new JsonParser();
            JsonElement rootObj = parser.parse(payLoad);
            JsonArray items = rootObj.getAsJsonArray();
            for (JsonElement item : items) {
                E resource = JacksonWrapper.fromJSON(item.toString(), clazz);
                resources.add(resource);
            }
        }
        return resources;
    }

    /**
     * Reads the HTTP payload from the given InputStream. Returns a RestArg
     */
    public RestArg readPayloadIntoArg(HttpServletRequest request, Class clazz)
            throws IOException {
        String payLoad = removeWS(streamToString(request.getInputStream()));
        return readJsonPayloadIntoArg(payLoad, clazz);
    }

    private <E extends RestArg> E readJsonPayloadIntoArg(String payLoad, Class<E> clazz) {
        E restArg = JacksonWrapper.fromJSON(payLoad, clazz);
        return restArg;
    }

    /**
     * Reads the given InputStream into a String.
     */
    private String streamToString(InputStream stream) throws IOException {
        BufferedReader buffReader =
                new BufferedReader(new InputStreamReader(stream));
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            int i = buffReader.read();
            if (i == -1) {
                break;
            }
            stringBuilder.append((char) i);
        }
        return stringBuilder.toString();
    }

    private String removeWS(String str) {
        String removeThese = "[^\\u0009\\u000A\\u000D\\u0020-\\uD7FF\\uE000-\\uFFFD\\u10000-\\u10FFFF]";
        String postRemoveStr = str.replaceAll(removeThese, "");
        return postRemoveStr;
    }

}