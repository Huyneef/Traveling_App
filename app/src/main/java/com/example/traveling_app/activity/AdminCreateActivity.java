package com.example.traveling_app.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.traveling_app.R;
import com.example.traveling_app.common.DataCallback;
import com.example.traveling_app.common.RandomValue;
import com.example.traveling_app.model.tour.Tour;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AdminCreateActivity extends AppCompatActivity {
    Dialog dialog;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference ref=database.getReference("tours");
    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference storageRef=storage.getReferenceFromUrl("gs://travelapp-31533.appspot.com");
    private static final int PICK_IMAGE = 1;
    EditText edtTen, edtDc, edtSdt, edtMt, edtNbd, edtNkt, edtGia, edtKm, edtAnhChinh;
    Button btnLuu, btnUpload;
    ImageView imgUpload;
    ProgressDialog progressDialog;
    Tour tour=null;
    Tour tourPresent=null;
    Spinner spinner_loai;
    ImageView calendar_bd, calendar_kt;
    List<String> loais = new ArrayList<>();
    DecimalFormat format = new DecimalFormat("#.##");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String idTour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_admin);

        anhXa();

        loais.add("Văn hóa");
        loais.add("Biển");
        loais.add("Núi");
        loais.add("Đảo");
        loais.add("Ẩm thực");
        loais.add("Team Building");
        loais.add("Mạo hiểm");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,loais);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner_loai.setAdapter(adapter);
        idTour= getIntent().getStringExtra("id");

        bindingData();
        calendar_bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                if  (idTour!=null){
                    Date date = null;
                    try {
                        date = dateFormat.parse(edtNbd.getText().toString());
                        calendar.setTime(date);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DecimalFormat decimalFormat = new DecimalFormat("00");
                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminCreateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = decimalFormat.format(dayOfMonth) + "/" + decimalFormat.format(monthOfYear + 1) + "/" + year;
                        edtNbd.setText(selectedDate);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        calendar_kt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                if  (idTour!=null){
                    Date date = null;
                    try {
                        date = dateFormat.parse(edtNkt.getText().toString());
                        calendar.setTime(date);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                int year =  calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DecimalFormat decimalFormat = new DecimalFormat("00");
                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminCreateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = decimalFormat.format(dayOfMonth) + "/" + decimalFormat.format(monthOfYear + 1) + "/" + year;
                        edtNkt.setText(selectedDate);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        actionBar.setTitle("Quay lại");

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idTour!=null)
                    updateData();
                else {
                    generateTour(new DataCallback() {
                        @Override
                        public void onDataLoaded(List<Tour> tours) {

                        }
                        @Override
                        public void onTourLoaded(Tour tour) {
                                ref.child(tour.getId()).setValue(tour, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        // thành công
                                        if (error==null){
                                            AlertDialog.Builder b = new AlertDialog.Builder(AdminCreateActivity.this);
                                            b.setTitle("Thông báo");
                                            b.setMessage("Tạo mới Tour thành công");
                                            b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    finish();
                                                    Intent intent=new Intent(getApplicationContext(), AdminActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                            b.setIcon(R.drawable.success);
                                            AlertDialog al = b.create();
                                            al.show();
                                        }
                                        else{}
                                    }
                                });
                            }
                        });
                    }
                }});
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==PICK_IMAGE && resultCode==RESULT_OK && data!=null){
            Uri imageUri = data.getData();
            try {
                // Đọc ảnh từ Uri
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgUpload.setImageBitmap(bitmap);
                // khi chọn ảnh thì đường dẫn url của ảnh là trống.
                edtAnhChinh.setText("");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void anhXa(){
        edtTen=(EditText) findViewById(R.id.edtTen);
        edtDc=(EditText)findViewById(R.id.edtDc);
        edtSdt=(EditText)findViewById(R.id.edtSdt);
        edtMt=(EditText)findViewById(R.id.edtMt);
        edtNbd=(EditText)findViewById(R.id.edtNbd);
        edtNkt=(EditText)findViewById(R.id.edtNkt);
        edtGia=(EditText)findViewById(R.id.edtGia);
        edtKm=(EditText)findViewById(R.id.edtKm);
        edtAnhChinh=(EditText)findViewById(R.id.edtAnhChinh);
        btnLuu=(Button)findViewById(R.id.btnLuu);
        btnUpload=(Button)findViewById(R.id.btnUpload);
        imgUpload=(ImageView) findViewById(R.id.imgUpload);
        dialog=new Dialog(getApplicationContext());
        calendar_bd=(ImageView) findViewById(R.id.calendar_bd);
        calendar_kt=(ImageView) findViewById(R.id.calendar_kt);
        spinner_loai=(Spinner) findViewById(R.id.spinner_loai);
    }

    public void generateTour(final DataCallback dataCallback){
        String ten=edtTen.getText().toString();
        String diachi=edtDc.getText().toString();
        String sdt=edtSdt.getText().toString();
        String mota=edtMt.getText().toString();
        String nbd=edtNbd.getText().toString();
        String nkt=edtNkt.getText().toString();
        if (ten.equals("") || diachi.equals("") || sdt.equals("") || mota.equals("") || nbd.equals("") || nkt.equals(""))
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        else{
            progressDialog = new ProgressDialog(AdminCreateActivity.this);
            progressDialog.setMessage("Đang tạo mới tour...");
            progressDialog.show();

            SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
            double price= Double.parseDouble(edtGia.getText().toString());
            double sale= Double.parseDouble(edtKm.getText().toString());
            double priceSale= price*(100-sale)/100.0;
            String idTour= RandomValue.generateRandomString(6);
            // nếu đầu vào là 1 ảnh upload từ máy.
            if (edtAnhChinh.getText().toString().equals("")) {
                Calendar calendar = Calendar.getInstance();
                StorageReference mountainsRef = storageRef.child("images").child("tours").child("im-age" + calendar.getTimeInMillis() + "png");
                imgUpload.setDrawingCacheEnabled(true);
                imgUpload.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imgUpload.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "Lỗi !!!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imgAvaUrl=uri.toString();
                                tour = new Tour(ten, diachi.toString(), sdt, mota, nbd, nkt, price, priceSale, format.format(new Date()), imgAvaUrl, idTour,spinner_loai.getSelectedItem().toString());
                                dataCallback.onTourLoaded(tour);
                            }
                        });
                    }
                });
            }
            else {
                tour = new Tour(ten, diachi.toString(), sdt, mota, nbd, nkt, price, priceSale, format.format(new Date()),edtAnhChinh.getText().toString(), idTour,spinner_loai.getSelectedItem().toString());
                dataCallback.onTourLoaded(tour);
            }
        }
    }


    public void bindingData(){
        // binding dữ liệu thông qua intent
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().equals(idTour)){
                    tourPresent=snapshot.getValue(Tour.class);
                    edtTen.setText(tourPresent.getName());
                    edtDc.setText(tourPresent.getAddress());
                    edtSdt.setText(tourPresent.getPhone());
                    edtMt.setText(tourPresent.getContent());
                    edtNbd.setText(tourPresent.getDateStart());
                    edtNkt.setText(tourPresent.getDateEnd());
                    edtGia.setText(tourPresent.getPrice()+"");
                    double khuyenmai=((tourPresent.getPrice()-tourPresent.getSalePrice())/tourPresent.getPrice())*100;
                    edtKm.setText(format.format(khuyenmai).replace(',','.') +"");
                    edtAnhChinh.setText(tourPresent.getMainImageUrl());
                    for (int i=0;i<loais.size();i++)
                        if (loais.get(i).equals(tourPresent.getType()))
                            spinner_loai.setSelection(i);
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

    public void updateData(){
        // màn hình chờ
        progressDialog = new ProgressDialog(AdminCreateActivity.this);
        progressDialog.setMessage("Đang cập nhập tour...");
        progressDialog.show();

        // tính giá trị của giá cả
        double price= Double.parseDouble(edtGia.getText().toString());
        double sale= Double.parseDouble(edtKm.getText().toString());
        double priceSale= price*(100-sale)/100.0;
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("tours").child(idTour);
        HashMap<String,Object> dataU=new HashMap<>();
        dataU.put("name",edtTen.getText().toString());
        dataU.put("address",edtDc.getText().toString());
        dataU.put("content",edtMt.getText().toString());
        dataU.put("dateStart",edtNbd.getText().toString());
        dataU.put("dateEnd",edtNkt.getText().toString());
        dataU.put("phone",edtSdt.getText().toString());
        dataU.put("price",price);
        dataU.put("salePrice",priceSale);
        dataU.put("type",spinner_loai.getSelectedItem().toString());
        // có 2 trường hợp upload ảnh lại bằng url và upload ảnh lại từ trong máy.
        if (edtAnhChinh.getText().toString().equals("")) {
            Calendar calendar = Calendar.getInstance();
            StorageReference mountainsRef = storageRef.child("images").child("tours").child("image" + calendar.getTimeInMillis() + "png");
            imgUpload.setDrawingCacheEnabled(true);
            imgUpload.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imgUpload.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = mountainsRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(), "Lỗi !!!", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imgAvaUrl=uri.toString();
                            dataU.put("mainImageUrl",imgAvaUrl);
                            // thành công
                            if (imgAvaUrl!=null)
                                reference.updateChildren(dataU).addOnSuccessListener(nothing -> {progressDialog.cancel(); startActivity(new Intent(getApplicationContext(), AdminActivity.class));});
                        }
                    });
                }
            });
        }
        else{
            dataU.put("mainImageUrl",edtAnhChinh.getText().toString());
            reference.updateChildren(dataU).addOnSuccessListener(nothing -> {progressDialog.cancel(); startActivity(new Intent(getApplicationContext(), AdminActivity.class));});
        }
    }
}