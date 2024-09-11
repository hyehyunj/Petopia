package com.djhb.petopia.presentation.admin

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.ReportModel
import com.djhb.petopia.data.remote.SignRepository
import com.djhb.petopia.data.remote.SignRepositoryImpl
import com.djhb.petopia.databinding.FragmentAdminReportBinding
import com.djhb.petopia.presentation.MainActivity
import com.djhb.petopia.presentation.register.RegisterViewModel


class AdminReportFragment : DialogFragment() {

    private val _binding: FragmentAdminReportBinding by lazy {
        FragmentAdminReportBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private val adminSharedViewModel: AdminViewModel by activityViewModels()
    private val registerViewModel: RegisterViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        adminSharedViewModel =
//            ViewModelProvider(requireParentFragment()).get(AdminViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        adminViewModel.selectReport(item)

        //버튼 클릭이벤트
        reportButtonClickListener()
        //데이터 변화감지
        reportDataObserver()
        initDialog()
        initView()
    }


    private fun initView(){
        val detailData = adminSharedViewModel.reportDetailLiveData.value?:ReportModel()
        binding.apply {
            adminReportTvReporter.text = detailData.reporterId
            adminReportTvTarget.text = detailData.targetUserId
            adminReportTvContentType.text = detailData.contentType.toString()
            adminReportTvContentUid.text = detailData.contentUid
            adminReportTvReason.text = detailData.reasonType.toString()
            adminReportTvDescription.text = detailData.reasonDescription
        }
    }

    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun reportButtonClickListener() {

        //뒤로가기버튼 클릭이벤트 : 갤러리 종료
        binding.apply {
            adminReportIvBack.setOnClickListener {
                dismiss()
            }
            //계정삭제버튼 클릭이벤트 : 갤러리 종료
            reportTvExile.setOnClickListener {
                (activity as MainActivity).exileUser()
                val detailData = adminSharedViewModel.reportDetailLiveData.value?:ReportModel()
//                registerViewModel.deleteUser(detailData.targetUserId)
//                adminSharedViewModel.deleteReport(detailData)
                LoginData.loginUser.reportList.add(detailData.targetUserId)
                registerViewModel.updateUser()
            }
        }
    }

    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun reportDataObserver() {
        adminSharedViewModel.reportDetailLiveData.observe(viewLifecycleOwner){
            binding.apply {
                adminReportTvReporter.text = it.reporterId
                adminReportTvTarget.text = it.targetUserId
                adminReportTvContentType.text = it.contentType.toString()
                adminReportTvContentUid.text = it.contentUid
                adminReportTvReason.text = it.reasonType.toString()
                adminReportTvDescription.text = it.reasonDescription
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