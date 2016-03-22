package com.lkl.framework.services.po.parent;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 实体查询追加条件
 * 
 * @author liaokailin
 * @version $Id: Condition.java, v 0.1 2015年10月15日 下午5:52:39 liaokailin Exp $
 */
public class Condition {

    private List<Segment> segments        = Lists.newArrayList(); //不能用set
    private List<Object>  params          = Lists.newArrayList();
    private List<Segment> orderBySegments = Lists.newArrayList();

    public static class Segment {
        private String attrName;
        private String symbol;

        public Segment() {
        }

        public Segment(String attrName) {
            this.attrName = attrName;
        }

        public Segment(String attrName, String symbol) {
            this.attrName = attrName;
            this.symbol = symbol;
        }

        public String getAttrName() {
            return attrName;
        }

        public void setAttrName(String attrName) {
            this.attrName = attrName;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((attrName == null) ? 0 : attrName.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Segment other = (Segment) obj;
            if (attrName == null) {
                if (other.attrName != null)
                    return false;
            } else if (!attrName.equals(other.attrName))
                return false;
            return true;
        }

    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

    public List<Segment> getOrderBySegments() {
        return orderBySegments;
    }

    public void setOrderBySegments(List<Segment> orderBySegments) {
        this.orderBySegments = orderBySegments;
    }

    /**
     * 判断额外条件中是否包含该属性
     * 
     * @param attrName
     * @return
     */
    public int indexOf(String attrName) {
        return this.segments.indexOf(new Segment(attrName));
    }

    public void clear() {
        this.segments.clear();
        this.params.clear();
        this.orderBySegments.clear();
    }

}
