package com.icbc.icbcwelcome.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.icbc.icbcwelcome.R;
import com.icbc.icbcwelcome.base.BaseActivity;
import com.icbc.icbcwelcome.config.bgPrama;
import com.icbc.icbcwelcome.config.constants;
import com.icbc.icbcwelcome.contract.HomeContract;
import com.icbc.icbcwelcome.json.FestivalListBean;
import com.icbc.icbcwelcome.json.MsgData;
import com.icbc.icbcwelcome.json.WelcomeData;
import com.icbc.icbcwelcome.presenter.HomePresenter;
import com.icbc.icbcwelcome.util.CustomVideoView;
import com.icbc.icbcwelcome.util.ShineTextView;
import com.icbc.icbcwelcome.util.checks.ShapeRevealSample;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;
import su.levenetc.android.textsurface.TextSurface;


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
    private List<String> bannerStartTime;
    private List<String> bannerEndTime;
    private List<String> bannerPlayType;  //0:日常播，1：插播，2：只播放
    private List<MsgData> vipPeopleList;
    private List<MsgData> birthPeopleList;
    private HomeContract.Presenter mPresenter;
    private int playNum = 0;
    private String welcomeMsg = "欢迎XXX莅临指导";
    private String birthDayMsg = "";
    private String rollMsg = "";
    private int rollDipTime;
    private String rollMsgSendTime;
    private String showText = "";
    private int welcomeTime;
    private TextSurface textBirthday;
    private TextView rollTextView;
    private RelativeLayout rollRL;
    private bgPrama BgPrama;
    private String VideoPath;           //节日祝福视频路径
    private String VideoContextColor;  //节日祝福内容字体颜色
    private String VideoContextfont;  //节日祝福内容字体
    private FestivalListBean VideoBean;
    private long time;  //系统当前时间
    private List<WelcomeData.PicDataBean> tempDatalist;
    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    //时刻循环
    public class TimeThread extends  Thread{
        @Override
        public void run(){
            time=0L;
            do{
                time=System.currentTimeMillis();
                if (tempDatalist != null) {
                    List<String> threadImgList=new ArrayList<>();
                    List<Integer> threadPlayTime=new ArrayList<>();
                    List<String> threadStartTime=new ArrayList<>();
                    List<String> threadEndTime=new ArrayList<>();
                    List<String> threadPlayType=new ArrayList<>();
                    for (WelcomeData.PicDataBean pic : tempDatalist) {
                        threadImgList.add(constants.LOCATPATH + pic.getFileName().replace(".jpg",".jpeg"));
                        threadPlayTime.add(pic.getDisplayTime());
                        threadStartTime.add(pic.getStartTime());
                        threadEndTime.add(pic.getEndTime());
                        threadPlayType.add(pic.getPlayType());
                    }
//                    Log.e("transForm00:","threadPlayType.size():"+threadPlayType.size()+" / threadStartTime.size:"+threadPlayType.size()+"  /bannerEndTime.size"+threadPlayType.size());
                    for(int j=0;j<threadPlayType.size();j++)
                    {
                        if(!threadPlayType.get(j).equals("0")){  //非日常
                            long startTime = transTimeForm(threadStartTime.get(j));
                            long endTime = transTimeForm(threadEndTime.get(j));
//                            Log.e("time:", "startTime:"+startTime+"  /time:"+time+"  /endTime:"+endTime);
                            if((startTime < time) && (time < startTime+10) ){
                                updateBanner(tempDatalist);
                                Log.e("enter:","start");
                            }

                            if((endTime< time) && (time < endTime+10) ){
                                updateBanner(tempDatalist);
                                Log.e("enter:","end");
                            }
                        }
                    }
//
                }
            }while (true);
        }
    }

    @Override
    public void showLoding() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Log.d("ICBCWelcome", "mDialog.show()");
                mDialog.show();
            }
        });
    }

    @Override
    public void hodeLoding() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Log.d("ICBCWelcome", "mDialog.dismiss()");
                mDialog.dismiss();
            }
        });
    }


    ///////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time=System.currentTimeMillis();
        mDialog = new SpotsDialog(this);
        mDialog.getWindow().setGravity(Gravity.CENTER);
        mPresenter = new HomePresenter(this);
        banner = (Banner) findViewById(R.id.banner);
        videoView = (CustomVideoView) findViewById(R.id.vedio_welcome);
        welcomeRL = (RelativeLayout) findViewById(R.id.welcomeRL);
        tvWelcomeText = (ShineTextView) findViewById(R.id.tvWelComeText);
        textBirthday = (TextSurface) findViewById(R.id.birthday_text);

        rollTextView=(TextView)findViewById(R.id.roll_tv);
        rollRL=(RelativeLayout)findViewById(R.id.roll_Rl);

        AssetManager assetManager = this.getApplicationContext().getAssets();
        Typeface mtypeface = Typeface.createFromAsset(assetManager, constants.FONTTYPEFACE);
        tvWelcomeText.setTypeface(mtypeface);
        bannnerImgList = new ArrayList<>();
        bannerPlayTime = new ArrayList<>();
        bannerStartTime=new ArrayList<>();
        bannerEndTime=new ArrayList<>();
        bannerPlayType=new ArrayList<>();
        vipPeopleList = new ArrayList<>();
        birthPeopleList = new ArrayList<>();
        welcomeTime = 10;
        welcomeMsg = "热烈欢迎XXX莅临我部指导工作";
        initView();
