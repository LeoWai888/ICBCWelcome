package com.icbc.icbcwelcome.config;

import android.os.Environment;
import android.util.Log;

import com.icbc.icbcwelcome.R;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class constants {
    //FTP
    public static final String HOST = "115.4.109.195";
    public static final int PORT = 21;
    public static final String USERNAME = "root";
    public static final String PASSWORD = "icbcgd";
    public static final String REMOTEPATH = "/datafs/GD/webserver/applications/FACEDEAL/static/test/";
    public static final String LOCATPATH = Environment.getExternalStorageDirectory() + "/test/";
    //WebSocket
    public WebSocketClient mSocketClient;
    public static final String URI="ws://115.4.109.195:8889/realtime/mygroup";




}
