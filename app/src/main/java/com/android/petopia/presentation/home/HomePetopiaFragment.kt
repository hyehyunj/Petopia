package com.android.petopia.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.petopia.R
import com.android.petopia.databinding.FragmentHomePetopiaBinding
import com.android.petopia.presentation.gallery.GalleryFragment
import com.android.petopia.presentation.guide.GuideCancelDialogFragment
import com.android.petopia.presentation.guide.GuideFragment
import com.android.petopia.presentation.guide.GuideViewModel
import com.android.petopia.presentation.letter.LetterFragment


class HomePetopiaFragment : Fragment() {
    private val _binding: FragmentHomePetopiaBinding by lazy {
        FragmentHomePetopiaBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private val homePetopiaViewModel by viewModels<HomePetopiaGuideSharedViewModel>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homePetopiaButtonClickListener()
        guideDataObserver()





    }

    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun homePetopiaButtonClickListener() {
        //갤러리버튼 클릭이벤트 : 클릭시 갤러리 이동
        binding.homeIvGallery.setOnClickListener {
            showGalleryFragment()
        }

        //가이드 버튼 클릭이벤트 : 클릭시 가이드 시작
        binding.homeTvGuide.setOnClickListener {
            showGuideFragment()
        }

        //편지버튼 클릭이벤트 : 클릭시 편지함 이동
        binding.homeIvLetter.setOnClickListener {
            showLetterFragment()
        }
    }

    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun guideDataObserver() {
        //가이드 상태 변화감지 : 가이드 상태에 따라 화면구성 변경
        homePetopiaViewModel.guideStateLiveData.observe(viewLifecycleOwner) {
            when(it) {
                "NONE" -> binding.apply {
                        homeTvNameUser.isVisible = false
                        homeTvNamePet.isVisible = false
                        homeIvPet.isVisible = false
                        homeIvArrowUnder.isVisible = false
                        homeIvGallery.isVisible = false
                        homeIvLetter.isVisible = false
                        homeTvGuide.isVisible = true
                    }
                "ESSENTIAL" -> binding.apply {
                    homeTvGuide.isVisible = false
                }
                     "DONE" -> binding.apply {
                homeTvNameUser.isVisible = true
                homeTvNamePet.isVisible = true
                homeIvPet.isVisible = true
                homeIvArrowUnder.isVisible = true
                homeIvGallery.isVisible = true
                homeIvLetter.isVisible = true
                homeTvGuide.isVisible = false
            }
                "OPTIONAL" -> binding.apply {
                    homeTvNameUser.isVisible = true
                    homeTvNamePet.isVisible = true
                    homeIvPet.isVisible = true
                    homeIvArrowUnder.isVisible = false
                    homeIvGallery.isVisible = false
                    homeIvLetter.isVisible = false
                }


            }
        }
}

    private fun showGalleryFragment() {
        GalleryFragment().show(childFragmentManager, "G_FRAGMENT")

//        childFragmentManager.beginTransaction()
//            .replace(
//                R.id.home_petopia_container, GalleryFragment()
//            )
//            .setReorderingAllowed(true)
//            .addToBackStack(null)
//            .commit()

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

    private fun showGuideFragment() {
        homePetopiaViewModel.updateGuideState("ESSENTIAL")
        childFragmentManager.beginTransaction()
            .replace(
                R.id.home_petopia_container, GuideFragment()
            )
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()

    }

    fun cancelGuide() {
        GuideCancelDialogFragment().show(childFragmentManager, "GUIDE_CANCEL_DIALOG_FRAGMENT")

    }


    private fun showLetterFragment() {
        LetterFragment().show(childFragmentManager, "LETTER_FRAGMENT")

    }




}