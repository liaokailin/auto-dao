package com.lkl.framework.services.po.parent;

import java.io.Serializable;
import java.util.List;

import com.google.common.base.Preconditions;
import com.lkl.framework.services.po.parent.Condition.Segment;

/**
 * 标识父类，所有数据库实体继承该类
 * 所有追加查询条件传递参数为实体属性值  
 * 
 * <pre class="code">
 *  
 *   eq： 
 *     class User extends PO{
 *      private String name ;
 *      private Integer age ;
 *      private Date birthDate;  // db column birth_data
 *      // getter() and setter()
 *     }
 *     
 *     class SomeService {
 *        POFactory poFactory ;
 *        //name＝lkl & age in (20,30) & birthDate >1990
 *        public List<User> queryUserInfo(){
 *           User u = new User();
 *           u.setName("lkl");
 *           List paramList = Lists.newArrayList(20, 30);
 *           u.appendIn("age",paramList).appendGreaterThan( new Date(1990)) ; //date need format
 *           return poFactory.select(u);
 *        }
 *        
 *     }
 * </pre>
 * 
 * <note>PO属性执行appendXxx方法后其属性值对应的相等条件失效</note>
 * 
 * @author liaokailin
 * @version $Id: PO.java, v 0.1 2015年10月13日 下午4:02:29 liaokailin Exp $
 */
public class PO implements Serializable {

    private static final long serialVersionUID = 7309330914387975189L;
    protected Condition       c                = new Condition();

    public PO appendIsNull(String attrName) {
        Preconditions.checkNotNull(attrName, "attrName must not be null. ");
        c.getSegments().add(new Segment(attrName, " IS NULL "));
        return this;
    }

    public PO appendIsNotNull(String attrName) {
        Preconditions.checkNotNull(attrName, "attrName must not be null. ");
        c.getSegments().add(new Segment(attrName, " IS NOT NULL "));
        return this;
    }

    public PO appendNotEqual(String attrName, Object value) {
        Preconditions.checkNotNull(attrName, "attrName must not be null. ");
        Preconditions.checkNotNull(value, "value must not be null. ");
        c.getSegments().add(new Segment(attrName, " <> ? "));
        c.getParams().add(value);
        return this;
    }

    public PO appendGreaterThan(String attrName, Object value) {
        Preconditions.checkNotNull(attrName, "attrName must not be null. ");
        Preconditions.checkNotNull(value, "value must not be null. ");
        c.getSegments().add(new Segment(attrName, " > ? "));
        c.getParams().add(value);
        return this;
    }

    public PO appendGreaterThanOrEqual(String attrName, Object value) {
        Preconditions.checkNotNull(attrName, "attrName must not be null. ");
        Preconditions.checkNotNull(value, "value must not be null. ");
        c.getSegments().add(new Segment(attrName, " >= ? "));
        c.getParams().add(value);
        return this;
    }

    public PO appendLessThan(String attrName, Object value) {
        Preconditions.checkNotNull(attrName, "attrName must not be null. ");
        Preconditions.checkNotNull(value, "value must not be null. ");
        c.getSegments().add(new Segment(attrName, " < ? "));
        c.getParams().add(value);
        return this;
    }

    public PO appendIn(String attrName, List<?> value) {
        Preconditions.checkNotNull(attrName, "attrName must not be null. ");
        Preconditions.checkNotNull(value, "value must not be null. ");
        Preconditions.checkArgument(value.size() > 0, "value size must more than zero. ");
        c.getSegments().add(new Segment(attrName, " IN " + parseSize(value.size())));
        c.getParams().addAll(value);
        return this;
    }

    public PO appendNotIn(String attrName, List<?> value) {
        Preconditions.checkNotNull(attrName, "attrName must not be null. ");
        Preconditions.checkNotNull(value, "value must not be null. ");
        Preconditions.checkArgument(value.size() > 0, "value size must more than zero. ");
        c.getSegments().add(new Segment(attrName, " NOT IN " + parseSize(value.size())));
        c.getParams().addAll(value);
        return this;
    }

    public PO appendBetween(String attrName, Object value1, Object value2) {
        Preconditions.checkNotNull(attrName, "attrName must not be null. ");
        Preconditions.checkNotNull(value1, "value1 must not be null. ");
        Preconditions.checkNotNull(value2, "value2 must not be null. ");
        c.getSegments().add(new Segment(attrName, " BETWEEN ? AND ?  "));
        c.getParams().add(value1);
        c.getParams().add(value2);
        return this;
    }

    public PO appendNotBetween(String attrName, Object value1, Object value2) {
        Preconditions.checkNotNull(attrName, "attrName must not be null. ");
        Preconditions.checkNotNull(value1, "value1 must not be null. ");
        Preconditions.checkNotNull(value2, "value2 must not be null. ");
        c.getSegments().add(new Segment(attrName, " NOT BETWEEN ? AND ?  "));
        c.getParams().add(value1);
        c.getParams().add(value2);
        return this;
    }

    public PO appendLike(String attrName, Object value) {
        Preconditions.checkNotNull(attrName, "attrName must not be null. ");
        Preconditions.checkNotNull(value, "value must not be null. ");
        c.getSegments().add(new Segment(attrName, " LIKE \"%\"?\"%\"  "));
        c.getParams().add(value);
        return this;
    }

    public PO appendNotLike(String attrName, Object value) {
        Preconditions.checkNotNull(attrName, "attrName must not be null. ");
        Preconditions.checkNotNull(value, "value must not be null. ");
        c.getSegments().add(new Segment(attrName, " NOT LIKE \"%\"?\"%\"  "));
        c.getParams().add(value);
        return this;
    }

    private String parseSize(int size) {
        StringBuilder sb = new StringBuilder();
        sb.append(" (");
        for (int i = 0; i < size; i++) {
            sb.append("?,");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") ");
        return sb.toString();
    }

    public PO appendLessThanOrEqual(String attrName, Object value) {
        Preconditions.checkNotNull(attrName, "attrName must not be null. ");
        Preconditions.checkNotNull(value, "value must not be null. ");
        c.getSegments().add(new Segment(attrName, " <= ? "));
        c.getParams().add(value);
        return this;
    }

    public PO appendOrderByAsc(String attrName) {
        Preconditions.checkNotNull(attrName, "attrName must not be null. ");
        c.getOrderBySegments().add(new Segment(attrName, " ASC "));
        return this;
    }

    public PO appendOrderByDesc(String attrName) {
        Preconditions.checkNotNull(attrName, "attrName must not be null. ");
        c.getOrderBySegments().add(new Segment(attrName, " DESC "));
        return this;
    }

    public Condition __conditon_info__() {
        return c;
    }

}
