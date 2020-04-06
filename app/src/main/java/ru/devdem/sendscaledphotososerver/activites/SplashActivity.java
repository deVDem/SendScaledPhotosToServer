package ru.devdem.sendscaledphotososerver.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.devdem.sendscaledphotososerver.R;
import ru.devdem.sendscaledphotososerver.helpers.SettingsController;

public class SplashActivity extends AppCompatActivity {
    SettingsController mSettingsController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_splash, null);
        setContentView(view);
        mSettingsController = SettingsController.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent;
        if (mSettingsController.isFirst()) intent = new Intent(this, FirstActivity.class);
        else intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
