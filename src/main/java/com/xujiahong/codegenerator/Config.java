package com.xujiahong.codegenerator;

/**
 * 配置文件
 * Created by xujiahong on 2019/2/26.
 * ======================功能列表======================
 */
public class Config {



    //肯定要改的======================
    public static final String PO_PACKAGE = "com.parsec.po";//PO文件单独生成
    public static final String AUTHOR = "xujiahong";//开发者名称，生成注释用
    /**
     * 生成文件存放路径
     * 为了避免错误的文件覆盖，建议将生成好的文件统一存放在桌面的一个临时文件夹，再拷贝到项目中。
     */
    public static final String CODE_PATH = "C:/Users/86152/Desktop/temp/";

    //可能要改的======================
    public static final int NAMED_FORMAT = NamedFormat.UNDERLINE;//数据表字段的命名方式（默认下划线）
    public static final String TABLE_PROFIX = "tbl_";//表名前缀（不计入对象名称中）

    public static final String TABLE_ID = "id";//数据库表的主键字段，默认是id
    public static final boolean USE_GENERATED_KEYS = true;//是否有采用自增主键，默认true
    public static final boolean SWAGGER_ON = true;//是否开启swagger API文档相关的生成，默认true

    //基本不改的======================
    public static final String CURRENT_VERSION = "1.0";//当前版本号


    public interface NamedFormat{
        int UNDERLINE = 1;//下划线格式
        int CAMEL = 2;//驼峰格式
    }
}
