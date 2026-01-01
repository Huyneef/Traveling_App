package com.example.traveling_app.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.traveling_app.R;
import com.example.traveling_app.common.ReviewConstants;
import com.example.traveling_app.common.CurrentUser;
import com.example.traveling_app.model.tour.Review;
import com.example.traveling_app.adapter.ReviewAdapter;
import com.example.traveling_app.model.other.SharedViewModel;
import com.example.traveling_app.model.tour.Tour;
import com.example.traveling_app.model.user.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class ReviewFragment extends Fragment {

    private List<Review> reviews=new ArrayList<>();
    private RecyclerView review_rcv;
    private View view;
    LinearLayout sao1, sao2, sao3, sao4, sao5;
    private static final int PICK_IMAGE = 1;
    ImageView btnUpload,bl_anh1, bl_anh2,bl_anh3,bl_anh4,star1,star2,star3,star4,star5, btn_bl1,image_user_review;
    int numStar;
    DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
    EditText inputBl;
    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference storageRef=storage.getReferenceFromUrl("gs://travelapp-31533.appspot.com");
    List<Uri> uriList=new ArrayList<>();
    private SharedViewModel viewModel;
    ReviewAdapter reviewAdapter;
    ProgressDialog progressDialog;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault());
    String now=sdf.format(new Date());
    TextView numCom1,numCom2,numCom3,numCom4,numCom5;
    String tourId;

    private Map<Integer, Integer> ratingCountMap = new HashMap<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_review, container, false);

        review_rcv=view.findViewById(R.id.review_rcv);
        LinearLayoutManager ln=new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        review_rcv.setLayoutManager(ln);

        bl_anh1=view.findViewById(R.id.bl_anh1);
        bl_anh2=view.findViewById(R.id.bl_anh2);
        bl_anh3=view.findViewById(R.id.bl_anh3);
        bl_anh4=view.findViewById(R.id.bl_anh4);

        star1=view.findViewById(R.id.star1);
        star2=view.findViewById(R.id.star2);
        star3=view.findViewById(R.id.star3);
        star4=view.findViewById(R.id.star4);
        star5=view.findViewById(R.id.star5);
        sao1=view.findViewById(R.id.sao1);
        sao2=view.findViewById(R.id.sao2);
        sao3=view.findViewById(R.id.sao3);
        sao4=view.findViewById(R.id.sao4);
        sao5=view.findViewById(R.id.sao5);
        image_user_review=view.findViewById(R.id.image_user_review);
        btnUpload=view.findViewById(R.id.btn_up_bl);

        numCom1=view.findViewById(R.id.numCom1);
        numCom2=view.findViewById(R.id.numCom2);
        numCom3=view.findViewById(R.id.numCom3);
        numCom4=view.findViewById(R.id.numCom4);
        numCom5=view.findViewById(R.id.numCom5);

        btn_bl1=view.findViewById(R.id.btn_bl1);
        inputBl=view.findViewById(R.id.inputBl);
        tourId=getActivity().getIntent().getStringExtra("id");
        bindingData();
        Bundle bundle = getArguments();
        if (bundle != null) {
            numStar=bundle.getInt("numStar",9999);
            viewModel.setNumRate(numStar);
        }

        sao1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataByStar(1);
            }
        });
        sao2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataByStar(2);
            }
        });
        sao3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataByStar(3);
            }
        });
        sao4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataByStar(4);
            }
        });
        sao5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataByStar(5);
            }
        });

        ref.child("users").child(CurrentUser.getCurrentUser().getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                if (user.getProfileImage()!=null && !user.getProfileImage().equals(""))
                    Glide.with(getContext()).load(user.getProfileImage()).into(image_user_review);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_bl1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (inputBl.getText()==null || inputBl.getText().toString().trim().equals("") || viewModel.getNumRate()==0){
                    AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                    b.setTitle("Thông báo");
                    b.setMessage("Vui lòng nhập đầy đủ thông tin");
                    b.setPositiveButton("BACK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

                    b.setIcon(R.drawable.error);
                    AlertDialog al = b.create();
                    al.show();
                }
                else {
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Đang tạo đánh giá...");
                    progressDialog.show();
                    Calendar calendar = Calendar.getInstance();

                    // Tạo danh sách tác vụ (Tasks) để lấy URL
                    List<Task<Uri>> tasks = new ArrayList<>();

                    for (int i = 0; i < uriList.size(); i++) {
                        Uri imageUri = uriList.get(i);
                        StorageReference reviewsRef = storageRef.child("images").child("reviews").child("image" + calendar.getTimeInMillis() + "_" + i + ".png");
                        UploadTask uploadTask = reviewsRef.putFile(imageUri);

                        // Thêm Task vào danh sách
                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                return reviewsRef.getDownloadUrl();
                            }
                        });

                        tasks.add(urlTask);
                    }

                    // Sử dụng Tasks.whenAllSuccess() để đợi cho đến khi tất cả các tác vụ hoàn thành
                    Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                        @Override
                        public void onSuccess(List<Object> list) {
                            // Tất cả các URL đã được lấy thành công
                            List<String> urlImages = new ArrayList<>();
                            for (Object obj : list) {
                                String imgAvaUrl = ((Uri) obj).toString();
                                urlImages.add(imgAvaUrl);
                            }

                            // Tạo đối tượng Review chỉ khi urlImages không rỗng
                            if (!urlImages.isEmpty() || uriList.size() == 0) {
                                Review review = new Review(CurrentUser.getCurrentUser().getUsername(),
                                        CurrentUser.getCurrentUser().getProfileImage(),
                                        viewModel.getNumRate(),
                                        now,
                                        inputBl.getText().toString(),
                                        urlImages);

                                // Đẩy dữ liệu lên Firebase
                                ref.child("tours").child(tourId).child("reviews").child(CurrentUser.getCurrentUser().getUsername()).push().setValue(review, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if (error == null) {
                                            progressDialog.cancel();
                                            for (int i = 0; i < 4; i++) {
                                                uriList.clear();
                                                // ẩn hình ảnh
                                                getImageViewByIndex(i).setVisibility(View.GONE);
                                            }
                                            for (int i = 1; i <= viewModel.getNumRate(); i++)
                                                getStarByIndex(i).setImageResource(R.drawable.main_star_disable);
                                            inputBl.setText("");
                                            // set giá trị để xem list danh sách
                                            getDataByStar(0);
                                            // vì giá trị này không lưu trong db nên t gọi hàm
                                            // lưu giá trị của numRate và numComment lại
                                            DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
                                            reference.child("tours").child(tourId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    Tour tour=snapshot.getValue(Tour.class);
                                                    double numRateCurrent=tour.getNumStar();
                                                    int numCommentCurrent=tour.getNumComment();
                                                    // cập nhập số lượng rate
                                                    double numNewRate=(numRateCurrent*(numCommentCurrent)/(numCommentCurrent+1))+viewModel.getNumRate()*1.0/(numCommentCurrent+1);
                                                    reference.child("tours").child(tourId).child("numStar").setValue(numNewRate);
                                                    // cập nhập số lượng comment
                                                    reference.child("tours").child(tourId).child("numComment").setValue(numCommentCurrent+1);
                                                    bindingData();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        } else {
                                            // Xử lý lỗi khi push dữ liệu lên Firebase
                                            Toast.makeText(getContext(), "Lỗi khi đẩy dữ liệu lên Firebase", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }

        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Cho phép chọn nhiều ảnh
                startActivityForResult(intent, PICK_IMAGE);
            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==PICK_IMAGE && resultCode==RESULT_OK && data!=null){
            if (data.getClipData() != null) {
                uriList.clear();
                ClipData clipData = data.getClipData();
                int length=clipData.getItemCount()>4?4:clipData.getItemCount();
                for (int i = 0; i < length; i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    setImageView(imageUri,i);
                    uriList.add(imageUri);
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                uriList.add(imageUri);
                setImageView(imageUri,0);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setImageView(Uri imageUri, int index) {
        ImageView imageView = getImageViewByIndex(index);
        Glide.with(this)
                .load(imageUri)
                .into(imageView);
        imageView.setVisibility(View.VISIBLE);
    }

    private ImageView getImageViewByIndex(int index) {
        switch (index) {
            case 0:
                return bl_anh1;
            case 1:
                return bl_anh2;
            case 2:
                return bl_anh3;
            case 3:
                return bl_anh4;
            default:
                return null;
        }
    }

    private LinearLayout getLlByIndex(int index) {
        switch (index) {
            case 1:
                return sao1;
            case 2:
                return sao2;
            case 3:
                return sao3;
            case 4:
                return sao4;
            case 5:
                return sao5;
            default:
                return null;
        }
    }

    private ImageView getStarByIndex(int index) {
        switch (index) {
            case 1:
                return star1;
            case 2:
                return star2;
            case 3:
                return star3;
            case 4:
                return star4;
            case 5:
                return star5;
            default:
                return null;
        }
    }

    public void bindingData(){
        reviews.clear();
        ratingCountMap.clear();
        ref.child("tours").child(tourId).child("reviews").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot listSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot reviewSnapshot : listSnapshot.getChildren()) {
                            Review review = reviewSnapshot.getValue(Review.class);
                            review.setNameReviewer(listSnapshot.getKey());
//                            fetchUserAndSetAvatar(review);
                            int rate = review.getRate();
                            ratingCountMap.put(rate, ratingCountMap.getOrDefault(rate, 0) + 1);
                            reviews.add(review);


                            ref.child("users").child(review.getNameReviewer()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshotUser) {
                                    User user = snapshotUser.getValue(User.class);
                                    if (user != null && user.getProfileImage() != null) {
                                        review.setAvatarReviewer(user.getProfileImage());
                                    }
                                    updateRatingCountUI();
                                    displaySortedReviews();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle onCancelled
                                }
                            });

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void getDataByStar(int star){

        for (int i = 1; i <= 5; i++) {
            LinearLayout ln = getLlByIndex(i);
            ln.setBackgroundResource(i==star ? R.color.star : R.drawable.review_border);
        }
        List<Review> list= viewModel.getReviews().stream().filter(r -> r.getRate()==star).collect(Collectors.toList());
        reviewAdapter=new ReviewAdapter(getContext(), list);
        review_rcv.setAdapter(reviewAdapter);
    }


    private void fetchUserAndSetAvatar(Review review) {
        ref.child("users").child(review.getNameReviewer()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotUser) {
                User user = snapshotUser.getValue(User.class);
                if (user != null && user.getProfileImage() != null) {
                    review.setAvatarReviewer(user.getProfileImage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void updateRatingCountUI() {
        numCom1.setText(ratingCountMap.getOrDefault(ReviewConstants.RATE_1, 0) + "");
        numCom2.setText(ratingCountMap.getOrDefault(ReviewConstants.RATE_2, 0) + "");
        numCom3.setText(ratingCountMap.getOrDefault(ReviewConstants.RATE_3, 0) + "");
        numCom4.setText(ratingCountMap.getOrDefault(ReviewConstants.RATE_4, 0) + "");
        numCom5.setText(ratingCountMap.getOrDefault(ReviewConstants.RATE_5, 0) + "");
    }

    private void displaySortedReviews() {
        List<Review> sortedReviews = reviews.stream()
                .sorted()
                .collect(Collectors.toList());
        Collections.reverse(sortedReviews);
        viewModel.setReviews(sortedReviews);
        reviewAdapter = new ReviewAdapter(getContext(), sortedReviews);
        review_rcv.setAdapter(reviewAdapter);
    }
}