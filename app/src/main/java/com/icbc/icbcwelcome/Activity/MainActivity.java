package com.icbc.icbcwelcome.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.icbc.icbcwelcome.R;
import com.icbc.icbcwelcome.base.BaseActivity;
import com.icbc.icbcwelcome.config.constants;
import com.icbc.icbcwelcome.contract.HomeContract;
import com.icbc.icbcwelcome.json.PicData;
import com.icbc.icbcwelcome.presenter.HomePresenter;


import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

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
    private ImageView showImg;
    private AlertDialog mDialog;//等待对话框
    private List<PicData.PicDataBean> picDatalist = new ArrayList<>();
    private HomeContract.Presenter mPresenter;

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
        setContentView(R.layout.activity_main);
        mPresenter = new HomePresenter(this);
        banner = (Banner) findViewById(R.id.banner);
        initView();
    }


    private void initView() {
        mPresenter.loadBannerData();
        initBanner();
    }

    public void initBanner() {
        List<String> list = new ArrayList<>();
        list.add(constants.LOCATPATH + "loading.jpg");
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        banner.setDelayTime(5000)
                .setImages(list)
                .setImageLoader(new GlideImageLoader())
                .start();
    }

    public void updateBanner(List<PicData.PicDataBean>  picDatalist) {
        //
        List<String> list = new ArrayList<>();
        for (PicData.PicDataBean pic : picDatalist) {
            list.add(constants.LOCATPATH + pic.getFileName());
        }
        banner.stopAutoPlay();
        banner.update(list);
        banner.startAutoPlay();
    }
}
