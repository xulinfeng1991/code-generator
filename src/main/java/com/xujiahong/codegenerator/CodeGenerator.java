package com.xujiahong.codegenerator;

import com.xujiahong.codegenerator.dao.CodeGenerateDao;
import com.xujiahong.codegenerator.entity.XColumn;
import com.xujiahong.codegenerator.template.CreateTestJson;
import com.xujiahong.codegenerator.tools.XParseName;
import com.xujiahong.codegenerator.tools.XParseType;
import com.xujiahong.codegenerator.entity.XTable;
import com.xujiahong.codegenerator.tools.ParsecFileTools;
import com.xujiahong.codegenerator.template.XParseTemplate;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 代码生成器
 * Created by xujiahong on 2017/8/3.
 * ======================功能列表======================
 */
public class CodeGenerator {

    static CodeGenerateDao dao;
    static SqlSession session;

    static{
        //读取配置
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("读取mybatis配置文件出错");
        }
        //获取sqlSession，实例化查询接口
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        session = sqlSessionFactory.openSession();
        dao = session.getMapper(CodeGenerateDao.class);
    }


    public static void main(String[] args) {

        Map<String,String> map = new HashMap<String, String>();
        //在此处添加需要生成代码的数据表
        map.put("tbl_sys_user","用户");
        map.put("tbl_sys_role","角色");
        map.put("tbl_sys_menu","菜单");
        map.put("tbl_sys_role_menu","角色菜单关联表");

        Set<String> set = map.keySet();
        for(String key : set){
            //单表操作
            XTable xTable = getTable(key,map.get(key));
            try {
                CreateTestJson.createJson(xTable);
                createFile(xTable);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        session.close();
    }

    public static void createFile(XTable xTable) throws Exception{

        ParsecFileTools.writeFile(Config.CODE_PATH + xTable.getPojoName() + ".java", scan("parsec/POJO.txt",xTable));
        System.out.println("=====POJO build success=====");
    }

    public static StringBuffer scan(String path,XTable xTable) throws Exception{

        String filePath = "src/main/java/com/xujiahong/codegenerator/template/"+path;

        StringBuffer fileBuffer = new StringBuffer();

        BufferedReader br = new BufferedReader(new InputStreamReader
                (new FileInputStream(new File(filePath)),"UTF-8"));

        String lineTxt = null;
        while ((lineTxt = br.readLine()) != null) {

            if(lineTxt.contains("【xjh-")){
                lineTxt = XParseTemplate.parse(lineTxt,xTable);
            }
            fileBuffer.append(lineTxt+"\n");
        }
        return fileBuffer;
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
            fieldBuffer.append("\t * "+column.getComment()+"\n");
            fieldBuffer.append("\t */\n");
            //swagger相关
            if(Config.SWAGGER_ON){
                fieldBuffer.append("\t@ApiModelProperty(value = \""+column.getComment()+"\")\n");
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

        codeBuffer.append("package " + Config.PO_PACKAGE + ";\n\n");

        //swagger相关
        if(Config.SWAGGER_ON){
            codeBuffer.append("import io.swagger.annotations.ApiModel;\n");
            codeBuffer.append("import io.swagger.annotations.ApiModelProperty;\n");
        }

        codeBuffer.append(classCommentBuffer);

        //swagger相关
        if(Config.SWAGGER_ON){
            codeBuffer.append("@ApiModel(value = \""+xTable.getPojoName()+"\", description = \""+
                    xTable.getChName()+"\")\n");
        }

        codeBuffer.append("public class " + xTable.getPojoName() + " {");
        codeBuffer.append(fieldBuffer);
        codeBuffer.append(getterAndSetterBuffer);
        codeBuffer.append("}");

        //写入文件
        ParsecFileTools.writeFile(Config.CODE_PATH + xTable.getPojoName() + ".java", codeBuffer);
        System.out.println("=====POJO build success=====");
    }


    /**
     * 获取表信息
     *
     * @param tableName
     * @param chName
     * @return XTable
     */
    public static XTable getTable(String tableName, String chName) {
        if (tableName == null || "".equals(tableName) || chName == null || "".equals(chName)) {
            throw new IllegalArgumentException("入参异常");
        }

        //查询数据表的Fields
        List<Map<String, Object>> list = dao.showFullFields(tableName);
        if (list == null || list.size() == 0) {
            throw new IllegalArgumentException("数据表字段为空");
        }
        List<XColumn> columns = new ArrayList<XColumn>();
        for (Map<String, Object> map : list) {
            if (map != null) {
                XColumn xColumn = new XColumn();
                if (map.containsKey("Field")) {
                    xColumn.setDbFieldName(map.get("Field").toString());
                    xColumn.setJavaFieldName(XParseName.parseNameToCamel(map.get("Field").toString()));
                }
                if (map.containsKey("Type")) {
                    xColumn.setDbFieldType(XParseType.parseType(map.get("Type").toString()));
                    xColumn.setJavaFieldType(XParseType.parseTypeToJava(map.get("Type").toString()));
                }
                if (map.containsKey("Comment")) {
                    xColumn.setComment(map.get("Comment").toString());
                }
                columns.add(xColumn);
            }
        }
        //创建表对象
        XTable xTable = new XTable(tableName, chName, columns);
        System.out.println("获取数据表" + tableName + "信息成功");
        return xTable;
    }


    /**
     * 获取所有表信息
     * @return
     */
    public static List<XTable> getAllTable(){
        List<XTable> list = new ArrayList<XTable>();
        List<String> tableNames = dao.showTables();
        for(String tableName : tableNames){
            XTable xTable = getTable(tableName,XParseName.parseNameToCamel(tableName));
            list.add(xTable);
        }
        return list;
    }

}
