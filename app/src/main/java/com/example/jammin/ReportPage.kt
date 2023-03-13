package com.example.jammin

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.jammin.databinding.ActivityReportPageBinding
import com.example.jammin.utils_chat.*
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response


// 그림, 챗봇 보고서 페이지
class ReportPage : AppCompatActivity() {

    private var gson: Gson = Gson()
    lateinit var binding: ActivityReportPageBinding
    var keywordTemp1: Int? = -1

    // get retrofit - keyWordReportInterface
    val inputService = getRetrofit().create(KeywordReportInterface::class.java)

    // get retrofit - ReportInterface
    val inputService2 = getRetrofit().create(ReportInterface::class.java)

    var dateInfo = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 화면 정보
        if (intent.hasExtra("dateInfo")) {
            dateInfo = intent.getStringExtra("dateInfo")!!
            Log.d("report-date", dateInfo.toString())
            binding.reportPageDateTv.text = dateInfo.toString()
        }

        // 날짜에 해당하는 리포트 가져옴 - 그림 리포트 , 챗봇 리포트
        getDrawReport(dateInfo)
        getChatReport(dateInfo)


        // 뒤로가기 버튼
        binding.reportPageBackIv.setOnClickListener {
            finish()
        }

        // keyword에 맞는 리포트 가져옴
        /**
        Log.d("getKeywordSen",getKeywordReport(1))
        val keyword1Sen = getKeywordReport(1)
        Log.d("keyword1sen",keyword1Sen)
        binding.reportChatbotDescribe1Tv.text = keyword1Sen

        binding.reportChatbotDescribe2Tv.text = getKeywordReport(3)

        binding.reportChatbotDescribe3Tv.text = getKeywordReport(5)
         **/

