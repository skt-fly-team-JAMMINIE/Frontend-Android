package com.example.jammin;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jammin.utils_draw.CallModel;
import com.example.jammin.utils_draw.KeywordResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecommendActivity extends AppCompatActivity {
    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://34.64.220.65:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    MediaPlayer mediaPlayer;

    Integer keyword;

    CallModel CallkeywordApi = retrofit.create(CallModel.class);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_music);

//        ImageButton btn_hamburger = findViewById(R.id.btn_hamburger);
//        btn_hamburger.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent2 = new Intent(getApplicationContext(), MenuActivity.class);
//                startActivity(intent2);
//            }
//        });


        Call<KeywordResponse> keyword_call = CallkeywordApi.CallKeyword(1);
        keyword_call.enqueue(new Callback<KeywordResponse>() {
            @Override
            public void onResponse(Call<KeywordResponse> call, Response<KeywordResponse> response) {
                keyword = response.body().result;
                Log.d("response", keyword.toString());

                //0 감정조절 - 명상
                //1 스트레스 - 산책
                //2 슬픔 - 노래(사건의 지평선)
                //3 속상 - 책(용서의 정원)
                //4 외로움 - 영화(코코)
                //5 불안 - 영상(브레드이발소)
                //6 걱정 - 영상(인형)
                //7 긴장 - 영화(랄프)
                //8 중립 - 없음
                //9 긍정 - 책(몬스터 차일드)
                //10 자신감 - 노래(Stronger)
                //11 성실 - 노래(수고했어 오늘도)
                //12 행복 - 일기


                switch (keyword){
                    case 0:
                        setContentView(R.layout.activity_recommend_mission_sleep);
                        ImageButton btn_hamburger = findViewById(R.id.btn_hamburger);
                        btn_hamburger.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2 = new Intent(getApplicationContext(), MenuActivity.class);
                                startActivity(intent2);
                            }
                        });
                        Log.d("keyword", keyword.toString());
                        break;
                    case 1:
                        setContentView(R.layout.activity_recommend_mission_stress);
                        ImageButton btn_hamburger1 = findViewById(R.id.btn_hamburger);
                        btn_hamburger1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2 = new Intent(getApplicationContext(), MenuActivity.class);
                                startActivity(intent2);
                            }
                        });
                        Log.d("keyword", keyword.toString());
                        break;
                    case 2:
                        setContentView(R.layout.activity_recommend_music);
                        ImageButton btn_hamburger2 = findViewById(R.id.btn_hamburger);

                        btn_hamburger2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2 = new Intent(getApplicationContext(), MenuActivity.class);
                                startActivity(intent2);
                            }
                        });
                        Log.d("keyword", keyword.toString());
                        break;
                    case 3:
                        setContentView(R.layout.activity_recommend_book);
                        ImageButton btn_hamburger3 = findViewById(R.id.btn_hamburger);
                        btn_hamburger3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2 = new Intent(getApplicationContext(), MenuActivity.class);
                                startActivity(intent2);
                            }
                        });
                        Log.d("keyword", keyword.toString());
                        break;
                    case 4:
                        setContentView(R.layout.activity_recommend_movie_coco);
                        ImageButton btn_hamburger4 = findViewById(R.id.imageButton);
                        btn_hamburger4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2 = new Intent(getApplicationContext(), MenuActivity.class);
                                startActivity(intent2);
                            }
                        });
                        Log.d("keyword", keyword.toString());
                        break;
                    case 5:
                        setContentView(R.layout.activity_recommend_video_bread);
                        ImageButton btn_hamburger5 = findViewById(R.id.btn_hamburger);
                        btn_hamburger5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2 = new Intent(getApplicationContext(), MenuActivity.class);
                                startActivity(intent2);
                            }
                        });
                        Log.d("keyword", keyword.toString());
                        break;
                    case 6:
                        setContentView(R.layout.activity_recommend_mission_doll);
                        ImageButton btn_hamburger6 = findViewById(R.id.imageButton);
                        btn_hamburger6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2 = new Intent(getApplicationContext(), MenuActivity.class);
                                startActivity(intent2);
                            }
                        });
                        Log.d("keyword", keyword.toString());
                        break;
                    case 7:
                        setContentView(R.layout.activity_recommend_movie_ralph);
                        ImageButton btn_hamburger7 = findViewById(R.id.imageButton);
                        btn_hamburger7.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2 = new Intent(getApplicationContext(), MenuActivity.class);
                                startActivity(intent2);
                            }
                        });
                        Log.d("keyword", keyword.toString());
                        break;
                    case 8:
                        break;
                    case 9:
                        setContentView(R.layout.activity_recommend_book);
                        ImageView playlist = findViewById(R.id.playlist);
                        TextView text_title = findViewById(R.id.text_title);
                        TextView text_content = findViewById(R.id.text_content);
                        //몬스터 차일드로 바꾸기
                        playlist.setBackgroundResource(R.drawable.playlist_book_monster);
                        text_title.setText("몬스터 차일드 (이재윤)");
                        text_content.setText("제 1회 사계절 어린이 문학상 대상 수상작! 이재윤 장편소설\n" +
                                "차별과 편견의 벽을 뛰어넘기 위한 돌연변이들의 힘찬 도약");
                        ImageButton btn_hamburger9 = findViewById(R.id.btn_hamburger);
                        btn_hamburger9.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2 = new Intent(getApplicationContext(), MenuActivity.class);
                                startActivity(intent2);
                            }
                        });
                        Log.d("keyword", keyword.toString());
                        break;
                    case 10:
                        setContentView(R.layout.activity_recommend_music);
                        TextView text = findViewById(R.id.textView);
                        text.setText("멋진 너에게\n" +
                                "이 노래를 추천해주고 싶어!");
                        //노래 Stronger로 바꾸기
                        TextView text_title_music = findViewById(R.id.text_title);
                        text_title_music.setText("Stronger (What Doesn’t Kill You)");
                        TextView text_conent_music = findViewById(R.id.text_content);
                        text_conent_music.setText("Kelly Clarkson");
                        ImageButton btn_hamburger10 = findViewById(R.id.btn_hamburger);
                        btn_hamburger10.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2 = new Intent(getApplicationContext(), MenuActivity.class);
                                startActivity(intent2);
                            }
                        });
                        Log.d("keyword", keyword.toString());
                        break;
                    case 11:
                        setContentView(R.layout.activity_recommend_music);
                        //노래 수고했어 오늘도
                        TextView text2 = findViewById(R.id.textView);
                        text2.setText("열심히 산 오늘\n" +
                                "추천해주고 싶은 노래");
                        //노래 Stronger로 바꾸기
                        TextView text_title_music2 = findViewById(R.id.text_title);
                        text_title_music2.setText("수고했어, 오늘도");
                        TextView text_conent_music2 = findViewById(R.id.text_content);
                        text_conent_music2.setText("옥상달빛");
                        ImageButton btn_hamburger11 = findViewById(R.id.btn_hamburger);
                        btn_hamburger11.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2 = new Intent(getApplicationContext(), MenuActivity.class);
                                startActivity(intent2);
                            }
                        });
                        Log.d("keyword", keyword.toString());
                        break;
                    case 12:
                        setContentView(R.layout.activity_recommend_diary);
                        ImageButton btn_hamburger12 = findViewById(R.id.imageButton);
                        btn_hamburger12.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2 = new Intent(getApplicationContext(), MenuActivity.class);
                                startActivity(intent2);
                            }
                        });
                        Log.d("keyword", keyword.toString());
                        break;
                }
            }

            @Override
            public void onFailure(Call<KeywordResponse> call, Throwable t) {
                Log.d("response", "통신오류");
            }
        });

    }

}
