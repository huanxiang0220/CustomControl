package com.tang.customcontrol.bean;

/**
 * 作者:  tang
 * 时间： 2018/8/17 0017 下午 2:25
 * 邮箱： 3349913147@qq.com
 * 描述：
 */

public class ItemBean {

    private String title;
    private String desc;

    public ItemBean(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }
}
