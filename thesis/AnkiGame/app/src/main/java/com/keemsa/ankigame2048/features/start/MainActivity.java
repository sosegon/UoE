package com.keemsa.ankigame2048.features.start;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.keemsa.ankigame2048.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String GAMEFRAGMENT_TAG = "GF_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //setSupportActionBar(tb);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frl_container, new MainFragment(), GAMEFRAGMENT_TAG)
                .commit();

    }
}
