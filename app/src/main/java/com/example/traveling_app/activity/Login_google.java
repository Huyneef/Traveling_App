package com.example.traveling_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.traveling_app.R;
import com.example.traveling_app.model.user.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;

public class Login_google extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    TextView name,email;
    Button signoutbtn;
    private String personUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_google);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        signoutbtn = findViewById(R.id.signoutbtn);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestServerAuthCode("424359554202-fgthtsl6ehka47r0r2g354obeh9sj35e.apps.googleusercontent.com")  // Thay đổi bằng Client ID của dự án mới
                .build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!= null){
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            personUsername = personEmail.substring(0, personEmail.indexOf('@'));
            name.setText(personName);
            email.setText(personEmail);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            HashMap<String, Object> updateVal = new HashMap<>();
            updateVal.put("fullName", personName);
            reference.child(personUsername).updateChildren(updateVal);
        }


        signoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_google.this, MainActivity.class);
                User user = new User();
                user.setUsername(personUsername);
                intent.putExtra("user", (Serializable) user);
                startActivity(intent);
            }
        });
    }

    void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                finish();
                startActivity(new Intent(Login_google.this,WelcomeActivity.class));
            }
        });
    }
}