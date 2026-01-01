package com.example.traveling_app.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;

import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.traveling_app.activity.LoginActivity;
import com.example.traveling_app.R;
import com.example.traveling_app.activity.ShareAppActivity;
import com.example.traveling_app.activity.PointActivity;
import com.example.traveling_app.activity.TourHistoryActivity;
import com.example.traveling_app.activity.AboutActivity;
import com.example.traveling_app.activity.ProfileActivity;
import com.example.traveling_app.model.other.MenuSectionItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class Constants {

    static {
        ArrayList<MenuSectionItem> menuSectionItems = new ArrayList<>();
        menuSectionItems.add(new MenuSectionItem(R.drawable.baseline_person_outline_24, R.string.profile_page, ProfileActivity.class));

        menuSectionItems.add(new MenuSectionItem(R.drawable.baseline_directions_car_24, R.string.place_went, TourHistoryActivity.class));
        menuSectionItems.add(new MenuSectionItem(R.drawable.baseline_share_24_1, R.string.share_app, ShareAppActivity.class));


        menuSectionItems.add(new MenuSectionItem(R.drawable.baseline_info_24_1, R.string.about_us, AboutActivity.class));
        menuSectionItems.add(new MenuSectionItem(R.drawable.baseline_logout_24, R.string.log_out, LoginActivity.class));
        MENU_SECTION_ITEMS = Collections.unmodifiableList(menuSectionItems);
    }

    public static final List<MenuSectionItem> MENU_SECTION_ITEMS;

    public static final void getCurrentAddress(Activity activity, Runnable onFailedListener, Consumer<Address> onSuccessListener) {
        Context context = activity.getApplicationContext();
        if (activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
            fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_LOW_POWER, null).addOnSuccessListener(activity, location -> {
                if (location != null) {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    Address address;
                    try {
                        address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
                        onSuccessListener.accept(address);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    onFailedListener.run();
                }

            });

        } else {
            activity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    public static final String DEFAULT_USERNAME = "defaultuser0";

    public static final PickVisualMediaRequest PICK_PHOTO_REQUEST = new PickVisualMediaRequest.Builder().setMediaType(new ActivityResultContracts.PickVisualMedia.SingleMimeType("image/jpeg")).build();
    public static final ActivityResultContract<PickVisualMediaRequest, Uri> PICK_PHOTO_RESULT_CONTRACT = new ActivityResultContracts.PickVisualMedia();

}
