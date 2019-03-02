package com.icbc.icbcwelcome.config;

import android.os.Environment;

public class constants {
    //FTP
    public static final String HOST = "115.6.29.1";
    public static final int PORT =21;
    public static final String USERNAME = "root";
    public static final String PASSWORD = "icbcgd";
    public static final String REMOTEPATH = "/data/kjbupload/";
//    public static final String LOCATPATH = Environment.getExternalStorageDirectory() + "/Publish/";
    public static final String LOCATPATH = "/mnt/sda/sda1" + "/Publish/";

    //webSocket
    public static final String WEBSOCKETURL= "ws://115.6.29.1:8887/websocket";

    //initurl
    public static final String INITURL = "http://115.6.29.1:9088/servlet/com.icbc.cte.cs.servlet.WithoutSessionReqServlet?flowActionName=apiget1&action=apiget1.flowc";
    public static final int WELCOMEPLAYNUM = 2;

    //欢迎字体
    public static final String FONTTYPEFACE = "dyhb.ttf";
    public static final int WELCOMEVIPCOUNT = 8;
    public static final int WELCOMEMSGTYPE = 0;
    /*0 ------ 只显示姓名
    * 1 ------ 显示姓名和职务
    * 2 ------ 只显示单位
    * */
}
