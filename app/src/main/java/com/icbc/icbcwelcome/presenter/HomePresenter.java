package com.icbc.icbcwelcome.presenter;


import android.os.Environment;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.icbc.icbcwelcome.Activity.MainActivity;
import com.icbc.icbcwelcome.contract.HomeContract;
import com.icbc.icbcwelcome.json.PicData;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import com.icbc.icbcwelcome.config.constants;


public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View mView;

    //正在传输的文件数量
    private int transferringFileCount = 0;
    //更新的轮播图片列表
    private  List<PicData.PicDataBean> imgDataList;
    /**
     * 监听文件传输的状态，上传下载时最后一个参数
     * @说明
     * @author cuisuqiang
     * @version 1.0
     * @since
     */
    private class MyTransferListener implements FTPDataTransferListener{

        // 文件开始上传或下载时触发
        public void started() {
            transferringFileCount = transferringFileCount ++;
        }
        // 显示已经传输的字节数
        public void transferred(int length) {
        }
        // 文件传输完成时，触发
        public void completed() {
            transferringFileCount = transferringFileCount --;
            if (transferringFileCount == 0){
                //所有文件传输完成，MainActive启动轮播
                mView.updateBanner(imgDataList);
            }
        }
        // 传输放弃时触发
        public void aborted() {
        }
        // 传输失败时触发
        public void failed() {
            transferringFileCount = transferringFileCount --;
            if (transferringFileCount == 0){
                //所有文件传输完成，MainActive启动轮播
            }
        }
    }

    @Override
    public void start() {
    }

    public HomePresenter(MainActivity view) {
        this.mView = view;
        view.setPresenter(this);
    }

    @Override
    public void loadBannerData() {
        String imgDataStr="{" +
                "\"picData\": [{" +
                "\"fileName\": \"1.jpg\"," +
                "\"displayTime\": 5," +
                "\"displayOrder\": 1" +
                "}," +
                "{" +
                "\"fileName\": \"2.jpg\"," +
                "\"displayTime\": 5," +
                "\"displayOrder\": 2" +
                "}," +
                "{" +
                "\"fileName\": \"3.jpg\"," +
                "\"displayTime\": 5," +
                "\"displayOrder\": 3" +
                "}," +
                "{" +
                "\"fileName\": \"4.jpg\"," +
                "\"displayTime\": 5," +
                "\"displayOrder\": 4" +
                "}," +
                "{" +
                "\"fileName\": \"5.jpg\"," +
                "\"displayTime\": 5," +
                "\"displayOrder\": 5" +
                "}," +
                "{" +
                "\"fileName\": \"6.jpg\"," +
                "\"displayTime\": 5," +
                "\"displayOrder\": 6" +
                "}," +
                "{" +
                "\"fileName\": \"7.jpg\"," +
                "\"displayTime\": 5," +
                "\"displayOrder\": 7" +
                "}" +
                "]" +
                "}";
        PicData imgDataJson = JSON.parseObject(imgDataStr, PicData.class);
        imgDataList = imgDataJson.getPicData();
        imgDataList = sortImgDataList(imgDataList);
        downloadFile(imgDataList);
    }
//对轮播图片列表进行排序
    private List<PicData.PicDataBean> sortImgDataList(List<PicData.PicDataBean> imgList)
    {
        Collections.sort(imgList, new Comparator<PicData.PicDataBean>(){
            /*
             * int compare(PicData.PicDataBean p1, PicData.PicDataBean p2) 返回一个基本类型的整型，
             * 返回负数表示：p1 小于p2，
             * 返回0 表示：p1和p2相等，
             * 返回正数表示：p1大于p2
             */
            public int compare(PicData.PicDataBean p1, PicData.PicDataBean p2) {
                //按照DisplayOrder对列表进行升序排列
                if(p1.getDisplayOrder() > p2.getDisplayOrder()){
                    return 1;
                }
                if(p1.getDisplayOrder() == p2.getDisplayOrder()){
                    return 0;
                }
                return -1;
            }
        });
        return imgList;
    }
    private void downloadFile(List<PicData.PicDataBean> fileList)
    {
        FTPClient client;
        client = new FTPClient();
        try {
            client.connect(constants.HOST, constants.PORT);
            client.login(constants.USERNAME, constants.PASSWORD);
            client.changeDirectory(constants.REMOTEPATH);
            /////////文件下载
            String dir = constants.LOCATPATH;

            File fileDir = new File(dir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            for ( PicData.PicDataBean picFile:fileList ) {
                final File file = new File(String.valueOf(dir + picFile.getFileName()));
                client.download(picFile.getFileName(), file,new MyTransferListener());
            }
        } catch (Exception e) {
            return;
        }
    }
}

