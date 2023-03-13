package com.example.jammin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.example.jammin.ReportPage
import com.example.jammin.ReportRVAdapter
import com.example.jammin.databinding.ActivityReportListBinding

// 보고서 리스트 페이지
class ReportList : AppCompatActivity() {

    private lateinit var binding: ActivityReportListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // dateInfos 임시 리스트
        var dateInfoList = ArrayList<String>()

        // 임시 더미 데이터
        dateInfoList.apply {
            //add("2022-12-19")
            // add("2023-01-25")
            //add("2023-02-14")
            add("2023-02-27")
            add("2023-02-28")
        }


        // reportListRv <-> ReportRVAdapter 연결
        val reportListAdapter = ReportRVAdapter(dateInfoList)
        binding.reportListRv.adapter = reportListAdapter

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.reportListRv.layoutManager = layoutManager


        // rv_item >> 클릭 인터페이스 정의
        reportListAdapter.setMyItemClickListener(object : ReportRVAdapter.MyItemClickListener {

            override fun onItemClick(dateInfo: String) {
                Log.d("Report_list:ItemClicked", "clicked ${dateInfo.toString()}")

                // 클릭된 list item 페이지
                moveToReportActivity(dateInfo)
            }

        })

    }


    // report 페이지로 이동
    private fun moveToReportActivity(dateInfo: String) {

        val intent = Intent(this, ReportPage::class.java)

        // val gson = Gson()
        // val newDateJson = gson.toJson(dateInfo)

        // 날짜 정보 보냄
        intent.putExtra("dateInfo", dateInfo)
        startActivity(intent)

    }
}