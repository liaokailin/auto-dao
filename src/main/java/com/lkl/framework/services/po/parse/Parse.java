package com.lkl.framework.services.po.parse;

import java.beans.PropertyDescriptor;

import com.lkl.framework.services.po.meta.POMetadata.Metadata;

/**
 * 元数据解析模板，提供多模板解析不同实体与表对应关系
 * 后期可考虑匹配多种模板匹配
 * 
 * @author liaokailin
 * @version $Id: Parse.java, v 0.1 2015年10月13日 下午4:45:40 liaokailin Exp $
 */
public interface Parse {

    /**
     * 解析元数据
     */
    Metadata parseMeta(PropertyDescriptor pd);

    /**
     * 解析表名称
     */
    String parseTableName(Class<?> clazz);
}
