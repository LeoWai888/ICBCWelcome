package com.icbc.icbcwelcome.contract;

import com.icbc.icbcwelcome.base.BasePresenter;
import com.icbc.icbcwelcome.base.BaseView;
import com.icbc.icbcwelcome.json.PicData;

import java.util.List;

public interface HomeContract {

    interface View extends BaseView<Presenter> {
        void updateBanner(List<PicData.PicDataBean>  picDatalist);
    }

    interface Presenter extends BasePresenter {
        void loadBannerData();

    }
}