package com.keemsa.ankigame2048.injection.component;

import android.app.Application;
import android.content.Context;

import com.keemsa.ankigame2048.Ankigame2048Application;
import com.keemsa.ankigame2048.data.DataManager;
import com.keemsa.ankigame2048.data.local.PreferencesHelper;
import com.keemsa.ankigame2048.injection.ApplicationContext;
import com.keemsa.ankigame2048.injection.module.ApplicationModule;
import com.keemsa.ankigame2048.util.RxEventBus;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(Ankigame2048Application boilerplateApplication);

    @ApplicationContext
    Context context();

    Application application();

    PreferencesHelper preferencesHelper();

    DataManager dataManager();

    RxEventBus eventBus();
}
