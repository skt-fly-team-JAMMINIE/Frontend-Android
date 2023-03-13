package com.example.jammin;

import static java.lang.Thread.sleep;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public class LoadingDialog extends AlertDialog {
    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.Dialog);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //ImageView adot_loading = findViewById(R.id.adot_loading);

//        while(true){
//            adot_loading.setBackgroundResource(R.drawable.adot_load2);
//
//        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);
        //ImageView adot = findViewById(R.id.adot_load);



    }
}
