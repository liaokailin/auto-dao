package com.lkl.framework.services.po.resolver;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.lkl.framework.services.po.parent.PO;
import com.lkl.framework.services.po.parse.Parse;

/**
 * sql语句解析器父类
 * 
 * @author liaokailin
 * @version $Id: SqlResolver.java, v 0.1 2015年10月15日 下午2:57:53 liaokailin Exp $
 */
public abstract class SqlResolver<T extends PO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqlResolver.class);

    protected Parse             parse;

    protected LinkedList<T>     target = Lists.newLinkedList();

    public SqlResolver() {
    }

    public SqlResolver(Parse parse) {
        this.parse = parse;
    }

    public SqlResolver<T> setParse(Parse parse) {
        this.parse = parse;
        return this;
    }

    public SqlResolver<T> setTarget(T t) {
        this.target.add(t);
        return this;
    }

    protected abstract SqlWrapper resolver();

    public SqlWrapper execute() {
        if (check()) {
            return resolver();
        }
        return null;
    }

    protected boolean check() {
        if (this.parse == null) {
            LOGGER.warn("Sql parser is null .");
            return false;
        }
        if (this.target == null || this.target.isEmpty()) {
            LOGGER.warn("Sql parse target is null .");
            return false;
        }
        return true;
    }

}
