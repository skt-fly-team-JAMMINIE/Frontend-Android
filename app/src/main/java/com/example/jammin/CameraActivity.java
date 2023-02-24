package com.example.jammin;

import java.io.IOException;
import java.io.*;
import java.util.Arrays;
import java.util.List;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
//import android.hardware.camera2.*;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
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


public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {
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
        setContentView(R.layout.activity_camera);

        adot = findViewById(R.id.imageView);
        bounding = findViewById(R.id.bounding);
        textbox = findViewById(R.id.textbox);
        btn_camera = findViewById(R.id.btn_camera);
        cameraSurfaceView = findViewById(R.id.surfaceView);
        Camera.PictureCallback jpegCallback;


        jpegCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                //FileOutputStream outputStream = null;
                //str = String.format("/sdcard/%d.jpg", System.currentTimeMillis());
                Toast.makeText(getApplicationContext(), "Picture Saved",
                        Toast.LENGTH_LONG).show();
                Log.d("Yeji", bytes.toString());
                refreshCamera();
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                //이미지 회전
//                Matrix matrix = new Matrix();
//                matrix.postRotate(90);
//                bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                //여기까지
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), baos.toByteArray());

                MultipartBody.Part fileToUploadhouse = MultipartBody.Part.createFormData("image", "image_house.jpg", requestBody);
                Log.d("Yeji", bytes.toString());
                Call<UploadImageResponse> call = serviceApi.UploadHouseImage(fileToUploadhouse);
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
                        Log.d("Yeji", "통신 실패");
                        Toast.makeText(getApplicationContext(), "Failure",
                                Toast.LENGTH_LONG).show();
                    }
                });
                //보고서 페이지에 이미지 전달
                //Intent intentforReport = new Intent(getApplicationContext(), NetworkModuleKt.class);
                //intentforReport.putExtra("img_house", bmp);
                Intent intent = new Intent(getApplicationContext(), DrawTreeActivity.class);
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
        if (surfaceHolder.getSurface() == null) {
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

    private boolean checkCAMERAPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    final int MY_PERMISSION_REQUEST_CODE = 100;
    int APIVersion = Build.VERSION.SDK_INT;

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

        if (APIVersion >= Build.VERSION_CODES.M) {
            if (checkCAMERAPermission()) {
                //예지코드였던것
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                //camera = Camera.open();
                //기존 카메라 회전 코드
//                Camera.Parameters param = camera.getParameters();
//                param.setRotation(90);
//                camera.setDisplayOrientation(90);


                // 카메라 미리보기 프레임의 크기를 가로와 세로를 바꾸어 설정합니다
                Camera.Parameters parameters = camera.getParameters();
                // 미리보기 화면 회전 각도 설정
                int rotation = getWindowManager().getDefaultDisplay().getRotation();
                int degrees = 0;
                switch (rotation) {
                    case Surface.ROTATION_0:
                        degrees = 90;
                        break;
                    case Surface.ROTATION_90:
                        degrees = 0;
                        break;
                    case Surface.ROTATION_180:
                        degrees = 270;
                        break;
                    case Surface.ROTATION_270:
                        degrees = 180;
                        break;
                }
                camera.setDisplayOrientation(degrees);

                // 미리보기 사이즈 설정
                Camera.Size optimalSize = getOptimalPreviewSize(parameters.getSupportedPreviewSizes(), cameraSurfaceView.getWidth(), cameraSurfaceView.getHeight());
                parameters.setPreviewSize(optimalSize.width, optimalSize.height);
                camera.setParameters(parameters);



                // 카메라 미리보기를 회전시킵니다
                //parameters.setRotation(90);
                camera.setDisplayOrientation(90);


                try {
                    camera.setPreviewDisplay(surfaceHolder);
                    camera.startPreview();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    if (cameraAccepted) {
                        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                    }
                }
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
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

    // 카메라 미리보기 프레임의 크기를 계산하는 메서드입니다
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }
}

