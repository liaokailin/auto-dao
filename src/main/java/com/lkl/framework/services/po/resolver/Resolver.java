package com.lkl.framework.services.po.resolver;

import com.lkl.framework.services.po.parent.PO;

/**
 * 解析器
 * 
 * @author liaokailin
 * @version $Id: Resolver.java, v 0.1 2015年10月13日 下午4:31:44 liaokailin Exp $
 */
public interface Resolver<T> {

    public T resolver(Class<? extends PO> clazz);

}
