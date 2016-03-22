package com.lkl.framework.services.po.exception;

/**
 * 解析异常
 * 
 * @author liaokailin
 * @version $Id: ParseException.java, v 0.1 2015年10月14日 下午3:34:39 liaokailin Exp $
 */
public class ParseException extends POException {

    private static final long serialVersionUID = 857667161683404344L;

    public ParseException(String msg) {
        super(msg);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }

    public ParseException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
