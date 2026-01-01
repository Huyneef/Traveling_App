package com.example.traveling_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.traveling_app.activity.DetailActivity;
import com.example.traveling_app.R;
import com.example.traveling_app.common.DatabaseReferences;
import com.example.traveling_app.model.tour.Tour;
import com.example.traveling_app.model.filter.FilterItemGroup;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class TourSearchResultAdapter extends RecyclerView.Adapter<ResultViewHolder> {

    private final ArrayList<Tour> tourList = new ArrayList<>();
    private final String keyword;
    private final FilterItemGroup[] filters;
    private final Query query = DatabaseReferences.TOURS_DATABASE_REF.orderByKey();
    private final RequestManager imageLoader;

    private final ChildEventListener listener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            if (snapshot.child("name").getValue(String.class).toLowerCase().contains(keyword) && Arrays.stream(filters).allMatch(f -> f.isSatisfied(snapshot.child(f.getKey()).getValue()))) {
                Tour tour = snapshot.getValue(Tour.class);
                tour.setId(snapshot.getKey());
                tourList.add(tour);
                notifyDataSetChanged();
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
        @Override
        public void onCancelled(@NonNull DatabaseError error) { }
    };



    public TourSearchResultAdapter(Context context, String keyword, Stream<FilterItemGroup> filterGroups) {
        this.keyword = keyword;
        this.filters = filterGroups.toArray(FilterItemGroup[]::new);
        this.imageLoader = Glide.with(context);
        query.addChildEventListener(listener);
    }
    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        ViewGroup tourInfoContainer = (ViewGroup) inflate.inflate(R.layout.tour_list_item, parent, false);
        return new ResultViewHolder(tourInfoContainer, imageLoader);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        holder.bindToView(tourList.get(position));
    }

    public void stopListening() {
        query.removeEventListener(listener);
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

}

class ResultViewHolder extends RecyclerView.ViewHolder {
    private final TextView title, place, oldPrice, newPrice, bookCount, rateCount;
    private final ImageView thumbnail, star;
    private final RequestManager imageLoader;
    private Tour tour;

    public ResultViewHolder(@NonNull View itemView, RequestManager imageLoader) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        star = itemView.findViewById(R.id.star);
        place = itemView.findViewById(R.id.place);
        oldPrice = itemView.findViewById(R.id.oldPrice);
        newPrice = itemView.findViewById(R.id.newPrice);
        bookCount = itemView.findViewById(R.id.bookCount);
        rateCount = itemView.findViewById(R.id.rateCount);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        this.imageLoader = imageLoader;

        itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("id", tour.getId());
            context.startActivity(intent);
        });
    }

    void bindToView(Tour tour) {
        this.tour = tour;
        title.setText(tour.getName());
        switch ((int) Math.round(tour.getNumStar())) {
            case 0:
                star.setImageResource(R.drawable.star_0);
                break;
            case 1:
                star.setImageResource(R.drawable.star_1);
                break;
            case 2:
                star.setImageResource(R.drawable.star_2);
                break;
            case 3:
                star.setImageResource(R.drawable.star_3);
                break;
            case 4:
                star.setImageResource(R.drawable.star_4);
                break;
            default:
                star.setImageResource(R.drawable.star_5);
                break;

        }
        place.setText(tour.getAddress());
        oldPrice.setText(Html.fromHtml("<strike>" + tour.getPrice() + " VND</strike>", HtmlCompat.FROM_HTML_MODE_LEGACY));
        newPrice.setText(tour.getSalePrice() + " VND");
        bookCount.setText(bookCount.getContext().getString(R.string.book_count, tour.getNumBooking()));
        rateCount.setText(bookCount.getContext().getString(R.string.rate_count, tour.getNumComment()));
        imageLoader.load(tour.getMainImageUrl()).into(thumbnail);
    }
}
