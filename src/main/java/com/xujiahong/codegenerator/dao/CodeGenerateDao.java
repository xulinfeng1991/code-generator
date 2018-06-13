package com.xujiahong.codegenerator.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by xujiahong on 2017/8/3.
 * ======================功能列表======================
 */
public interface CodeGenerateDao {

    /**
     * 查询 tableName 表的所有元数据字段
     * @param tableName
     * @return
     */
    List<Map<String,Object>> showFullFields(String tableName);

    /**
     * 查询所有表格
     * @return
     */
    List<String> showTables();

}
