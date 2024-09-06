package com.djhb.petopia.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.djhb.petopia.R
import com.djhb.petopia.data.PetAppearance
import com.djhb.petopia.databinding.FragmentHomePetopiaBinding
import com.djhb.petopia.presentation.MainActivity
import com.djhb.petopia.presentation.gallery.GalleryFragment
import com.djhb.petopia.presentation.letter.LetterFragment
import io.github.muddz.styleabletoast.StyleableToast


class HomePetopiaFragment : Fragment() {
    private val _binding: FragmentHomePetopiaBinding by lazy {
        FragmentHomePetopiaBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private lateinit var mainHomeGuideViewModel: MainHomeGuideSharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mainHomeGuideViewModel =
            ViewModelProvider(requireActivity()).get(MainHomeGuideSharedViewModel::class.java)

        homePetopiaButtonClickListener()
        guideDataObserver()
        mainHomeGuideViewModel.userPetLiveData.observe(viewLifecycleOwner) {
            binding.apply {
                Log.d("이름", "${it.petName}")
                homeTvNamePet.text = it.petName
                when (it.petAppearance) {
                    PetAppearance.ABYSSINIAN -> binding.homeIvPet.setImageResource(R.drawable.img_abyssinian)
                    PetAppearance.AMERICANSHORTHAIR->binding.homeIvPet.setImageResource(R.drawable.img_americanshoerthair)
                    PetAppearance.BICHON->binding.homeIvPet.setImageResource(R.drawable.img_bichon)
                    PetAppearance.CHIHUAHUA->binding.homeIvPet.setImageResource(R.drawable.img_chihuahua)
                    PetAppearance.KOREANSHORTHAIR->binding.homeIvPet.setImageResource(R.drawable.img_koreanshorthair)
                    PetAppearance.MALTESE->binding.homeIvPet.setImageResource(R.drawable.img_maltese)
                    PetAppearance.NORWEGIANFOREST->binding.homeIvPet.setImageResource(R.drawable.img_norwegianforest)
                    PetAppearance.PERSIAN->binding.homeIvPet.setImageResource(R.drawable.img_persian)
                    PetAppearance.POMERANIAN->binding.homeIvPet.setImageResource(R.drawable.img_pomeranian)
                    PetAppearance.POODLE->binding.homeIvPet.setImageResource(R.drawable.img_poodle)
                    PetAppearance.RETRIEVER->binding.homeIvPet.setImageResource(R.drawable.img_retriever)
                    PetAppearance.RUSSIANBLUE->binding.homeIvPet.setImageResource(R.drawable.img_russianblue)
                    PetAppearance.SCOTTISHFOLD->binding.homeIvPet.setImageResource(R.drawable.img_scottishfold)
                    PetAppearance.SHIBA->binding.homeIvPet.setImageResource(R.drawable.img_shiba)
                    PetAppearance.SHIHTZU->binding.homeIvPet.setImageResource(R.drawable.img_shihtzu)
                    PetAppearance.SIAMESE->binding.homeIvPet.setImageResource(R.drawable.img_siamese)
                    PetAppearance.TURKISHANGORA->binding.homeIvPet.setImageResource(R.drawable.img_turkishangora)
                    PetAppearance.WELSHCORGI->binding.homeIvPet.setImageResource(R.drawable.img_welshcorgi)
                }
            }
        }


    }

    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun homePetopiaButtonClickListener() {
        //갤러리버튼 클릭이벤트 : 클릭시 갤러리 이동

        binding.homeIvGallery.setOnClickListener {
            if (mainHomeGuideViewModel.guideStateLiveData.value == "OPTIONAL")
                toastMoveUnder() else showGalleryFragment()
        }
        //편지버튼 클릭이벤트 : 클릭시 편지함 이동
        binding.homeIvLetter.setOnClickListener {
            if (mainHomeGuideViewModel.guideStateLiveData.value == "OPTIONAL")
                toastMoveUnder()
            else showLetterFragment()
        }
        //가이드 버튼 클릭이벤트 : 클릭시 가이드 시작
        binding.homeTvGuide.setOnClickListener {
            (activity as MainActivity).showGuideFragment()
        }
    }

    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun guideDataObserver() {
        //가이드 상태 변화감지 : 가이드 상태에 따라 화면구성 변경

        mainHomeGuideViewModel.guideStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                "NONE" -> {
                    binding.apply {
                        homeTvNameUser.isVisible = false
                        homeTvNamePet.isVisible = false
                        homeIvPet.isVisible = false
                        homeIvArrowUnder.isVisible = false
                        homeIvGallery.isVisible = false
                        homeIvLetter.isVisible = false
                        homeTvGuide.isVisible = true
                    }
                }

                "ESSENTIAL" -> {
                    binding.apply {
                        homeTvGuide.isVisible = false
                    }
                }

                "DONE" -> {
                    binding.apply {
                        homeTvNameUser.isVisible = true
                        homeTvNamePet.isVisible = true
                        homeIvPet.isVisible = true
                        homeIvArrowUnder.isVisible = true
                        homeIvGallery.isVisible = true
                        homeIvLetter.isVisible = true
                        homeTvGuide.isVisible = false
                    }
                }

                "OPTIONAL" -> {
                    binding.apply {
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

        mainHomeGuideViewModel.guideFunctionLiveData.observe(viewLifecycleOwner) {
            when (it) {
                "GALLERY_LETTER_GONE" -> binding.apply {
                    homeIvGallery.isVisible = false
                    homeIvLetter.isVisible = false
                }

                "GALLERY_LETTER" -> binding.apply {
                    homeIvGallery.isVisible = true
                    homeIvLetter.isVisible = true
                }

                "MOVE_MEMORY_BRIDGE" -> {
                    binding.homeIvArrowUnder.isVisible = true
                }

            }
        }
    }

    private fun toastMoveUnder() {
        if (mainHomeGuideViewModel.guideFunctionLiveData.value == "MOVE_MEMORY_BRIDGE")
            StyleableToast.makeText(
                requireActivity(),
                "아래로 이동해주세요.",
                R.style.toast_custom
            ).show()
        else StyleableToast.makeText(
            requireActivity(),
            "가이드 종료 후 이용 가능합니다.",
            R.style.toast_custom
        )
            .show()
    }


    private fun showGalleryFragment() {
        GalleryFragment().show(childFragmentManager, "GALLERY_FRAGMENT")


//        childFragmentManager.beginTransaction()
//            .replace(
//                R.id.home_petopia_container, GalleryFragment()
//            )
//            .setReorderingAllowed(true)
//            .addToBackStack(null)
//            .commit()

        //위는 자식프래그먼트로 추가하기(뒤로가기시 트랜잭션 정의해줘야함)
        // 아래는 액티비티에서 추가하기(프레임 달라서 뒤로가기 정의 필요없음)
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

    private fun initAnimation() {
        binding.homeIvArrowUnder.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), R.anim.move_under)
        )
    }

    private fun showLetterFragment() {
        LetterFragment().show(childFragmentManager, "LETTER_FRAGMENT")
    }

    override fun onResume() {
        super.onResume()
        initAnimation()

    }

}