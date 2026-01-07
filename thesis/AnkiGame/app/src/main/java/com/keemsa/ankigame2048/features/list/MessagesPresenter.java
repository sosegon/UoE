package com.keemsa.ankigame2048.features.list;

import com.keemsa.ankigame2048.base.BasePresenter;
import com.keemsa.ankigame2048.data.DataManager;
import com.keemsa.ankigame2048.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class MessagesPresenter extends BasePresenter<MessagesMvpView> {

    private final DataManager mDataManager;

    @Inject
    public MessagesPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

}
