package com.lkl.framework.services.po.parse;

/**
 * 驼峰-属性一致
 * <pre class="code">
 *  attribute:birthDate --> column:birthDate
 * </pre>
 * 
 * @author liaokailin
 * @version $Id: HumpConsistencyParse.java, v 0.1 2015年10月16日 下午4:47:51 liaokailin Exp $
 */
public class HumpConsistencyParse extends HumpParse {

    public HumpConsistencyParse() {
    }

    public HumpConsistencyParse(boolean isLowerTable) {
        super.setLowerTable(isLowerTable);
    }

    @Override
    public String parseColName(String attrName) {
        return attrName.toUpperCase();
    }

}
