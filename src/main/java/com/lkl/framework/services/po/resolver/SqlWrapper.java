package com.lkl.framework.services.po.resolver;

import java.util.LinkedList;

import com.lkl.framework.services.po.meta.POMetadata;

/**
 * sql信息封装
 * 
 * @author liaokailin
 * @version $Id: SqlWrapper.java, v 0.1 2015年10月15日 上午11:25:04 liaokailin Exp $
 */
public class SqlWrapper {

    private String             sql;
    private POMetadata         meta;
    private LinkedList<Object> params = new LinkedList<Object>();

    public SqlWrapper() {
    }

    public SqlWrapper(String sql) {
        super();
        this.sql = sql;
    }

    public SqlWrapper(String sql, POMetadata meta) {
        super();
        this.sql = sql;
        this.meta = meta;
    }

    public SqlWrapper(String sql, POMetadata meta, LinkedList<Object> params) {
        this.sql = sql;
        this.meta = meta;
        this.params = params;
    }

    public POMetadata getMeta() {
        return meta;
    }

    public void setMeta(POMetadata meta) {
        this.meta = meta;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public LinkedList<Object> getParams() {
        return params;
    }

    public void setParams(LinkedList<Object> params) {
        this.params = params;
    }

}
