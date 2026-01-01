package com.example.traveling_app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.traveling_app.R;
import com.example.traveling_app.activity.AdminActivity;
import com.example.traveling_app.activity.MainActivity;
import com.example.traveling_app.activity.SearchAndFilterActivity;
import com.example.traveling_app.activity.VoucherActivity;
import com.example.traveling_app.adapter.BannerTourAdapter;
import com.example.traveling_app.adapter.HintTourAdapter;
import com.example.traveling_app.adapter.HotTourAdapter;
import com.example.traveling_app.adapter.NearTourAdapter;
import com.example.traveling_app.adapter.RecentTourAdapter;
import com.example.traveling_app.adapter.VoucherTourAdapter;
import com.example.traveling_app.common.CurrentUser;
import com.example.traveling_app.common.DataCallback;
import com.example.traveling_app.common.ImageLoader;
import com.example.traveling_app.model.tour.Discount;
import com.example.traveling_app.model.tour.Tour;
import com.example.traveling_app.model.user.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Menu_Home extends Fragment{
    private RecyclerView tour_hint_rcv, recent_rcv, voucher_rcv, hot_rcv, near_rcv;
    private HintTourAdapter hintTourAdapter;
    private RecentTourAdapter recentTourAdapter;
    private VoucherTourAdapter voucherTourAdapter;
    private HotTourAdapter hotTourAdapter;
    private NearTourAdapter nearTourAdapter;
    HashMap<String, Tour> tours=new HashMap<>();
    private List<Discount> discounts=new ArrayList<>();
    private MainActivity mainActivity;
    private View view;
    private EditText searchInput;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference ref=database.getReference();
    TextView username1;
    ImageView imgAvaMain;
    CurrentUser currentUser=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_menu__home, container, false);
        LinearLayout vanHoaContainer = view.findViewById(R.id.vanHoaContainer),
                bienContainer = view.findViewById(R.id.bienContainer),
                nuiContainer = view.findViewById(R.id.nuiContainer),
                daoContainer = view.findViewById(R.id.daoContainer),
                amThucContainer = view.findViewById(R.id.amThucContainer),
                teamBuildingContainer = view.findViewById(R.id.teamBuildingContainer),
                maoHiemContainer = view.findViewById(R.id.maoHiemContainer),
                voucherContainer=view.findViewById(R.id.voucherContainer);

        setOnClickToSearchActivity(vanHoaContainer, "Văn hóa");
        setOnClickToSearchActivity(bienContainer, "Biển");
        setOnClickToSearchActivity(nuiContainer, "Núi");
        setOnClickToSearchActivity(daoContainer, "Đảo");
        setOnClickToSearchActivity(amThucContainer, "Ẩm thực");
        setOnClickToSearchActivity(teamBuildingContainer, "Building");
        setOnClickToSearchActivity(maoHiemContainer, "Mạo hiểm");

        voucherContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), VoucherActivity.class);
                startActivity(intent);
            }
        });
        searchInput=view.findViewById(R.id.searchInput);
        username1=view.findViewById(R.id.username1);
        imgAvaMain=view.findViewById(R.id.imgAvaMain);
        searchInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mainActivity, SearchAndFilterActivity.class);
                startActivity(intent);
            }
        });

        mainActivity= (MainActivity) getActivity();

        if (mainActivity.getIntent().getSerializableExtra("user")!=null){
            User user= (User) mainActivity.getIntent().getSerializableExtra("user");
            currentUser=new CurrentUser(mainActivity,user);
        }

        username1.setText(currentUser.getCurrentUser().getUsername());

        ref.child("users").child(CurrentUser.getCurrentUser().getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User user=snapshot.getValue(User.class);
                    if (user.getProfileImage()!=null && !user.getProfileImage().equals(""))
                        Glide.with(getContext()).load(user.getProfileImage()).into(imgAvaMain);
                    else
                        imgAvaMain.setImageResource(R.drawable.main_avatar);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imgAvaMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser.getCurrentUser().getRole()==1)
                    startActivity(new Intent(mainActivity, AdminActivity.class));
            }
        });


        tour_hint_rcv=view.findViewById(R.id.tour_hint_rcv);
        recent_rcv=view.findViewById(R.id.recent_rcv);

        voucher_rcv=view.findViewById(R.id.voucher_rcv);
        LinearLayoutManager ln3=new LinearLayoutManager(mainActivity,RecyclerView.HORIZONTAL,false);
        voucher_rcv.setLayoutManager(ln3);

        hot_rcv=view.findViewById(R.id.hot_rcv);
        near_rcv=view.findViewById(R.id.near_rcv);


        LinearLayoutManager ln1=new LinearLayoutManager(mainActivity,RecyclerView.HORIZONTAL,false);;
        tour_hint_rcv.setLayoutManager(ln1);
        LinearLayoutManager ln2=new LinearLayoutManager(mainActivity,RecyclerView.HORIZONTAL,false);
        recent_rcv.setLayoutManager(ln2);

        LinearLayoutManager ln4=new LinearLayoutManager(mainActivity,RecyclerView.HORIZONTAL,false);;
        hot_rcv.setLayoutManager(ln4);
        LinearLayoutManager ln5=new LinearLayoutManager(mainActivity,RecyclerView.VERTICAL,false);;
        near_rcv.setLayoutManager(ln5);

        getDataVoucher();
        getData(new DataCallback() {
            @Override
            public void onDataLoaded(List<Tour> tours) {

                hintTourAdapter =new HintTourAdapter(mainActivity,tours);
                tour_hint_rcv.setAdapter(hintTourAdapter);

                recentTourAdapter =new RecentTourAdapter(mainActivity,tours);
                recent_rcv.setAdapter(recentTourAdapter);

                hotTourAdapter =new HotTourAdapter(mainActivity,tours);
                hot_rcv.setAdapter(hotTourAdapter);

                nearTourAdapter=new NearTourAdapter(mainActivity,tours);
                near_rcv.setAdapter(nearTourAdapter);


            }

            @Override
            public void onTourLoaded(Tour Tour) {

            }

        });




        return view;
    }

    public void getData(final DataCallback callback){
        ref.child("tours").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Tour tour=snapshot.getValue(Tour.class);
                tour.setId(snapshot.getKey().toString());
                String id=snapshot.getKey();
                tour.setId(id);
//                updateInfo(id);
                tours.put(id, tour);
                callback.onDataLoaded(new ArrayList<>(tours.values()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String idTourModified=snapshot.getKey();
                Tour tourModified=snapshot.getValue(Tour.class);
                tourModified.setId(snapshot.getKey().toString());
                tours.put(idTourModified, tourModified);
                callback.onDataLoaded(new ArrayList<>(tours.values()));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String idTourDeleted=snapshot.getKey();
                tours.remove(idTourDeleted);
                callback.onDataLoaded(new ArrayList<>(tours.values()));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    public void updateInfo(String tourName) {
//        DatabaseReference tourRef = ref.child("tours").child(tourName);
//
//        tourRef.child("reviews").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                int numComment = 0;
//                double numRate = 0;
//
//                for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
//                    for (DataSnapshot ds : reviewSnapshot.getChildren()) {
//                        Review review = ds.getValue(Review.class);
//                        numComment++;
//                        numRate += review.getRate();
//                    }
//                }
//
//                double averageRate = numComment > 0 ? numRate / numComment : 0;
//                tourRef.child("numComment").setValue(numComment);
//                tourRef.child("numStar").setValue(averageRate);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Xử lý lỗi nếu cần
//            }
//        });
//    }


    public void getDataVoucher(){
        ref.child("vouchers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Discount d=ds.getValue(Discount.class);
                    discounts.add(d);
                }
                voucherTourAdapter =new VoucherTourAdapter(mainActivity,discounts);
                voucher_rcv.setAdapter(voucherTourAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private static void setOnClickToSearchActivity(View view, String keyword) {
        view.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, SearchAndFilterActivity.class);
            intent.putExtra(SearchAndFilterActivity.TYPE_FILTER_PARAMS, keyword);
            context.startActivity(intent);
        });
    }
}