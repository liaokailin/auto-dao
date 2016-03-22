package com.lkl.framework.services.po.page;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页结果封装
 * 
 * @author liaokailin
 * @version $Id: PageResult.java, v 0.1 2015年10月14日 下午7:33:38 liaokailin Exp $
 */
public class PageResult<T> {

    public static final Integer DEFAULT_PAGEINDEX = 1;
    public static final Integer DEFAULT_PAGESIZE  = 20;
    public static final Integer MAX_PAGESIZE      = 1000;

    private int                 pageIndex         = DEFAULT_PAGEINDEX;

    private int                 pageSize          = DEFAULT_PAGESIZE;

    private long                totalRecords      = 0;

    private List<T>             records           = new ArrayList<T>();

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize > MAX_PAGESIZE) {
            pageSize = DEFAULT_PAGESIZE;
        }
        this.pageSize = pageSize;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "PageResult [pageIndex=" + pageIndex + ", pageSize=" + pageSize + ", totalRecords=" + totalRecords
               + ", records=" + records + "]";
    }

}
