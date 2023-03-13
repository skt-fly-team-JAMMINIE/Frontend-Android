package com.example.jammin

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jammin.databinding.ReportRvItemBinding


// 보고서 리스트 리사이클러뷰 어댑터
class ReportRVAdapter(val dateInfos: ArrayList<String>):
    RecyclerView.Adapter<ReportRVAdapter.ViewHolder>() {

    // 클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onItemClick(dateInfo: String)
    }

    // 리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }


    inner class ViewHolder(val binding: ReportRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dateInfo: String) {

            binding.reportRvDateInfoTv.text=dateInfo

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ReportRVAdapter.ViewHolder {
        val binding: ReportRvItemBinding = ReportRvItemBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportRVAdapter.ViewHolder, position: Int) {
        val binding = (holder as ReportRVAdapter.ViewHolder).binding
        holder.bind(dateInfos[position])
        binding.reportRvItemRoot.setOnClickListener {
            // 클릭된 item 확인
            Log.d("ReportRVAdapter:clicked", "${position} , ${dateInfos[position]}")
        }

        // itemView (보고서 item) 의 클릭이벤트
        holder.itemView.setOnClickListener{
            mItemClickListener.onItemClick(dateInfos[position])
        }

    }

    // dateInfo 개수
    override fun getItemCount(): Int = dateInfos.size
}

