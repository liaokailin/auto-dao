package com.lkl.framework.services.po.factory;

import java.util.List;
import java.util.Map;

import com.lkl.framework.services.po.page.PageResult;
import com.lkl.framework.services.po.parent.PO;

/**
 * 数据库操作,获得该bean操作数据库
 * @author liaokailin
 * @version $Id: POFactory.java, v 0.1 2015年10月13日 下午4:03:34 liaokailin Exp $
 */
public interface POFactory {

    /**
     * 插入
     */
    public <T extends PO> int insert(T t);

    /**
     * 插入并获取主键
     */
    public <T extends PO> int insertAndGetKey(T t);

    /**
     * 更新
     * @param condition 更新条件
     * @param target 更新值
     */
    public <T extends PO> int update(T condition, T target);

    /**
     * 按sql更新
     */
    public int update(String sql, List<?> paramList);

    /**
     * 自定义sql插入
     */
    public int insert(String sql, List<?> paramList);

    /**
     * 批量插入
     */
    public <T extends PO> int insert(List<T> poList);

    /**
     * 删除实体
     */
    public <T extends PO> int delete(T t);

    /**
     * 自定义sql删除
     */
    public int delete(String sql, List<?> paramList);

    /**
     * 查询
     */
    public <T extends PO> List<T> select(T condition);

    /**
     * 查询单条数据
     */
    public <T extends PO> T selectSingle(T condition);

    /**
     * 查询返回Map集合，map#key为数据库字段
     */
    public List<Map<String, Object>> select(String sql, List<?> paramList);

    /**
     * 依据类型查询，支持单column、多行，例如 select id from table_name
     * 
     */
    public <T> List<T> selectListByType(String sql, List<?> paramList, Class<T> type);

    /**
     * 依据类型查询,支持单column、单行,例如 select count(1) from table_name  
     */
    public <T> T selectByType(String sql, List<?> paramList, Class<T> type);

    /**
     * 分页查询
     */
    public <T extends PO> PageResult<T> splitPageSelect(T condition, Integer pageIndex, Integer pageSize);

    /**
     * 分页查询
     */
    public PageResult<Map<String, Object>> splitPageSelect(String sql, List<?> paramList, Integer pageIndex,
                                                           Integer pageSize);

}
