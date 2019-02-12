package com.icbc.icbcwelcome.contract;

import com.icbc.icbcwelcome.base.BasePresenter;
import com.icbc.icbcwelcome.base.BaseView;
import com.icbc.icbcwelcome.json.WelcomeData;


import java.util.List;

public interface HomeContract {

    interface View extends BaseView<Presenter> {
        void updateBanner(List<WelcomeData.PicDataBean>  picDatalist,
                          String welcomeMsg,
                          int welcomeTime);
    }

    interface Presenter extends BasePresenter {
        void loadBannerData();
        void initWebSocket();
    }

}