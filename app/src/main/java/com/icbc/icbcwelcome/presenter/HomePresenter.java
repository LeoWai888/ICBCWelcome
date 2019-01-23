package com.icbc.icbcwelcome.presenter;


import android.os.Environment;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.icbc.icbcwelcome.Activity.MainActivity;
import com.icbc.icbcwelcome.contract.HomeContract;
import com.icbc.icbcwelcome.json.PicData;

import java.io.File;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPClient;
import com.icbc.icbcwelcome.config.constants;


public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View mView;


    @Override
    public void start() {

    }


    public HomePresenter(MainActivity view) {
        this.mView = view;
        view.setPresenter(this);
    }


    @Override
    public List<PicData.PicDataBean> loadBannerData() {
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
        List<PicData.PicDataBean> imgDataList = imgDataJson.getPicData();
        downloadFile(imgDataList);
        mView.initBanner(imgDataList);
        return imgDataList;
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
                client.download(picFile.getFileName(), file);
            }
        } catch (Exception e) {
            return;
        }
    }
}

