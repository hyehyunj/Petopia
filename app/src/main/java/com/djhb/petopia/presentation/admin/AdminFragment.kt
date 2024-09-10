package com.djhb.petopia.presentation.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.djhb.petopia.databinding.FragmentAdminBinding
import com.djhb.petopia.presentation.gallery.GalleryReadFragment
import com.djhb.petopia.presentation.gallery.GallerySharedViewModel
import com.djhb.petopia.presentation.gallery.GallerySharedViewModelFactory

//관리자 페이지
class AdminFragment : Fragment() {
    private val _binding: FragmentAdminBinding by lazy {
        FragmentAdminBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private val adminViewModel by viewModels<AdminViewModel> {
        AdminViewModelFactory()
    }
    private lateinit var adminRecyclerViewAdapter: AdminRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        initAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adminButtonClickListener()
        adminDataObserver()
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


    }


    //어댑터 초기화 함수 :
    private fun initAdapter() {
        adminRecyclerViewAdapter = AdminRecyclerViewAdapter(
            adminViewModel.reportListLiveData.value ?: listOf(),
            itemClickListener = { item, position ->
                adminViewModel.selectReport(item)
                AdminReportFragment().show(childFragmentManager, "ADMIN_REPORT_FRAGMENT")
            },
            itemLongClickListener = { item, position -> },
        )

        binding.galleryRv.adapter = adminRecyclerViewAdapter
        binding.galleryRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }


}