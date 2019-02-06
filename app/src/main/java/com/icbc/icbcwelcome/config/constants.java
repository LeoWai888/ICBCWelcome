package com.icbc.icbcwelcome.config;

import android.os.Environment;

public class constants {
    //FTP
    public static final String HOST = "115.6.29.1";
    public static final int PORT =21;
    public static final String USERNAME = "root";
    public static final String PASSWORD = "icbcgd";
    public static final String REMOTEPATH = "/data/kjbupload/";
    public static final String LOCATPATH = Environment.getExternalStorageDirectory() + "/Publish/";

    //webSocket
    public static final String WEBSOCKETURL= "ws://115.6.29.1:8887/websocket";

    //initurl
    public static final String INITURL = "http://115.6.29.1:9088/servlet/com.icbc.cte.cs.servlet.WithoutSessionReqServlet?flowActionName=apiget1&action=apiget1.flowc";
    public static final int WELCOMEPLAYNUM = 5;

    //欢迎字体
    public static final String FONTTYPEFACE = "dnmxingshu.ttf";
}
