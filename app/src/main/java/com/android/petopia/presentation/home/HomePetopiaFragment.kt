package com.android.petopia.presentation.home

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.android.petopia.R
import com.android.petopia.databinding.FragmentHomePetopiaBinding
import com.android.petopia.presentation.MainActivity
import com.android.petopia.presentation.gallery.GalleryFragment
import com.android.petopia.presentation.letter.LetterFragment
import com.android.petopia.presentation.memory.MemoryFragment


class HomePetopiaFragment : Fragment() {
    private val _binding: FragmentHomePetopiaBinding by lazy {
        FragmentHomePetopiaBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //버튼 클릭이벤트 : 클릭시 갤러리 이동
        binding.homeIvGallery.setOnClickListener {
            showGalleryFragment()
        }

        //버튼 클릭이벤트 : 클릭시 가이드 시작
        binding.homeIvGallery.setOnClickListener {
            showGalleryFragment()
        }

        //편지버튼 클릭이벤트 : 클릭시 편지함 이동
        binding.homeIvLetter.setOnClickListener {
            showLetterFragment()
        }





    }


    private fun showGalleryFragment() {
        childFragmentManager.beginTransaction()
            .replace(
                R.id.home_petopia_container, GalleryFragment(),"a"
            )
            .setReorderingAllowed(true)
            .addToBackStack("a")
            .commit()

        //위는 자식프래그먼트로 추가하기(뒤로가기시 트랜잭션 정의해줘야함)
    // 아래는 액티비티에서 추가하기(프레임 달라서 뒤로가기 정의 필요없음)
//        requireActivity().supportFragmentManager.beginTransaction()
//            .replace(
//                R.id.main_sub_frame, GalleryFragment(), "BACK_PETOPIA"
//            )
//            .setReorderingAllowed(true)
//            .addToBackStack("BACK_PETOPIA")
//            .commitAllowingStateLoss()
    }
    private fun showLetterFragment() {
        LetterFragment().show(childFragmentManager, "LETTER_FRAGMENT")

    }




}