package com.ysh.bean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author 闫世豪
 * @date 2019/11/10 12:44 上午
 * @email whynightcode@gmail.com
 */
public class Message {


    private Boolean success = true; // 默认是true，如果只要有一个地方出错，就变为false，
    // 只要下一个能够返回页面的处理环节，检查到为false就返回
//	文字提示消息
    private String msg = "";
    //	文字提示消息类型，自定义自维护
    private String msgType = "";
    private String uri = "";
    private String template = "";
    public HashMap<String, Object> Fileds = new HashMap<String, Object>();


    private Object object = null; // 用于传输对象
    private Object object1 = null; // 用于传输对象
    private Object object2 = null; // 用于传输对象
    private Object object3 = null; // 用于传输对象

    public Object getObject3() {
        return object3;
    }

    public void setObject3(Object object3) {
        this.object3 = object3;
    }

    public Object getObject1() {
        return object1;
    }

    public void setObject1(Object object1) {
        this.object1 = object1;
    }

    public Object getObject2() {
        return object2;
    }

    public void setObject2(Object object2) {
        this.object2 = object2;
    }

    private ArrayList<Object> objects = null; // 用于传输对象


    //	扩展操作方式，比如“另存为”
    private String opration;


    public String getOpration() {
        return opration;
    }

    public void setOpration(String opration) {
        this.opration = opration;
    }


    public HashMap<String, Object> getFileds() {
        return Fileds;
    }

    public void putField(String key, Object o) {
        Fileds.put(key, o);
    }

    public Object getField(String key) {
        return Fileds.get(key);
    }

    public void setMsg(String msg) {
        if (this.msg.indexOf(msg) < 0)
            this.msg = this.msg + msg;
    }

    public void setErrorMsg(String msg) {
        this.success = false;
        if (msg != null && this.msg.indexOf(msg) < 0)
            this.msg = this.msg + msg;
    }

    public void setErrorMsg(String msg, String uri, String template) {
        this.success = false;
        this.uri = uri;
        this.template = template;
        if (msg != null && this.msg.indexOf(msg) < 0)
            this.msg = this.msg + msg;
    }


    public void addObjects(Object object) {
        if (objects == null) {
            objects = new ArrayList<Object>();
        }
        objects.add(object);

    }


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public ArrayList<Object> getObjects() {
        return objects;
    }

    public String getMsg() {
        return msg;
    }


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }


    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
