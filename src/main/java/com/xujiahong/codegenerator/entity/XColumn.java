package com.xujiahong.codegenerator.entity;

/**
 * 数据表列
 * Created by xujiahong on 2017/8/4.
 * ======================功能列表======================
 */
public class XColumn {

    /**
     * 数据库中字段名
     */
    private String dbFieldName;
    /**
     * 数据库中字段类型
     */
    private String dbFieldType;
    /**
     * Java字段名
     */
    private String javaFieldName;
    /**
     * Java字段类型
     */
    private String javaFieldType;
    /**
     * 注释
     */
    private String comment;
    /**
     * 是否允许为空（暂未使用 since V1.0）
     */
//    private boolean isNull;

    public String getDbFieldName() {
        return dbFieldName;
    }

    public void setDbFieldName(String dbFieldName) {
        this.dbFieldName = dbFieldName;
    }

    public String getDbFieldType() {
        return dbFieldType;
    }

    public void setDbFieldType(String dbFieldType) {
        this.dbFieldType = dbFieldType;
    }

    public String getJavaFieldName() {
        return javaFieldName;
    }

    public void setJavaFieldName(String javaFieldName) {
        this.javaFieldName = javaFieldName;
    }

    public String getJavaFieldType() {
        return javaFieldType;
    }

    public void setJavaFieldType(String javaFieldType) {
        this.javaFieldType = javaFieldType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "XColumn{" +
                "dbFieldName='" + dbFieldName + '\'' +
                ", dbFieldType='" + dbFieldType + '\'' +
                ", javaFieldName='" + javaFieldName + '\'' +
                ", javaFieldType='" + javaFieldType + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
