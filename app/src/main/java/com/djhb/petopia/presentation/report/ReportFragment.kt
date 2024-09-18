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
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.djhb.petopia.R
import com.djhb.petopia.ReportReasonType
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.databinding.FragmentReportBinding
import com.djhb.petopia.presentation.community.CommunityDetailViewModel
import com.djhb.petopia.presentation.community.CommunityViewModel
import com.djhb.petopia.presentation.register.RegisterViewModel
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReportFragment : DialogFragment() {

    private val reportViewModel: ReportViewModel by activityViewModels()
    private val communityViewModel: CommunityViewModel by viewModels()
    private val communityDetailViewModel: CommunityDetailViewModel by viewModels()
    private val registerViewModel: RegisterViewModel by activityViewModels()

    private val _binding: FragmentReportBinding by lazy {
        FragmentReportBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding

    private var reason = ReportReasonType.UNPLEASANT_CONTENT

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

                val content = binding.etReportContent.text.toString()
                if(content.isBlank()) {
                    Toast.makeText(requireActivity(), "상세 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    val reportModel = reportViewModel.reportModelLiveData.value
//                    val reasonType = binding.reportRg.
                    if (reportModel != null) {
                        reportViewModel.setContent(reportModel.copy(
                            reasonDescription = content,
                            reasonType = reason
                            )
                        )
                    }
                    Log.i("ReportFragment", "reportModel = ${reportViewModel.reportModelLiveData.value}")
                    reportViewModel.createReport()
                    if (reportModel != null) {
                        LoginData.loginUser.reportList.add(reportModel.targetUserId)
                        registerViewModel.updateUser()
                    }
                    dismiss()
                }
            }

//            reportViewModel.reportReason.observe(viewLifecycleOwner) { reason ->
//                Log.d("reportReason", "${reason}")
//            }
//            reportViewModel.reportReasonDetail.observe(viewLifecycleOwner) { detail ->
//                Log.d("reportReasonDetail", detail)
//            }
        }
    }

    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun reportDataObserver() {
        binding.apply {
            reportRg.setOnCheckedChangeListener { _, checkedId ->
                reason = when (checkedId) {
                    R.id.report_rb_1 -> ReportReasonType.UNPLEASANT_CONTENT
                    R.id.report_rb_2 -> ReportReasonType.INCORRECT_INFORMATION
                    R.id.report_rb_3 -> ReportReasonType.LEGAL_PROBLEM
                    R.id.report_rb_4 -> ReportReasonType.SPAM_OR_ADD
                    else -> ReportReasonType.ETC
                }
                reportViewModel.setReportReason(reason)
            }
//            etReportContent.addTextChangedListener {
//                val content = it.toString()
//                reportViewModel.setReportReasonDetail(content)
//            }
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CommunityCreateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}