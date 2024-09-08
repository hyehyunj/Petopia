package com.djhb.petopia.presentation.home

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class UpdateMemoryTextWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.d("UpdateMemoryTextWorker", "doWork() called")

        val dummyText: MutableList<String> = mutableListOf()
        dummyText.add("첫만남의 기분은 어땠나요?")
        dummyText.add("가장 좋아했던 간식은 무엇인가요")
        dummyText.add("1111111111?")
        dummyText.add("22222222222?")
        dummyText.add("333333333333?")
        dummyText.add("44444444444444?")
        dummyText.add("5555555555555555?")
        dummyText.add("666666666666666666?")
        dummyText.add("기억에 남는 산책장소는 어디인가요?")

        val selectedText = dummyText.random()

        val sharedPreferences =
            applicationContext.getSharedPreferences("Memory", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("memoryText", selectedText).commit()
        Log.d("UpdateMemoryTextWorker", "selectedText: $selectedText")

        return Result.success()
    }
}