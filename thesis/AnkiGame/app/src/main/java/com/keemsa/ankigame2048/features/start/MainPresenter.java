package com.keemsa.ankigame2048.features.start;

import com.keemsa.ankigame2048.base.BasePresenter;
import com.keemsa.ankigame2048.data.DataManager;
import com.keemsa.ankigame2048.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    private final DataManager mDataManager;

    @Inject
    public MainPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void addMessage(String message) {

    }

}
