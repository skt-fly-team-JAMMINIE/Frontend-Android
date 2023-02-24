package com.example.jammin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jammin.utils_draw.GetImages;
import com.example.jammin.utils_draw.Images;
import com.example.jammin.utils_draw.ImagesResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    GetImages serviceApi = retrofit.create(GetImages.class);


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

        //보낼 이미지 정의 후 보내기
//        String house_img = "house_img";
//        String tree_img = "tree_img";
//        String person_img = "person_img";
//        Images images = new Images(house_img, tree_img, person_img);
//        postImages(images);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textbox.setText("먼저, 종이 3장이랑\n연필, 지우개를 준비해줘!");
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



                //*******///
                //이미지 전송 코드
//                File imagefile = new File("res/drawable/house.jpg");
//                RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), baos.toByteArray());
//                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", imagefile.getName(), requestBody);
//
//                Call<UploadImageResponse> call = serviceApi.UploadImage(fileToUpload);
//                Log.d("Yeji", fileToUpload.toString());
//                // 요청 실행 및 응답 처리
//                call.enqueue(new Callback<UploadImageResponse>() {
//                    @Override
//                    public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
//                         //응답 본문을 문자열로 변환하여 출력
//                        String responseString = response.body().toString();
//                        Log.d("Yeji", "Response: " + responseString);
//                    }
//
//                    @Override
//                    public void onFailure(Call<UploadImageResponse> call, Throwable t) {
//                        t.printStackTrace();
//                    }
//                });
                ////*********///

//                serviceApi.UploadImage(fileToUpload).enqueue(new Callback<UploadImageResponse>() {
//                    @Override
//                    public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
//                        UploadImageResponse result = response.body();
//                        Log.d("Onresponse", result.toString());
//                    }
//
//                    @Override
//                    public void onFailure(Call<UploadImageResponse> call, Throwable t) {
//
//                    }
//                });
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

