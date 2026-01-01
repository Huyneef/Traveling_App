package com.example.traveling_app.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.traveling_app.R;
import com.example.traveling_app.model.user.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ResetPassActivity extends AppCompatActivity {
    Button resetmkbtn1;

    EditText resetmk_mkmoi,dangky_xnmatkhau;
    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetmk);

        resetmk_mkmoi=findViewById(R.id.resetmk_mkmoi);
        dangky_xnmatkhau=findViewById(R.id.dangky_xnmatkhau);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.baseline_keyboard_backspace_24); // Thay thế ic_arrow_back bằng ID của hình ảnh của bạn

        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        // Hiển thị nút quay lại trên Action Bar
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resetmkbtn1= (Button) findViewById(R.id.resetmk_btn1);
        resetmkbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAccount();
            }
        });
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Hoặc thực hiện hành động cụ thể khi nút quay lại được nhấn
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateAccount(){
        String pass=resetmk_mkmoi.getText().toString();
        String rePass=dangky_xnmatkhau.getText().toString();
        if(!pass.equals(rePass))
            Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
        else if(pass.trim().equals(""))
            Toast.makeText(this, "Mật khẩu không được trống", Toast.LENGTH_SHORT).show();
        else{
            Query query=ref.orderByChild("email").equalTo(getIntent().getStringExtra("email"));
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.exists()){
                        User user=snapshot.getValue(User.class);
                        user.setToken("");
                        user.setPassword(pass);
                        ref.child(snapshot.getKey()).setValue(user);
                        Intent intent=new Intent(ResetPassActivity.this, ResetPassSuccessActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}