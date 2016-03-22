package com.lkl.framework.services.po.exception;

/**
 * PO异常父类
 * 
 * @author liaokailin
 * @version $Id: POException.java, v 0.1 2015年10月14日 下午3:34:48 liaokailin Exp $
 */
public class POException extends RuntimeException {

    private static final long serialVersionUID = -6736143210280288839L;

    public POException(String msg) {
        super(msg);
    }

    public POException(Throwable cause) {
        super(cause);
    }

    public POException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
