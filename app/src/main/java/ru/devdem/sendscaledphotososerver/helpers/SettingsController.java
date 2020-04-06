package ru.devdem.sendscaledphotososerver.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsController {
    private Context mContext;
    private static SharedPreferences mSettings;
    private static SharedPreferences mAccountData;


    public static SettingsController getInstance(Context context) {
        return new SettingsController(context);
    }
    SettingsController(Context context) {
        mSettings=context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        mAccountData=context.getSharedPreferences("account", Context.MODE_PRIVATE);
        mContext=context;
    }
    public boolean isFirst() {
        return mSettings.getBoolean("first", true);
    }
}
