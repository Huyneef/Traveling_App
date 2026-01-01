package com.example.traveling_app.model.savedtours;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.example.traveling_app.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.io.Closeable;

public class SavedTourAdapter extends FirebaseRecyclerAdapter<SavedTour, SavedTourViewHolder> {

    private static final Runnable EMPTY_RUNNABLE = () -> {};
    private Runnable onDataChangedCallback = EMPTY_RUNNABLE;
    public SavedTourAdapter(@NonNull FirebaseRecyclerOptions<SavedTour> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SavedTourViewHolder holder, int position, @NonNull SavedTour model) {
        holder.bindDataToViewHolder(model);
    }

    @NonNull
    @Override
    public SavedTourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.love_tour_item, parent, false);
        return new SavedTourViewHolder(rootView);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if (onDataChangedCallback != null)
            onDataChangedCallback.run();
    }

    public void setOnDataChanged(Runnable onDataChangedCallback) {
        this.onDataChangedCallback = onDataChangedCallback;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        super.stopListening();
        this.onDataChangedCallback = null;
    }

}