//        initVideo();
        mPresenter.initWebSocket();
//        Log.d("welcomeMsg", "onCreate: " + vipPeopleList.size());
        startMinuteThread();
    }

    private void startMinuteThread() {
        new TimeThread().start();
    }

    public void initVideo()
    {
        //初始化节日视频背景、字体、字体颜色
        BgPrama=new bgPrama(this);
        VideoBean=BgPrama.bgPramaSetting(constants.LOCATPATH + "Static/birthday/");
        VideoPath=VideoBean.getMVPath();
        VideoContextColor=VideoBean.getTxtColor();
        VideoContextfont=VideoBean.getFontsPath();
    }

    /*欢迎屏弹出欢迎视频，播放次数可以参数化控制*/
    public void popVideoView(MsgData _msgDataJson,String msgType) {
//        Log.d("welcomeMsg", "welcomeMsg: " + welcomeMsg);

        if (msgType.equals("ISVIP")){
            String vipPeople = addVisitorList(_msgDataJson);
            if (!showText.equals(welcomeMsg.replace("XXX", vipPeople))) {
                showText = welcomeMsg.replace("XXX", vipPeople);
//                Log.d("welcomeMsg", "showText: " + showText);
                playVideo(msgType);
            }

            onRemoveMsgs(R.id.init_welcome_state);
            onSendMsgDelayed(R.id.init_welcome_state, null, 60000*welcomeTime);
        }else if (msgType.equals("ISBIRTHDAY")){
            birthDayMsg = addBirthPeopleList(_msgDataJson);
            playVideo(msgType);
        }
    }

    private String addBirthPeopleList(MsgData __birthDataJson) {
        boolean isInserted = false;
        if ((birthPeopleList != null)&&(birthPeopleList.size()>0)) {
            int idx=0;
            for (MsgData birthDayPeopleItem:birthPeopleList){
                if (birthDayPeopleItem.getUSERID().equals(__birthDataJson.getUSERID())){
                    birthPeopleList.remove(birthDayPeopleItem);
//                    Log.d("birthdayMsg", "addBirthPeopleList: remove "+birthDayPeopleItem.getUSERNAME() );
                    continue;
                }
//                Log.d("birthdayMsg", "addBirthPeopleList: difftime is " + diffTwoDate(__birthDataJson.getVIPTIME(),birthDayPeopleItem.getBIRTHDAYSENDTIME()) );
                if (diffTwoDate(__birthDataJson.getVIPTIME(),birthDayPeopleItem.getBIRTHDAYSENDTIME()) > welcomeTime) //大于设定好的时间
                {
                    birthPeopleList.remove(birthDayPeopleItem); //删除元素（后面追加）
//                    Log.d("birthdayMsg", "addBirthPeopleList: remove "+birthDayPeopleItem.getUSERNAME() + "; time :" +  birthDayPeopleItem.getBIRTHDAYSENDTIME());
                    continue;
                }
            }
        }
        if (!isInserted) {
            birthPeopleList.add(__birthDataJson);
//            Log.d("birthdayMsg", "addBirthPeopleList: insert " + __birthDataJson.getUSERNAME() + "; time:"+__birthDataJson.getBIRTHDAYSENDTIME());
        }

        String birthDayPeopleNameStr = "";
        for (int i = 0; i < birthPeopleList.size(); i++) {
            birthDayPeopleNameStr = birthDayPeopleNameStr + birthPeopleList.get(i).getUSERNAME() + "、";//只有姓名2018/10/19
        }

        if (!birthDayPeopleNameStr.equals("")) {
            birthDayPeopleNameStr = birthDayPeopleNameStr.substring(0, birthDayPeopleNameStr.lastIndexOf("、"));
        }
//        Log.d("birthdayMsg", "addVisitorList: msg is " + birthDayPeopleNameStr);
        return birthDayPeopleNameStr;
    }

    public void setICBCWelcomeParam(String _welcomeMsg,
                                    int _welcomeTime,
                                    String _rollMsg,
                                    int _rollTime,
                                    String _rollMsgSendTime){
        if (_welcomeMsg != null && !_welcomeMsg.equals("")) {
            welcomeMsg = _welcomeMsg;
            welcomeTime = _welcomeTime;
        }
        if (_rollMsg != null && !_rollMsg.equals("")){
            rollDipTime=_rollTime;
            rollMsg = _rollMsg;
            rollMsgSendTime = _rollMsgSendTime;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rollRL.setVisibility(View.VISIBLE);
                    rollTextView.setVisibility(View.VISIBLE);
                    rollTextView.setText(rollMsg);
                    rollTextView.setSelected(true);
                }
            });
            onRemoveMsgs(R.id.init_rollMsg_state);
            onSendMsgDelayed(R.id.init_rollMsg_state, null, 60000*rollDipTime);

        }
    }

    private void playVideo(final String msgType){
        playNum = 0;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String uri="";
                if (msgType.equals("ISBIRTHDAY")) {
                    uri=VideoPath;
                    Log.e("video uri: ",uri);
                }else {
                    uri = constants.LOCATPATH + "Static/welcome.mp4";
                    Log.e("video uri: ",uri);
                }
                videoView.setVideoPath(uri);
                videoView.setVideoURI(Uri.parse(uri));
                videoView.requestFocus();
                videoView.start();
                banner.setVisibility(View.GONE);
                welcomeRL.setVisibility(View.VISIBLE);
                if (msgType.equals("ISBIRTHDAY")){
                    initBirthDayView();
                    //todo 显示生日字幕
                }else {
                    tvWelcomeText.setVisibility(View.VISIBLE);
                    tvWelcomeText.setText(showText);
                }
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playNum += 1;
                        String uri="";
                        if (msgType.equals("ISBIRTHDAY")) {
                            uri = VideoPath;
                        }else {
                            uri = constants.LOCATPATH + "Static/welcome.mp4";
                        }
                        videoView.setVideoPath(uri);
                        videoView.requestFocus();
                        videoView.start();
                        if (playNum >= constants.WELCOMEPLAYNUM) {
                            videoView.stopPlayback();
                            videoView.suspend();
                            welcomeRL.setVisibility(View.GONE);
                            if (msgType.equals("ISBIRTHDAY")){
                                //todo 隐藏生日字幕
                                textBirthday.setVisibility(View.GONE);
                            }else {
                                tvWelcomeText.setVisibility(View.GONE);
                            }
                            banner.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

    }

    private void initBirthDayView() {
        textBirthday.setVisibility(View.VISIBLE);
        textBirthday.postDelayed(new Runnable() {
            @Override public void run() {
                birthDayTextShow();
            }
        }, 1000);
        textBirthday.reset();
    }


    private void initView() {
        initBanner();
        mPresenter.loadBannerData();
    }

    private void birthDayTextShow() {
        textBirthday.reset();
//        ShapeRevealSample.setBirthMsg("让我们为您祝福，让我们为您欢笑，" +
//                "因为今天是您的生日，我们把缀满幸福快乐和平安的祝福悄然奉送，祝您心想事成！幸福快乐！生日快乐！");
        ShapeRevealSample.setBirthMsg();
        ShapeRevealSample.setBirthPeopleName(birthDayMsg);
//        ShapeRevealSample shapeRevealSample = null;
//        shapeRevealSample = new ShapeRevealSample(birthDayMsg,"让我们为您祝福，让我们为您欢笑，" +
//                "因为今天是您的生日，我们把缀满幸福快乐和平安的祝福悄然奉送，祝您心想事成！幸福快乐！生日快乐！");
        ShapeRevealSample.play(textBirthday,getAssets(),VideoContextfont,VideoContextColor);
    }

    public void initBanner() {
        bannnerImgList.add(constants.LOCATPATH + "Static/loading.jpeg");
//        Toast.makeText(this,getApplicationContext().getFilesDir().getAbsolutePath(),Toast.LENGTH_LONG).show();;
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        banner.setDelayTime(5000)
                .setImages(bannnerImgList)
                .setImageLoader(new GlideImageLoader())
                .start();
    }

    private String addVisitorList(MsgData __vipDataJson) {
        boolean isInserted = false;
        if ((vipPeopleList != null)&&(vipPeopleList.size()>0)) {
            int idx=0;
            for (MsgData vipPeopleItem:vipPeopleList){
                if (vipPeopleItem.getUSERID().equals(__vipDataJson.getUSERID())){
                    vipPeopleList.remove(vipPeopleItem);
                    Log.d("welcomeMsg", "addVisitorList: remove "+vipPeopleItem.getUSERNAME() );
                    continue;
                }
                Log.d("welcomeMsg", "addVisitorList: difftime is " + diffTwoDate(__vipDataJson.getVIPTIME(),vipPeopleItem.getVIPTIME()) );
                if (diffTwoDate(__vipDataJson.getVIPTIME(),vipPeopleItem.getVIPTIME()) > welcomeTime) //大于设定好的时间
                {
                    vipPeopleList.remove(vipPeopleItem); //删除元素（后面追加）
                    Log.d("welcomeMsg", "addVisitorList: remove "+vipPeopleItem.getUSERNAME() + "; time :" +  vipPeopleItem.getVIPTIME());
                    continue;
                }

                //判断是否应该插入list,判断职级的高低.2018/10/19
                if (!isInserted && vipPeopleItem.getUSERLEVEL() <
                        __vipDataJson.getUSERLEVEL()) {
                    idx =  vipPeopleList.indexOf(vipPeopleItem);
                    vipPeopleList.add(idx, __vipDataJson);
                    Log.d("welcomeMsg", "addVisitorList: insert " + __vipDataJson.getUSERNAME() + "; time:"+__vipDataJson.getVIPTIME());
                    isInserted = true;
                }
            }
        }
        if (!isInserted) {
            vipPeopleList.add(__vipDataJson);
            Log.d("welcomeMsg", "addVisitorList: insert " + __vipDataJson.getUSERNAME() + "; time:"+__vipDataJson.getVIPTIME());
        }
        int chosenVisitorCount = constants.WELCOMEVIPCOUNT; //只播放最多前constants.WELCOMEVIPCOUNT个,2018/10/19
        if (vipPeopleList.size() < chosenVisitorCount) {
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
        Log.d("welcomeMsg", "addVisitorList: msg is " + visitorStr);
        return visitorStr;
    }

    public void updateBanner(List<WelcomeData.PicDataBean> picDatalist) {
        tempDatalist=picDatalist;
        bannnerImgList.clear();
        if (bannerPlayTime.size() > 0 && bannerPlayTime != null && !bannerPlayTime.isEmpty()) {
            bannerPlayTime.clear();
        }
        if (bannerStartTime.size() > 0 && bannerStartTime != null && !bannerStartTime.isEmpty()) {
            bannerStartTime.clear();
        }
        if (bannerEndTime.size() > 0 && bannerEndTime != null && !bannerEndTime.isEmpty()) {
            bannerEndTime.clear();
        }
        if (bannerPlayType.size() > 0 && bannerPlayType != null && !bannerPlayType.isEmpty()) {
            bannerPlayType.clear();
        }

        if (picDatalist != null) {
            for (WelcomeData.PicDataBean pic : picDatalist) {
                bannnerImgList.add(constants.LOCATPATH + pic.getFileName().replace(".jpg",".jpeg"));
                bannerPlayTime.add(pic.getDisplayTime());
                bannerStartTime.add(pic.getStartTime());
                bannerEndTime.add(pic.getEndTime());
                bannerPlayType.add(pic.getPlayType());
            }
            Log.e("transForm001:","bannerPlayType.size():"+bannerPlayType.size()+" / bannerStartTime.size:"+bannerStartTime.size()+"  /bannerEndTime.size"+bannerEndTime.size());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<String> tempImgList = bannnerImgList;
                    List<Integer> tempPlayTime = bannerPlayTime;
                    List<String> tempStartTime = bannerStartTime;
                    List<String> tempEndTime = bannerEndTime;
                    List<String> tempPlayType = bannerPlayType;

                    //只播存放区
                    List<String> advanceImgList = new ArrayList<>();
                    List<Integer> advancePlayTime = new ArrayList<>();
                    List<String> advanceStartTime = new ArrayList<>();
                    List<String> advanceEndTime = new ArrayList<>();
                    List<String> advancePlayType = new ArrayList<>();

                    for (int position = 0; position < tempPlayTime.size(); position++) {
                        if (tempPlayType.get(position).equals("0")) {
                            //日常播放
                        } else {
                            //非日常播放
                            long startTime = transTimeForm(tempStartTime.get(position));
                            long endTime = transTimeForm(tempEndTime.get(position));
                            if (time < startTime) {
                                //未到播放时间
                                tempImgList.remove(position);
                                tempPlayTime.remove(position);
                                tempEndTime.remove(position);
                                tempStartTime.remove(position);
                                tempPlayType.remove(position);
                                position = position - 1;
                                //                                return;
                            } else {
                                if (time < endTime) {
                                    //在播放时间内
                                    if (tempPlayType.get(position).equals("2")) {
                                        //只播
                                        advanceImgList.add(tempImgList.get(position));
                                        advancePlayTime.add(tempPlayTime.get(position));
                                        advanceStartTime.add(tempStartTime.get(position));
                                        advanceEndTime.add(tempEndTime.get(position));
                                        advancePlayType.add(tempPlayType.get(position));
                                        Log.e("advance:", tempPlayTime.get(position)+"/"+tempImgList.get(position)+" /"+tempStartTime.get(position)+" /"+tempEndTime.get(position)+" /"+tempPlayType.get(position));

                                        tempImgList.remove(position);
                                        tempPlayTime.remove(position);
                                        tempEndTime.remove(position);
                                        tempStartTime.remove(position);
                                        tempPlayType.remove(position);
                                        position = position - 1;


                                    } else if (tempPlayType.get(position).equals("1")) {
                                        //插播
                                    }
                                } else {
                                    //超出播放时间
                                    tempImgList.remove(position);
                                    tempPlayTime.remove(position);
                                    tempEndTime.remove(position);
                                    tempStartTime.remove(position);
                                    tempPlayType.remove(position);
                                    position = position - 1;
                                }
                            }
                        }
                    }

                    if(!advanceImgList.isEmpty()){
                        tempImgList.clear();
                        tempPlayTime.clear();
                        tempEndTime.clear();
                        tempStartTime.clear();
                        tempPlayType.clear();
                        Log.e("transForm00111:","advanceImgList.size():"+advanceImgList.size()+" / advancePlayTime.size:"+advancePlayTime.size()+"  /advanceStartTime.size"+advanceStartTime.size());

                        tempImgList=advanceImgList;
                        tempPlayTime=advancePlayTime;
                        tempEndTime=advanceEndTime;
                        tempStartTime=advanceStartTime;
                        tempPlayType=advancePlayType;

                    }
                    Log.e("transForm0012:","tempPlayType.size():"+tempPlayType.size()+" / tempStartTime.size:"+tempStartTime.size()+"  /tempEndTime.size"+tempEndTime.size());

                    if(!tempPlayTime.isEmpty()){
                        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
                        banner.setDelayTime(tempPlayTime.get(0) * 1000).setImages(tempImgList).setImageLoader(new GlideImageLoader()).start();
                        final List<Integer> finalTempPlayTime = tempPlayTime;
                        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {   //滑动监听
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                            }

                            @Override
                            public void onPageSelected(int position) {
                                banner.setDelayTime(finalTempPlayTime.get(position) * 1000);
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                            }

                        });
                    }
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

    private long transTimeForm(String t){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long tempTime=0L;
        try{
            tempTime=format.parse(t).getTime();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return tempTime;
    }


    @Override
    protected void onMsgResult(Message msg) {
        super.onMsgResult(msg);
        switch (msg.what) {
            case R.id.init_welcome_state:
                showText = "";
                break;
            case R.id.init_rollMsg_state:
                rollMsg="";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rollRL.setVisibility(View.GONE);
                        rollTextView.setVisibility(View.GONE);
                    }
                });
                break;
        }
    }
}
