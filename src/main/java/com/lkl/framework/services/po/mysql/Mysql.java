package com.lkl.framework.services.po.mysql;

import com.lkl.framework.services.po.comm.CommAccessor;
import com.lkl.framework.services.po.operator.Operator;

/**
 * mysql操作
 * 
 * @author liaokailin
 * @version $Id: Mysql.java, v 0.1 2015年10月13日 下午4:06:58 liaokailin Exp $
 */

public class Mysql extends CommAccessor {

    private Operator operator;

    public Mysql() {
    }

    public Mysql(Operator operator) {
        this.operator = operator;
    }

    @Override
    protected Operator getOperator() {
        return this.operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

}
