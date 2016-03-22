package com.lkl.framework.services.po.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.lkl.framework.services.po.cache.DataCache;
import com.lkl.framework.services.po.meta.POMetadata;
import com.lkl.framework.services.po.parent.PO;

/**
 * 删除语句解析器
 * @author liaokailin
 * @version $Id: InsertSqlResolver.java, v 0.1 2015年10月15日 上午12:30:40 liaokailin Exp $
 */
public class DeleteSqlResolver<T extends PO> extends SqlParamResolver<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteSqlResolver.class);

    @Override
    public String resolverSql(POMetadata meta, ParamWrapper paramWrapper) {
        String resultSql = null;
        boolean isCache = false;
        if (!CollectionUtils.isEmpty(paramWrapper.getColumnNames())) {
            resultSql = DataCache.DELETE_SQL_HOLDER.get(meta.getTabName() + paramWrapper.getColumnNames().toString());
            if (resultSql != null) {
                isCache = true;
            } else {
                if (!CollectionUtils.isEmpty(paramWrapper.getColumnNames())) {
                    StringBuilder sql = new StringBuilder();
                    sql.append("DELETE FROM " + meta.getTabName() + " WHERE 1=1 ");
                    if (!CollectionUtils.isEmpty(paramWrapper.getColumnNames())) {
                        for (String columnName : paramWrapper.getColumnNames()) {
                            sql.append("AND " + columnName + "=? ");
                        }
                    }
                    resultSql = sql.toString();
                    DataCache.DELETE_SQL_HOLDER.put(meta.getTabName() + paramWrapper.getColumnNames().toString(),
                        resultSql);
                }
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(isCache ? "+++retrieve sql from cache " : "+++dynamic splicing sql");
            LOGGER.debug("sql: {} ;param: {}", resultSql, paramWrapper.getParams());
        }
        return resultSql;

    }

}
