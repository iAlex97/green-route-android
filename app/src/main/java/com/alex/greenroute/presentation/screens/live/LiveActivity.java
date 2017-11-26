package com.alex.greenroute.presentation.screens.live;

import android.animation.ArgbEvaluator;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alex.greenroute.R;
import com.alex.greenroute.component.GreenApplication;
import com.alex.greenroute.data.DataRepository;
import com.alex.greenroute.data.remote.response.PollutionResponse;
import com.alex.greenroute.views.ShadowView;
import com.github.yongjhih.mismeter.MisMeter;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LiveActivity extends AppCompatActivity implements LiveCallback {

    @BindView(R.id.meter1)
    MisMeter meter1;

    @BindView(R.id.shadow1)
    ShadowView shadow1;

    @BindColor(R.color.primary)
    int green;

    @BindColor(R.color.accent)
    int red;

    @Inject
    DataRepository dataRepository;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            dataRepository.getLivePollutionJson(LiveActivity.this);
        }
    };

    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        ButterKnife.bind(this);

        LiveComponent component = DaggerLiveComponent.builder()
                .appComponent(GreenApplication.component())
                .build();
        component.inject(this);

        mHandler.post(mRunnable);
    }

    @Override
    public void onNewLiveData(PollutionResponse data) {
        interpretData(data);
        mHandler.postDelayed(mRunnable, 5000);
    }

    @Override
    public void onError() {
        mHandler.postDelayed(mRunnable, 3000);
    }

    private void interpretData(PollutionResponse data) {
        float NO2 = (float) data.polluants.get("NO2").doubleValue();

        int color = (Integer) new ArgbEvaluator().evaluate(NO2 / 1.2f, green, red);
        shadow1.changeColor(color);

        meter1.setProgress(NO2 * 100);
    }
}
