package com.lkl.framework.services.po.operator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

import com.lkl.framework.services.po.exception.DaoException;
import com.lkl.framework.services.po.exception.ParseException;
import com.lkl.framework.services.po.mapper.ColumnMapPORowMapper;
import com.lkl.framework.services.po.mapper.GenericTypePORowMapper;
import com.lkl.framework.services.po.meta.MetaType;
import com.lkl.framework.services.po.meta.POMetadata;
import com.lkl.framework.services.po.meta.POMetadata.Metadata;
import com.lkl.framework.services.po.parent.PO;
import com.lkl.framework.services.po.parse.Parse;
import com.lkl.framework.services.po.resolver.BatchInsertSqlResolver;
import com.lkl.framework.services.po.resolver.DeleteSqlResolver;
import com.lkl.framework.services.po.resolver.InsertSqlResolver;
import com.lkl.framework.services.po.resolver.SelectSqlResolver;
import com.lkl.framework.services.po.resolver.SqlWrapper;
import com.lkl.framework.services.po.resolver.UpdateSqlResolver;

/**
 * JdbcTemplate数据库通用操作实现
 * 
 * @author liaokailin
 * @version $Id: Operator.java, v 0.1 2015年10月13日 下午4:22:53 liaokailin Exp $
 */
public abstract class JdbcTemplateCommOperator implements Operator {
    private static final Logger              LOGGER      = LoggerFactory.getLogger(JdbcTemplateCommOperator.class);
    private static final Map<String, String> primaryKeys = new ConcurrentHashMap<String, String>();

    @Override
    public <T extends PO> int insert(T t) {
        SqlWrapper sqlWrapper = new InsertSqlResolver<T>().setParse(getParse()).setTarget(t).execute();
        if (sqlWrapper != null) {
            return this.jdbcTemplateUpdate(sqlWrapper.getSql(), sqlWrapper.getParams());
        }
        return 0;
    }

