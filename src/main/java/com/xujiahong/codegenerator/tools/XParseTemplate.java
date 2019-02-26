package com.xujiahong.codegenerator.tools;

import com.xujiahong.codegenerator.Config;
import com.xujiahong.codegenerator.entity.XColumn;
import com.xujiahong.codegenerator.entity.XTable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 根据XTable中的数据，将txt模板解析为可用的java源代码
 *
 * @author xujiahong
 * @date 2019/2/25
 */
public class XParseTemplate {

    /**
     * @param lineTxt
     * @param xTable
     * @return
     */
    public static String parse(String lineTxt, XTable xTable){
        if(lineTxt.contains(TemplateItem.PARSEC_OBJECT_CH_NAME)){
            lineTxt = lineTxt.replaceAll(TemplateItem.PARSEC_OBJECT_CH_NAME,xTable.getChName());
        }
        if(lineTxt.contains(TemplateItem.PARSEC_OBJECT_NAME)){
            lineTxt = lineTxt.replaceAll(TemplateItem.PARSEC_OBJECT_NAME,xTable.getPojoName());
        }
        if(lineTxt.contains(TemplateItem.PARSEC_LOWER_OBJECT_NAME)){
            lineTxt = lineTxt.replaceAll(TemplateItem.PARSEC_LOWER_OBJECT_NAME,XParseName.toLowerCase(xTable.getPojoName()));
        }
        if(lineTxt.contains(TemplateItem.PARSEC_USER_NAME)){
            lineTxt = lineTxt.replaceAll(TemplateItem.PARSEC_USER_NAME, Config.AUTHOR);
        }
        if(lineTxt.contains(TemplateItem.PARSEC_DATE)){
            lineTxt = lineTxt.replaceAll(TemplateItem.PARSEC_DATE,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }
        if(lineTxt.contains(TemplateItem.PARSEC_TABLE_NAME)){
            lineTxt = lineTxt.replaceAll(TemplateItem.PARSEC_TABLE_NAME,xTable.getTableName());
        }
        if(lineTxt.contains("PARSEC_LOOP")){
            StringBuffer fieldBuffer = new StringBuffer();//field
            List<XColumn> columns = xTable.getColumns();
            if(columns!=null && columns.size()>0){
                for (int i=0;i<columns.size();i++) {
                    XColumn column = columns.get(i);

                    fieldBuffer.append("\t/**\n");
                    fieldBuffer.append("\t * "+column.getComment()+"\n");
                    fieldBuffer.append("\t */\n");
                    fieldBuffer.append("\tprivate " + column.getJavaFieldType() + " " + column.getJavaFieldName() + ";\n");

                }
            }
            if(lineTxt.contains(TemplateItem.PARSEC_LOOP_FIELDS)){
                lineTxt = lineTxt.replaceAll(TemplateItem.PARSEC_LOOP_FIELDS,fieldBuffer.toString());
            }
        }
        return lineTxt;
    }

    /**
     * txt模板中，可设置的用于解析生成代码的术语
     */
    public interface TemplateItem{
        String TEMPLATE_ITEM_PREFIX = "PARSEC_";
        /**
         * 对象名称
         */
        String PARSEC_OBJECT_NAME = "PARSEC_OBJECT_NAME";
        /**
         * 首字母小写的，对象名称
         */
        String PARSEC_LOWER_OBJECT_NAME = "PARSEC_LOWER_OBJECT_NAME";
        /**
         * 模块中文名称
         */
        String PARSEC_OBJECT_CH_NAME = "PARSEC_OBJECT_CH_NAME";
        /**
         * 用户名称
         */
        String PARSEC_USER_NAME = "PARSEC_USER_NAME";
        /**
         * 时间
         */
        String PARSEC_DATE = "PARSEC_DATE";
        /**
         * 表名
         */
        String PARSEC_TABLE_NAME = "PARSEC_TABLE_NAME";
        /**
         * 属性循环（PO用）
         */
        String PARSEC_LOOP_FIELDS = "PARSEC_LOOP_FIELDS";
    }
}
