package com.lkl.framework.services.po.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.lkl.framework.services.po.cache.DataCache;
import com.lkl.framework.services.po.meta.POMetadata;
import com.lkl.framework.services.po.parent.PO;

/**
 * 插入sql解析器
 * @author liaokailin
 * @version $Id: InsertSqlResolver.java, v 0.1 2015年10月15日 上午11:30:40 liaokailin Exp $
 */
public class InsertSqlResolver<T extends PO> extends SqlParamResolver<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsertSqlResolver.class);

    @Override
    public String resolverSql(POMetadata meta, ParamWrapper paramWrapper) {
        String resultSql = null;
        boolean isCache = false;
        if (!CollectionUtils.isEmpty(paramWrapper.getColumnNames())) {
            resultSql = DataCache.INSERT_SQL_HOLDER.get(meta.getTabName() + paramWrapper.getColumnNames().toString());
            if (resultSql != null) {
                isCache = true;
            } else { //拼接sql
                StringBuilder sql = new StringBuilder();
                sql.append("INSERT INTO  " + meta.getTabName() + " (");
                for (String colName : paramWrapper.getColumnNames()) {
                    sql.append(colName + ",");
                }
                sql.deleteCharAt(sql.length() - 1);
                sql.append(") VALUES(");
                for (int i = 0; i < paramWrapper.getColumnNames().size(); i++) {
                    sql.append("?,");
                }
                sql.deleteCharAt(sql.length() - 1);
                sql.append(")");
                resultSql = sql.toString();

                DataCache.INSERT_SQL_HOLDER
                    .put(meta.getTabName() + paramWrapper.getColumnNames().toString(), resultSql);
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(isCache ? "+++retrieve sql from cache " : "+++dynamic splicing sql");
            LOGGER.debug("sql: {} ;param: {}", resultSql, paramWrapper.getParams());
        }
        return resultSql;
    }

}
