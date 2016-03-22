package com.lkl.framework.services.po.operator;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.lkl.framework.services.po.page.PageResult;
import com.lkl.framework.services.po.parent.PO;
import com.lkl.framework.services.po.parse.Parse;
import com.lkl.framework.services.po.resolver.SelectSqlResolver;
import com.lkl.framework.services.po.resolver.SqlWrapper;

/**
 * db操作实现
 * 
 * @author liaokailin
 * @version $Id: Operator.java, v 0.1 2015年10月13日 下午4:22:53 liaokailin Exp $
 */
public class MySqlJdbcTemplateOperator extends JdbcTemplateCommOperator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlJdbcTemplateOperator.class);

    private JdbcTemplate        jdbcTemplate;
    private Parse               parse;

    public MySqlJdbcTemplateOperator() {
    }

    public MySqlJdbcTemplateOperator(JdbcTemplate jdbcTemplate, Parse parse) {
        this.jdbcTemplate = jdbcTemplate;
        this.parse = parse;
    }

    @Override
    protected JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    @Override
    protected Parse getParse() {
        return this.parse;
    }

    @Override
    public <T extends PO> PageResult<T> splitPageSelect(T condition, Integer pageIndex, Integer pageSize) {
        PageResult<T> result = new PageResult<T>();
        if (pageIndex == null || pageIndex < 0) {
            pageIndex = PageResult.DEFAULT_PAGEINDEX;
        }
        if (pageSize == null || pageSize > PageResult.MAX_PAGESIZE) {
            pageSize = PageResult.DEFAULT_PAGESIZE;
        }

        SqlWrapper sqlWrapper = new SelectSqlResolver<T>().setParse(getParse()).setTarget(condition).execute();
        if (sqlWrapper != null) {

            Long total = super.selectByType(resolverTotalSql(sqlWrapper.getSql()), sqlWrapper.getParams(), Long.class);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("sql: {} ;param: {}", sqlWrapper.getSql(), sqlWrapper.getParams());
            }
            result.setTotalRecords(total);
            result.setPageIndex(pageIndex);
            result.setPageSize(pageSize);
            if (total <= 0) {
                return result;
            }

            String limitSql = sqlWrapper.getSql() + " limit " + (pageIndex - 1) * pageSize + " , " + pageSize;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("sql: {} ;param: {}", limitSql, sqlWrapper.getParams());
            }

            List<Map<String, Object>> r = this.select(limitSql, sqlWrapper.getParams());
            if (r != null && !r.isEmpty()) {
                List<T> rt = MapConvert(r, sqlWrapper.getMeta());
                result.setRecords(rt);
            }
        }
        return result;

    }

    @Override
    public PageResult<Map<String, Object>> splitPageSelect(String sql, List<?> paramList, Integer pageIndex,
                                                           Integer pageSize) {

        PageResult<Map<String, Object>> result = new PageResult<Map<String, Object>>();
        if (pageIndex == null || pageIndex < 0) {
            pageIndex = PageResult.DEFAULT_PAGEINDEX;
        }
        if (pageSize == null || pageSize > PageResult.MAX_PAGESIZE) {
            pageSize = PageResult.DEFAULT_PAGESIZE;
        }

        Long total = super.selectByType(resolverTotalSql(sql), paramList, Long.class);
        result.setTotalRecords(total);
        result.setPageIndex(pageIndex);
        result.setPageSize(pageSize);
        if (total <= 0) {
            return result;
        }
        List<Map<String, Object>> rList = super.select(sql + " limit " + (pageIndex - 1) * pageSize + " , " + pageSize,
            paramList);
        result.setRecords(rList);

        return result;

    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setParse(Parse parse) {
        this.parse = parse;
    }

}
