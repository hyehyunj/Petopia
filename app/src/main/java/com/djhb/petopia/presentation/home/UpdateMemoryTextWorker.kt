package com.djhb.petopia.presentation.home

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class UpdateMemoryTextWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
//        Log.d("UpdateMemoryTextWorker", "doWork() called")

        val memoryText: MutableList<String> = mutableListOf()
        memoryText.add("우리 친구와의 첫만남은 어땠나요?")
        memoryText.add("친구가 가장 좋아했던 간식은 무엇인가요?")
        memoryText.add("친구의 특별한 습관이 있었나요?")
        memoryText.add("친구의 최애 장난감은 무엇인가요?")
        memoryText.add("기억에 남는 산책장소는 어디인가요?")
        memoryText.add("친구가 좋아하는 음식은 무엇인가요?")
        memoryText.add("친구가 집에서 제일 편안해하던 장소는 어디였나요?")
        memoryText.add("가장 그리운 모습은 무엇인가요?")
        memoryText.add("가장 좋아하던 산책 루틴이 있었나요?")
        memoryText.add("친구는 어떤 장난을 가장 좋아했나요?")
        memoryText.add("당신을 처음 보고 했던 행동은 무엇이었나요?")



        val selectedText = memoryText.random()

        val sharedPreferences =
            applicationContext.getSharedPreferences("Memory", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("memoryText", selectedText).commit()
//        Log.d("UpdateMemoryTextWorker", "selectedText: $selectedText")

        return Result.success()
    }
}