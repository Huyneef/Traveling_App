package com.example.traveling_app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveling_app.R;
import com.example.traveling_app.model.other.Service;
import com.example.traveling_app.adapter.ServiceTourAdapter;
import com.example.traveling_app.model.tour.Tour;
import com.example.traveling_app.activity.BookTourActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {
    private List<Service> services=new ArrayList<>();
    private RecyclerView service_rcv;
    private View view;
    private TextView book_tour,tour_detail_des;

    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("tours");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_detail, container, false);

        book_tour=view.findViewById(R.id.book_tour);
        tour_detail_des=view.findViewById(R.id.tour_detail_des);
        bindingData();
        book_tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idTour= getActivity().getIntent().getStringExtra("id");
                Intent intent = new Intent(getContext(), BookTourActivity.class);
                intent.putExtra("id",idTour);
                startActivity(intent);
            }
        });

        service_rcv=view.findViewById(R.id.service_rcv);
        services.add(new Service(R.drawable.face_smile,"Được hỗ trợ miễn phí phương tiện di chuyển"));
        services.add(new Service(R.drawable.face_smile,"Thưởng thức bữa sáng miễn phí"));
        services.add(new Service(R.drawable.face_smile,"Ngủ 1 đêm tại khách sạn tại phòng Standard"));
        services.add(new Service(R.drawable.face_smile,"Nhận voucher 500.000 đ"));

        ServiceTourAdapter serviceTourAdapter=new ServiceTourAdapter(getContext(),services);
        LinearLayoutManager ln=new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        service_rcv.setLayoutManager(ln);
        service_rcv.setAdapter(serviceTourAdapter);
        return view;
    }

    public void bindingData(){
        String idTour= getActivity().getIntent().getStringExtra("id");
        ref.child(idTour).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Tour tour=snapshot.getValue(Tour.class);
                tour_detail_des.setText(tour.getContent());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}