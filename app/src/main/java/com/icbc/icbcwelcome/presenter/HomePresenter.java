package com.icbc.icbcwelcome.presenter;

import android.os.Handler;
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
    private FTPClient client;
    //更新的轮播图片列表
    private  List<PicData.PicDataBean> imgDataList;
    private int transferringFileCount = 0;

    /**
     * 监听文件传输的状态，上传下载时最后一个参数
     */
    private class MyTransferListener implements FTPDataTransferListener{


        // 文件开始上传或下载时触发
        public void started() {
//            transferringFileCount = transferringFileCount --;
        }
        // 显示已经传输的字节数
        public void transferred(int length) {
        }
        // 文件传输完成时，触发
        public void completed() {
            transferringFileCount = transferringFileCount -1 ;
            if (transferringFileCount==0){
                mView.hodeLoding();
                mView.updateBanner(imgDataList);
            }
        }
        // 传输放弃时触发
        public void aborted() {
        }
        // 传输失败时触发
        public void failed() {
            transferringFileCount = transferringFileCount -1 ;
            if (transferringFileCount==0){
                mView.hodeLoding();
                mView.updateBanner(imgDataList);
            }
        }
    }

    /**
     * 监听文件传输的状态，上传下载时最后一个参数
     */
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Model model;
            super.handleMessage(msg);
            SignJson signJson= JSON.parseObject(msg.obj.toString(),SignJson.class);
            if(signJson!=null) {
                model = new Model();
                model.setTitle(signJson.getName().toString()+"   "+signJson.getUserDept());
                model.setContent(signJson.getCompareTime().toString());
                model.setImgBase64(signJson.getCardImg());
                datas.add(0,model);   //数据添加到0位置
//                changeImg(signJson.getCardImg());  //显示照片

//                Bitmap bitmap;
//                byte[] bitmapArray;
//                bitmapArray = Base64.decode(signJson.getCardImg(), Base64.DEFAULT);
//                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
//                mImg.setImageBitmap(bitmap);

                number();    //数字变化
//                ObjectAnimator animator=ObjectAnimator.ofFloat(mAddimg,"alpha",7f,0f);
//                mAddimg.setVisibility(View.VISIBLE);   //显示+1图片
//                animator.setDuration(500);
//                animator.start();
                adapter.notifyDataSetChanged();   //更新listview并显示
            }
        }
    };

    @Override
    public void start() {
    }

    public HomePresenter(MainActivity view) {
        this.mView = view;
        view.setPresenter(this);
    }

    @Override
    public void loadBannerData() {
        mView.showLoding();
        String imgDataStr="{" +
                "\"picData\": [{" +
                "\"fileName\": \"1.jpeg\"," +
                "\"displayTime\": 5," +
                "\"displayOrder\": 1" +
                "}," +
                "{" +
                "\"fileName\": \"2.jpeg\"," +
                "\"displayTime\": 5," +
                "\"displayOrder\": 2" +
                "}," +
                "{" +
                "\"fileName\": \"3.jpeg\"," +
                "\"displayTime\": 5," +
                "\"displayOrder\": 3" +
                "}," +
                "{" +
                "\"fileName\": \"4.jpeg\"," +
                "\"displayTime\": 5," +
                "\"displayOrder\": 4" +
                "}," +
                "{" +
                "\"fileName\": \"5.jpeg\"," +
                "\"displayTime\": 5," +
                "\"displayOrder\": 5" +
                "}," +
                "{" +
                "\"fileName\": \"6.jpeg\"," +
                "\"displayTime\": 5," +
                "\"displayOrder\": 6" +
                "}," +
                "{" +
                "\"fileName\": \"7.jpeg\"," +
                "\"displayTime\": 5," +
                "\"displayOrder\": 7" +
                "}" +
                "]" +
                "}";
        PicData imgDataJson = JSON.parseObject(imgDataStr, PicData.class);
        imgDataList = imgDataJson.getPicData();
        imgDataList = sortImgDataList(imgDataList);
        transferringFileCount = imgDataList.size();
        downloadFile();
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
    private void downloadFile()
    {
        client = new FTPClient();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.connect(constants.HOST, constants.PORT);
                    client.login(constants.USERNAME, constants.PASSWORD);
                    client.changeDirectory(constants.REMOTEPATH);
                    String dir = constants.LOCATPATH;

                    File fileDir = new File(dir);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    for ( PicData.PicDataBean picFile:imgDataList ) {
                        final File file = new File(String.valueOf(dir + picFile.getFileName()));
                        client.download(picFile.getFileName(), file,new MyTransferListener());
                    }
                } catch (Exception e) {
                    return;
                }
            }
        }).start();
    }
}

