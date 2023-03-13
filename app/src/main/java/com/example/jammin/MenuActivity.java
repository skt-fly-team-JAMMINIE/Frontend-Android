package com.example.jammin;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button start_draw = findViewById(R.id.start);
        Button report = findViewById(R.id.report);

        report.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), ReportList.class);
//                startActivity(intent);
//            }
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Input_pwd.class);
                startActivity(intent);
            }
        });

        start_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DrawHouseActivity.class);
                startActivity(intent);
            }
        });
    }
}
