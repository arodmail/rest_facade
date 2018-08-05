package org.rest.facade.http;

/**
 * Provides methods to split up a URI into fragments.
 *
 * @author A. Rodriguez
 */
public class RequestPath {

    /**
     * String constant for the slash char
     */
    public static final String SLASH = "/";

    /**
     * Holds the entire pathInfo String
     */
    private String pathInfo = "";

    /**
     * The pathInfo String split up into fragments
     */
    private String[] pathFragments = {""};

    public static final int ROOT = 0;
    public static final int TYPE = 2;
    public static final int ID = 3;
    public static final int ASYNC = 4;
    public static final int FILE = 5;

    /**
     * Creates a new RequestPath.
     */
    public RequestPath(String pathInfo) {
        this.pathInfo = pathInfo;
        if (this.pathInfo != null) {
            this.pathFragments = pathInfo.split("/");
        }
    }

    /**
     * Returns the first path fragment. Given /path1/path2/, returns "path1".
     */
    public String getFirstFragment() {
        String result = "";
        if (hasFragments()) {
            result = pathFragments[0];
        }
        return result;
    }

    /**
     * Returns the first path fragment. Given /path1/path2/, returns "path2".
     */
    public String getLastFragment() {
        String result = "";
        if (hasFragments()) {
            result = pathFragments[pathFragments.length - 1];
        }
        return result;
    }

    /**
     * Returns the given path fragment by index. Given /path1/path2/,
     * and i = 0, returns "path1".
     */
    public String getFragment(int i) {
        String result = "";
        if (hasFragments() && i >= 0 && i < pathFragments.length) {
            result = pathFragments[i];
        }
        return result;
    }

    /**
     * Returns the total number of path fragments. Given /path1/path2/,
     * returns 2.
     */
    public int getFragmentCount() {
        if (hasFragments()) {
            return pathFragments.length;
        }
        return 0;
    }

    /**
     * Returns true if the request path is not empty.
     */
    private boolean hasFragments() {
        if (this.pathInfo != null && pathFragments != null) {
            return true;
        }
        return false;
    }

}
