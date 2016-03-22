package com.lkl.framework.services.po.parse;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import com.lkl.framework.services.po.meta.POMetadata.Metadata;

/**
 * 驼峰类型解析，表名采用下划线分割，区分大小写  User -> T_User
 * 
 * @author liaokailin
 * @version $Id: HumpParse.java, v 0.1 2015年10月16日 下午4:47:51 liaokailin Exp $
 */
public abstract class HumpParse implements Parse {

    public static final String TABLE_PREFIX = "T_";

    private boolean            isLowerTable = false; //是否小写表名称

    @Override
    public Metadata parseMeta(PropertyDescriptor pd) {
        Method writeMethod = pd.getWriteMethod();
        if (writeMethod != null) {
            Metadata m = new Metadata();
            m.setWriteMethod(writeMethod);
            m.setReadMethod(pd.getReadMethod());
            m.setAttrName(pd.getName());
            m.setAttrType(pd.getPropertyType());
            m.setColumnName(parseColName(pd.getName()));
            return m;
        }
        return null;
    }

    protected abstract String parseColName(String attrName);

    /**
     * 解析表名称 
     * note:不处理内部类
     */
    @Override
    public String parseTableName(Class<?> clazz) {
        String tabName = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);

        StringBuilder sbuf = new StringBuilder();

        for (int i = 0; i < tabName.length(); i++) {
            char ch = tabName.charAt(i);
            if ((ch <= 'Z') && (ch >= 'A') && (i != 0)) {
                sbuf.append('_');
            }
            sbuf.append(ch);
        }
        String tableName = TABLE_PREFIX + sbuf.toString();
        return isLowerTable() ? tableName.toLowerCase() : tableName;
    }

    public boolean isLowerTable() {
        return isLowerTable;
    }

    protected void setLowerTable(boolean isLowerTable) {
        this.isLowerTable = isLowerTable;
    }

}
