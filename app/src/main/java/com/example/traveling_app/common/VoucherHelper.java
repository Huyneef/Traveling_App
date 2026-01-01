package com.example.traveling_app.common;

import com.example.traveling_app.model.tour.Discount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VoucherHelper {
    private DatabaseReference databaseReference;

    public VoucherHelper() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("vouchers");
    }
    // Create or update a voucher
    public void saveVoucher(Discount voucher) {
        if (voucher.getId() == null) {
            String id = databaseReference.push().getKey();
            voucher.setId(id);
        }

        // Save the voucher to Firebase
        databaseReference.child(voucher.getId()).setValue(voucher);
    }
    public void readVoucher(String id, final OnVoucherDataChangedListener listener) {
        databaseReference.child(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Discount voucher = task.getResult().getValue(Discount.class);
                listener.onVoucherDataChanged(voucher);
            } else {
                listener.onError(task.getException().getMessage());
            }
        });
    }
    public void updateVoucher(Discount voucher) {
        saveVoucher(voucher); // Since Firebase is NoSQL, updating is the same as saving
    }

    // Delete a voucher
    public void deleteVoucher(String id) {
        databaseReference.child(id).removeValue();
    }
    public interface OnVoucherDataChangedListener {
        void onVoucherDataChanged(Discount voucher);
        void onError(String errorMessage);
    }

}
