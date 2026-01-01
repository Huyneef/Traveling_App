package com.example.traveling_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.traveling_app.R;
import com.facebook.login.LoginManager;

public class Login_facebook extends AppCompatActivity {
    TextView name;
    Button logoutbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_facebook);
        name= findViewById(R.id.name);
        logoutbtn = findViewById(R.id.signoutbtn);

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                startActivity(new Intent(Login_facebook.this, MainActivity.class));
                finish();
            }
        });

    }
}