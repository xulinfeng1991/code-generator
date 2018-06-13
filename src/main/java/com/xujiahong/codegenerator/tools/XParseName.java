package com.xujiahong.codegenerator.tools;

import com.xujiahong.codegenerator.Config;

/**
 * 解析命名方式的工具类
 * Created by xujiahong on 2017/8/4.
 * ======================功能列表======================
 */
public class XParseName {

    /**
     * 将字段名转换为符合Java规范的驼峰命名法
     * @param str
     * @return
     */
    public static String parseNameToCamel(String str){
        return formatNameToCamel(str, Config.NAMED_FORMAT);
    }

    /**
     * 将字符串转换为符合Java规范的驼峰命名法
     * @param str 字符串
     * @param namedFormat 字符串原来的命名规范
     * @return
     */
    private static String formatNameToCamel(String str,int namedFormat){
        //如果字符串为空直接返回
        if(str==null || "".equals(str)){
            return str;
        }
        String javaName = "";
        if(namedFormat== Config.NamedFormat.UNDERLINE){//如果数据库字段是下划线格式
            String[] strs = str.split("_");
            for(int i=0;i<strs.length;i++){
                if(!"".equals(strs[i])){
                    if("".equals(javaName)){//第一个字符串直接赋值
                        javaName += strs[i];
                    }else{//其他字段首字母大写
                        javaName += toUpperCase(strs[i]);
                    }
                }
            }
            return javaName;
        }
        return str;
    }

    /**
     * 首字母大写
     * @return
     */
    public static String toUpperCase(String str){
        //如果字符串为空直接返回
        if(str==null || "".equals(str)){
            return str;
        }
        if(str.length()==1){
            return str.toUpperCase();
        }
        return str.substring(0,1).toUpperCase()+str.substring(1);
    }

    /**
     * 首字母小写
     * @return
     */
    public static String toLowerCase(String str){
        //如果字符串为空直接返回
        if(str==null || "".equals(str)){
            return str;
        }
        if(str.length()==1){
            return str.toUpperCase();
        }
        return str.substring(0,1).toLowerCase()+str.substring(1);
    }


}
