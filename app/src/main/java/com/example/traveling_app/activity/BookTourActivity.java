package com.example.traveling_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.traveling_app.R;
import com.example.traveling_app.common.CurrentUser;
import com.example.traveling_app.common.RandomValue;
import com.example.traveling_app.common.VoucherHelper;
import com.example.traveling_app.model.tour.HistoryTour;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vn.momo.momo_partner.AppMoMoLib;

public class BookTourActivity extends AppCompatActivity {
    Button dattour_btn;
    TextView amount_tv,name_tour_tv,address_tour_tv,price_tour_tv,saleprice_tour_tv,point_tv, email_tv, name_person_tv,phone_number_tv;
    TextInputEditText dateEnd_inp,dateStart_inp;
    ImageView tour_img;
    TextInputLayout calStart,calEnd;
    ImageButton next_mgg_btn,next_point_btn;
    private static String point, sale, dateEnd1, dateStart1, amount, email, name_person, phone_number, price1,idTour;
    private  DatabaseReference ref;
    private boolean isUpdatingNum = false;
    private boolean isUpdatingPoint = false;
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "LeQuangLuu";
    private String merchantCode = "MOMOC2IC20220510";
    private String merchantNameLabel = "Nhà cung cấp";
    private String description = "Thanh toán dịch vụ đặt tour du lịch";

