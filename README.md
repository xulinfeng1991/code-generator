# code-generator

## 简介
简易代码生成器，基于mybatis和mysql，可根据模板生成各种形式的文件，目前模板配置模式还比较死板

方便本人在项目中快速生成（稳定可靠的）通用的代码文件
包括但不限于

 1. POJO
 2. mapper
 3. service
 4. controller
 5. json数据文件
 6. 非mysql的数据库DDL文件

## 操作步骤

（1）在resources/mysql.properties中配置自己的数据库环境（目前只支持mysql）
（2）在Config配置类中定义更多的细节
（3）使用CodeGenerator生成代码
