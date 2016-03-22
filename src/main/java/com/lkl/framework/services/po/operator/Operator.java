package com.lkl.framework.services.po.operator;

import java.util.List;
import java.util.Map;

import com.lkl.framework.services.po.page.PageResult;
import com.lkl.framework.services.po.parent.PO;

/**
 * 具体数据库操作实现父接口，实现该接口编写多数据库兼容
 * 
 * @author liaokailin
 * @version $Id: Operator.java, v 0.1 2015年10月14日 下午7:01:16 liaokailin Exp $
 */
public interface Operator {

    public <T extends PO> int insert(T t);

    public <T extends PO> int insertAndGetKey(T t);

    public int insert(String sql, List<?> paramList);

    public <T extends PO> int insert(List<T> paramList);

    public <T extends PO> int update(T condition, T target);

    public int update(String sql, List<?> paramList);

    public <T extends PO> int delete(T t);

    public int delete(String sql, List<?> params);

    public <T extends PO> List<T> select(T condition);

    public List<Map<String, Object>> select(String sql, List<?> paramList);

    public <T> List<T> selectListByType(String sql, List<?> paramList, Class<T> type);

    public <T> T selectByType(String sql, List<?> paramList, Class<T> type);

    public <T extends PO> T selectSingle(T condition);

    public <T extends PO> PageResult<T> splitPageSelect(T condition, Integer pageIndex, Integer pageSize);

    public PageResult<Map<String, Object>> splitPageSelect(String sql, List<?> paramList, Integer pageIndex,
                                                           Integer pageSize);

}