    //--------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_tour);
        anhXa();
        loadData();

        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);
        dattour_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePayment();
                requestPayment();
            }
        });

        next_mgg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveForm();
                Intent intent = new Intent(BookTourActivity.this, VoucherActivity.class);
                startActivity(intent);
                amount_tv.setText(amount);
            }
        });
        next_point_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveForm();
                Intent intent = new Intent(BookTourActivity.this, PointActivity.class);
                intent.putExtra("amount",amount);
                startActivity(intent);
                amount_tv.setText(amount);
            }
        });
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        actionBar.setTitle("Đặt tour");
    }
    private void saveForm() {
        name_person = name_person_tv.getText().toString();
        email = email_tv.getText().toString();
        phone_number = phone_number_tv.getText().toString();
        dateEnd1 = dateEnd_inp.getText().toString();
        dateStart1 = dateStart_inp.getText().toString();
    }
    private void loadData() {
        if (getIntent().getStringExtra("id")!=null)
        {
            idTour= getIntent().getStringExtra("id");
        }

        if (idTour != null) {
            ref = FirebaseDatabase.getInstance().getReference().child("tours").child(idTour);
        }
        else {
            ref = FirebaseDatabase.getInstance().getReference().child("tours").child("RS2ohC");
        }
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String address = snapshot.child("address").getValue(String.class);
                String end = snapshot.child("dateEnd").getValue(String.class);
                String start = snapshot.child("dateStart").getValue(String.class);
                String mainImageUrl = snapshot.child("mainImageUrl").getValue(String.class);
                name_tour_tv.setText(name);
                address_tour_tv.setText(address);
                dateStart_inp.setText(start);
                dateEnd_inp.setText(end);
                Glide.with(tour_img).load(mainImageUrl).into(tour_img);
                price1 = snapshot.child("salePrice").getValue(Integer.class).toString();
                price_tour_tv.setText(price1+"đ");
                if (getIntent().getStringExtra("key_saleprice")==null && getIntent().getStringExtra("key_point")==null) {
                    amount_tv.setText(price1);
                    amount = amount_tv.getText().toString();
                    saleprice_tour_tv.setText("0");
                    point_tv.setText("0");
                }
                else {
                    if (getIntent().getStringExtra("key_saleprice")!=null)
                    {
                        int saleprice = Integer.parseInt(getIntent().getStringExtra("key_saleprice"))*1000;
                        amount_tv.setText(Integer.parseInt(price1) -saleprice - Integer.parseInt((String) point_tv.getText())  +"");
                        amount = amount_tv.getText().toString();
                        sale = saleprice+"";
                        saleprice_tour_tv.setText(saleprice+"");
                    }
                    if (getIntent().getStringExtra("key_point")!=null)
                    {
                        point = getIntent().getStringExtra("key_point");
                        amount_tv.setText(String.valueOf(Integer.parseInt(price1) - Integer.parseInt((String) saleprice_tour_tv.getText())- Integer.parseInt(point)));
                        amount = amount_tv.getText().toString();
                        point_tv.setText(point);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        if (getIntent().getStringExtra("key_point")!=null) {
            String point = getIntent().getStringExtra("key_point");
            point_tv.setText(point);
        }
        if (sale!=null) {
            saleprice_tour_tv.setText(sale);
        }
        if (point !=null) {
            point_tv.setText(point);
        }
        if (email!= null) {
            email_tv.setText(email);
        }
        if (name_person!=null) {
            name_person_tv.setText(name_person);
        }
        if (phone_number!=null) {
            phone_number_tv.setText(phone_number);
        }
        if (dateEnd1!=null) {
            dateEnd_inp.setText(dateEnd1);
        }
        if (dateStart1!=null) {
            dateStart_inp.setText(dateStart1);
        }
    }
    private void anhXa() {
        dattour_btn = (Button) findViewById(R.id.dattour_btn);
        name_tour_tv = findViewById(R.id.name_tour);
        address_tour_tv = findViewById(R.id.address_tour);
        price_tour_tv = findViewById(R.id.price_tour);
        amount_tv = findViewById(R.id.amount);
        next_mgg_btn = (ImageButton) findViewById(R.id.next_mgg_btn);
        next_point_btn = findViewById(R.id.next_point_btn);
        saleprice_tour_tv = findViewById(R.id.saleprice_tour);
        point_tv = findViewById(R.id.point_tv);
        dateEnd_inp = findViewById(R.id.dateEnd);
        dateStart_inp = findViewById(R.id.dateStart);
        calEnd = findViewById(R.id.calEnd);
        calStart = findViewById(R.id.calStart);
        email_tv = findViewById(R.id.email_tv);
        name_person_tv = findViewById(R.id.name_person_tv);
        phone_number_tv = findViewById(R.id.phone_number_tv);
        tour_img = findViewById(R.id.tour_img);
    }


    private int calPoint(int price) {
        int savePoint;
        if (price<500000)
            savePoint = 0;
        else if (price <=1000000)
            savePoint = 20000;
        else
            savePoint = price/1000000*10000 + 20000;
        return  savePoint;
    }
    private void savePayment() {
        HistoryTour historyObj = new HistoryTour();
        historyObj.setIdtour(idTour);
        historyObj.setPrice(Integer.parseInt((String) amount_tv.getText()));
        historyObj.setStartDate(String.valueOf(dateStart_inp.getText()));
        historyObj.setEndDate(String.valueOf(dateEnd_inp.getText()));
        historyObj.setUsername(CurrentUser.getCurrentUser().getUsername());
        DatabaseReference ref1;
        ref1 = FirebaseDatabase.getInstance().getReference().child("booking");
        ref1.child(RandomValue.generateRandomString(6)).setValue(historyObj);
        if (getIntent().getStringExtra("key_id")!=null) {
            VoucherHelper voucherHelper = new VoucherHelper();
            voucherHelper.deleteVoucher(getIntent().getStringExtra("key_id").toString());
        }
        //reset Point
        resetNumPoint();
        //increase numBooking
        resetNumBooking();
    }
    private void resetNumPoint() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(CurrentUser.getCurrentUser().getUsername());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isUpdatingPoint) {
                    isUpdatingPoint = true;
                    int numPoint = snapshot.child("point").getValue(Integer.class);
                    databaseReference.child("point").setValue(numPoint + calPoint(Integer.parseInt((String) amount_tv.getText())));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        databaseReference.child("point").setValue(calPoint(Integer.parseInt((String) amount_tv.getText())));
    }
    private void resetNumBooking() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isUpdatingNum) {
                    isUpdatingNum = true;
                    int num = snapshot.child("numBooking").getValue(Integer.class);
                    ref.child("numBooking").setValue(num + 1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void resetData() {
        saleprice_tour_tv.setText("0");
        point_tv.setText("0");
        sale = "0";
        point = "0";
        amount = "0";
    }

    // request app MoMo
    private void requestPayment() {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
        Map<String, Object> eventValue = new HashMap<>();
        eventValue.put("merchantname", merchantName);
        eventValue.put("merchantcode", merchantCode);
        eventValue.put("amount", amount);
        eventValue.put("orderId", "bookId123456789");
        eventValue.put("orderLabel", "Mã đơn hàng");

        eventValue.put("merchantnamelabel", "Dịch vụ");
        eventValue.put("fee", "0");
        eventValue.put("description", description);

        eventValue.put("requestId",  merchantCode+"merchant_billId_"+System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("book", "1234");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());
        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if(data != null) {
                if(data.getIntExtra("status", -1) == 0) {
                    resetData();
                    finish();
                    startActivity(new Intent(BookTourActivity.this, PaySuccessActivity.class));
                    }
                }
            }

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}