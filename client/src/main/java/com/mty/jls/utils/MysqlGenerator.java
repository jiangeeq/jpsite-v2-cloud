package com.mty.jls.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.mty.jls.config.ssh.MyContextListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 掘金-蒋老湿（公众号：十分钟学编程）
 * @date 2018/11/29
 */
public class MysqlGenerator {

    /**
     * 包名
     */
    private static final String PACKAGE_NAME = "com.mty.jls.iflytek";
    /**
     * 模块名称
     */
    private static final String MODULE_NAME = "";
    /**
     * 代码生成者
     */
    private static final String AUTHOR = "掘金-蒋老湿（公众号：十分钟学编程）";

    /**
     * JDBC相关配置
     */
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/jpsite?useUnicode=true&characterEncoding=UTF-8";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root";
    private static final String SOURCE_PATH = File.separator + "src" + File.separator + "main" + File.separator + "java";
    private static final String XML_PATH = File.separator + "src" + File.separator + "main" + File.separator + "resources";
    private static final String OUT_PATH = System.getProperty("user.dir") + File.separator + "client";

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    /**
     * <p>
     * MySQL 生成演示
     * </p>
     */
    public static void main(String[] args) {
        String modelName = scanner("模块名");
        String tableName = scanner("表名");

        start(modelName, tableName);
    }

    public static void start(String modelName, String tableName) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<TableFill>();
        /*如 每张表都有一个创建时间、修改时间
        而且这基本上就是通用的了，新增时，创建时间和修改时间同时修改
        修改时，修改时间会修改，
        虽然像Mysql数据库有自动更新几只，但像ORACLE的数据库就没有了，
        使用公共字段填充功能，就可以实现，自动按场景更新了。
        如下是配置*/
        TableFill createField = new TableFill("gmt_create_time", FieldFill.INSERT);
        TableFill updateField = new TableFill("gmt_update_time", FieldFill.INSERT_UPDATE);
        tableFillList.add(createField);
        tableFillList.add(updateField);

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig().setOutputDir(OUT_PATH + SOURCE_PATH)// 输出目录
                .setFileOverride(true)// 是否覆盖文件 默认值：false
                .setOpen(false)  //是否打开输出目录  默认值：true
                .setSwagger2(false)  //开启 swagger2 模式
                .setActiveRecord(true)// 开启 activeRecord 模式
                .setEnableCache(false)// 是否在xml中添加二级缓存配置  默认值：`false
                .setBaseResultMap(true)// XML ResultMap
                .setBaseColumnList(true)// XML columList
                .setAuthor(AUTHOR)
                .setEntityName("%s")
                .setMapperName("%sDao")
                .setServiceName("%sService")
                .setServiceImplName("%sServiceImpl")
                .setControllerName("%sController")
                .setXmlName("%sMapper");

        // 数据源配置
        DataSourceConfig dataSource = new DataSourceConfig().setDbType(DbType.MYSQL)
                .setDriverName(DRIVER)
                .setUsername(USER_NAME)
                .setPassword(PASSWORD)
                .setUrl(URL);

        PackageConfig packageConfig = new PackageConfig()
                .setParent(PACKAGE_NAME)//  父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
                //.setModuleName(modelName) //父包模块名
                .setEntity("entity")
                .setService("service")
                .setServiceImpl("service.impl")
                .setController("controller")
                .setXml("xml")
                .setMapper("dao");

        // 自定义属性注入
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };

        // 调整 xml 生成目录演示
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return OUT_PATH + XML_PATH + "/mapper/" + camel(tableInfo.getName()) + "Mapper.xml";
            }
        });
        injectionConfig.setFileOutConfigList(focList);


        // 策略配置
        StrategyConfig strategy = new StrategyConfig()
                .setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setTableFillList(tableFillList)   //表填充字段
                .setEntityColumnConstant(false) //【实体】是否生成字段常量（默认 false）
                .setChainModel(true)
                .setEntityLombokModel(true)  //【实体】是否为lombok模型（默认 false）
                .setEntityBooleanColumnRemoveIsPrefix(false) //Boolean类型字段是否移除is前缀（默认 false）
                .setRestControllerStyle(true); //生成 @RestController 控制器
        if (tableName != null && !"".equals(tableName)) {
            strategy.setInclude(tableName);  //需要包含的表名，允许正则表达式（与exclude二选一配置）
        }

        mpg.setGlobalConfig(globalConfig).setDataSource(dataSource)
                .setStrategy(strategy)
                .setPackageInfo(packageConfig)
                .setCfg(injectionConfig)
//                .setTemplateEngine(new FreemarkerTemplateEngine())  // 切换为 freemarker 模板引擎, 默认velocity 模板引擎
                .setTemplate(new TemplateConfig().setXml(null)); // 关闭默认 xml 生成，调整生成至根目录

        // 执行生成
        mpg.execute();
        System.out.println("代码生产结束！");
    }

    /**
     * 下划线转驼峰
     *
     * @param str
     * @return
     */
    public static String camel(String str) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] cs = str.toCharArray();
        cs[0] -= 32;
        //利用正则删除下划线，把下划线后一位改成大写
        Pattern pattern = Pattern.compile("_(\\w)");
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer(String.valueOf(cs));

        if (matcher.find()) {
            sb = new StringBuffer();
            //将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
            //正则之前的字符和被替换的字符
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
            //把之后的也添加到StringBuffer对象里
            matcher.appendTail(sb);
        } else {
            return sb.toString();
        }
        return camel(sb.toString());
    }
}
