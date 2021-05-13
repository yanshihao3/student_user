package com.ysh.config;

import com.jfinal.config.*;
import com.jfinal.core.Const;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.ysh.action.*;

/**
 * @author 闫世豪
 * @date 2019/11/9 11:37 下午
 * @email whynightcode@gmail.com
 */
public class UserConfig extends JFinalConfig {
    @Override
    public void configConstant(Constants me) {
        PropKit.use("config.txt");//加载数据库连接数据
        me.setDevMode(PropKit.getBoolean("devMode", true));
        me.setViewType(ViewType.JSP); // 设置视图类型为Jsp，否则默认为FreeMarker
        me.setViewExtension(".jsp");
        me.setMaxPostSize(100 * Const.DEFAULT_MAX_POST_SIZE);
        me.setInjectDependency(true);
    }

    /**
     * 配置路由
     */
    @Override
    public void configRoute(Routes me) {
        //配置路由
        me.add("/login", LoginController.class, "/");
        me.add("/upload", UploadController.class, "/");
        me.add("/show", ShowController.class, "/");
        me.add("/manager", ManagerController.class, "/");
        me.add("/teacher", TeacherController.class, "/");
    }

    @Override
    public void configEngine(Engine me) {
        // TODO Auto-generated method stub

    }

    /**
     * 配置插件
     */
    @Override
    public void configPlugin(Plugins plugins) {
        //获取jdbc连接池
        Prop prop = PropKit.use("config.txt");
        DruidPlugin druidPlugin = new DruidPlugin(prop.get("wxyz").trim(), prop.get("user").trim(), prop.get("password").trim());
        plugins.add(druidPlugin);
        //配置ActiveRecordPlugin插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        //控制台显示sql语句
        arp.setShowSql(true);
        plugins.add(arp);
        //controller对应的数据表 "user"对应的是数据库中表名，
        // User.class对应的是model中的User模型

    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {
        me.add(new ContextPathHandler("contextPath"));

    }

    public static void main(String[] args) {
        JFinal.start("src/main/webapp", 8080, "/", 5);
    }

}
