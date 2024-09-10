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
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.djhb.petopia.R
import com.djhb.petopia.databinding.FragmentReportBinding
import com.djhb.petopia.presentation.community.CommunityDetailViewModel
import com.djhb.petopia.presentation.community.CommunityViewModel


class ReportFragment : DialogFragment() {

    private val reportViewModel: ReportViewModel by viewModels()
    private val communityViewModel: CommunityViewModel by viewModels()
    private val communityDetailViewModel: CommunityDetailViewModel by viewModels()

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
            reportTvComplete.setOnClickListener {
                reportViewModel.reportReason.observe(viewLifecycleOwner) { reason ->
                    Log.d("reportReason", reason)
                }
                reportViewModel.reportReasonDetail.observe(viewLifecycleOwner) { detail ->
                    Log.d("reportReasonDetail", detail)
                }
                dismiss()


            }
        }
    }

    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun reportDataObserver() {
        binding.apply {
            reportRg.setOnCheckedChangeListener { _, checkedId ->
                val reason = when (checkedId) {
                    R.id.report_rb_1 -> "불쾌한 내용"
                    R.id.report_rb_2 -> "잘못된 정보"
                    R.id.report_rb_3 -> "법적 문제"
                    R.id.report_rb_4 -> "스팸 또는 광고"
                    R.id.report_rb_5 -> "기타"
                    else -> ""
                }
                reportViewModel.setReportReason(reason)
            }
            etReportContent.addTextChangedListener {
                val content = it.toString()
                reportViewModel.setReportReasonDetail(content)
            }
        }

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