package com.example.jammin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.jammin.databinding.ActivityInputPwdBinding

class Input_pwd : AppCompatActivity() {
    lateinit var binding:ActivityInputPwdBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputPwdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 비번 입력 페이지
        binding.inputPwdBtn.setOnClickListener {
            Toast.makeText(this, "비밀번호 입력 완료!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ReportList::class.java)
            startActivity(intent)
        }
    }
}