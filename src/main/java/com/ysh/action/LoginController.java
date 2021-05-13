package com.ysh.action;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.ysh.bean.Message;

import java.util.Date;

/**
 * @author 闫世豪
 * @date 2019/11/9 11:48 下午
 * @email whynightcode@gmail.com
 */
public class LoginController extends Controller {

    public void test(){
        render("test.html");
    }

    public void student() {
        setAttr("type","student");
        render("index.jsp");
    }

    public void teacher() {
        setAttr("type","teacher");
        render("index.jsp");

    }



    public void login() {
        Message message = new Message();
        getFile();
        String username = getPara("username");
        String password = getPara("password");
        String type=getPara("type");
        String sql="";
        if ("student".equals(type)){
             sql= "SELECT * FROM student_users WHERE name = ? and password = ?";
        }else {
            sql= "SELECT * FROM teacher_users WHERE personnel = ? and password = ?";
        }
        Record user = Db.findFirst(sql, username, password);
        if (user == null) {
            message.setSuccess(false);
            message.setErrorMsg("用户名或密码错误");
        } else {
            message.setSuccess(true);
        }
        renderJson(message);
    }

    public void register() {
        Message message = new Message();
        getFile();
        String username = getPara("username");
        String password = getPara("password1");
        String sql = "SELECT * FROM student_users WHERE name = ? and password = ?";
        Record user = Db.findFirst(sql, username, password);
        if (user == null) {
            user=new Record();
            user.set("name", username)
                    .set("password", password)
                    .set("create_time", new Date());
            Db.save("student_users", user);
            message.setSuccess(true);
            message.setMsg("注册成功");
        } else {
            message.setSuccess(false);
            message.setMsg("改用户已经注册");
        }
        renderJson(message);
    }

    public void forget() {
        Message message = new Message();
        getFile();
        String username = getPara("username");
        String password = getPara("password1");
        String type=getPara("type");
        String sql="";
        if ("student".equals(type)){
            sql= "SELECT * FROM student_users WHERE name = ?";
        }else {
            sql= "SELECT * FROM teacher_users WHERE personnel = ?";
        }
        Record user = Db.findFirst(sql, username);
        if (user == null) {
            message.setSuccess(false);
            message.setMsg("系统没有当前用户，用户名错误");
        } else {
            user.set("password", password)
                    .set("update_time", new Date());
            if ("student".equals(type)){
                Db.update("student_users", user);
            }else {
                Db.update("teacher_users", user);
            }
        }
        renderJson(message);
    }



}
