package com.lkl.framework.services.po.resolver;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lkl.framework.services.po.exception.ParseException;
import com.lkl.framework.services.po.meta.POMetadata;
import com.lkl.framework.services.po.meta.POMetadata.Metadata;
import com.lkl.framework.services.po.parent.PO;

/**
 * 更新sql解析器
 * @author liaokailin
 * @version $Id: UpdateSqlResolver.java, v 0.1 2015年10月15日 下午3:46:16 liaokailin Exp $
 */
public class UpdateSqlResolver<T extends PO> extends SqlResolver<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSqlResolver.class);

    @Override
    public SqlWrapper resolver() {
        if (this.target.size() != 2) {
            LOGGER.warn(" targe size is error . ");
            return null;
        }
        T condition = this.target.get(0);
        T targetValue = this.target.get(1);
        POMetadata meta = POMetaResolver.getInstance(parse).resolver(condition.getClass());
        if (meta != null) {
            StringBuilder sql = new StringBuilder();
            LinkedList<Object> params = new LinkedList<Object>();
            sql.append("UPDATE  " + meta.getTabName() + " SET ");
            LinkedList<Metadata> metaList = meta.getMetadata();
            if (metaList != null && !metaList.isEmpty()) {
                for (Metadata m : metaList) {
                    Object obj = null;
                    try {
                        obj = m.getReadMethod().invoke(targetValue, new Object[0]);
                    } catch (Exception e) {
                        throw new ParseException("invoke readmethod error", e);
                    }
                    if (obj != null) {
                        sql.append(m.getColumnName() + "=?,");
                        params.addLast(obj);
                    }
                }
                sql.deleteCharAt(sql.length() - 1);
                sql.append(" WHERE 1 = 1 ");
                for (Metadata m : metaList) {
                    Object obj = null;
                    try {
                        obj = m.getReadMethod().invoke(condition, new Object[0]);
                    } catch (Exception e) {
                        throw new ParseException("invoke readmethod error", e);
                    }
                    if (obj != null) {
                        sql.append("AND " + m.getColumnName() + "= ?");
                        params.addLast(obj);
                    }
                }
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("sql: {} ;param: {}", sql.toString(), params);
                }
            }
            return new SqlWrapper(sql.toString(), meta, params);
        }
        return null;

    }

}
