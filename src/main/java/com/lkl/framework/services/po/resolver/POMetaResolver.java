package com.lkl.framework.services.po.resolver;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lkl.framework.services.po.cache.DataCache;
import com.lkl.framework.services.po.meta.POMetadata;
import com.lkl.framework.services.po.parent.PO;
import com.lkl.framework.services.po.parse.Parse;

/**
 * 实体元数据解析器
 * 
 * @author liaokailin
 * @version $Id: POMetaResolver.java, v 0.1 2015年10月13日 下午5:20:14 liaokailin Exp $
 */
public class POMetaResolver implements Resolver<POMetadata> {

    private static final Logger LOGGER = LoggerFactory.getLogger(POMetaResolver.class);

    private POMetaResolver(Parse parse) {
        this.parse = parse;
    }

    private volatile static POMetaResolver resolver;

    public static POMetaResolver getInstance(Parse parse) {
        if (resolver == null) {
            synchronized (POMetaResolver.class) {
                if (resolver == null) {
                    resolver = new POMetaResolver(parse);
                }
            }
        }
        return resolver;
    }

    private Parse parse;

    @Override
    public POMetadata resolver(Class<? extends PO> clazz) {
        POMetadata meta = DataCache.META_HOLDER.get(clazz);
        if (meta == null) {
            meta = new POMetadata();
            meta.setClazz(clazz);
            meta.setTabName(parse.parseTableName(clazz));
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(clazz, PO.class);
                PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
                if (pds != null && pds.length > 0) {
                    for (int i = 0; i < pds.length; i++) {
                        meta.getMetadata().addLast(parse.parseMeta(pds[i]));
                    }
                }

                DataCache.META_HOLDER.put(clazz, meta);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("class [{}] parse info is {} .", clazz, meta);
                }

                try {
                    meta.setConditionMethod(clazz.getMethod("__conditon_info__"));
                } catch (Exception e) {
                    LOGGER.error(" resolver the condition method of {} error.", clazz);
                }
            } catch (IntrospectionException e) {
                LOGGER.error("resolver class[{}] bean info error.", clazz);
            }

        }
        return meta;
    }

}
