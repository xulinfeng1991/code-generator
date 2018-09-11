package com.xujiahong.codegenerator.template;

import com.xujiahong.codegenerator.Config;
import com.xujiahong.codegenerator.entity.XColumn;
import com.xujiahong.codegenerator.entity.XTable;
import com.xujiahong.codegenerator.tools.XParseName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 根据XTable中的数据，将txt模板解析为可用的java源代码
 * Hubble项目的解析规则（每个项目代码规范不同，需要修改此文件）
 *
 * @author xujiahong
 * @date 2018/4/25
 */
public class XParseTemplate {

    /**
     * 【xjh-chName】模块中文名称
     * 【xjh-objectName】对象名称
     * 【xjh-lower-objectName】首字母小写的，对象名称
     * 【xjh-name】
     * 【xjh-date】
     * 【xjh-tableName】
     * 【xjh-loopInsert1】
     * 【xjh-loopInsert2】
     * 【xjh-loopUpdate】
     * 【xjh-loopSelect】
     * 【xjh-loopResult】
     * 【xjh-loopSwagger】
     * @param lineTxt
     * @param xTable
     * @return
     */
    public static String parse(String lineTxt,XTable xTable){
        if(lineTxt.contains("【xjh-chName】")){
            lineTxt = lineTxt.replaceAll("【xjh-chName】",xTable.getChName());
        }
        if(lineTxt.contains("【xjh-objectName】")){
            lineTxt = lineTxt.replaceAll("【xjh-objectName】",xTable.getPojoName());
        }
        if(lineTxt.contains("【xjh-lower-objectName】")){
            lineTxt = lineTxt.replaceAll("【xjh-lower-objectName】",XParseName.toLowerCase(xTable.getPojoName()));
        }
        if(lineTxt.contains("【xjh-name】")){
            lineTxt = lineTxt.replaceAll("【xjh-name】",Config.AUTHOR);
        }
        if(lineTxt.contains("【xjh-date】")){
            lineTxt = lineTxt.replaceAll("【xjh-date】",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }
        if(lineTxt.contains("【xjh-tableName】")){
            lineTxt = lineTxt.replaceAll("【xjh-tableName】",xTable.getTableName());
        }
        if(lineTxt.contains("【xjh-loop")){
            String loopInsert1 = "";
            String loopInsert2 = "";
            String loopUpdate = "";
            String loopSelect = "";
            String loopResult = "";
            String loopSwagger = "";
            List<XColumn> columns = xTable.getColumns();
            if(columns!=null && columns.size()>0){
                for (int i=0;i<columns.size();i++) {
                    XColumn column = columns.get(i);
                    //非主键字段（id字段较为特殊，都是模板中写死的，不加入遍历）
                    if (!column.getDbFieldName().equals(Config.TABLE_ID)) {
                        //最后一个字段，字符串中间少逗号
                        if(i==columns.size()-1){
                            loopInsert1 += "\""+column.getDbFieldName()+"\" +";
                            loopInsert2 += "\"#{"+column.getJavaFieldName()+"}\"+";
                            loopResult += "@Result(property = \""+column.getJavaFieldName()+"\", column = \""+column
                                    .getDbFieldName()+"\")";
                        }else{
                            loopInsert1 += "\""+column.getDbFieldName()+",\" +\n";
                            loopInsert2 += "\"#{"+column.getJavaFieldName()+"},\"+\n";
                            loopResult += "@Result(property = \""+column.getJavaFieldName()+"\", column = \""+column
                                    .getDbFieldName()+"\"),\n";
                        }
                        loopUpdate += "\"<if test='"+column.getJavaFieldName()+" != null'> "+column.getDbFieldName()+
                                " = #{"+column.getJavaFieldName()+"},</if>\" +";
                        //不同类型的查询匹配规则也不同，字符串用like，其他用=
                        if("String".equals(column.getJavaFieldType())){
                            loopSelect += "\"<if test='"+column.getJavaFieldName()+" != null'> and "+column
                                    .getDbFieldName()+" like" +
                                    " concat('%',#{"+column.getJavaFieldName()+"},'%')</if>\"";
                        }else{
                            loopSelect += "\"<if test='"+column.getJavaFieldName()+" != null'> and "+column
                                    .getDbFieldName()+" = " +
                                    "#{"+column.getJavaFieldName()+"}</if>\"";
                        }
                        //最后一个字段，少符号或缩进
                        if(i!=columns.size()-1){
                            loopUpdate += "\n";
                            loopSelect += "+\n";
                        }
                        //API文档不管开始时间和修改时间
                        if(!column.getDbFieldName().equals("gmt_create")&&
                                !column.getDbFieldName().equals("gmt_modified")){
                            loopSwagger +="\t\t\t@ApiImplicitParam(name = \""+column.getJavaFieldName()
                                    +"\", value = \""+column.getComment()+"\"),\n";
                        }
                    }
                }
            }
            if (loopSwagger.length() > 0) {
                if (loopSwagger.charAt(loopSwagger.length() - 2) == ',') {
                    loopSwagger = loopSwagger.substring(0, loopSwagger.length() - 2);
                }
            }
            if(lineTxt.contains("【xjh-loopInsert1】")){
                lineTxt = lineTxt.replaceAll("【xjh-loopInsert1】",loopInsert1);
            }
            if(lineTxt.contains("【xjh-loopInsert2】")){
                lineTxt = lineTxt.replaceAll("【xjh-loopInsert2】",loopInsert2);
            }
            if(lineTxt.contains("【xjh-loopUpdate】")){
                lineTxt = lineTxt.replaceAll("【xjh-loopUpdate】",loopUpdate);
            }
            if(lineTxt.contains("【xjh-loopSelect】")){
                lineTxt = lineTxt.replaceAll("【xjh-loopSelect】",loopSelect);
            }
            if(lineTxt.contains("【xjh-loopResult】")){
                lineTxt = lineTxt.replaceAll("【xjh-loopResult】",loopResult);
            }
            if(lineTxt.contains("【xjh-loopSwagger】")){
                lineTxt = lineTxt.replaceAll("【xjh-loopSwagger】",loopSwagger);
            }
        }


        return lineTxt;
    }
}
