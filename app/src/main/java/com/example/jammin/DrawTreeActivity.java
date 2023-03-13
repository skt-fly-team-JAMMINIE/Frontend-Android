package com.example.jammin;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import com.example.jammin.DrawHouseActivity;
import com.example.jammin.utils_draw.GetVoice;
import com.example.jammin.utils_draw.VoiceResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DrawTreeActivity extends Activity {

    ImageButton btn_camera;

    Retrofit retrofit2 = new Retrofit.Builder().baseUrl("http://34.64.152.40:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    GetVoice ttsApi = retrofit2.create(GetVoice.class);

    private MediaPlayer mediaPlayer = new MediaPlayer();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawtree);
        TTS("tree");

        btn_camera = findViewById(R.id.btn_camera2);

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity2.class);
                startActivity(intent);
            }
        });
    }

    public void TTS(String type) {
        Call<VoiceResponse> tts_call = ttsApi.TTSVoice(type);
        tts_call.enqueue(new Callback<VoiceResponse>() {
            @Override
            public void onResponse(Call<VoiceResponse> call, Response<VoiceResponse> response) {
                String base64String = response.body().sentence;
                byte[] base64Bytes = android.util.Base64.decode(base64String.getBytes(), android.util.Base64.DEFAULT);
                playMp3(base64Bytes);
                Log.d("tts", base64Bytes.toString());
            }

            @Override
            public void onFailure(Call<VoiceResponse> call, Throwable t) {
                Log.d("tts", t.toString());
            }
        });
    }

    public void playMp3(byte[] mp3SoundByteArray) {
        try {
            // create temp file that will hold byte array
            File tempMp3 = File.createTempFile("kurchina", "mp3", getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();

            // resetting mediaplayer instance to evade problems
            mediaPlayer.reset();

            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());

            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }
    }
}
