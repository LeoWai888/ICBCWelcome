package com.icbc.icbcwelcome.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.VideoView;

import com.alibaba.fastjson.JSON;
import com.icbc.icbcwelcome.config.constants;
import com.icbc.icbcwelcome.contract.HomeContract;
import com.icbc.icbcwelcome.json.MsgData;
import com.icbc.icbcwelcome.json.WelcomeData;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View mView;
    private FTPClient client;
    private VideoView videoView;
    //更新的轮播图片列表
    private List<WelcomeData.PicDataBean> imgDataList;
    private int transferringFileCount = 0;

    private String welcomeMsg;
    private int welcomeTime;
    private String rollMsg;
    private int rollDisTime;
    private String rollMsgSendTime;


    private WebSocketClient mSocketClient;

    public HomePresenter(HomeContract.View view) {
        this.mView = view;
        view.setPresenter(this);
    }

    /**
     * 监听文件传输的状态，上传下载时最后一个参数
     */
    private class MyTransferListener implements FTPDataTransferListener {


        // 文件开始上传或下载时触发
        public void started() {
        }

        // 显示已经传输的字节数
        public void transferred(int length) {
        }

        // 文件传输完成时，触发
        public void completed() {
            transferringFileCount = transferringFileCount - 1;
            if (transferringFileCount == 0) {
                mView.hodeLoding();
                mView.updateBanner(imgDataList);
            }
        }

        // 传输放弃时触发
        public void aborted() {
        }

        // 传输失败时触发
        public void failed() {
            transferringFileCount = transferringFileCount - 1;
            if (transferringFileCount == 0) {
                mView.hodeLoding();
                mView.updateBanner(imgDataList);
            }
        }
    }

    /**
     * 监听文件传输的状态，上传下载时最后一个参数
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            WelcomeData imgDataJson = JSON.parseObject(msg.obj.toString(), WelcomeData.class);
            imgDataList = imgDataJson.getPicData();
            imgDataList = sortImgDataList(imgDataList);
            transferringFileCount = imgDataList.size();
            if (transferringFileCount > 0) {
                // TODO: 2019/1/25 下载发布的图片，同时更新banner列表
                downloadFile();
            }
        }
    };

    //websocket
    @Override
    public void initWebSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //TODO 切换URL为自己的IP
                    mSocketClient = new WebSocketClient(new URI(constants.WEBSOCKETURL), new Draft_6455()) {

                        @Override
                        public void onOpen(ServerHandshake handshakedata) {
                            Log.d("picher_log", "run() return:" + "连接到服务器");
                        }

                        @Override
                        public void onMessage(String message) {
                            mView.initVideo();
                            Log.d("picher_log", "接收消息" + message);
                            if (message.contains("\"ISVIP\":1")) {
                                MsgData msgDataJson = JSON.parseObject(message,MsgData.class);
                                mView.popVideoView(msgDataJson,"ISVIP");
                            }else if (message.contains("\"ISBIRTHDAY\":1")){
                                MsgData msgDataJson = JSON.parseObject(message,MsgData.class);
                                mView.popVideoView(msgDataJson,"ISBIRTHDAY");
                            }else {
                                WelcomeData imgDataJson = JSON.parseObject(message, WelcomeData.class);
                                welcomeMsg = imgDataJson.getWelcomeMsg();
                                welcomeTime = imgDataJson.getWelcomeTime();
                                rollMsg = imgDataJson.getRollMsg();
                                rollDisTime =imgDataJson.getRollDisTime();
                                rollMsgSendTime = imgDataJson.getRollMsgSendTime();
                                mView.setICBCWelcomeParam(welcomeMsg,welcomeTime,rollMsg,rollDisTime,rollMsgSendTime);
                                imgDataList = imgDataJson.getPicData();
                                if ((imgDataList != null)&&(imgDataList.size()>0)) {
                                    imgDataList = sortImgDataList(imgDataList);
                                    transferringFileCount = imgDataList.size();
                                    Log.d("welcomeMsg", "onMessage: welcomeMsg is" + welcomeMsg +";" +
                                            "welcomeTime is " + welcomeTime + ";" +
                                            "transferringFileCount is " + transferringFileCount);
                                    if (transferringFileCount>0) {
                                        downloadFile();
                                    }
                                }

                            }

                        }

                        @Override
                        public void onClose(int code, String reason, boolean remote) {
                            Log.d("picher_log", "通道关闭");
                        }

                        @Override
                        public void onError(Exception ex) {
                            Log.d("picher_log", "链接错误");
                        }
                    };
                    mSocketClient.connect();

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void start() {
    }


    @Override
    public void loadBannerData() {
        mView.showLoding();
//        String imgDataStr="{" +
//                "\"picData\": [{" +
//                "\"fileName\": \"1.jpeg\"," +
//                "\"displayTime\": 5," +
//                "\"displayOrder\": 1" +
//                "}," +
//                "{" +
//                "\"fileName\": \"2.jpeg\"," +
//                "\"displayTime\": 5," +
//                "\"displayOrder\": 2" +
//                "}," +
//                "{" +
//                "\"fileName\": \"3.jpeg\"," +
//                "\"displayTime\": 5," +
//                "\"displayOrder\": 3" +
//                "}," +
//                "{" +
//                "\"fileName\": \"4.jpeg\"," +
//                "\"displayTime\": 5," +
//                "\"displayOrder\": 4" +
//                "}," +
//                "{" +
//                "\"fileName\": \"5.jpeg\"," +
//                "\"displayTime\": 5," +
//                "\"displayOrder\": 5" +
//                "}," +
//                "{" +
//                "\"fileName\": \"6.jpeg\"," +
//                "\"displayTime\": 5," +
//                "\"displayOrder\": 6" +
//                "}," +
//                "{" +
//                "\"fileName\": \"7.jpeg\"," +
//                "\"displayTime\": 5," +
//                "\"displayOrder\": 7" +
//                "}" +
//                "]" +
//                "}";
        String url = constants.INITURL;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                转圈结束
                mView.hodeLoding();
                Log.d("ICBCWelcome", "onFailure: 系统初始化失败！");
                //弹出错误提示窗口
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr = response.body().string();  //playTime:0是日常播放、1是插播、2是只播
                String tempResponseStr="{'picData':[{'fileName':'2019052460136.三级保障.jpg','displayTime':10,'startTime':'00:00','endTime':'00:00','playType':'0','displayOrder':2},{'fileName':'2019052144834.全国金融五一劳动奖章（范小钢）.jpg','displayTime':10,'startTime':'2019-05-31 17:55','endTime':'2019-06-30 18:34','playType':'1','displayOrder':1},{'fileName':'201905292310.林伟退休.jpg','displayTime':10,'startTime':'2019-05-31 17:55','endTime':'2019-6-01 18:35','playType':'2','displayOrder':3}],'welcomeMsg':'热烈欢迎保卫部XXX总经理莅临我部指导','welcomeTime':2,'vipSendTime':'2019-04-24 09:40:05','msgSendTime':'2019-05-29 17:45:50','rollMsgSendTime':'2019-05-23 14:40:51','rollDisTime':12,'rollMsg':'架构师交流培训系列之 融e行业务群组拆分实践（5月23日周四 18：20开始）（金融科技部五楼视频会议室）'}";
                Log.d("ICBCWelcome", "onResponse: " + responseStr);
                WelcomeData imgDataJson = JSON.parseObject(tempResponseStr, WelcomeData.class);  //生产网将tempResponseStr改为responseStr

                welcomeMsg = imgDataJson.getWelcomeMsg();
                welcomeTime = imgDataJson.getWelcomeTime();
//                rollMsg =imgDataJson.getRollMsg();
//                rollDisTime =imgDataJson.getRollDisTime();
//                rollMsgSendTime = imgDataJson.getRollMsgSendTime();
                mView.setICBCWelcomeParam(welcomeMsg,welcomeTime,rollMsg,rollDisTime,rollMsgSendTime);
                imgDataList = imgDataJson.getPicData();
                imgDataList = sortImgDataList(imgDataList);
                transferringFileCount = imgDataList.size();
                downloadFile();
            }
        });

    }

    //对轮播图片列表进行排序
    private List<WelcomeData.PicDataBean> sortImgDataList(List<WelcomeData.PicDataBean> imgList) {
        Collections.sort(imgList, new Comparator<WelcomeData.PicDataBean>() {
            /*
             * int compare(PicData.PicDataBean p1, PicData.PicDataBean p2) 返回一个基本类型的整型，
             * 返回负数表示：p1 小于p2，
             * 返回0 表示：p1和p2相等，
             * 返回正数表示：p1大于p2
             */
            public int compare(WelcomeData.PicDataBean p1, WelcomeData.PicDataBean p2) {
                //按照DisplayOrder对列表进行升序排列
                if (p1.getDisplayOrder() > p2.getDisplayOrder()) {
                    return 1;
                }
                if (p1.getDisplayOrder() == p2.getDisplayOrder()) {
                    return 0;
                }
                return -1;
            }
        });
        return imgList;
    }

    private void downloadFile() {
        client = new FTPClient();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.connect(constants.HOST, constants.PORT);
                    client.login(constants.USERNAME, constants.PASSWORD);
//                    Log.d("ftpClient", client.getCharset());
                    client.setCharset("GBK");
                    client.setType(FTPClient.TYPE_BINARY);
                    Log.d("ftpClient", client.getCharset());
                    client.changeDirectory(constants.REMOTEPATH);

                    String dir = constants.LOCATPATH;

                    File fileDir = new File(dir);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    for (WelcomeData.PicDataBean picFile : imgDataList) {
                        final File file = new File(String.valueOf(dir + picFile.getFileName().replace(".jpg",".jpeg")));
                        Log.d("ftpClient", picFile.getFileName());
                        client.download(picFile.getFileName(), file, new MyTransferListener());
                    }
                } catch (Exception e) {
                    Log.d("download", "run: " +e);
                    return;
                }
            }
        }).start();
    }
}

