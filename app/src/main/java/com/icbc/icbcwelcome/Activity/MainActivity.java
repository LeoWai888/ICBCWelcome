package com.icbc.icbcwelcome.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.icbc.icbcwelcome.R;
import com.icbc.icbcwelcome.base.BaseActivity;
import com.icbc.icbcwelcome.config.constants;
import com.icbc.icbcwelcome.contract.HomeContract;
import com.icbc.icbcwelcome.json.VipData;
import com.icbc.icbcwelcome.json.WelcomeData;
import com.icbc.icbcwelcome.presenter.HomePresenter;
import com.icbc.icbcwelcome.util.CustomVideoView;
import com.icbc.icbcwelcome.util.ShineTextView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MainActivity extends BaseActivity implements HomeContract.View {

    private class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .into(imageView);
        }
    }

    Banner banner;
    private CustomVideoView videoView;
    private ShineTextView tvWelcomeText;
    private RelativeLayout welcomeRL;
    private AlertDialog mDialog;//等待对话框
    private List<String> bannnerImgList;
    private List<Integer> bannerPlayTime;
    private List<VipData> vipPeopleList;
    private HomeContract.Presenter mPresenter;
    private int playNum = 0;
    private String welcomeMsg = "欢迎XXX莅临指导";
    private String showText = "";
    private int welcomeTime;

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showLoding() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("ICBCWelcome", "mDialog.show()");
                mDialog.show();
            }
        });
    }

    @Override
    public void hodeLoding() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("ICBCWelcome", "mDialog.dismiss()");
                mDialog.dismiss();
            }
        });
    }


    ///////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);   //连FTP测试

        mDialog = new SpotsDialog(this);
        mDialog.getWindow().setGravity(Gravity.CENTER);
        mPresenter = new HomePresenter(this);
        banner = (Banner) findViewById(R.id.banner);
        videoView = (CustomVideoView) findViewById(R.id.vedio_welcome);
        welcomeRL = (RelativeLayout) findViewById(R.id.welcomeRL);
        tvWelcomeText = (ShineTextView) findViewById(R.id.tvWelComeText);
        AssetManager assetManager = this.getApplicationContext().getAssets();
        Typeface mtypeface = Typeface.createFromAsset(assetManager, constants.FONTTYPEFACE);
        tvWelcomeText.setTypeface(mtypeface);
        bannnerImgList = new ArrayList<>();
        bannerPlayTime = new ArrayList<>();
        vipPeopleList = new ArrayList<>();
        welcomeTime = 30;
        welcomeMsg = "热烈欢迎XXX莅临我部指导工作";
        initView();
        mPresenter.initWebSocket();
    }


    /*欢迎屏弹出欢迎视频，播放次数可以参数化控制*/
    public void popWelcomeView(VipData vipDataJson) {
        if (!showText.equals(welcomeMsg.replace("XXX", addVisitorList(vipDataJson)))) {
            showText = welcomeMsg.replace("XXX", addVisitorList(vipDataJson));
            playNum = 0;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String uri = constants.LOCATPATH + "welcome.mp4";
                    videoView.setVideoPath(uri);
                    videoView.setVideoURI(Uri.parse(uri));
                    videoView.requestFocus();
                    videoView.start();
                    banner.setVisibility(View.GONE);
                    welcomeRL.setVisibility(View.VISIBLE);
                    tvWelcomeText.setText(showText);
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            playNum += 1;
                            String uri = constants.LOCATPATH + "welcome.mp4";
                            videoView.setVideoPath(uri);
                            videoView.requestFocus();
                            videoView.start();
                            if (playNum > constants.WELCOMEPLAYNUM) {
                                videoView.stopPlayback();
                                videoView.suspend();
                                welcomeRL.setVisibility(View.GONE);
                                banner.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            });
        }
    }


    private void initView() {
        initBanner();
        mPresenter.loadBannerData();
    }

    public void initBanner() {
        bannnerImgList.add(constants.LOCATPATH + "loading.jpeg");
//        Toast.makeText(this,getApplicationContext().getFilesDir().getAbsolutePath(),Toast.LENGTH_LONG).show();;
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        banner.setDelayTime(5000)
                .setImages(bannnerImgList)
                .setImageLoader(new GlideImageLoader())
                .start();

    }

    public void updateBanner(List<WelcomeData.PicDataBean> picDatalist,
                             String welMsg,
                             int welTime) {
        bannnerImgList.clear();
        if (bannerPlayTime.size() > 0 && bannerPlayTime != null && !bannerPlayTime.isEmpty()) {
            bannerPlayTime.clear();
        }
        if (welMsg != null && welMsg != "") {
            welcomeMsg = welMsg;
            welcomeTime = welTime;
        }
        if (picDatalist != null) {
            for (WelcomeData.PicDataBean pic : picDatalist) {
                bannnerImgList.add(constants.LOCATPATH + pic.getFileName().replace(".jpg",".jpeg"));
                bannerPlayTime.add(pic.getDisplayTime());
            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
                    banner.setDelayTime(bannerPlayTime.get(0) * 1000)
                            .setImages(bannnerImgList)
                            .setImageLoader(new GlideImageLoader())
                            .start();
                    banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        }

                        @Override
                        public void onPageSelected(int position) {
                            Log.d("onPageSelected", String.valueOf(bannerPlayTime.get(position) * 1000));
                            banner.setDelayTime(bannerPlayTime.get(position) * 1000);
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {
                        }
                    });
                }
            });
        }
    }

    private long diffTwoDate(String fristDate, String secondDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(fristDate);
            Date d2 = df.parse(secondDate);
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);

            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            return minutes;
        } catch (Exception e) {
            return 0;
        }
    }

    private String addVisitorList(VipData vipDataJson) {
        boolean isInserted = false;
        if (vipPeopleList != null) {
            for (int j = 0; j < vipPeopleList.size(); j++) //判断列表内访客是否超过规定时间（10分钟）
            {
                if (vipPeopleList.get(j).getUSERID().equals(vipDataJson.getUSERID())) //判断是否是同一个人，同一个人数据更新
                {
                    vipPeopleList.remove(j); //删除元素
                    j = j - 1;
                    continue;
                }

                if (diffTwoDate(vipDataJson.getVIPTIME(), vipPeopleList.get(j).getVIPTIME()) > welcomeTime) //大于10分钟
                {
                    vipPeopleList.remove(j); //删除元素（后面追加）
                    j = j - 1;
                    showText = "";
                    continue;
                }

                //判断是否应该插入list,判断职级的高低.2018/10/19
                if (!isInserted && vipPeopleList.get(j).getUSERLEVEL() <
                        vipDataJson.getUSERLEVEL()) {

                    vipPeopleList.add(j, vipDataJson);
                    isInserted = true;
                }
            }
        }
        if (!isInserted) {
            vipPeopleList.add(vipDataJson);
        }
        int chosenVisitorCount = constants.WELCOMEVIPCOUNT; //只播放最多前8个,2018/10/19
        if (vipPeopleList.size() < 8) {
            chosenVisitorCount = vipPeopleList.size();
        }
        String visitorStr = "";
        for (int i = 0; i < chosenVisitorCount; i++) {
            if (constants.WELCOMEMSGTYPE == 0)
                visitorStr = visitorStr + vipPeopleList.get(i).getUSERNAME() + "、";//只有姓名2018/10/19
            if (constants.WELCOMEMSGTYPE == 1)
                visitorStr = visitorStr + vipPeopleList.get(i).getUSERNAME() + vipPeopleList.get(i).getTITLE() + "、";//姓名＋职务
        }

        if (!visitorStr.equals("")) {
            visitorStr = visitorStr.substring(0, visitorStr.lastIndexOf("、"));
        }
        return visitorStr;
    }
}
