package org.rest.facade.response;

import org.rest.facade.support.ResultList;

import java.util.Map;

/**
 * Defines methods common for REST API responses, toJSON().
 *
 * @author A. Rodriguez
 */
public interface RestResponse {

    String ACCEPT = "Accept";
    String ACCEPT_ENCODING = "Accept-Encoding";
    String CONTENT_ENCODING = "Content-Encoding";
    String FORMAT = "format";
    String JSON_MIME = "application/json";
    String CALLBACK = "callback";
    String GZIP = "gzip";
    String LOCATION = "Location";
    String QUEUE = "queue";
    String ASYNC = "async";
    String CACHE_CONTROL = "Cache-Control";
    String NO_CACHE = "no-store, no-cache";

    /**
     * @param responseCode an HTTP response code.
     */
    void setCode(int responseCode);

    /**
     * Returns the HTTP response code.
     *
     * @return int an HTTP response code.
     */
    int getCode();

    /**
     * Sets the error message.
     *
     * @param msg an error message.
     */
    void setMsg(String msg);

    /**
     * Gets the error message.
     *
     * @return and error message.
     */
    String getMsg();

    /**
     * Returns a JSON representation of an HTTP response.
     *
     * @return JSON String representing the response.
     */
    String toJSON();

    /**
     * Returns a Map of String that holds the names and values of response
     * headers.
     *
     * @return Map of String naming headers and corresponding values.
     */
    Map<String, String> getHeaders();

    /**
     * Assigns a Map of String that holds the names and values of response
     * headers.
     *
     * @param headers of String naming headers and corresponding values.
     */
    void setHeaders(Map<String, String> headers);

    ResultList<?> getResultList();

}
