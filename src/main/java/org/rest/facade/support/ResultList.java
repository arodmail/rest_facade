package org.rest.facade.support;

import java.util.ArrayList;
import java.util.List;

/**
 * Holder for a List of items to marshal as a response payload.
 *
 * @author A. Rodriguez
 */
public class ResultList<T> {
    private List<T> items = new ArrayList<T>();

    // paging attributes
    private int start = 0;
    private int limit = 0;
    private int totalCount = 0;

    public ResultList(List<T> items) {
        this.items = items;
    }

    public List<T> getItems() {
        if (this.items == null) {
            return new ArrayList<T>();
        }
        return new ArrayList<T>(this.items);
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

}
