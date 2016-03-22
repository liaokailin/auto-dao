package com.lkl.framework.services.po.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
 * 配置
 * 
 * @author liaokailin
 * @version $Id: ContextConfig.java, v 0.1 2015年10月14日 下午8:48:30 liaokailin Exp $
 */
//@Configuration
//@EnableTransactionManagement
public class ContextConfig {
    private static final Logger     LOGGER = LoggerFactory.getLogger(ContextConfig.class);

    @Autowired
    private ConfigurableEnvironment env;

    @Bean
    @Primary
    public DataSource dataSource() throws Exception {
        Properties properties = new Properties();
        properties.put("driverClassName", env.getProperty("driverClassName"));
        properties.put("url", env.getProperty("po.url"));
        properties.put("username", env.getProperty("po.username"));
        properties.put("password", env.getProperty("po.password"));
        properties.put("initialSize", env.getProperty("po.initialSize"));
        properties.put("maxActive", env.getProperty("po.maxActive"));
        properties.put("minIdle", env.getProperty("po.minIdle"));
        properties.put("maxWait", env.getProperty("po.maxWait"));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("load druid datasource.");
        }
        return DruidDataSourceFactory.createDataSource(properties);
    }

    @Bean
    public JdbcTemplate jdbcTemplate() throws Exception {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    @Primary
    public DataSourceTransactionManager transactionManager() throws Exception {
        return new DataSourceTransactionManager(dataSource());
    }

}
