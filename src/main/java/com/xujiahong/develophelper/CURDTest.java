package com.xujiahong.develophelper;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Random;

/**
 * Created by xujiahong on 2017/8/14.
 * ======================功能列表======================
 */
public class CURDTest {

    public static void main(String[] args) {
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
        SqlSession session = sqlSessionFactory.openSession();
        ExampleDao dao = session.getMapper(ExampleDao.class);



        Example example = getRandomObj();
        System.out.println("insert return "+dao.insert(example));
        try {
            Example dbExample = dao.detail(example.getExampleId());
            System.out.println(dbExample);
            Example upExample = getRandomObj();
            upExample.setExampleId(dbExample.getExampleId());
            System.out.println("update return "+dao.update(upExample));
        } finally {
            session.close();
        }

    }

    public static Example getRandomObj(){
        Random random = new Random();
        int num = Math.abs(random.nextInt());
        Date now = new Date();

        Example example = new Example();
        example.setStringValue("Str"+num);
        example.setDateValue(now);
        now.setTime(now.getTime()-(random.nextInt()));
        example.setTimeValue(now);
        return example;
    }

    /*

    CREATE TABLE `example` (
  `example_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '示例表ID',
  `string_value` varchar(32) DEFAULT NULL COMMENT '字符串值',
  `date_value` date DEFAULT NULL COMMENT '日期值',
  `time_value` datetime DEFAULT NULL COMMENT '时间值',
  PRIMARY KEY (`example_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8

     */
}
