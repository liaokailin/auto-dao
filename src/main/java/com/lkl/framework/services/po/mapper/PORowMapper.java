package com.lkl.framework.services.po.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.lkl.framework.services.po.utils.POJdbcUtils;

/**
 * 查询结果处理
 * 
 * @author liaokailin
 * @version $Id: PORowMapper.java, v 0.1 2015年10月17日 下午4:01:49 liaokailin Exp $
 */
public abstract class PORowMapper<T> implements RowMapper<T> {

    public abstract T mapRow(ResultSet rs, int rowNum) throws SQLException;

    protected Map<String, Object> createColumnMap(int columnCount) {
        return new LinkedCaseInsensitiveMap<Object>(columnCount);
    }

    protected String getColumnKey(String columnName) {
        return columnName;
    }

    protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
        return POJdbcUtils.getResultSetValue(rs, index);
    }

}
