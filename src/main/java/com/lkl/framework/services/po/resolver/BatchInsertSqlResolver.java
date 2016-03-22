package com.lkl.framework.services.po.resolver;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lkl.framework.services.po.cache.DataCache;
import com.lkl.framework.services.po.meta.POMetadata;
import com.lkl.framework.services.po.meta.POMetadata.Metadata;
import com.lkl.framework.services.po.parent.PO;

/**
 * 批量插入sql解析器
 * @author liaokailin
 * @version $Id: BatchInsertSqlResolver.java, v 0.1 2015年10月15日 下午8:52:38 liaokailin Exp $
 */
public class BatchInsertSqlResolver<T extends PO> extends SqlResolver<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchInsertSqlResolver.class);

    @Override
    public SqlWrapper resolver() {

        if (this.target == null || this.target.isEmpty()) {
            LOGGER.warn("Sql parse target is null .");
            return null;
        }
        T t = this.target.get(0);
        String resultSql = DataCache.INSERT_SQL_HOLDER.get(t.getClass().toString());
        POMetadata meta = POMetaResolver.getInstance(parse).resolver(t.getClass());
        if (meta != null) {
            boolean isCache = false;
            if (resultSql != null) {
                isCache = true;
            } else {
                StringBuilder sql = new StringBuilder();
                sql.append("INSERT INTO  " + meta.getTabName() + " (");
                LinkedList<Metadata> metaList = meta.getMetadata();
                if (metaList != null && !metaList.isEmpty()) {
                    for (Metadata m : metaList) {
                        sql.append(m.getColumnName() + ",");
                    }
                }
                sql.deleteCharAt(sql.length() - 1);
                sql.append(") VALUES(");
                for (int i = 0; i < metaList.size(); i++) {
                    sql.append("?,");
                }
                sql.deleteCharAt(sql.length() - 1);
                sql.append(")");
                resultSql = sql.toString();
                DataCache.INSERT_SQL_HOLDER.put(t.getClass().toString(), resultSql);
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(isCache ? "+++retrieve sql from cache " : "+++dynamic splicing sql");
                LOGGER.debug("sql: {} ", resultSql);
            }
            return new SqlWrapper(resultSql, meta);
        }
        return null;

    }

}
