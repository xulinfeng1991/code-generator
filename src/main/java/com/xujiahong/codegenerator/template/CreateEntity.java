package com.xujiahong.codegenerator.template;

import com.xujiahong.codegenerator.Config;
import com.xujiahong.codegenerator.entity.XColumn;
import com.xujiahong.codegenerator.entity.XTable;
import com.xujiahong.codegenerator.tools.ParsecFileTools;
import com.xujiahong.codegenerator.tools.XParseName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CreateEntity {

    /**
     * 创建实体，2019年实践版本
     */
    public static void createEntity2019(XTable xTable) {
        //装生成代码的buffer对象
        StringBuffer codeBuffer = new StringBuffer();//整个文件的buffer
        StringBuffer fieldBuffer = new StringBuffer();//field
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

        List<XColumn> columns = xTable.getColumns();
        for (XColumn column : columns) {
            //field
            /**
             * 关键字ID
             */
            if (column.getJavaFieldName().equals("id")) {
                fieldBuffer.append("\t@Id\n");
                fieldBuffer.append("\t@KeySql(useGeneratedKeys = true)\n");
            }
            //swagger相关
            if (Config.SWAGGER_ON) {
                fieldBuffer.append("\t@ApiModelProperty(value = \"" + column.getComment() + "\")\n");
            }
            fieldBuffer.append("\tprivate " + column.getJavaFieldType() + " " + column.getJavaFieldName() + ";\n\n");
        }

        codeBuffer.append("package " + Config.ENTITY_PACKAGE + ";\n\n");

        //swagger相关
        if (Config.SWAGGER_ON) {
            codeBuffer.append("import io.swagger.annotations.ApiModel;\n");
            codeBuffer.append("import io.swagger.annotations.ApiModelProperty;\n");

            codeBuffer.append("import lombok.Data;\n");
            codeBuffer.append("import lombok.EqualsAndHashCode;\n");
            codeBuffer.append("import lombok.experimental.Accessors;\n");
            codeBuffer.append("import tk.mybatis.mapper.annotation.KeySql;\n");

            codeBuffer.append("import javax.persistence.Id;\n");
            codeBuffer.append("import javax.persistence.Table;\n");
            codeBuffer.append("import java.time.LocalDateTime;\n");
        }

        codeBuffer.append(classCommentBuffer);
        codeBuffer.append("@Data\n");
        codeBuffer.append("@Accessors(chain = true)\n");
        codeBuffer.append("@EqualsAndHashCode(callSuper = false)\n");
        codeBuffer.append("@Table(name = \"" + xTable.getTableName() + "\")\n");

        //swagger相关
        if (Config.SWAGGER_ON) {
            codeBuffer.append("@ApiModel(value = \"" + xTable.getPojoName() + " | " + xTable.getChName()
                    + "\", description = \"" + xTable.getChName() + "\")\n");
        }

        codeBuffer.append("public class " + xTable.getPojoName() + " {");
        codeBuffer.append(fieldBuffer);
        codeBuffer.append("}");

        //写入文件
        ParsecFileTools.writeFile(Config.CODE_PATH + "entity/" + xTable.getPojoName() + ".java", codeBuffer);
        System.out.println("=====POJO build success=====");
    }

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
        getterAndSetterBuffer.append("\n\t// getter and setter\n\n");

        List<XColumn> columns = xTable.getColumns();
        for (XColumn column : columns) {
            //field
            /**
             * 关键字ID
             */
            fieldBuffer.append("\t/**\n");
            fieldBuffer.append("\t * " + column.getComment() + "\n");
            fieldBuffer.append("\t */\n");
            //swagger相关
            if (Config.SWAGGER_ON) {
                fieldBuffer.append("\t@ApiModelProperty(value = \"" + column.getComment() + "\")\n");
            }
            fieldBuffer.append("\tprivate " + column.getJavaFieldType() + " " + column.getJavaFieldName() + ";\n");
            //getter and setter
            getterAndSetterBuffer.append("\tpublic " + column.getJavaFieldType() + " get" + XParseName.toUpperCase(column
                    .getJavaFieldName()) + "() {\n");
            getterAndSetterBuffer.append("\t\treturn " + column.getJavaFieldName() + ";\n");
            getterAndSetterBuffer.append("\t}\n");
            getterAndSetterBuffer.append("\t\n");
            getterAndSetterBuffer.append("\tpublic void set" + XParseName.toUpperCase(column.getJavaFieldName()) + "(" + column.getJavaFieldType() + " " + column.getJavaFieldName() + ") {\n");
            getterAndSetterBuffer.append("\t\tthis." + column.getJavaFieldName() + " = " + column.getJavaFieldName() + ";\n");
            getterAndSetterBuffer.append("\t}\n");
            getterAndSetterBuffer.append("\t\n");
        }

        codeBuffer.append("package " + Config.ENTITY_PACKAGE + ";\n\n");

        //swagger相关
        if (Config.SWAGGER_ON) {
            codeBuffer.append("import io.swagger.annotations.ApiModel;\n");
            codeBuffer.append("import io.swagger.annotations.ApiModelProperty;\n");
        }

        codeBuffer.append(classCommentBuffer);

        //swagger相关
        if (Config.SWAGGER_ON) {
            codeBuffer.append("@ApiModel(value = \"" + xTable.getPojoName() + "\", description = \"" +
                    xTable.getChName() + "\")\n");
        }

        codeBuffer.append("public class " + xTable.getPojoName() + " {");
        codeBuffer.append(fieldBuffer);
        codeBuffer.append(getterAndSetterBuffer);
        codeBuffer.append("}");

        //写入文件
        ParsecFileTools.writeFile(Config.CODE_PATH + "entity/" + xTable.getPojoName() + ".java", codeBuffer);
        System.out.println("=====POJO build success=====");
    }
}
