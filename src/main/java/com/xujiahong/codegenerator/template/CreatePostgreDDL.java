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
 * @date 2018/5/2
 */
public class CreatePostgreDDL {

    public static void createDDL(XTable xTable) {
        //装生成代码的buffer对象
        StringBuffer codeBuffer = new StringBuffer();//整个文件的buffer
        StringBuffer commentBuffer = new StringBuffer();

        codeBuffer.append("drop table if exists "+xTable.getTableName()+";\n");
        codeBuffer.append("CREATE TABLE "+xTable.getTableName()+" (\n");
        codeBuffer.append("id  bigserial primary key,\n");

        List<XColumn> columns = xTable.getColumns();
        if(columns!=null && columns.size()>0){
            for (int i=0;i<columns.size();i++) {
                XColumn column = columns.get(i);

                //comment
                commentBuffer.append("COMMENT ON COLUMN "+xTable.getTableName()+"."+column.getDbFieldName()+" IS " +
                        "'"+column.getComment()+"';\n");


                //非主键字段（id字段较为特殊，都是模板中写死的，不加入遍历）
                if (!column.getDbFieldName().equals(Config.TABLE_ID)) {
                    //最后一个字段，少逗号
                    if(i==columns.size()-1){
                        codeBuffer.append(column.getDbFieldName()+" "+column.getDbFieldType()+"\n");
                    }else{
                        codeBuffer.append(column.getDbFieldName()+" "+column.getDbFieldType()+",\n");
                    }
                }



            }
        }

        codeBuffer.append(");\n");
        codeBuffer.append(commentBuffer);

        //写入文件
        ParsecFileTools.writeFile(Config.CODE_PATH + "Vxxx_Create_"+xTable.getPojoName() + ".sql", codeBuffer);
        System.out.println("=====DDL.sql build success=====");
    }
}
