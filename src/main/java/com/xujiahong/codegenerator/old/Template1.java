package com.xujiahong.codegenerator.old;

import com.xujiahong.codegenerator.Config;
import com.xujiahong.codegenerator.entity.XColumn;
import com.xujiahong.codegenerator.entity.XTable;
import com.xujiahong.codegenerator.tools.ParsecFileTools;
import com.xujiahong.codegenerator.tools.XParseName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * description
 *
 * @author xujiahong
 * @date 2018/4/25
 */
public class Template1 {

    /**
     * 创建实体
     */
    public static void createPo(XTable xTable) {
        //装生成代码的buffer对象
        StringBuffer codeBuffer = new StringBuffer();//整个文件的buffer
        StringBuffer fieldBuffer = new StringBuffer();//field
        StringBuffer getterAndSetterBuffer = new StringBuffer();//getter and setter
        StringBuffer classCommentBuffer = new StringBuffer();//生成类的注释

        /*
        代码生成
         */
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(new Date());
        classCommentBuffer.append("\n/**\n" +
                " * Created by AutoGenerateCode on " + now + ".\n" +
                " * @author " + Config.AUTHOR + " \n" +
                " */  \n");
        fieldBuffer.append("\n\n");
        getterAndSetterBuffer.append("\n\t// getter and setter\n");

        List<XColumn> columns = xTable.getColumns();
        for (XColumn column : columns) {
            //field
            fieldBuffer.append("\tprivate " + column.getJavaFieldType() + " " + column.getJavaFieldName() + "; //" + column.getComment() + "\n");
            //getter and setter
            getterAndSetterBuffer.append("\tpublic " + column.getJavaFieldType() + " get" + XParseName.toUpperCase(column
                    .getJavaFieldName()) + "() {\n");
            getterAndSetterBuffer.append("\t\treturn " + column.getJavaFieldName() + ";\n");
            getterAndSetterBuffer.append("\t}\n");
            getterAndSetterBuffer.append("\t\n");
            getterAndSetterBuffer.append("\tpublic void set" + XParseName.toUpperCase(column.getJavaFieldName()) + "(" + column
                    .getJavaFieldType() + " " + column.getJavaFieldName() + ") {\n");
            getterAndSetterBuffer.append("\t\tthis." + column.getJavaFieldName() + " = " + column.getJavaFieldName() + ";\n");
            getterAndSetterBuffer.append("\t}\n");
            getterAndSetterBuffer.append("\t\n");
        }

        codeBuffer.append("package " + Config.ENTITY_PACKAGE + ";\n\n");
        codeBuffer.append(classCommentBuffer);
        codeBuffer.append("public class " + xTable.getPojoName() + "{");
        codeBuffer.append(fieldBuffer);
        codeBuffer.append(getterAndSetterBuffer);
        codeBuffer.append("}");

        //写入文件
        ParsecFileTools.writeFile(Config.CODE_PATH + xTable.getPojoName() + ".java", codeBuffer);
        System.out.println("=====POJO build success=====");
    }

