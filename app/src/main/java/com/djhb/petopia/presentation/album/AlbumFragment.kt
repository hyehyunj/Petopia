package com.djhb.petopia.presentation.album

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
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.djhb.petopia.databinding.FragmentAlbumBinding
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer2


class AlbumFragment : DialogFragment() {


    private val _binding: FragmentAlbumBinding by lazy {
        FragmentAlbumBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding

    private lateinit var albumViewPager: ViewPager2
    private val albumSharedViewModel by viewModels<AlbumSharedViewModel> {
        AlbumSharedViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //버튼 클릭이벤트
        galleryButtonClickListener()
        //데이터 변화감지
        galleryDataObserver()
        initDialog()
        initAlbumViewPager()

    }


    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun galleryButtonClickListener() {


    }


    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun galleryDataObserver() {


    }


    private fun initAlbumViewPager() {
        albumViewPager = binding.albumPager
        val albumViewPagerAdapter = AlbumViewPagerAdapter(requireActivity())
        albumViewPager.adapter = albumViewPagerAdapter
        //페이지 변환기 : 책 넘기는 모션으로 변환
        val bookFlipPageTransformer = BookFlipPageTransformer2()
        bookFlipPageTransformer.isEnableScale = false
        albumViewPager.setPageTransformer(bookFlipPageTransformer)


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
        params?.width = (deviceWidth * 1).toInt()
        params?.height = (deviceWidth * 2.1).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}