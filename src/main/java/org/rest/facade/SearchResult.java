package org.rest.facade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SearchResult contains a list of items for a current page and a count
 * of total items that match a findAll request.
 *
 * @author A. Rodriguez
 */
public class SearchResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<T> items = new ArrayList<T>();
    private Integer totalCount = null;

    public SearchResult() {
    }

    /**
     * @param data       List of items
     * @param totalCount Total count of all items available using the last query
     */
    public SearchResult(List<T> data, int totalCount) {
        if (data != null) {
            this.items = new ArrayList<T>(data);
        } else {
            this.items = new ArrayList<T>();
        }
        this.totalCount = totalCount;
    }

    /**
     * Contains an array of items matching a request. If the 'totalCount' property
     * is greater than the size of the list, then there are more items to paginate.
     *
     * @return List
     */
    public List<T> getItems() {
        if (this.items == null) {
            return new ArrayList<T>();
        }
        return this.items;
    }

    /**
     * Contains an array of items matching a request. If the 'totalCount' property
     * is greater than the size of this list then there are more items to paginate.
     *
     * @param data
     * @read-only
     */
    public void setItems(List<T> data) {
        if (data != null) {
            this.items = new ArrayList<T>(data);
        } else {
            this.items = new ArrayList<T>();
        }
    }

    /**
     * Total count of all items that match a request. Indicates pages of items available.
     *
     * @return int
     */
    public Integer getTotalCount() {
        return this.totalCount != null ? this.totalCount : 0;
    }

    /**
     * Total count of all items that match a request. Indicates pages of items available.
     *
     * @param totalCount
     * @read-only
     */
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

}
