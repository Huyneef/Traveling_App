package com.example.traveling_app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.example.traveling_app.R;

public class LoadingDialog extends Dialog {

    private final int stringRes;
    public LoadingDialog(@NonNull Context context, @StringRes int stringRes) {
        super(context);
        this.stringRes = stringRes;
        setCancelable(false);
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.loading_dialog_layout);
        ((TextView) findViewById(R.id.loadingMessageTextView)).setText(stringRes);
    }

    @Override
    public void show() {
        super.show();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
