package com.lkl.framework.services.po.resolver;

import java.util.LinkedList;

import org.springframework.util.CollectionUtils;

import com.lkl.framework.services.po.exception.ParseException;
import com.lkl.framework.services.po.meta.POMetadata;
import com.lkl.framework.services.po.meta.POMetadata.Metadata;
import com.lkl.framework.services.po.parent.PO;

/**
 * 参数解析
 * 
 * @author liaokailin
 * @version $Id: SqlParamResolver.java, v 0.1 2015年10月18日 下午12:09:13 liaokailin Exp $
 */
public abstract class SqlParamResolver<T extends PO> extends SqlResolver<T> {
    protected T          t;
    protected POMetadata meta;

    @Override
    public SqlWrapper resolver() {
        ParamWrapper paramWrapper = this.resolverParam();
        String resultSql = resolverSql(meta, paramWrapper);
        return resultSql == null ? null : new SqlWrapper(resultSql, meta, paramWrapper.getParams());
    }

    /**
     * 解析sql
     */
    public abstract String resolverSql(POMetadata meta, ParamWrapper paramWrapper);

    public ParamWrapper resolverParam() {
        ParamWrapper paramWrapper = new ParamWrapper();
        if (t == null)
            t = this.target.get(0);
        meta = POMetaResolver.getInstance(this.parse).resolver(t.getClass());
        if (meta != null) {
            LinkedList<Metadata> metaList = meta.getMetadata();
            if (!CollectionUtils.isEmpty(metaList)) {
                for (Metadata m : metaList) {
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
        return paramWrapper;
    }
}
