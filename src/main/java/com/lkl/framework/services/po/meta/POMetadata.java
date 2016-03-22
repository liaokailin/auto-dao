package com.lkl.framework.services.po.meta;

import java.lang.reflect.Method;
import java.util.LinkedList;

import org.springframework.util.CollectionUtils;

import com.lkl.framework.services.po.exception.ParseException;
import com.lkl.framework.services.po.parent.PO;

/**
 * PO元数据
 * 
 * @author liaokailin
 * @version $Id: POMetadata.java, v 0.1 2015年10月13日 下午4:34:08 liaokailin Exp $
 */
public class POMetadata {

    private Class<? extends PO>  clazz;
    private String               tabName;
    private LinkedList<Metadata> metadata = new LinkedList<Metadata>();
    private Method               conditionMethod;

    public Metadata indexOf(String fieldValue, MetaType type) {
        try {
            if (CollectionUtils.isEmpty(metadata))
                return null;
            Metadata m = new Metadata(fieldValue.trim(), type);
            return metadata.get(metadata.indexOf(m));
        } catch (Exception e) {
            throw new ParseException(" can't find " + fieldValue + " info", e);
        }
    }

    public static class Metadata {
        private Method   writeMethod;
        private Method   readMethod;
        private String   attrName;
        private String   columnName;
        private Class<?> attrType;

        public Metadata() {

        }

        public Metadata(String fieldValue, MetaType type) {
            if (MetaType.attribute == type) {
                this.attrName = fieldValue;
            } else {
                this.columnName = fieldValue;
            }
        }

        public Metadata(String columnName) {
            this.columnName = columnName;
        }

        public Method getWriteMethod() {
            return writeMethod;
        }

        public void setWriteMethod(Method writeMethod) {
            this.writeMethod = writeMethod;
        }

        public Method getReadMethod() {
            return readMethod;
        }

        public void setReadMethod(Method readMethod) {
            this.readMethod = readMethod;
        }

        public String getAttrName() {
            return attrName;
        }

        public void setAttrName(String attrName) {
            this.attrName = attrName;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public Class<?> getAttrType() {
            return attrType;
        }

        public void setAttrType(Class<?> attrType) {
            this.attrType = attrType;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((attrName == null) ? 0 : attrName.hashCode());
            result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
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
            Metadata other = (Metadata) obj;
            if (attrName == null && columnName == null) {
                return false;
            }
            if (attrName != null && columnName == null) {
                return attrName.equals(other.attrName);
            }
            if (attrName == null && columnName != null) {
                return columnName.equalsIgnoreCase(other.columnName);
            }
            if (attrName != null && columnName != null) {
                return attrName.equals(other.attrName) && columnName.equalsIgnoreCase(other.columnName);
            }
            return true;
        }

        @Override
        public String toString() {
            return "Metadata [writeMethod=" + writeMethod + ", readMethod=" + readMethod + ", attrName=" + attrName
                   + ", columnName=" + columnName + ", attrType=" + attrType + "]";
        }

    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends PO> clazz) {
        this.clazz = clazz;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public LinkedList<Metadata> getMetadata() {
        return metadata;
    }

    public void setMetadata(LinkedList<Metadata> metadata) {
        this.metadata = metadata;
    }

    public Method getConditionMethod() {
        return conditionMethod;
    }

    public void setConditionMethod(Method conditionMethod) {
        this.conditionMethod = conditionMethod;
    }

    @Override
    public String toString() {
        return "POMetadata [clazz=" + clazz + ", tabName=" + tabName + ", metadata=" + metadata + ", conditionMethod="
               + conditionMethod + "]";
    }

}