        // 에이닷 캐릭터 상태 지정
        // setAidotStatus(2)


    }


    // get target keyword question from server
    fun getKeywordReport(keyword: Int) {

        var keywordSentence = ""
        inputService.getKeywordReport(keyword)
            .enqueue(object : retrofit2.Callback<KeywordReportResponse> {

                override fun onResponse(
                    call: Call<KeywordReportResponse>,
                    response: Response<KeywordReportResponse>
                ) {

                    if (response.isSuccessful) {
                        Log.d("keyword-report-response", response.toString())
                        val keywordReportResponse: KeywordReportResponse = response.body()!!

                        if (binding.reportChatbotDescribe1Tv.text == "") {

                            binding.reportChatbotDescribe1Tv.text = keywordReportResponse.sentence
                        } else if (binding.reportChatbotDescribe2Tv.text == "") {

                            binding.reportChatbotDescribe2Tv.text = keywordReportResponse.sentence
                        } else {

                            binding.reportChatbotDescribe3Tv.text =
                                "??"//keywordReportResponse.sentence
                        }
                        Log.d("keyword-report-response", keywordReportResponse.toString())
                        // binding.reportChatbotDescribe1Tv.text=keywordReportResponse.sentence.toString()

                        keywordSentence = keywordReportResponse.sentence.toString()
                    }

                }

                override fun onFailure(call: Call<KeywordReportResponse>, t: Throwable) {
                    Log.d("keywordQ-onFailure", t.toString())
                    binding.reportChatbotDescribe1Tv.text = "1번 keyword 서버 x"
                    binding.reportChatbotDescribe3Tv.text = "2번 keyword 서버 x"
                    binding.reportChatbotDescribe3Tv.text = "3번 keyword 서버 x"
                }

            })
        Log.d("??", keywordSentence)

    }

    fun getDrawReport(day: String) {

        inputService2.getDrawReport(day)
            .enqueue(object : retrofit2.Callback<DrawReportResponse> {

                override fun onResponse(
                    call: Call<DrawReportResponse>,
                    response: Response<DrawReportResponse>
                ) {

                    if (response.isSuccessful) {
                        Log.d("draw-report-response", response.toString())
                        val drawReportResponse: DrawReportResponse = response.body()!!

                        Log.d("draw-report-response", drawReportResponse.toString())
                        // binding.reportChatbotDescribe1Tv.text=keywordReportResponse.sentence.toString()

                        // HTP 설명하는 값 받아옴
                        binding.reportDescribeHouseTv.text = drawReportResponse.house_text
                        binding.reportDescribeTreeTv.text = drawReportResponse.tree_text
                        binding.reportDescribeHumanTv.text = drawReportResponse.person_text

                        // HTP 전체 보고서
                        binding.reportDrawingTotalDescribeTv.text = drawReportResponse.result

                        // 퍼지 추론 값 > progress bar
                        binding.reportDrawCate1Pb.setProgress(drawReportResponse.f_type1)
                        Log.d("checkFuzzy", drawReportResponse.f_type1.toString())
                        binding.reportDrawCate2Pb.setProgress(drawReportResponse.f_type2)
                        Log.d("checkFuzzy", drawReportResponse.f_type2.toString())
                        binding.reportDrawCate3Pb.setProgress(drawReportResponse.f_type3)
                        Log.d("checkFuzzy", drawReportResponse.f_type3.toString())

                        // 퍼지 추론 값 > tv
                        binding.reportChatFuzzy1Tv.text = drawReportResponse.f_type1.toString()
                        binding.reportChatFuzzy2Tv.text = drawReportResponse.f_type2.toString()
                        binding.reportChatFuzzy3Tv.text = drawReportResponse.f_type3.toString()

                        //이미지 > iv
                        val sharedPreferences = getSharedPreferences("image_house", MODE_PRIVATE)
                        // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
                        val bmp_house_string = sharedPreferences.getString("image_house", "")
                        val encodeByte: ByteArray = Base64.decode(bmp_house_string, Base64.DEFAULT)
                        val bitmap_house = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
                        binding.reportHouseIv.setImageBitmap(bitmap_house)

                        val sharedPreferences2 = getSharedPreferences("image_tree", MODE_PRIVATE)
                        val bmp_tree_string = sharedPreferences2.getString("image_tree", "")
                        val encodeByte2: ByteArray = Base64.decode(bmp_tree_string, Base64.DEFAULT)
                        val bitmap_tree = BitmapFactory.decodeByteArray(encodeByte2, 0, encodeByte2.size)
                        binding.reportTreeIv.setImageBitmap(bitmap_tree)

                        val sharedPreferences3 = getSharedPreferences("image_person", MODE_PRIVATE)
                        val bmp_person_string = sharedPreferences3.getString("image_person", "")
                        val encodeByte3: ByteArray = Base64.decode(bmp_person_string, Base64.DEFAULT)
                        val bitmap_person = BitmapFactory.decodeByteArray(encodeByte3, 0, encodeByte3.size)
                        binding.reportPersonIv.setImageBitmap(bitmap_person)
                    }

                }

                override fun onFailure(call: Call<DrawReportResponse>, t: Throwable) {
                    Log.d("getDrawReport-onFailure", t.toString())
                }

            })
    }

    fun getChatReport(day: String) {

        Log.d("keyword-1", keywordTemp1.toString())
        inputService2.getChatReport(day)
            .enqueue(object : retrofit2.Callback<ChatReportResponse> {

                override fun onResponse(
                    call: Call<ChatReportResponse>,
                    response: Response<ChatReportResponse>
                ) {

                    if (response.isSuccessful) {
                        Log.d("chat-report-response", response.toString())
                        val chatReportResponse: ChatReportResponse = response.body()!!


                        Log.d("chat-report-response", chatReportResponse.toString())

//                        keywordTemp1 = chatReportResponse.keyword[0].toInt()
                        Log.d("keyword-2", keywordTemp1.toString())
                        //getKeywordReport(keywordTemp1)
                        binding.reportChatbotDescribe1Tv.text=setKeywordReport(chatReportResponse.keyword[0])
                        binding.reportChatbotDescribe2Tv.text=setKeywordReport(chatReportResponse.keyword[1])
                        binding.reportChatbotDescribe3Tv.text=setKeywordReport(chatReportResponse.keyword[2])

                        Log.d("report-getkeywords","======================================")
                        Log.d("report-getkeyword-1","${chatReportResponse.keyword[0]}")
                        Log.d("report-getkeyword-2","${chatReportResponse.keyword[1]}")
                        Log.d("report-getkeyword-3","${chatReportResponse.keyword[2]}")


                        Log.d("report-getkeyword-total","======================================")
                        Log.d("report-getkeyword",setKeyword(chatReportResponse.keyword[0]))
                        binding.reportChatTotalKeywordTv.text= "챗봇 대화의 주요 키워드는 "+ "'"+setKeyword(chatReportResponse.keyword[0])+"','"+setKeyword(chatReportResponse.keyword[1])+"','"+setKeyword(chatReportResponse.keyword[2])+"' 입니다."
                        ///////////////////////////////////////////////////////////
//                        if (chatReportResponse.keyword[0] == "1") {
//                            binding.reportChatbotDescribe1Tv.text = "1번 키워드 서버 ok"
//                        }
//                        if (chatReportResponse.keyword[1] == "2") {
//                            binding.reportChatbotDescribe2Tv.text = "1번 키워드 서버 ok"
//                        }


                        /**
                        val one = chatReportResponse.keyword[0].toInt()
                        val two = chatReportResponse.keyword[1].toInt()
                        val three = chatReportResponse.keyword[2].toInt()

                        getKeywordReport(one)
                        getKeywordReport(two)
                        getKeywordReport(three)
                         **/
                        /////////////////////////////////////////////////////////////

                        // binding.reportChatbotDescribe1Tv.text=keywordReportResponse.sentence.toString()
                        val emotion = chatReportResponse.emotion
                        setAidotStatus(emotion)

                    }

                }

                override fun onFailure(call: Call<ChatReportResponse>, t: Throwable) {
                    Log.d("getChatReport-onFailure", t.toString())
                }

            })

        Log.d("keyword-3", keywordTemp1.toString())
    }

    fun setAidotStatus(state: Int) {
        when (state) {
            1 -> binding.reportChatState1Iv.visibility = View.VISIBLE
            2 -> binding.reportChatState2Iv.visibility = View.VISIBLE
            3 -> binding.reportChatState3Iv.visibility = View.VISIBLE
            4 -> binding.reportChatState4Iv.visibility = View.VISIBLE
            5 -> binding.reportChatState5Iv.visibility = View.VISIBLE
        }
    }


    // report >> total describe keywords
    fun setKeyword(state: String):String {

        var keyword = ""
        when (state) {

            // 감정조절
            "0" -> keyword="감정조절"
            // 스트레스
            "1" -> keyword="스트레스"
            // 슬픔
            "2" -> keyword="슬픔"
            // 속상
            "3" -> keyword="속상"
            // 외로움
            "4" -> keyword="외로움"
            // 불안
            "5" -> keyword="불안"
            // 걱정
            "6" -> keyword="걱정"
            // 긴장
            "7" -> keyword="긴장"
            // 중립
            "8" -> keyword=""
            // 긍정
            "9" -> keyword="긍정"
            // 자신감
            "10" -> keyword="자신감"
            // 성실
            "11" -> keyword="성실"
            // 행복
            "12" -> keyword="행복"

        }
        return keyword
    }

    fun setKeywordReport(state: String):String {

        var result = ""
        when (state) {
            // 감정조절
            "0" -> result= "대화 분석 결과 아동의 주요 키워드는 ‘감정조절’ 입니다. 격한 감정이 느껴지는 단어를 많이 사용했으며 아동의 감정에서 화가 느껴지기도 합니다. 화가 난 원인을 자세히 파악하는 질문을 하고, 아이가 즐거운 하루가 될 수 있게 노력해주세요."
            // 스트레스
            "1" -> result= "대화 분석 결과 아동의 주요 키워드는 ‘스트레스’입니다. 오늘은 아이의 피곤도가 높아 스트레스에 예민한 상태입니다. 충분한 휴식을 통해 컨디션을 회복할 수 있도록 도와주세요."
            // 슬픔
            "2" -> result= "대화 분석 결과 아동의 주요 키워드는 ‘슬픔’입니다. 지금 감정 상태는 슬픔, 우울 상태이므로 해당 상태의 원인을 아동에게 질문해 보는 것이 좋겠습니다. 슬플 때는 아이가 안정을 취할 수 있도록 편안한 상태를 유지해주세요."
            // 속상
            "3"-> result= "대화 분석 결과 아동의 주요 키워드는 ‘속상’입니다. 대인관계에서 오는 서운함과 속상함이 담긴 언어를 많이 사용했습니다. 아동이 ‘속상’하다고 느끼는 이유는 [소분류키워드] 때문입니다. 아동이 하는 말을 잘 들어주고, 공감해주는 자세가 필요합니다."
            // 외로움
            "4"-> result= "대화 분석 결과 아동의 주요 키워드는 ‘외로움’입니다. 오늘 대인관계에서 오는 외로움과 쓸쓸함이 느껴집니다. 학교나 가정에서 아동이 다른사람과 교류하지 못하고 있다고 느낄 수 있습니다. 아동이 혼자라고 느끼지 않도록 함께 놀러 가는 시간을 가져보는게 어떨까요?"
            // 불안
            "5"-> result= "대화 분석 결과 아동의 주요 키워드는 ‘불안’입니다. 아동이 어떤 행동을 할 때 불안해하는지 지켜보시는 게 어떨까요? 아동에게 ‘넌 뭐든 잘할 수 있어’, ‘다 잘 될 거야’,’ 넌 혼자가 아니야’라는 말로 아동에게 힘을 실어주면 좋을 것 같습니다:) 아동이 불안함에서 잠시 멀어지도록 조용한 산이나, 바다로 잠시 여행을 가보는 것이 어떨까요?"
            // 걱정
            "6"-> result= "대화 분석 결과 아동의 주요 키워드는 '걱정' 입니다.대화에서 미래에서 나오는 걱정이 느껴집니다. 아동에게 다정하게 걱정되는 일이 무엇인지 물어보면 어떨까요? 아동에게 ‘넌 뭐든 잘할 수 있어’, ‘다 잘 될 거야’,’ 넌 혼자가아니야’ 라는 말로 아동에게 힘을 실어주면 좋을 것 같습니다:)"
            // 긴장
            "7"-> result="대화 분석 결과 아동의 주요 키워드는 ‘긴장' 입니다. 대화에서 ‘두려움’에서 나오는 긴장이 느껴집니다. 아동에게 다정하게 ‘긴장’되는 일이 무엇인지 물어보면 어떨까요? 아동에게 ‘네 옆엔 항상 우리가 있어’, ‘넌 혼자가 아니야’, ‘오늘은 같이 잘까?’ 라는 말로 아동에게 힘을 실어주면 좋을 것 같습니다:)"
            // 중립
            "8"-> result = ""
            // 긍정
            "9"-> result="대화 분석 결과 아동의 주요 키워드는 '긍정'입니다. 아동은 오늘 대화에서 자기긍정감이 느껴지는 단어를 자주 사용하였습니다. 현재 아동은 가정, 대인관계 , 학업 면에서 큰 문제없이 안정적인 생활을 하고 있는 것으로 판단됩니다. 이러한 안정된 생활을 기반으로 본인 스스로에 대한 건강한 자아상을 가지고 있는 것으로 보입니다."
            // 자신감
            "10"->result = "대화 분석 결과 아동의 주요 키워드는 '자신감'입니다. 아동은 오늘 대화에서 높은 자존감이 느껴지는 단어를 자주 사용하였습니다. 현재 아동은 스스로의 생각과 능력을 믿는 건강한 자아상을 가진 상태로 생각됩니다. 높은 자존감을 가진 아동은 대체로 타인에 대한 심리적 수용력이 크고, 어려운 일을 겪어도 회복 탄력성이 높습니다."
            // 성실
            "11"->result="대화 분석 결과 아동의 주요 키워드는 ‘성실’입니다. 아동이 자신이 성실한 것에 대해 보람을 느낄 수 있도록 칭찬해 주시는 것이 어떨까요? 구체적으로 행동에 대한 추천일수록 더욱 좋습니다! 오늘부터 아동의 사소한 행동부터 칭찬해 보세요!"
            // 행복
            "12" -> result = "대화 분석 결과 아동의 주요 키워드는 ‘성실’입니다. 부모님이 자부심을 가지셔도 될 만큼 잘하고 계세요. 앞으로도 아동이 행복할 수 있도록 사소한 대화를 시작해 보시는 게 어떨까요?"
        }

        return result
    }
}