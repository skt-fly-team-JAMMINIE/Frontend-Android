package com.example.jammin;

import java.io.IOException;
import java.io.*;
import java.util.Arrays;
import java.util.List;

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

import com.example.jammin.utils_draw.GetImages;
import com.example.jammin.utils_draw.UploadImageResponse;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CameraActivity2 extends AppCompatActivity implements SurfaceHolder.Callback {
    ImageView adot, bounding;
    TextView textbox;
    ImageButton btn_camera;
    SurfaceView cameraSurfaceView;
    //Surface cameraSurfaceView;
    Camera camera;
    //CameraDevice camera;
    String str;

    SurfaceHolder surfaceHolder;
    boolean previewing = false;

    //HTPback 주소 쓰기
    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://34.64.220.65:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    GetImages serviceApi = retrofit.create(GetImages.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_tree);

        SharedPreferences sharedPreferences = getSharedPreferences("image_tree", MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();

        adot = findViewById(R.id.imageView2);
        bounding = findViewById(R.id.bounding2);
        textbox = findViewById(R.id.textbox2);
        btn_camera = findViewById(R.id.btn_camera3);
        cameraSurfaceView = findViewById(R.id.surfaceView2);
        Camera.PictureCallback jpegCallback;


        jpegCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                //FileOutputStream outputStream = null;
                //str = String.format("/sdcard/%d.jpg", System.currentTimeMillis());
//                Toast.makeText(getApplicationContext(), "Picture Saved",
//                        Toast.LENGTH_LONG).show();
//                Log.d("Picture",bytes.toString());
                refreshCamera();
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                //이미지 회전
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                //이미지 Bitmap -> String
                byte[] bytes2 = baos.toByteArray();
                String temp2 = Base64.encodeToString(bytes2, Base64.DEFAULT);

                //이미지 보고서 페이지에 전송
                editor.putString("image_tree", temp2); // key,value 형식으로 저장
                editor.commit();

                RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), baos.toByteArray());

                MultipartBody.Part fileToUploadtree = MultipartBody.Part.createFormData("image", "image_tree.jpg", requestBody);

                Call<UploadImageResponse> call = serviceApi.UploadTreeImage(fileToUploadtree);
                // 요청 실행 및 응답 처리
                call.enqueue(new Callback<UploadImageResponse>() {
                    @Override
                    public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                        // 응답 본문을 문자열로 변환하여 출력
                        String responsetoString = response.body().imagefile;
                        Log.d("Yeji", responsetoString);
                    }

                    @Override
                    public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failure",
                                Toast.LENGTH_LONG).show();
                    }
                });
                Intent intent = new Intent(getApplicationContext(), DrawPersonActivity.class);
                startActivity(intent);

                finish();

            }
        };

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.takePicture(null, null, jpegCallback);
            }
        });

        getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceHolder = cameraSurfaceView.getHolder();
        Surface previewSurface = cameraSurfaceView.getHolder().getSurface();
        List<Surface> targets = Arrays.asList(previewSurface);

//        try {
//            camera.createCaptureSession(targets, new CameraCaptureSession.StateCallback() {
//                @Override
//                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
//
//                }
//
//                @Override
//                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
//
//                }
//            }, null);
//        } catch (CameraAccessException e) {
//            throw new RuntimeException(e);
//        }

        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


//        camera.createCaptureRequest(camera.getId());
//        //camera = Camera.open();
//        try {
//            camera.setPreviewDisplay(surfaceHolder);
//            camera.startPreview();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        btn_camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(camera != null){
//                    camera.startPreview();
//                    camera.release();
//                    camera = null;
//                }
//            }
//        });


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

