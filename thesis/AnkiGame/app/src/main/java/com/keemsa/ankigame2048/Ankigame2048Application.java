package com.keemsa.ankigame2048;

import android.app.Application;
import android.content.Context;

import com.keemsa.ankigame2048.injection.component.ApplicationComponent;
import com.keemsa.ankigame2048.injection.component.DaggerApplicationComponent;
import com.keemsa.ankigame2048.injection.module.ApplicationModule;

public class Ankigame2048Application extends Application {
    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static Ankigame2048Application get(Context context) {
        return (Ankigame2048Application) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
