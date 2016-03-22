package com.lkl.framework.services.po.exception;

/**
 * 数据库访问异常
 * 
 * @author liaokailin
 * @version $Id: DaoException.java, v 0.1 2015年10月14日 下午7:03:16 liaokailin Exp $
 */
public class DaoException extends POException {

    private static final long serialVersionUID = -9062155514055062150L;

    public DaoException(String msg) {
        super(msg);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }

    public DaoException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
