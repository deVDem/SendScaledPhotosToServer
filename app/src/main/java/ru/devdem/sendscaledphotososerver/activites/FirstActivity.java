package ru.devdem.sendscaledphotososerver.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.devdem.sendscaledphotososerver.R;

public class FirstActivity extends AppCompatActivity {
    private Button btnRules;
    private Button btnRegister;
    private Button btnLogin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_first, null);
        setContentView(view);
        btnRules = view.findViewById(R.id.btnRules);
        btnRules.setOnClickListener(v->{
            stop();
            startActivity(new Intent(this, RulesActivity.class));
            finish();
        });
        btnRegister = view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            stop();
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });
        btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            stop();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    void stop() {
        btnRules.setEnabled(false);
        btnRegister.setEnabled(false);
        btnLogin.setEnabled(false);
    }
}
