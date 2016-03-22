package com.lkl.framework.services.po.parse;

/**
 * 驼峰-下划线结构解析
 * <pre class="code">
 *    attribute:birthDate --> column:birth_date
 *     
 * </pre>
 * 
 * @author liaokailin
 * @version $Id: HumpUnderLineParse.java, v 0.1 2015年10月13日 下午4:47:51 liaokailin Exp $
 */
public class HumpUnderLineParse extends HumpParse {

    public HumpUnderLineParse() {
    }

    public HumpUnderLineParse(boolean isLowerTable) {
        super.setLowerTable(isLowerTable);
    }

    @Override
    public String parseColName(String attrName) {

        StringBuffer sbuf = new StringBuffer();

        for (int i = 0; i < attrName.length(); i++) {
            char ch = attrName.charAt(i);
            if ((ch <= 'Z') && (ch >= 'A') && (i != 0))
                sbuf.append('_');
            sbuf.append(ch);
        }
        return sbuf.toString().toUpperCase();
    }

}