    /**
     * 创建Mapper.xml
     */
    public static void createXml(XTable xTable) {
        //装生成代码的buffer对象
        StringBuffer sqlBuffer = new StringBuffer();//整个文件的buffer
        StringBuffer resultMapBuffer = new StringBuffer();//resultMap
        StringBuffer baseColumnsBuffer = new StringBuffer();//base_columns
        StringBuffer baseWhereBuffer = new StringBuffer();//base_where

        StringBuffer insertParamsBuffer = new StringBuffer();//insert_params
        StringBuffer insertValuesBuffer = new StringBuffer();//insert_values
        StringBuffer updateBuffer = new StringBuffer();//update_sqls

        //别名
        String objName = Config.ENTITY_PACKAGE + "." + xTable.getPojoName();

        String idColumnJavaName = XParseName.parseNameToCamel(Config.TABLE_ID);
        String idColumnJavaType = xTable.getIdColumnJavaType(idColumnJavaName);

        /*
        代码生成
         */
        resultMapBuffer.append("\t<resultMap id=\"BaseResultMap\" type=\"" + objName + "\">\n");
        baseColumnsBuffer.append("\t<sql id=\"base_columns\">\n\t");
        baseWhereBuffer.append("\t<sql id=\"base_where\">\n");

        //loop to generate code
        List<XColumn> columns = xTable.getColumns();
        for (XColumn column : columns) {
            if (column.getDbFieldName().equals(Config.TABLE_ID)) {//主键字段
                resultMapBuffer.append("\t\t<id");
                baseColumnsBuffer.append("\t" + column.getDbFieldName());
                if (!Config.USE_GENERATED_KEYS) {
                    insertParamsBuffer.append("\n\t\t\t" + column.getDbFieldName() + ",");
                    insertValuesBuffer.append("\n\t\t\t#{" + column.getJavaFieldName() + "},");
                }
            } else {
                resultMapBuffer.append("\t\t<result");
                baseColumnsBuffer.append("," + column.getDbFieldName());
                insertParamsBuffer.append("\n\t\t\t" + column.getDbFieldName() + ",");
                insertValuesBuffer.append("\n\t\t\t#{" + column.getJavaFieldName() + "},");
            }
            resultMapBuffer.append(" column=\"" + column.getDbFieldName() + "\" property=\"" + column.getJavaFieldName() + "\"/>\n");
            baseWhereBuffer.append("\t\t<if test=\"" + column.getJavaFieldName() + "!=null\">and " + column.getDbFieldName() + "=#{" + column.getJavaFieldName() + "}</if>\n");
            updateBuffer.append("\t\t\t<if test=\"" + column.getJavaFieldName() + "!=null\"> " + column.getDbFieldName() + "=#{" + column.getJavaFieldName() + "},</if>\n");
        }

        resultMapBuffer.append("\t</resultMap>\n\n");
        baseColumnsBuffer.append("\n\t</sql>\n\n");
        baseWhereBuffer.append("\t</sql>\n");
        insertParamsBuffer.delete(insertParamsBuffer.length() - 1, insertParamsBuffer.length());
        insertValuesBuffer.delete(insertValuesBuffer.length() - 1, insertValuesBuffer.length());

        sqlBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        sqlBuffer.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n\n");

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(new Date());
        sqlBuffer.append("\n<!-- Created by AutoGenerateCode on " + now + ". -->\n");
        sqlBuffer.append("<!-- Author " + Config.AUTHOR + " -->\n");
        sqlBuffer.append("<mapper namespace=\"" + Config.ENTITY_PACKAGE + "." + xTable.getPojoName() + "Dao" + "\">\n\t\n");

        sqlBuffer.append(resultMapBuffer);
        sqlBuffer.append(baseColumnsBuffer);
        sqlBuffer.append(baseWhereBuffer);

        sqlBuffer.append("\t\n\t<!--====================【增】====================-->\n\n");
        sqlBuffer.append("\t<insert id=\"insert\" parameterType=\"" + objName);
        if (Config.USE_GENERATED_KEYS) {
            sqlBuffer.append("\" useGeneratedKeys=\"true\" keyProperty=\"" + idColumnJavaName + "\">\n");
        } else {
            sqlBuffer.append(">\n");
        }
        sqlBuffer.append("\t\tinsert into " + xTable.getTableName() + " (");
        sqlBuffer.append(insertParamsBuffer);
        sqlBuffer.append("\n\t\t) values (");
        sqlBuffer.append(insertValuesBuffer);
        sqlBuffer.append("\n\t\t)\n");
        sqlBuffer.append("\t</insert>\n");
        sqlBuffer.append("\t\n\t<!--====================【删】====================-->\n\n");
        sqlBuffer.append("\t<delete id=\"delete\" parameterType=\"" + idColumnJavaType + "\">\n");
        sqlBuffer.append("\t\tdelete from " + xTable.getTableName() + " where " + Config.TABLE_ID + "=#{value}\n");
        sqlBuffer.append("\t</delete>\n");
        sqlBuffer.append("\t\n\t<!--====================【改】====================-->\n\n");
        sqlBuffer.append("\t<update id=\"update\" parameterType=\"" + objName + "\">\n");
        sqlBuffer.append("\t\tupdate " + xTable.getTableName() + "\n");
        sqlBuffer.append("\t\t<set>\n");
        sqlBuffer.append(updateBuffer);
        sqlBuffer.append("\t\t\t" + Config.TABLE_ID + "=#{" + idColumnJavaName + "}\n");
        sqlBuffer.append("\t\t</set>\n");
        sqlBuffer.append("\t\twhere " + Config.TABLE_ID + "=#{" + idColumnJavaName + "}\n");
        sqlBuffer.append("\t</update>\n");
        sqlBuffer.append("\t\n\t<!--====================【查】====================-->\n\n");
        sqlBuffer.append("\t<select id=\"detail\" parameterType=\"" + idColumnJavaType + "\" resultMap=\"BaseResultMap\">\n");
        sqlBuffer.append("\t\tselect * from " + xTable.getTableName() + " where " + Config.TABLE_ID + "=#{value}\n");
        sqlBuffer.append("\t</select>\n");
        sqlBuffer.append("\t\n");
        sqlBuffer.append("\t<select id=\"list\" parameterType=\"" + objName + "\" resultMap=\"BaseResultMap\">\n");
        sqlBuffer.append("\t\tselect * from " + xTable.getTableName() + "\n");
        sqlBuffer.append("\t\t<where>\n");
        sqlBuffer.append("\t\t\t<include refid=\"base_where\"/>\n");
        sqlBuffer.append("\t\t</where>\n");
        sqlBuffer.append("\t\torder by " + Config.TABLE_ID + " desc\n");
        sqlBuffer.append("\t</select>\n");
        sqlBuffer.append("\n\n</mapper>");

        //写入文件
        ParsecFileTools.writeFile(Config.CODE_PATH + xTable.getPojoName() + "Mapper.xml", sqlBuffer);
        System.out.println("===dao.xml build success======");
    }