    @Override
    public <T extends PO> int insertAndGetKey(T t) {
        SqlWrapper sqlWrapper = new InsertSqlResolver<T>().setParse(getParse()).setTarget(t).execute();
        if (sqlWrapper != null) {
            KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
            MyPreparedStatementCreator creator = new MyPreparedStatementCreator(sqlWrapper.getSql(),
                sqlWrapper.getParams(), sqlWrapper.getMeta().getTabName());
            int result = getJdbcTemplate().update(creator, generatedKeyHolder);
            try {
                Metadata priMeta = sqlWrapper.getMeta().indexOf(creator.primaryKey, MetaType.column);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("primary key metedata is {}, value is {} ", priMeta,
                        priMeta.getAttrType().cast(generatedKeyHolder.getKey()));
                }
                priMeta.getWriteMethod().invoke(t, priMeta.getAttrType().cast(generatedKeyHolder.getKey()));
            } catch (Exception e) {
                throw new ParseException("retrieve primary key error.", e);
            }
            return result;
        }
        return 0;

    }

    @Override
    public int insert(String sql, List<?> paramList) {
        return this.jdbcTemplateUpdate(sql, paramList);
    }

    @Override
    public <T extends PO> int insert(List<T> paramList) {
        if (paramList == null || paramList.isEmpty()) {
            return 0;
        }
        SqlWrapper sqlWrapper = new BatchInsertSqlResolver<T>().setParse(getParse()).setTarget(paramList.get(0))
            .execute();
        try {
            return getJdbcTemplate().batchUpdate(sqlWrapper.getSql(),
                new MyBatchPreparedStatementSetter<T>(paramList, sqlWrapper.getMeta().getMetadata())).length;
        } catch (Exception e) {
            throw new DaoException("batch insert error.", e);
        }

    }

    @Override
    public <T extends PO> int update(T condition, T target) {
        SqlWrapper sqlWrapper = new UpdateSqlResolver<T>().setParse(getParse()).setTarget(condition).setTarget(target)
            .execute();
        if (sqlWrapper != null) {
            return this.jdbcTemplateUpdate(sqlWrapper.getSql(), sqlWrapper.getParams());
        }
        return 0;
    }

    @Override
    public int update(String sql, List<?> paramList) {
        return this.jdbcTemplateUpdate(sql, paramList);
    }

    @Override
    public <T extends PO> int delete(T t) {
        SqlWrapper sqlWrapper = new DeleteSqlResolver<T>().setParse(getParse()).setTarget(t).execute();
        if (sqlWrapper != null) {
            return this.delete(sqlWrapper.getSql(), sqlWrapper.getParams());
        }
        return 0;
    }

    @Override
    public int delete(String sql, List<?> params) {
        if (getJdbcTemplate() != null) {
            return this.jdbcTemplateUpdate(sql, params);
        }
        return 0;
    }

    @Override
    public <T extends PO> List<T> select(T condition) {
        SqlWrapper sqlWrapper = new SelectSqlResolver<T>().setParse(getParse()).setTarget(condition).execute();
        if (sqlWrapper != null) {

            return getJdbcTemplate().query(
                sqlWrapper.getSql(),
                CollectionUtils.isEmpty(sqlWrapper.getParams()) ? null : sqlWrapper.getParams().toArray(
                    new Object[sqlWrapper.getParams().size()]), new GenericTypePORowMapper<T>(sqlWrapper.getMeta()));
            /**
             * List<Map<String, Object>> result = this.select(sqlWrapper.getSql(), sqlWrapper.getParams());
            if (result != null && !result.isEmpty()) {
                return MapConvert(result, sqlWrapper.getMeta());
            }
             */
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> select(String sql, List<?> paramList) {
        return getJdbcTemplate().query(sql,
            CollectionUtils.isEmpty(paramList) ? null : paramList.toArray(new Object[] { paramList.size() }),
            new ColumnMapPORowMapper());
    }

    @Override
    public <T> List<T> selectListByType(String sql, List<?> paramList, Class<T> clazz) {
        return getJdbcTemplate().queryForList(sql, clazz,
            CollectionUtils.isEmpty(paramList) ? null : paramList.toArray(new Object[] { paramList.size() }));
    }

    @Override
    public <T> T selectByType(String sql, List<?> paramList, Class<T> type) {
        return getJdbcTemplate().queryForObject(sql,
            CollectionUtils.isEmpty(paramList) ? null : paramList.toArray(new Object[] { paramList.size() }), type);
    }

    @Override
    public <T extends PO> T selectSingle(T condition) {
        List<T> result = this.select(condition);
        if (!CollectionUtils.isEmpty(result)) {
            return result.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    protected <T extends PO> List<T> MapConvert(List<Map<String, Object>> result, POMetadata meta) {
        List<T> list = new LinkedList<T>();
        try {
            LinkedList<Metadata> metaList = meta.getMetadata();
            for (Map<String, Object> map : result) {
                T t = (T) meta.getClazz().newInstance();
                for (int i = 0; i < metaList.size(); i++) {
                    Metadata m = metaList.get(i);
                    Object o = map.get(m.getColumnName());
                    if (o != null) {
                        m.getWriteMethod().invoke(t, m.getAttrType().cast(o));
                    }
                }
                list.add(t);
            }

        } catch (Exception e) {
            throw new ParseException("handle result error .", e);
        }
        return list;
    }

    public int jdbcTemplateUpdate(String sql, List<?> params) {
        return getJdbcTemplate().update(sql,
            CollectionUtils.isEmpty(params) ? null : params.toArray(new Object[params.size()]));
    }

    protected abstract JdbcTemplate getJdbcTemplate();

    protected abstract Parse getParse();

    class MyBatchPreparedStatementSetter<T> implements BatchPreparedStatementSetter {
        List<T>              list;
        LinkedList<Metadata> meta;

        public MyBatchPreparedStatementSetter(List<T> list, LinkedList<Metadata> meta) {
            this.list = list;
            this.meta = meta;
        }

        @Override
        public void setValues(PreparedStatement ps, int i) {
            T t = this.list.get(i);
            for (int j = 0; j < this.meta.size(); j++) {
                Metadata data = this.meta.get(j);
                try {
                    ps.setObject(j + 1, data.getReadMethod().invoke(t, new Object[0]));
                } catch (Exception e) {
                    LOGGER.error("invoke the readMethod of field {} error; detail is {}", data.getAttrName(),
                        e.toString());
                    throw new ParseException("set param error.", e);
                }
            }

        }

        @Override
        public int getBatchSize() {
            return list.size();
        }

    }

    class MyPreparedStatementCreator implements PreparedStatementCreator {

        List<Object> list;
        String       sql;
        String       tableName;
        String       primaryKey;

        public MyPreparedStatementCreator(String sql, List<Object> list, String tableName) {
            this.list = list;
            this.sql = sql;
            this.tableName = tableName;
        }

        @Override
        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
            try {
                primaryKey = primaryKeys.get(tableName);
                if (primaryKey == null) {
                    ResultSet rs = con.getMetaData().getPrimaryKeys(null, null, tableName);
                    if (rs.next()) {
                        primaryKey = rs.getString("COLUMN_NAME");
                        primaryKeys.put(tableName, primaryKey);
                    }
                }
            } catch (Exception e) {
                throw new ParseException("retrieve primary key error.", e);
            }
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < list.size(); i++) {
                try {
                    ps.setObject(i + 1, list.get(i));
                } catch (Exception e) {
                    throw new ParseException("set param error.", e);
                }
            }
            return ps;
        }

    }

    public String resolverTotalSql(String sql) {
        return "SELECT COUNT(1) FROM ( " + sql + " ) AS T_DAO_COUNT";
    }

}
