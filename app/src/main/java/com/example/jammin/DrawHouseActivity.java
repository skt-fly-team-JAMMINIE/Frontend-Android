package com.example.jammin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jammin.utils_draw.GetImages;
import com.example.jammin.utils_draw.GetVoice;
import com.example.jammin.utils_draw.Images;
import com.example.jammin.utils_draw.ImagesResponse;
import com.example.jammin.utils_draw.ModelResponse;
import com.example.jammin.utils_draw.VoiceResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DrawHouseActivity extends Activity {

    //서버 통신 retrofit 객체 불러오기
    //GetImages serviceApi = NetworkModuleKt.getRetrofit().create(GetImages.class);
    //http://34.64.63.93:8000
    //http://127.0.0.1:8000
    //http://10.0.2.2:8000

    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://34.64.220.65:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    Retrofit retrofit2 = new Retrofit.Builder().baseUrl("http://34.64.152.40:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    GetImages serviceApi = retrofit.create(GetImages.class);
    GetVoice ttsApi = retrofit2.create(GetVoice.class);

    private MediaPlayer mediaPlayer = new MediaPlayer();


    ImageButton btn_ok, btn_no, btn_ready, btn_camera;
    TextView textbox;
    ImageView adot, background;

    @SuppressLint("WrongThread")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawhouse);

        btn_ok = findViewById(R.id.btn_ok);
        btn_no = findViewById(R.id.btn_no);
        btn_ready = findViewById(R.id.btn_ready);
        btn_camera = findViewById(R.id.btn_camera);
        textbox = findViewById(R.id.textView);
        adot = findViewById(R.id.adot);
        background = findViewById(R.id.background);

        //첫 tts
        TTS("hello");


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textbox.setText("먼저, 종이 3장이랑\n연필, 지우개를 준비해줘!");
                TTS("ready");
                adot.setImageResource(R.drawable.adot_book);
                btn_ready.setVisibility(View.VISIBLE);
                btn_ok.setVisibility(View.INVISIBLE);
                btn_no.setVisibility(View.INVISIBLE);
            }
        });

        // 이미지 파일 경로나 리소스 ID를 사용하여 비트맵으로 변환
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.house);

        // 비트맵을 JPEG 이미지로 압축하여 ByteArrayOutputStream으로 변환
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        // 바이트 배열을 RequestBody로 변환
        //RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"), baos.toByteArray());

        //String imgString = bitmapToString(bitmap);
        //Bitmap bitmap = BitmapFactory.decodeFile("파일 경로");
        //Log.d("bitmap", imgString);


        btn_ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textbox.setText("종이를 가로로 놓고\n" +
                        "집을 그려보자! 다 그리면\n" +
                        "카메라 버튼을 눌러줘!");
                TTS("house");
                background.setBackgroundResource(R.drawable.background_house);
                adot.setImageResource(R.drawable.adot_drawing);
                btn_ready.setVisibility(View.INVISIBLE);
                btn_camera.setVisibility(View.VISIBLE);
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //CameraActivity로 이동
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);


            }
        });

    }


    //bitmap 을  string 형태로 변환하는 메서드 (이렇게 string 으로 변환된 데이터를 mysql 에서 longblob 의 형태로 저장하는식으로 사용가능)
    public String bitmapToString(Bitmap bitmap){
        String image = "";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            image = Base64.getEncoder().encodeToString(byteArray);
        }
        return image;
    }

    //string 을  bitmap 형태로 변환하는 메서드
    public Bitmap stringToBitmap(String data){
        Bitmap bitmap = null;
        byte[] byteArray = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            byteArray = Base64.getDecoder().decode(data);
        }
        ByteArrayInputStream stream = new ByteArrayInputStream(byteArray);
        bitmap = BitmapFactory.decodeStream(stream);
        return bitmap;
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



    //이미지 서버에 보내는 함수
    private void postImages(Images images) {
        serviceApi.PostImages(images).enqueue(new Callback<ImagesResponse>() {
            @Override
            public void onResponse(Call<ImagesResponse> call, Response<ImagesResponse> response) {
                ImagesResponse result = response.body();
                //서버로부터의 응답을 위에서 정의한 JoinResponse객체에 담는다.
                //Toast.makeText(CreateAccount.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                // getMessage를 통해 성공시 서버로부터 회원가입 성공이라는 메시지를 받음
//                if(result.getStatus() == 200) {
//                    finish();  //getStatus로 받아온 코드가 200(OK)면 회원가입 프래그먼트 종료
//                }
                Log.d("Onresponse", result.toString());
            }

            @Override
            public void onFailure(Call<ImagesResponse> call, Throwable t) {

            }
        });
    }
}

