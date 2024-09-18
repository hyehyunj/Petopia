package com.djhb.petopia.presentation.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.djhb.petopia.databinding.FragmentAlbumMainBinding


class AlbumMainFragment : Fragment() {


    private val _binding: FragmentAlbumMainBinding by lazy {
        FragmentAlbumMainBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding


    private val albumSharedViewModel by viewModels<AlbumSharedViewModel> {
        AlbumSharedViewModelFactory()
    }
//    private val gallerySharedViewModel by viewModels<GallerySharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //버튼 클릭이벤트
        albumMainButtonClickListener()
        //데이터 변화감지
        albumMainDataObserver()

    }


    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun albumMainButtonClickListener() {


    }


    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun albumMainDataObserver() {


    }




}