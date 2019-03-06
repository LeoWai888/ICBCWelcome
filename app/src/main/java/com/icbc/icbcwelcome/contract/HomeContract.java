package com.icbc.icbcwelcome.contract;

import com.icbc.icbcwelcome.base.BasePresenter;
import com.icbc.icbcwelcome.base.BaseView;
import com.icbc.icbcwelcome.json.MsgData;
import com.icbc.icbcwelcome.json.WelcomeData;


import java.util.List;

public interface HomeContract {

    interface View extends BaseView<Presenter> {
        void updateBanner(List<WelcomeData.PicDataBean> picDatalist);

        void popVideoView(MsgData msgDataJson,String msgType);
        void setICBCWelcomeParam(String _welcomeMsg,
                                 int _welcomeTime,
                                 String _rollMsg,
                                 int _rollTime,
                                 String _rollMsgSendTime);
    }

    interface Presenter extends BasePresenter {
        void loadBannerData();

        void initWebSocket();
    }

}