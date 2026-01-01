package com.example.traveling_app.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.traveling_app.R;
import com.example.traveling_app.model.user.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AcpemailActivity extends AppCompatActivity {
    Button acpemailbtn1;
    EditText xt1,xt2,xt3,xt4;

    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acpemail);

        xt1=findViewById(R.id.xt1);
        xt2=findViewById(R.id.xt2);
        xt3=findViewById(R.id.xt3);
        xt4=findViewById(R.id.xt4);

        enterData();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.baseline_keyboard_backspace_24); // Thay thế ic_arrow_back bằng ID của hình ảnh của bạn

        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        // Hiển thị nút quay lại trên Action Bar
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        acpemailbtn1= (Button) findViewById(R.id.acpemail_btn1);
        acpemailbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
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

    public void check(){
        String tokenEntered= xt1.getText().toString()+xt2.getText().toString()+xt3.getText().toString()+xt4.getText().toString();
        String email=getIntent().getStringExtra("email");
        Query query = ref.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        User user=userSnapshot.getValue(User.class);
                        if (user.getToken().equals(tokenEntered))
                        {
                            Intent myintent2 = new Intent(getApplicationContext(), ResetPassActivity.class);
                            myintent2.putExtra("email",getIntent().getStringExtra("email"));
                            startActivity(myintent2);
                        }
                        else{
                            Toast.makeText(AcpemailActivity.this, "Mã code không chính xác", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(AcpemailActivity.this, "Mã code không chính xác", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu
                System.out.println("Lỗi đọc dữ liệu: " + error.getMessage());
            }
        });

    }

    public void enterData(){
        xt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0)
                    xt2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        xt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0)
                    xt3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        xt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0)
                    xt4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}