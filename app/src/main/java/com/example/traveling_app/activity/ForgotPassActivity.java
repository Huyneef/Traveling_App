package com.example.traveling_app.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.traveling_app.R;
import com.example.traveling_app.common.RandomValue;
import com.example.traveling_app.model.user.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPassActivity extends AppCompatActivity {
    Button quenmk_btn1;

    EditText quenmk_email;

    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quenmk);

        quenmk_email=findViewById(R.id.quenmk_email);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.baseline_keyboard_backspace_24); // Thay thế ic_arrow_back bằng ID của hình ảnh của bạn
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        // Hiển thị nút quay lại trên Action Bar
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        quenmk_btn1= (Button) findViewById(R.id.quenmk_btn1);
        quenmk_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData(quenmk_email.getText().toString());
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

    public void sendEmail(String email, String code, String username){
        try {
            String stringSenderEmail = "duongngocdanh6@gmail.com";
            String stringReceiverEmail = email;
            String stringPasswordSenderEmail = "appzzjhgofpjyxuz";

            String stringHost = "smtp.gmail.com";
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");
            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));
            mimeMessage.setSubject("Đặt lại mật khẩu");
            mimeMessage.setText("Xin chào "+ username +", \n\nMã code: " + code);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void addData(String email){

        Query query = ref.orderByChild("email").equalTo(email);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        User user=userSnapshot.getValue(User.class);
                        String code= RandomValue.generateRandomNumber(4);
                        sendEmail(email,code, userSnapshot.getKey());
                        user.setToken(code);
                        ref.child(userSnapshot.getKey()).setValue(user);

                        Intent myintent2 = new Intent(getApplicationContext(), AcpemailActivity.class);
                        myintent2.putExtra("email",quenmk_email.getText().toString());
                        startActivity(myintent2);
                    }
                } else {
                    AlertDialog.Builder b = new AlertDialog.Builder(ForgotPassActivity.this);
                    b.setTitle("Thông báo");
                    b.setMessage("Không tồn tại người dùng này");
                    b.setPositiveButton("BACK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    b.setIcon(R.drawable.error);
                    AlertDialog al = b.create();
                    al.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu
                System.out.println("Lỗi đọc dữ liệu: " + error.getMessage());
            }
        });
    }
}
