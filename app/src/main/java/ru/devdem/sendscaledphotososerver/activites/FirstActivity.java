package ru.devdem.sendscaledphotososerver.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.devdem.sendscaledphotososerver.R;

public class FirstActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_first, null);
        setContentView(view);
        Button btnRules = view.findViewById(R.id.btnRules);
        btnRules.setOnClickListener(v->{
            startActivity(new Intent(this, RulesActivity.class));
            finish();
        });
        Button btnRegister = view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });
    }
}
