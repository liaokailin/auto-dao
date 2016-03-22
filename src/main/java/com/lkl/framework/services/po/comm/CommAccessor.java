package com.lkl.framework.services.po.comm;

import java.util.List;
import java.util.Map;

import com.lkl.framework.services.po.factory.POFactory;
import com.lkl.framework.services.po.operator.Operator;
import com.lkl.framework.services.po.page.PageResult;
import com.lkl.framework.services.po.parent.PO;

/**
 * 数据库访问通用操作
 * 
 * @author liaokailin
 * @version $Id: CommAccessor.java, v 0.1 2015年10月13日 下午4:08:21 liaokailin Exp $
 */
public abstract class CommAccessor implements POFactory {

    protected abstract Operator getOperator();

    @Override
    public <T extends PO> int insert(T t) {
        return getOperator().insert(t);
    }

    @Override
    public <T extends PO> int insertAndGetKey(T t) {
        return getOperator().insertAndGetKey(t);
    }

    @Override
    public int insert(String sql, List<?> paramList) {
        return getOperator().insert(sql, paramList);
    }

    @Override
    public <T extends PO> int insert(List<T> paramList) {
        return getOperator().insert(paramList);
    }

    @Override
    public <T extends PO> int update(T condition, T target) {
        return getOperator().update(condition, target);
    }

    @Override
    public int update(String sql, List<?> paramList) {
        return getOperator().update(sql, paramList);
    }

    @Override
    public <T extends PO> int delete(T t) {
        return getOperator().delete(t);
    }

    @Override
    public int delete(String sql, List<?> paramList) {
        return getOperator().delete(sql, paramList);
    }

    @Override
    public <T extends PO> List<T> select(T condition) {
        return getOperator().select(condition);
    }

    @Override
    public List<Map<String, Object>> select(String sql, List<?> paramList) {
        return getOperator().select(sql, paramList);
    }

    @Override
    public <T> List<T> selectListByType(String sql, List<?> paramList, Class<T> type) {
        return getOperator().selectListByType(sql, paramList, type);
    }

    @Override
    public <T> T selectByType(String sql, List<?> paramList, Class<T> type) {
        return getOperator().selectByType(sql, paramList, type);
    }

    @Override
    public <T extends PO> T selectSingle(T condition) {
        return getOperator().selectSingle(condition);
    }

    @Override
    public <T extends PO> PageResult<T> splitPageSelect(T condition, Integer pageIndex, Integer pageSize) {
        return getOperator().splitPageSelect(condition, pageIndex, pageSize);
    }

    @Override
    public PageResult<Map<String, Object>> splitPageSelect(String sql, List<?> paramList, Integer pageIndex,
                                                           Integer pageSize) {
        return getOperator().splitPageSelect(sql, paramList, pageIndex, pageSize);
    }

}
