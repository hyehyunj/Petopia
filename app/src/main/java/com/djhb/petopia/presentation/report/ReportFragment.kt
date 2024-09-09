package com.djhb.petopia.presentation.report

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.djhb.petopia.databinding.FragmentReportBinding


class ReportFragment : DialogFragment() {

    private val _binding: FragmentReportBinding by lazy {
        FragmentReportBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //버튼 클릭이벤트
        reportButtonClickListener()
        //데이터 변화감지
        reportDataObserver()
        initDialog()
    }




    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun reportButtonClickListener() {

        //뒤로가기버튼 클릭이벤트 : 갤러리 종료
        binding.apply {
            reportIvBack.setOnClickListener {
                dismiss()
            }
            //신고버튼 클릭이벤트
            reportTvReport.setOnClickListener {

            }
        }
    }

    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun reportDataObserver() {

        }




    //다이얼로그 초기화 함수 : 화면에 맞춰 갤러리 표현
    private fun initDialog() {
        val windowManager =
            requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay

        val size = Point()
        display.getSize(size)
        size.x
        size.y
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.9).toInt()
        params?.height = (deviceWidth * 1.8).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}