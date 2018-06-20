package com.xujiahong.codegenerator.template;

import com.xujiahong.codegenerator.Config;
import com.xujiahong.codegenerator.entity.XColumn;
import com.xujiahong.codegenerator.entity.XTable;
import com.xujiahong.codegenerator.tools.ParsecFileTools;

import java.util.List;

/**
 * description
 *
 * @author xujiahong
 * @date 2018/5/3
 */
public class CreateTestJson {

    public static void createJson(XTable xTable) {
        //装生成代码的buffer对象
        StringBuffer codeBuffer = new StringBuffer();//整个文件的buffer

        codeBuffer.append("{\n");

        List<XColumn> columns = xTable.getColumns();
        if(columns!=null && columns.size()>0){
            for (int i=0;i<columns.size();i++) {
                XColumn column = columns.get(i);
                if(column.getJavaFieldType().equals("Integer")){
                    codeBuffer.append("\""+column.getJavaFieldName()+"\":"+0);
                }else if(column.getJavaFieldType().equals("Long")){
                    codeBuffer.append("\""+column.getJavaFieldName()+"\":"+0L);
                }else if(column.getJavaFieldType().equals("String")){
                    codeBuffer.append("\""+column.getJavaFieldName()+"\":\"string\"");
                }else{
                    codeBuffer.append("\""+column.getJavaFieldName()+"\":\""+column.getJavaFieldType()+"\"");
                }
                if(i!=columns.size()-1){
                    codeBuffer.append(",\n");
                }
            }
        }

        codeBuffer.append("\n}\n");

        //写入文件
        ParsecFileTools.writeFile(Config.CODE_PATH + xTable.getPojoName() + ".json", codeBuffer);
        System.out.println("=====test.json build success=====");
    }
}
