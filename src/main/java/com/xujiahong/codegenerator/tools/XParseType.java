package com.xujiahong.codegenerator.tools;

import java.util.HashMap;

/**
 * 解析从数据库到Java的类型映射
 * Created by xujiahong on 2017/8/4.
 * ======================功能列表======================
 */
public class XParseType {

    public static HashMap<String, String> typeMap = new HashMap<String, String>();

    static {
        //常用
        typeMap.put("INT", "Integer");
        typeMap.put("BIGINT", "Long");
        typeMap.put("FLOAT", "Double");
        typeMap.put("DOUBLE", "Double");
        typeMap.put("DECIMAL", "Double");
        typeMap.put("VARCHAR", "String");
        typeMap.put("DATE", "LocalDateTime");
        typeMap.put("DATETIME", "LocalDateTime");
        typeMap.put("TIMESTAMP", "LocalDateTime");

        //其他
        typeMap.put("TINYINT", "Integer");
        typeMap.put("TINYTEXT", "String");
        typeMap.put("TEXT", "String");
        typeMap.put("LONGTEXT", "String");
        typeMap.put("JSON", "String");
        typeMap.put("ENUM", "String");

    }

    /**
     * 去掉数据库类型的长度描述文本（比如 int(20)-->int）
     *
     * @param dbType
     * @return
     */
    public static String parseType(String dbType) {
        if (dbType == null || "".equals(dbType)) {
            return null;
        }
        if (dbType.indexOf("(") != -1) {
            dbType = dbType.substring(0, dbType.indexOf("("));
        }
        return dbType.toUpperCase();
    }

    public static String parseTypeToJava(String dbType) {
        dbType = parseType(dbType);
        if (!typeMap.keySet().contains(dbType)) {
            System.out.println("未找到 【" + dbType + "】 对应的Java类型映射");
            return "String";
        }
        return typeMap.get(dbType);
    }
}
