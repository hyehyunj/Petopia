package com.djhb.petopia.presentation.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.djhb.petopia.databinding.FragmentAdminBinding
import com.djhb.petopia.presentation.gallery.GalleryReadFragment
import com.djhb.petopia.presentation.gallery.GallerySharedViewModel
import com.djhb.petopia.presentation.gallery.GallerySharedViewModelFactory
import kotlinx.coroutines.launch

//관리자 페이지
class AdminFragment : Fragment() {
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
            requireActivity().supportFragmentManager.beginTransaction()
                .remove(this@AdminFragment)
                .commit()
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


}