package com.xujiahong.codegenerator;

/**
 * 配置文件
 * Created by xujiahong on 2017/8/4.
 * ======================功能列表======================
 */
public class Config {



    //肯定要改的======================
    public static final String PO_PACKAGE = "com.coloseo.hubblenet.dao.domain";//po实体包名
    public static final String DAO_PACKAGE = "com.coloseo.hubblenet.dao.mapper";//dao接口包名
//    public static final String PO_PACKAGE = "com.xujiahong.develophelper";//po实体包名
//    public static final String DAO_PACKAGE = "com.xujiahong.develophelper";//dao接口包名
    public static final String AUTHOR = "xujiahong";//开发者名称，生存注释用

    //可能要改的======================
    public static final int NAMED_FORMAT = NamedFormat.UNDERLINE;//数据表字段的命名方式（默认下划线）

    public static final String TABLE_ID = "id";//数据库表的主键字段，默认是id
    public static final boolean USE_GENERATED_KEYS = true;//是否有采用自增主键，默认true
    public static final boolean SWAGGER_ON = false;//是否开启swagger API文档相关的生存，默认true

    //基本不改的======================
    public static final String CURRENT_VERSION = "1.0";//当前版本号
    public static final String CODE_PATH = "C:/Users/xujiahong/Desktop/temp/";//生成文件存放路径

    public interface NamedFormat{
        int UNDERLINE = 1;//下划线格式
        int CAMEL = 2;//驼峰格式
    }
}
