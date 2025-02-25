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
import androidx.recyclerview.widget.LinearLayoutManager
import com.djhb.petopia.databinding.FragmentAdminBinding

//관리자 페이지
class AdminFragment : DialogFragment() {
    private val _binding: FragmentAdminBinding by lazy {
        FragmentAdminBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
//    private val adminViewModel by viewModels<AdminViewModel> {
//        AdminViewModelFactory()
//    }
    private val adminViewModel:AdminViewModel by activityViewModels()
//    private val reportList = adminViewModel.reportListLiveData.value?: mutableListOf()
    private val adminRecyclerViewAdapter: AdminRecyclerViewAdapter by lazy {
        AdminRecyclerViewAdapter(
            itemClickListener = { item, position ->
                adminViewModel.selectReport(item)
                AdminReportFragment().show(childFragmentManager, "ADMIN_REPORT_FRAGMENT")
            },

            itemLongClickListener = { item, position -> },
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        adminViewModel.loadInitReportList()
        initAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adminViewModel.loadInitReportList()
        adminDataObserver()
        adminButtonClickListener()

    }

    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun adminButtonClickListener() {
        //뒤로가기버튼 클릭이벤트 :
        binding.adminIvBack.setOnClickListener {
      dismiss()
        }

    }


    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun adminDataObserver() {

        adminViewModel.reportListLiveData.observe(viewLifecycleOwner) {
            adminRecyclerViewAdapter.submitList(it.toMutableList())
        }

    }


    //어댑터 초기화 함수 :
    private fun initAdapter() {
//        Log.i("AdminFragment", "reportListLiveData = ${adminViewModel.reportListLiveData.value}")
//        adminRecyclerViewAdapter = AdminRecyclerViewAdapter(
////            reportList = reportList,
//            itemClickListener = { item, position ->
//                adminViewModel.selectReport(item)
//                AdminReportFragment().show(childFragmentManager, "ADMIN_REPORT_FRAGMENT")
//            },
//
//            itemLongClickListener = { item, position -> },
//        )
        binding.galleryRv.adapter = adminRecyclerViewAdapter
        binding.galleryRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }
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
        params?.width = (deviceWidth * 1).toInt()
        params?.height = (deviceWidth * 2.1).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onResume() {
        super.onResume()
        initDialog()
    }

}