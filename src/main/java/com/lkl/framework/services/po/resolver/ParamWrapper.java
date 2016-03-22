package com.lkl.framework.services.po.resolver;

import java.util.LinkedList;

public class ParamWrapper {

    LinkedList<Object> params      = new LinkedList<Object>();
    LinkedList<String> columnNames = new LinkedList<String>();

    public LinkedList<Object> getParams() {
        return params;
    }

    public void setParams(LinkedList<Object> params) {
        this.params = params;
    }

    public LinkedList<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(LinkedList<String> columnNames) {
        this.columnNames = columnNames;
    }

}
