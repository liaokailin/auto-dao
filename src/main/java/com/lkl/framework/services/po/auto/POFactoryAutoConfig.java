package com.lkl.framework.services.po.auto;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.lkl.framework.services.po.factory.POFactory;
import com.lkl.framework.services.po.mysql.Mysql;
import com.lkl.framework.services.po.operator.MySqlJdbcTemplateOperator;
import com.lkl.framework.services.po.operator.Operator;
import com.lkl.framework.services.po.parse.HumpUnderLineParse;
import com.lkl.framework.services.po.parse.Parse;

/**
 * 自动配置
 * @author liaokailin
 * @version $Id: POFactoryAutoConfig.java, v 0.1 2015年10月13日 下午7:33:50 liaokailin Exp $
 */
@Configuration
@ConditionalOnClass({ JdbcTemplate.class, POFactory.class })
@ConditionalOnBean(JdbcTemplate.class)
public class POFactoryAutoConfig {

    @Configuration
    @ConditionalOnMissingBean(Parse.class)
    public static class ParseCreator {
        @Bean
        public Parse parse() {
            return new HumpUnderLineParse(true);
        }
    }

    @Configuration
    @ConditionalOnMissingBean(Operator.class)
    public static class OperatorCreator {
        @Bean
        public Operator operator(JdbcTemplate jdbcTemplate, Parse parse) {
            return new MySqlJdbcTemplateOperator(jdbcTemplate, parse);
        }

    }

    @Configuration
    @ConditionalOnMissingBean(POFactory.class)
    public static class POFactoryCreator {
        @Bean
        public POFactory mysql(Operator operator) {
            return new Mysql(operator);
        }

    }

}
