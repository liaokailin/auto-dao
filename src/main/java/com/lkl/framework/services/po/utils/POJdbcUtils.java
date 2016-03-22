package com.lkl.framework.services.po.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.support.JdbcUtils;

/**
 * 工具类
 * 
 * @see {@link JdbcUtils}
 * @author liaokailin
 * @version $Id: POJdbcUtils.java, v 0.1 2015年10月17日 下午5:17:40 liaokailin Exp $
 */
public class POJdbcUtils {

    /**
     * 处理转换异常问题
     */
    public static Object getResultSetValue(ResultSet rs, int index) throws SQLException {
        Object obj = null;
        ResultSetMetaData rsmd = rs.getMetaData();
        if (rsmd.getColumnTypeName(index).equals("TINYINT")) {
            obj = rs.getInt(index);
        } else if (rsmd.getColumnTypeName(index).equals("BIGINT")) {
            obj = rs.getLong(index);
        } else {
            obj = JdbcUtils.getResultSetValue(rs, index);
        }

        return obj;
    }
}
