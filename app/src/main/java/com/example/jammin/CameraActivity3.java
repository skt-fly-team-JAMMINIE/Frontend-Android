package com.example.jammin;

import java.io.IOException;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
//import android.hardware.camera2.*;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.jammin.utils_draw.CallModel;
import com.example.jammin.utils_draw.GetImages;
import com.example.jammin.utils_draw.ModelResponse;
import com.example.jammin.utils_draw.UploadImageResponse;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CameraActivity3 extends AppCompatActivity implements SurfaceHolder.Callback {
    ImageView adot, bounding;
    TextView textbox;
    ImageButton btn_camera;
    SurfaceView cameraSurfaceView;
    //Surface cameraSurfaceView;
    Camera camera;
    //CameraDevice camera;
    String str;

    LoadingDialog loadingDialog;

    SurfaceHolder surfaceHolder;
    boolean previewing = false;

    //타임아웃 시간 설정
    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(1000000, TimeUnit.SECONDS)
            .readTimeout(1000000, TimeUnit.SECONDS)
            .writeTimeout(1000000, TimeUnit.SECONDS)
            .build();

    //HTPback 주소 쓰기
    private final Retrofit retrofit = new Retrofit.Builder().baseUrl("http://34.64.220.65:8000")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    GetImages serviceApi = retrofit.create(GetImages.class);
    CallModel CallmodelApi = retrofit.create(CallModel.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_tree);

        SharedPreferences sharedPreferences = getSharedPreferences("image_person", MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();

        adot = findViewById(R.id.imageView2);
        bounding = findViewById(R.id.bounding2);
        textbox = findViewById(R.id.textbox2);
        btn_camera = findViewById(R.id.btn_camera3);
        cameraSurfaceView = findViewById(R.id.surfaceView2);
        Camera.PictureCallback jpegCallback;
        loadingDialog = new LoadingDialog(this);

        textbox.setText("사람 그림을 찍어줘");


        jpegCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                //FileOutputStream outputStream = null;
                //str = String.format("/sdcard/%d.jpg", System.currentTimeMillis());
//                Toast.makeText(getApplicationContext(), "Picture Saved",
//                        Toast.LENGTH_LONG).show();
                Log.d("Picture",bytes.toString());
                refreshCamera();
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);


                //이미지회전
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                //이미지 Bitmap -> String
                byte[] bytes3 = baos.toByteArray();
                String temp3 = Base64.encodeToString(bytes3, Base64.DEFAULT);

                //이미지 보고서 페이지에 전송
                editor.putString("image_person", temp3); // key,value 형식으로 저장
                editor.commit();

                RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), baos.toByteArray());
                MultipartBody.Part fileToUploadperson = MultipartBody.Part.createFormData("image", "image_person.jpg", requestBody);

                Call<UploadImageResponse> call = serviceApi.UploadPersonImage(fileToUploadperson);
                // 요청 실행 및 응답 처리
                call.enqueue(new Callback<UploadImageResponse>() {
                    @Override
                    public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                        // 응답 본문을 문자열로 변환하여 출력
                        String responsetoString = response.body().imagefile;
                        Log.d("Yeji", responsetoString);

                        Call<ModelResponse> model_call = CallmodelApi.CallModel(1);
                        //1. 잠시 기다려줘 화면
                        loadingDialog.show();

                        //2. Intent하기(대화시작)
                        model_call.enqueue(new Callback<ModelResponse>() {
                            @Override
                            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                                Log.d("Yeji", response.body().result);
                                //Intent하기
                                //Intent intent = new Intent(getApplicationContext(), AidotChatbot.class);
                                Intent intent = new Intent(getApplicationContext(), ReportList.class);
                                startActivity(intent);
                                loadingDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<ModelResponse> call, Throwable t) {
                                Log.d("Yeji", "모델부르기실패");
                                //Intent하기
//                                Intent intent = new Intent(getApplicationContext(), AidotChatbot.class);
//                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failure",
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        };

        //            @Override
//            public void onClick(View view) {
//                camera.takePicture(null, null, jpegCallback);
//            }
        btn_camera.setOnClickListener(view -> {
            camera.takePicture(null, null, jpegCallback);
        });

        getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceHolder = cameraSurfaceView.getHolder();
        Surface previewSurface = cameraSurfaceView.getHolder().getSurface();
        List<Surface> targets = Arrays.asList(previewSurface);


        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);



    }

    private void refreshCamera() {
        if (surfaceHolder.getSurface() == null){
            return;
        }
        camera.stopPreview();
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkCAMERAPermission(){
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    final int MY_PERMISSION_REQUEST_CODE = 100;
    int APIVersion = Build.VERSION.SDK_INT;
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

        if (APIVersion >= Build.VERSION_CODES.M){
            if(checkCAMERAPermission()){
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                //camera = Camera.open();
                Camera.Parameters param = camera.getParameters();
                param.setRotation(0);
                camera.setDisplayOrientation(90);
                camera.setParameters(param);
                camera.startPreview();

                try {
                    camera.setPreviewDisplay(surfaceHolder);
                    camera.startPreview();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0) {
                    boolean cameraAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    if(cameraAccepted){
                        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                    }
                }
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//        if(camera != null){
//            try {
//                camera.setPreviewDisplay(surfaceHolder);
//                camera.startPreview();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

}


