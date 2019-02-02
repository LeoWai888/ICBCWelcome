package com.icbc.icbcwelcome.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.icbc.icbcwelcome.R;
import com.icbc.icbcwelcome.base.BaseActivity;
import com.icbc.icbcwelcome.config.constants;
import com.icbc.icbcwelcome.contract.HomeContract;
import com.icbc.icbcwelcome.json.PicData;
import com.icbc.icbcwelcome.presenter.HomePresenter;


import com.icbc.icbcwelcome.util.CustomVideoView;
import com.icbc.icbcwelcome.util.ShineTextView;
import com.icbc.icbcwelcome.util.StrokeTextView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
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
    private HomeContract.Presenter mPresenter;
    int playNum = 0;

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showLoding() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("BACS", "mDialog.show()");
                mDialog.show();
            }
        });
    }

    @Override
    public void hodeLoding() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("BACS", "mDialog.dismiss()");
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
        videoView=(CustomVideoView) findViewById(R.id.vedio_welcome);
        welcomeRL=(RelativeLayout)findViewById(R.id.welcomeRL);
        tvWelcomeText = (ShineTextView)findViewById(R.id.tvWelComeText);
        AssetManager assetManager = this.getApplicationContext().getAssets();
        Typeface mtypeface=Typeface.createFromAsset(assetManager,constants.FONTTYPEFACE);
        tvWelcomeText.setTypeface(mtypeface);
        bannnerImgList = new ArrayList<>();
        initView();
        mPresenter.initWebSocket();
        popWelcomeView();
    }


    private void popWelcomeView(){
        String uri = constants.LOCATPATH + "welcome.mp4";
        videoView.setVideoPath(uri);
        videoView.setVideoURI(Uri.parse(uri));
        videoView.requestFocus();
        videoView.start();
        banner.setVisibility(View.GONE);
        welcomeRL.setVisibility(View.VISIBLE);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNum +=1;
                String uri = constants.LOCATPATH + "welcome.mp4";
                videoView.setVideoPath(uri);
                videoView.requestFocus();
                videoView.start();
                if (playNum==constants.WELCOMEPLAYNUM){
                    videoView.stopPlayback();
                    videoView.suspend();
                    welcomeRL.setVisibility(View.GONE);
                    banner.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initView() {
        mPresenter.loadBannerData();
        initBanner();
    }

    public void initBanner() {
        bannnerImgList.add(constants.LOCATPATH+"loading.jpeg");

        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        banner.setDelayTime(5000)
                .setImages(bannnerImgList)
                .setImageLoader(new GlideImageLoader())
                .start();
    }

    public void updateBanner(List<PicData.PicDataBean>  picDatalist) {
        bannnerImgList.clear();
        for (PicData.PicDataBean pic : picDatalist) {
            bannnerImgList.add(constants.LOCATPATH + pic.getFileName());
        }

        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
                              banner.setDelayTime(5000)
                                      .setImages(bannnerImgList)
                                      .setImageLoader(new GlideImageLoader())
                                      .start();
                          }
                      });
    }
}
