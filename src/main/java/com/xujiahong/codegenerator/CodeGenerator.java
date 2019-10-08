package com.xujiahong.codegenerator;

import com.mysql.jdbc.StringUtils;
import com.xujiahong.codegenerator.dao.CodeGenerateDao;
import com.xujiahong.codegenerator.entity.XColumn;
import com.xujiahong.codegenerator.entity.XTable;
import com.xujiahong.codegenerator.template.CreateEntity;
import com.xujiahong.codegenerator.tools.ParsecFileTools;
import com.xujiahong.codegenerator.tools.XParseName;
import com.xujiahong.codegenerator.tools.XParseTemplate;
import com.xujiahong.codegenerator.tools.XParseType;
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

    static {
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

        //====================整库生成====================
        List<XTable> tables = getAllTable();
        for (XTable xTable : tables) {
            try {
                createFile(xTable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //====================单独配置表====================
//        Map<String, String> map = new HashMap<String, String>();
//        //在此处添加需要生成代码的数据表
//        map.put("tbl_case", "病例");
//
//
//        Set<String> set = map.keySet();
//        for (String key : set) {
//            //单表操作
//            XTable xTable = getTable(key, map.get(key));
//            try {
//                createFile(xTable);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        session.close();
    }

    /**
     * 根据实际配置，生成各种文件
     *
     * @param xTable
     * @throws Exception
     */
    public static void createFile(XTable xTable) throws Exception {

        //生成实体
        CreateEntity.createEntity2019(xTable);
        //生成Mapper
        String MAPPER_FILE_PATH = Config.CODE_PATH + "mapper/";
        File mapperPath = new File( MAPPER_FILE_PATH );
        if ( !mapperPath.exists()){//若此目录不存在，则创建之
            mapperPath.mkdir();
            System.out.println("创建 mapper 文件存放路径为："+ MAPPER_FILE_PATH);
        }
        ParsecFileTools.writeFile(MAPPER_FILE_PATH + xTable.getPojoName() + "Mapper.java", XParseTemplate.scan("parsec/Mapper.txt", xTable));
        //生成ApiController
        String API_CONTROLLER_FILE_PATH = Config.CODE_PATH + "controller/api/";
        File apiControllerPath = new File( MAPPER_FILE_PATH );
        if ( !apiControllerPath.exists()){//若此目录不存在，则创建之
            apiControllerPath.mkdir();
            System.out.println("创建 controller/api/ 文件存放路径为："+ API_CONTROLLER_FILE_PATH);
        }

        ParsecFileTools.writeFile(API_CONTROLLER_FILE_PATH + xTable.getPojoName() + "Controller.java", XParseTemplate.scan("parsec/ApiController.txt", xTable));
        //生成MgrController
        String MANAGER_CONTROLLER_FILE_PATH = Config.CODE_PATH + "controller/manager/";
        File managerControllerPath = new File( MAPPER_FILE_PATH );
        if ( !managerControllerPath.exists()){//若此目录不存在，则创建之
            managerControllerPath.mkdir();
            System.out.println("创建 controller/manager/ 文件存放路径为："+ MANAGER_CONTROLLER_FILE_PATH);
        }
        ParsecFileTools.writeFile(MANAGER_CONTROLLER_FILE_PATH + xTable.getPojoName() + "Controller.java", XParseTemplate.scan("parsec/MgrController.txt", xTable));
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
     *
     * @return
     */
    public static List<XTable> getAllTable() {
        List<XTable> list = new ArrayList<XTable>();
        List<Map<String, String>> tables = dao.selectTables(Config.DATABASE_NAME);
        for (Map<String, String> map : tables) {
            String tableName = map.get("tableName");
            String tableComment = map.get("tableComment");
            if (tableComment == null || "".equals(tableComment)) {
                tableComment = tableName;
            }
            if (tableName.equals("tbl_acl") || tableName.equals("tbl_directory")) {
                continue;
            }
            XTable xTable = getTable(tableName, tableComment);
            list.add(xTable);
        }
        return list;
    }

}
