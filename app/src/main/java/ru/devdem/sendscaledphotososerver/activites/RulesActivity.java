package ru.devdem.sendscaledphotososerver.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;

import org.json.JSONObject;

import ru.devdem.sendscaledphotososerver.R;
import ru.devdem.sendscaledphotososerver.helpers.NetworkController;

public class RulesActivity extends AppCompatActivity {

    private static final String TAG = "RulesActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_rules, null);
        setContentView(view);
        TextView textView = view.findViewById(R.id.textView);
        NetworkController networkController = NetworkController.getNetworkController();
        Response.Listener<String> listener = response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String status = jsonResponse.getString("status");
                switch (status) {
                    case "OK" :
                        textView.setText(jsonResponse.getString("rules"));
                        break;
                    case "ERROR":
                        Toast.makeText(this, "Произошла ошибка: "+jsonResponse.getString("error"), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        break;
                    default:
                        Toast.makeText(this, "Произошла неизвестная ошибка", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        break;

                }
            } catch (Exception e) {
                Log.e(TAG, "onCreate: ", e);
                Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        };

        networkController.getRulesInfo(this, listener);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }
}
