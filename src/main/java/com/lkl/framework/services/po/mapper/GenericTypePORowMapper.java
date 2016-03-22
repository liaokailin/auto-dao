package com.lkl.framework.services.po.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.support.JdbcUtils;

import com.lkl.framework.services.po.exception.ParseException;
import com.lkl.framework.services.po.meta.MetaType;
import com.lkl.framework.services.po.meta.POMetadata;
import com.lkl.framework.services.po.meta.POMetadata.Metadata;
import com.lkl.framework.services.po.parent.PO;

/**
 * 查询结果处理，返回泛型类型
 * 
 * @author liaokailin
 * @version $Id: PORowMapper.java, v 0.1 2015年10月17日 下午4:01:49 liaokailin Exp $
 */
public class GenericTypePORowMapper<T extends PO> extends PORowMapper<T> {

    private POMetadata meta;

    public GenericTypePORowMapper(POMetadata meta) {
        this.meta = meta;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        T t = null;
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        try {
            t = (T) meta.getClazz().newInstance();
            for (int i = 1; i <= columnCount; i++) {
                String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i)); //获取数据库字段
                try {
                    Object obj = getColumnValue(rs, i);
                    if (obj != null) {
                        Metadata metadata = this.meta.indexOf(key, MetaType.column);
                        metadata.getWriteMethod().invoke(t, metadata.getAttrType().cast(obj));
                    }
                } catch (Exception e) {
                    throw new ParseException("resolver field " + key + " error.", e);
                }

            }
        } catch (Exception e) {
            throw new ParseException("resolver query result error .", e);
        }
        return t;
    }

}
