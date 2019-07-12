package com.xujiahong.codegenerator;

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

        Map<String, String> map = new HashMap<String, String>();
        //在此处添加需要生成代码的数据表
        map.put("tbl_user","用户");
        map.put("tbl_bargain","砍价记录");
        map.put("tbl_bargain_code","核销码");
        map.put("tbl_bargain_flow","砍价流水");
//        map.put("tbl_sys_menu","菜单");
//        map.put("tbl_sys_role_menu","角色菜单关联");
//        map.put("tbl_sys_log","日志");
//        map.put("tbl_policy","政策");


        Set<String> set = map.keySet();
        for (String key : set) {
            //单表操作
            XTable xTable = getTable(key, map.get(key));
            try {
                createFile(xTable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        session.close();
    }

    /**
     * 根据实际配置，生成各种文件
     * @param xTable
     * @throws Exception
     */
    public static void createFile(XTable xTable) throws Exception {
        //生成实体
        CreateEntity.createEntity2019(xTable);
        //生成Mapper
        ParsecFileTools.writeFile(Config.CODE_PATH + xTable.getPojoName() + "Mapper.java", XParseTemplate.scan("parsec/Mapper.txt",xTable));
        //生成Controller
        ParsecFileTools.writeFile(Config.CODE_PATH + xTable.getPojoName() + "Controller.java", XParseTemplate.scan("parsec/Controller.txt",xTable));
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
        List<String> tableNames = dao.showTables();
        for (String tableName : tableNames) {
            XTable xTable = getTable(tableName, XParseName.parseNameToCamel(tableName));
            list.add(xTable);
        }
        return list;
    }

}
