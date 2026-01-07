package com.keemsa.ankigame2048.injection.component;


import com.keemsa.ankigame2048.features.list.MessagesFragment;
import com.keemsa.ankigame2048.features.start.MainFragment;
import com.keemsa.ankigame2048.injection.PerActivity;
import com.keemsa.ankigame2048.injection.module.ActivityModule;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainFragment mainFragment);
    void inject(MessagesFragment messagesFragment);
}
