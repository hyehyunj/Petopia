package com.djhb.petopia.presentation.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.djhb.petopia.databinding.FragmentAdminBinding
import com.djhb.petopia.presentation.gallery.GalleryReadFragment
import com.djhb.petopia.presentation.gallery.GalleryRecyclerViewAdapter

//관리자 페이지
class AdminFragment : Fragment() {
    private val _binding: FragmentAdminBinding by lazy {
        FragmentAdminBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
//    private lateinit var mainHomeGuideViewModel: MainHomeGuideSharedViewModel
private lateinit var adminRecyclerViewAdapter: AdminRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        mainHomeGuideViewModel =
//            ViewModelProvider(requireActivity()).get(MainHomeGuideSharedViewModel::class.java)

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
        //가이드 상태 변화감지 : 가이드 상태에 따라 화면구성 변경


    }



    //어댑터 초기화 함수 :
    private fun initAdapter() {
        adminRecyclerViewAdapter = AdminRecyclerViewAdapter(
            gallerySharedViewModel.galleryListLiveData.value ?: listOf(),
            //사진 클릭이벤트 : 상세페이지로 이동하여 사진 편집,삭제모드인 경우 삭제할 항목에 추가
            itemClickListener = { item, position ->
                when(gallerySharedViewModel.removeModeLiveData.value) {
                    "COMPLETE" -> {
                        gallerySharedViewModel.changeLayoutMode("Read")
                        GalleryReadFragment().show(childFragmentManager, "GALLERY_READ_FRAGMENT")

                    }
                }
                gallerySharedViewModel.updateGalleryList(item, position)

            },
            itemLongClickListener = { item, position -> },
        )

        binding.galleryRv.adapter = adminRecyclerViewAdapter
        binding.galleryRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }



}