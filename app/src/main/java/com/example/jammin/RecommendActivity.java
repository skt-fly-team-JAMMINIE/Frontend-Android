package com.example.jammin;

import android.os.Bundle;
import android.util.Log;

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

    Integer keyword;

    CallModel CallkeywordApi = retrofit.create(CallModel.class);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_recommend_music);

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
                        //setContentView(R.layout.activity_recommend_sleep);
                        Log.d("keyword", keyword.toString());
                        break;
                    case 1:
                        setContentView(R.layout.activity_recommend_mission_stress);
                        Log.d("keyword", keyword.toString());
                        break;
                    case 2:
                        setContentView(R.layout.activity_recommend_music);
                        Log.d("keyword", keyword.toString());
                        break;
                    case 3:
                        setContentView(R.layout.activity_recommend_book);
                        Log.d("keyword", keyword.toString());
                        break;
                    case 4:
                        //setContentView(R.layout.activity_recommend_movie);
                        Log.d("keyword", keyword.toString());
                        break;
                    case 5:
                        //setContentView(R.layout.activity_recommend_video_bread);
                        Log.d("keyword", keyword.toString());
                        break;
                    case 6:
                        //setContentView(R.layout.activity_recommend_video_doll);
                        Log.d("keyword", keyword.toString());
                        break;
                    case 7:
                        //setContentView(R.layout.activity_recommend_movie);
                        Log.d("keyword", keyword.toString());
                        break;
                    case 8:
                        break;
                    case 9:
                        //setContentView(R.layout.activity_recommend_book);
                        Log.d("keyword", keyword.toString());
                        break;
                    case 10:
                        //setContentView(R.layout.activity_recommend_music);
                        Log.d("keyword", keyword.toString());
                        break;
                    case 11:
                        //setContentView(R.layout.activity_recommend_music);
                        Log.d("keyword", keyword.toString());
                        break;
                    case 12:
                        //setContentView(R.layout.activity_recommend_diary);
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