    /**
     * 创建 dao 接口
     */
    public static void createDao(XTable xTable) {

        StringBuffer daoBuffer = new StringBuffer();
        String idColumnJavaName = XParseName.parseNameToCamel(Config.TABLE_ID);
        String idColumnJavaType = xTable.getIdColumnJavaType(idColumnJavaName);


        //dao接口代码
        daoBuffer.append("package " + Config.ENTITY_PACKAGE + ";\n\n");
        daoBuffer.append("import " + Config.ENTITY_PACKAGE + "." + xTable.getPojoName() + ";\n");
//        daoBuffer.append("import org.apache.ibatis.annotations.Mapper;\n\n");
        daoBuffer.append("import java.util.List;\n\n");

        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        daoBuffer.append("/**\n" +
                " * Created by AutoGenerateCode on " + now + ".\n" +
                " * @author " + Config.AUTHOR +"\n"+
                " */  \n");
//        daoBuffer.append("@Mapper\n");
        daoBuffer.append("public interface " + xTable.getPojoName() + "Dao" + " {\n");
        daoBuffer.append("\t\n");
        daoBuffer.append("\t//====================【增】====================\n");
        daoBuffer.append("\t\n");
        daoBuffer.append("\tint insert(" + xTable.getPojoName() + " obj);\n");
        daoBuffer.append("\t\n");
        daoBuffer.append("\t//====================【删】====================\n");
        daoBuffer.append("\t\n");
        daoBuffer.append("\tint delete(" + idColumnJavaType + " " + idColumnJavaName + ");\n");
        daoBuffer.append("\t\n");
        daoBuffer.append("\t//====================【改】====================\n");
        daoBuffer.append("\t\n");
        daoBuffer.append("\tint update(" + xTable.getPojoName() + " obj);\n");
        daoBuffer.append("\t\n");
        daoBuffer.append("\t//====================【查】====================\n");
        daoBuffer.append("\t\n");
        daoBuffer.append("\t" + xTable.getPojoName() + " detail(" + idColumnJavaType + " " + idColumnJavaName + ");\n");
        daoBuffer.append("\tList<" + xTable.getPojoName() + "> list(" + xTable.getPojoName() + " obj);\n");
        daoBuffer.append("\n}");

        //写入文件
        ParsecFileTools.writeFile(Config.CODE_PATH + xTable.getPojoName() + "Dao.java", daoBuffer);
        System.out.println("=====Dao build success=====");
    }

}
