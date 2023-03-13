package com.example.jammin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.jammin.databinding.ActivityAidotChatbotBinding
import com.example.jammin.utils_chat.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Thread.sleep

class AidotChatbot : AppCompatActivity() {


    lateinit var binding: ActivityAidotChatbotBinding

    // get retrofit 1 >> QuestionInterface 질문 받아옴
    val inputService = getRetrofit().create(QuestionInterface::class.java)

    // get retrofit 2 >> UserChatInterface 유저가 입력한 채팅 post
    val inputService2 = getRetrofit().create(UserChatInterface::class.java)

    // media player
    private val mediaPlayer = MediaPlayer()

    val respondTime: Long = 6000
    val modelrespondTime: Long = 5000

    // keyword question 2개
    var keywordQ1: String = ""
    var keywordQ2: String = ""

    // keyword TTS 2개
    var keywordTTS1: String = ""
    var keywordTTS2: String = ""

    // Google STT
    private var intent: Intent? = null
    private var speechRecognizer: SpeechRecognizer? = null
    private val PERMISSION = 1 //permission 변수
    var recording = false // 현재 녹음중인지 확인

    // 챗봇 제어
    var bot1 = false
    var bot2 = false
    var bot3 = false
    var bot4 = false
    var bot5 = false
    var bot6 = false
    var bot7 = false
    var bot8 = false
    var bot9 = false
    var bot10 = false
    var bot11 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAidotChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /** >>>>>>> 질문 잘 받아와지는지 확인용 <<<<<<<<
        getNormalQ("hello")
        getNormalQ("normal")
        getKeywordQ(1)
        inputChat(SendText(1, "일반 질문에 대한 아동 답변 ")) // > chatting table에만 저장되고 모델엔 안들어감
        inputChatToModel(SendText(1, "키워드 질문에 대한 아동 답변")) // > chatting table저장 , 모델에 들어가서 모델 답변 생성"
         **/

        Log.d("aidot-chat-test", "======================================")

        // 인사말
        getNormalQ("hello")

        checkPermission() // 녹음 퍼미션 체크


        // 마이크 버튼 클릭
        binding.recordBtn.setOnClickListener {

            Log.d("aidot-mike-clicked", "마이크 버튼 클릭")

            Toast.makeText(applicationContext, "에이닷 듣는중", Toast.LENGTH_SHORT).show()
            controlMike()

            if (bot4) {

                if (!bot5) {
                    bot5 = true
                }

            } else if (bot3) {

                if (!bot4) {
                    bot4 = true
                }
            } else if (bot2) {
                getKeywordQ2() // >> keywordQ 2개
            } else if (bot1) {
                // 두번째 일반 질문
                getNormalQ("normal")
            }

        }

        intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent!!.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        intent!!.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")


    }



    // Normal Question 받아오기
    fun getNormalQ(type: String) {

        if (type == "hello") {
            bot1 = true
        } else if (type == "normal") {
            bot2 = true
        } else if (type == "goodbye") {

            bot10 = true
            bot11=true

        }

        inputService.getNormalQuestion(type).enqueue(object : Callback<QuestionResponse> {

            override fun onResponse(
                call: Call<QuestionResponse>,
                response: Response<QuestionResponse>
            ) {


                if (response.isSuccessful) {
                    Log.d("aidot-normalQ-response", response.toString())
                    val normalQResponse: QuestionResponse = response.body()!!

                    Log.d("aidot-normalQ-response", normalQResponse.toString())


                    if (type == "normal") {

                        Log.d("aidot-normal-sleep", "type = ${type.toString()}")
                        try {
                            sleep(respondTime)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }

                    // TTS
                    var b64 = normalQResponse.send_tts
                    val decoder: ByteArray = Base64.decode(b64, 1)
                    playMp3(decoder)

                    binding.chatbotAidotTv.text = normalQResponse.sentence.toString()
                }

                    // 대화 끝
                    if(type=="goodbye"){
                        bot11=true

                        try {

                            sleep(respondTime)

                            // 챗봇 감정분석 로딩페이지
                            val intent = Intent(this@AidotChatbot, LoadingDialog_chat::class.java)
                            startActivity(intent)

                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }

                    }
            }

            override fun onFailure(call: Call<QuestionResponse>, t: Throwable) {
                Log.d("aidot-normalQ-onFailure", t.toString())
            }

        })

    }


    // 키워드 질문 2개 받아옴
    fun getKeywordQ2() {

        if (!bot3) {
            bot3 = true
        }

        inputService.getKeyword2Question()
            .enqueue(object : retrofit2.Callback<KeywordQResponse> {

                override fun onResponse(
                    call: Call<KeywordQResponse>,
                    response: Response<KeywordQResponse>
                ) {

                    if (response.isSuccessful) {
                        Log.d("aidot-key2-response", response.toString())
                        val keywordQResponse: KeywordQResponse = response.body()!!

                        Log.d("aidot-key2-response", keywordQResponse.toString())
                        Log.d("aidot-key2-response", "===================================")
                        Log.d("aidot-key2-response", keywordQResponse.sentence1.toString())
                        Log.d("aidot-key2-response", keywordQResponse.sentence2.toString())
                        Log.d("aidot-key2-response", "===================================")
                        keywordQ1 = keywordQResponse.sentence1
                        keywordQ2 = keywordQResponse.sentence2
                        keywordTTS1 = keywordQResponse.send_tts1
                        keywordTTS2 = keywordQResponse.send_tts2

                        // time sleep
                        try {
                            sleep(respondTime)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }

                        aidotStatus2()
                        // TTS
                        val decoder: ByteArray = Base64.decode(keywordTTS1, 1)
                        playMp3(decoder)
                        binding.chatbotAidotTv.text = keywordQ1

                    }

                }

                override fun onFailure(call: Call<KeywordQResponse>, t: Throwable) {
                    Log.d("aidot-key2-onFailure", t.toString())
                }

            })


    }


    // ************* user 답변 >> post (모델에 들어갈 keyword 답변) ****************
    fun inputChatToModel(sendText: SendText) {

        if (bot7 and bot8 and !bot9) {
            bot9 = true
        }

        if (bot6 and bot7 and !bot8) {
            bot8 = true
        }

        if (bot5 and bot6 and !bot7) {
            bot7 = true
        }

        if (bot5 and !bot6) {
            bot6 = true
        }

        inputService2.inputChatToModel(sendText)
            .enqueue(object : Callback<UserChatResponse> {

                override fun onResponse(
                    call: Call<UserChatResponse>,
                    response: Response<UserChatResponse>
                ) {

                    if (response.isSuccessful) {
                        Log.d("aidot-input-response", response.toString())
                        val userChatInterface: UserChatResponse = response.body()!!

                        Log.d("aidot-input-response", userChatInterface.toString())
                        Log.d("aidot-bot이 만든 질문", userChatInterface.bot)

                        try {
                            sleep(modelrespondTime)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }

                        // TTS
                        var b64 = userChatInterface.send_tts
                        val decoder: ByteArray = Base64.decode(b64, 1)
                        playMp3(decoder)

                        // 아동 답변에 대한 모델 대답 화면에 출력
                        binding.chatbotAidotTv.text = userChatInterface.bot.toString()


                    }

                }

                override fun onFailure(call: Call<UserChatResponse>, t: Throwable) {
                    Log.d("aidot-input-onFailure", t.toString())
                }

            })
    }


    // ************* user 답변 >> post (user table에만 저장되고 다른동작 x) ****************
    fun inputChat(sendText: SendText) {

        if (bot9) {
            bot10=true
        }

        inputService2.inputChat(sendText).enqueue(object : Callback<UserChat2Response> {

            override fun onResponse(
                call: Call<UserChat2Response>,
                response: Response<UserChat2Response>
            ) {
                Log.d("aidot-chat2-response", response.toString())

            }

            override fun onFailure(call: Call<UserChat2Response>, t: Throwable) {
                Log.d("aidot-input2-onFailure", t.toString())
            }

        })
        if (bot10){
            Log.d("aidot-bot10-goodbye","goodbye")
            getNormalQ("goodbye")
        }

    }

    // byte array -> audio
    private fun playMp3(mp3SoundByteArray: ByteArray) {
        try {
            // create temp file that will hold byte array
            val tempMp3 = File.createTempFile("kurchina", "mp3", cacheDir)
            tempMp3.deleteOnExit()
            val fos = FileOutputStream(tempMp3)
            fos.write(mp3SoundByteArray)
            fos.close()

            // resetting mediaplayer instance to evade problems
            mediaPlayer.reset()

            val fis = FileInputStream(tempMp3)
            mediaPlayer.setDataSource(fis.fd)

            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (ex: IOException) {
            val s = ex.toString()
            ex.printStackTrace()
        }
    }


    // ********************************** 마이크 제어 *****************************************
    fun controlMike() {
        if (!recording) {
            StartRecord()

        } else {
            StopRecord()
            StartRecord()
        }

    }


    // 녹음 퍼미션 체크
    fun checkPermission() {
        //안드로이드 버전이 6.0 이상
        if (Build.VERSION.SDK_INT >= 23) {
            //인터넷이나 녹음 권한이 없으면 권한 요청
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.INTERNET
                ) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.INTERNET,
                        Manifest.permission.RECORD_AUDIO
                    ), PERMISSION
                )
            }
        }
    }


    var listener: RecognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(bundle: Bundle) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(v: Float) {}
        override fun onBufferReceived(bytes: ByteArray) {}
        override fun onEndOfSpeech() {}
        override fun onError(error: Int) {
            val message: String
            message = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "오디오 에러"
                SpeechRecognizer.ERROR_CLIENT ->                     //message = "클라이언트 에러";
                    //speechRecognizer.stopListening()을 호출하면 발생하는 에러
                    return  //토스트 메세지 출력 X
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "퍼미션 없음"
                SpeechRecognizer.ERROR_NETWORK -> "네트워크 에러"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트웍 타임아웃"
                SpeechRecognizer.ERROR_NO_MATCH -> {
                    //message = "찾을 수 없음";
                    //녹음을 오래하거나 speechRecognizer.stopListening()을 호출하면 발생하는 에러
                    //speechRecognizer를 다시 생성하여 녹음 재개
                    if (recording) StartRecord()
                    return  //토스트 메세지 출력 X
                }
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RECOGNIZER가 바쁨"
                SpeechRecognizer.ERROR_SERVER -> "서버가 이상함"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "말하는 시간초과"
                else -> "알 수 없는 오류임"
            }
            Toast.makeText(applicationContext, "에러가 발생하였습니다. : $message", Toast.LENGTH_SHORT).show()
        }

        override fun onResults(bundle: Bundle) {
            val matches =
                bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) //인식 결과를 담은 ArrayList

            //인식 결과
            var newText = ""
            for (i in matches!!.indices) {
                newText += matches[i]
            }

            Log.d("aidot-STT-result", newText)


            // ~~~~~~~~~~~~~~~~~~~ user STT 제어 >> InputChat or InputChatModel ~~~~~~~~~~~~~~~~~~~~~~~~
            if (bot9) {
                Log.d(
                    "aidot-bot9-??",
                    "${bot1},${bot2},${bot3},${bot4},${bot5},${bot6},${bot7},${bot8},${bot9},${bot10}"
                )

                inputChat(SendText(1, newText))
            } else if (bot8) {
                inputChatToModel(SendText(1, newText))
            } else if (bot7) {
                inputChatToModel(SendText(1, newText))
            } else if (bot6) {
                ///////////// time sleep /////////////
                try {
                    sleep(respondTime)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                // TTS
                val decoder: ByteArray = Base64.decode(keywordTTS2, 1)
                playMp3(decoder)
                // 앞에 bot2에서 받아온 sentence1 , sentence2 중에서 sentence2 화면에 출력
                binding.chatbotAidotTv.text = keywordQ2
                if (!bot7) {
                    bot7 = true
                }
                if (!bot7) {
                    bot7 = true
                }

            } else if (bot5) {
                inputChatToModel(SendText(1, newText))
            } else if (bot4) {
                inputChatToModel(SendText(1, newText))
            } else if (bot3) {
                inputChat(SendText(1, newText))
            } else if (bot2) {
                inputChat(SendText(1, newText))
            } else if (bot1) {
                inputChat(SendText(1, newText))
            }

        }

        override fun onPartialResults(bundle: Bundle) {}
        override fun onEvent(i: Int, bundle: Bundle) {}
    }


    // 녹음 멈춤
    private fun StopRecord() {
        recording = false

        speechRecognizer!!.stopListening() //녹음 중지
        Log.d("chat-record-stop", "음성 기록 중지")
    }


    // 녹음 시작
    private fun StartRecord() {
        recording = true
        Log.d("chat-record-start", "음성 기록 시작")
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(applicationContext)
        speechRecognizer?.setRecognitionListener(listener)
        speechRecognizer?.startListening(intent)

    }

    // ================================= 에이닷 캐릭터 상태변경===================================
    fun aidotStatus1() {
        binding.chatbotAidotIv.visibility = View.VISIBLE
        binding.chatbotAidotIvStatus2.visibility = View.INVISIBLE
        binding.chatbotAidotIvStatus3.visibility = View.INVISIBLE
    }

    fun aidotStatus2() {
        binding.chatbotAidotIv.visibility = View.INVISIBLE
        binding.chatbotAidotIvStatus2.visibility = View.VISIBLE
        binding.chatbotAidotIvStatus3.visibility = View.INVISIBLE
    }
}