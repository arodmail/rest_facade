package org.rest.facade.response;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for implementations of RestResponse. Holds an HTTP
 * response code, and an error message.
 *
 * @author A. Rodriguez
 */
public abstract class BaseResponse implements RestResponse {

    // holds an HTTP response code
    private int code = 200;

    // holds a detailed error message
    private String msg = "";

    // holds response headers
    private Map<String, String> headers = new HashMap<String, String>();

    /**
     * Returns the HTTP response code.
     *
     * @return int an HTTP response code.
     */
    @Override
    public int getCode() {
        return this.code;
    }

    /**
     * Sets the HTTP response code.
     *
     * @param code HTTP response code.
     */
    @Override
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Gets the error message.
     *
     * @return and error message.
     */
    @Override
    public String getMsg() {
        return this.msg;
    }

    /**
     * Sets the error message.
     *
     * @param msg an error message.
     */
    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * Returns a JSON representation of an HTTP response.
     *
     * @return JSON String representing the response.
     */
    @Override
    public String toJSON() {
        return "";
    }

    @Override
    public void setHeaders(Map headers) {
        this.headers = headers;
    }

    @Override
    public Map<String, String> getHeaders() {
        return this.headers;
    }

}
