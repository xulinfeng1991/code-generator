package com.xujiahong.codegenerator.entity;

import com.xujiahong.codegenerator.Config;
import com.xujiahong.codegenerator.tools.XParseName;

import java.util.List;

/**
 * 数据库表
 * Created by xujiahong on 2017/8/4.
 * ======================功能列表======================
 */
public class XTable {

    /**
     * 数据表名称
     */
    private String tableName;
    /**
     * java对象名称
     */
    private String pojoName;
    /**
     * 对象中文名称
     */
    private String chName;
    /**
     * 数据表列
     */
    private List<XColumn> columns;

    private XTable() {
    }

    public XTable(String tableName, String chName, List<XColumn> columns) {
        this.tableName = tableName;
        String nonePrefixTableName = tableName;
        for (String s : Config.TABLE_PREFIX) {
            nonePrefixTableName = nonePrefixTableName.replace(s, "");
        }
        this.pojoName = XParseName.toUpperCase(XParseName.parseNameToCamel(nonePrefixTableName));
        this.chName = chName;
        this.columns = columns;
    }

    /**
     * 通过ID字段名称获取ID字段类型
     *
     * @param idColumnJavaName
     * @return
     */
    public String getIdColumnJavaType(String idColumnJavaName) {
        if (idColumnJavaName == null || "".equals(idColumnJavaName)) {
            return null;
        }
        if (this.columns.size() == 0) {
            return null;
        }
        for (XColumn xColumn : this.columns) {
            if (idColumnJavaName.equals(xColumn.getJavaFieldName())) {
                return xColumn.getJavaFieldType();
            }
        }
        return null;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPojoName() {
        return pojoName;
    }

    public void setPojoName(String pojoName) {
        this.pojoName = pojoName;
    }

    public String getChName() {
        return chName;
    }

    public void setChName(String chName) {
        this.chName = chName;
    }

    public List<XColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<XColumn> columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "XTable{" +
                "tableName='" + tableName + '\'' +
                ", pojoName='" + pojoName + '\'' +
                ", chName='" + chName + '\'' +
                ", columns=" + columns +
                '}';
    }
}
