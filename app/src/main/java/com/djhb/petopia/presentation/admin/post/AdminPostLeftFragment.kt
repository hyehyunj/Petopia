package com.djhb.petopia.presentation.admin.post

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
import com.djhb.petopia.databinding.FragmentAdminPostBinding
import shivam.developer.featuredrecyclerview.FeatureLinearLayoutManager

//관리자 게시글
class AdminPostLeftFragment : DialogFragment() {
    private val _binding: FragmentAdminPostBinding by lazy {
        FragmentAdminPostBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private val adminPostViewModel: AdminPostViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.adminPostTvTitle.text = "배웅하기"
        adminPostLeftDataObserver()
        adminPostLeftButtonClickListener()
        initFoldingCells()
    }

    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun adminPostLeftButtonClickListener() {
        //뒤로가기버튼 클릭이벤트 :
        binding.adminPostIvBack.setOnClickListener {
            dismiss()
        }

    }


    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun adminPostLeftDataObserver() {
adminPostViewModel.adminPostLeftListLiveData.observe(viewLifecycleOwner) { post ->
//    val fcTitleList = listOf(binding.adminPostFc1Title)
//    val fcSubTitleList = listOf()
//        val fcPostList = listOf()
//  for(i in post) {
//  fcPostList[i] = i.title
//
//
//}
}
    }

//게시글 구성하는 함수
    private fun initFoldingCells() {

    val featuredRecyclerView = binding.adminPostRv
    val layoutManager = FeatureLinearLayoutManager(requireContext())
    featuredRecyclerView.layoutManager = layoutManager
    val adapter = AdminPostFeatureRecyclerViewAdapter()
    val list = listOf("1","2","3","4","5","6","7","8")
    adapter.swapData(list)
    featuredRecyclerView.adapter = adapter

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