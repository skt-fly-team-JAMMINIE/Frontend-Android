package com.example.jammin;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DrawTreeActivity extends Activity {

    ImageButton btn_camera;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawtree);

        btn_camera = findViewById(R.id.btn_camera2);

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity2.class);
                startActivity(intent);
            }
        });
    }
}
