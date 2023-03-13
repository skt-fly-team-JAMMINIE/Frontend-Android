package com.example.jammin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.jammin.databinding.ActivityLoadingDialogChatBinding
import com.example.jammin.utils_chat.UserChatInterface
import com.example.jammin.utils_chat.getRetrofit
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class LoadingDialog_chat : AppCompatActivity() {

    lateinit var binding:ActivityLoadingDialogChatBinding

    // get retrofit 2 >> UserChatInterface 유저가 입력한 채팅 post할 interface
    val inputService2 = getRetrofit().create(UserChatInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingDialogChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        inputService2.getAnalysis().enqueue(object : retrofit2.Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                Log.d("aidot-analysis-page", "LoadingDialog_chat")
                Log.d("aidot-analysis-response", response.toString())
                val intent:Intent = Intent(this@LoadingDialog_chat,RecommendActivity::class.java)
                startActivity(intent)

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("aidot-analysis-onFail", t.toString())
            }

        })
    }

}