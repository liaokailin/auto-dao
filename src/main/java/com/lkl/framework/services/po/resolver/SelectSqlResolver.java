package com.lkl.framework.services.po.resolver;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.lkl.framework.services.po.cache.DataCache;
import com.lkl.framework.services.po.exception.ParseException;
import com.lkl.framework.services.po.meta.MetaType;
import com.lkl.framework.services.po.meta.POMetadata;
import com.lkl.framework.services.po.meta.POMetadata.Metadata;
import com.lkl.framework.services.po.parent.Condition;
import com.lkl.framework.services.po.parent.Condition.Segment;
import com.lkl.framework.services.po.parent.PO;

;

/**
 * 查询语句解析器
 * @author liaokailin
 * @version $Id: InsertSqlResolver.java, v 0.1 2015年10月15日 上午12:30:40 liaokailin Exp $
 */
public class SelectSqlResolver<T extends PO> extends SqlResolver<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SelectSqlResolver.class);
    private T                   t;
    private POMetadata          meta;
    private Method              conditionMethod;
    private Condition           cc;

    public String resolverSql(POMetadata meta, ParamWrapper paramWrapper) {
        String resultSql = null;
        boolean isCache = false;
        resultSql = DataCache.SELECT_SQL_HOLDER.get(meta.getTabName() + paramWrapper.getColumnNames().toString());
        if (resultSql != null) {
            isCache = true;
        } else {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ");
            LinkedList<Metadata> metaList = meta.getMetadata();
            if (metaList != null && !metaList.isEmpty()) {
                for (Metadata m : metaList) {
                    sql.append(m.getColumnName() + ",");
                }
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(" FROM " + meta.getTabName() + " WHERE 1 = 1 ");
            if (!CollectionUtils.isEmpty(paramWrapper.getColumnNames())) {
                for (String colName : paramWrapper.getColumnNames()) {
                    sql.append(" AND " + colName + "=? ");
                }
            }
            resultSql = sql.toString();
            DataCache.SELECT_SQL_HOLDER.put(meta.getTabName() + paramWrapper.getColumnNames().toString(), resultSql);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(isCache ? "+++retrieve sql from cache " : "+++dynamic splicing sql");
        }
        return resultSql;
    }

    @Override
    public SqlWrapper resolver() {
        t = this.target.get(0);
        ParamWrapper paramWrapper = new ParamWrapper();
        meta = POMetaResolver.getInstance(this.parse).resolver(t.getClass());
        if (meta != null) {
            paramWrapper = resolverParam();
            String sql = this.resolverSql(meta, paramWrapper);
            //parse extra condition 
            if (conditionMethod != null) {
                try {
                    if (cc != null) {
                        List<Segment> segments = cc.getSegments();
                        if (!CollectionUtils.isEmpty(segments)) {
                            StringBuilder sqlSegment = new StringBuilder();
                            for (Segment s : segments) {
                                sqlSegment.append(" AND ")
                                    .append(meta.indexOf(s.getAttrName(), MetaType.attribute).getColumnName())
                                    .append(s.getSymbol());
                            }
                            sql = sql + sqlSegment.toString();
                        }
                        // parse order by 
                        List<Segment> orderBySegments = cc.getOrderBySegments();
                        if (!CollectionUtils.isEmpty(orderBySegments)) {
                            StringBuilder orderBySegment = new StringBuilder();
                            for (Segment s : orderBySegments) {
                                orderBySegment
                                    .append(meta.indexOf(s.getAttrName(), MetaType.attribute).getColumnName())
                                    .append(s.getSymbol()).append(",");
                            }
                            if (orderBySegment.length() > 1) {
                                orderBySegment.deleteCharAt(orderBySegment.length() - 1);
                                sql = sql + " ORDER BY " + orderBySegment.toString();
                            }
                        }
                        List<Object> conditionParams = cc.getParams();
                        if (!CollectionUtils.isEmpty(conditionParams)) {
                            paramWrapper.getParams().addAll(conditionParams);
                        }
                        //fix 清除cc中数据，防止第二次调用重新使用append条件
                        cc.clear();
                    }
                } catch (Exception e) {
                    LOGGER.warn("resolver extra condition error ,ignore this .");
                }
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("sql: {} ;param: {}", sql.toString(), paramWrapper.getParams());
            }
            return new SqlWrapper(sql.toString(), meta, paramWrapper.getParams());
        }
        return null;

    }

    protected ParamWrapper resolverParam() {
        ParamWrapper paramWrapper = new ParamWrapper();
        if (meta != null) {
            conditionMethod = meta.getConditionMethod();
            if (conditionMethod != null) {
                try {
                    cc = (Condition) conditionMethod.invoke(t, new Object[0]);
                } catch (Exception e) {
                    LOGGER.error(" invoke condition method error .detail is {} ", e.getMessage());
                }
            }
            LinkedList<Metadata> metaList = meta.getMetadata();
            if (!CollectionUtils.isEmpty(metaList)) {
                for (Metadata m : metaList) {
                    if (cc == null || cc.indexOf(m.getAttrName()) == -1) { //额外条件中存在该属性对应条件时，该属性对应的equals条件失效
                        Object obj = null;
                        try {
                            obj = m.getReadMethod().invoke(t, new Object[0]);
                        } catch (Exception e) {
                            throw new ParseException("invoke readmethod error", e);
                        }
                        if (obj != null) {
                            paramWrapper.getColumnNames().addLast(m.getColumnName());
                            paramWrapper.getParams().addLast(obj);
                        }
                    }
                }
            }
        }
        return paramWrapper;
    }
}
