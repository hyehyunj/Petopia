package com.djhb.petopia.presentation.home

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.djhb.petopia.R
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.PetAppearance
import com.djhb.petopia.databinding.FragmentHomePetopiaBinding
import com.djhb.petopia.presentation.MainActivity
import com.djhb.petopia.presentation.album.AlbumFragment
import com.djhb.petopia.presentation.d_day.DDayFragment
import com.djhb.petopia.presentation.d_day.DDayViewModel
import com.djhb.petopia.presentation.d_day.DDayViewModelFactory
import com.djhb.petopia.presentation.letter.LetterFragment
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar


class HomePetopiaFragment : Fragment() {
    private val _binding: FragmentHomePetopiaBinding by lazy {
        FragmentHomePetopiaBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private lateinit var mainHomeGuideViewModel: MainHomeGuideSharedViewModel
    private val dDayViewModel by viewModels<DDayViewModel> {
        DDayViewModelFactory()
    }
    private val homePetopiaViewModel by viewModels<HomePetopiaViewModel> {
        HomePetopiaViewModelFactory()
    }

    private var isClickedBack = false
    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Log.i("HomePetopiaFragment", "click back")
            if(isClickedBack) {
                requireActivity().finish()
            } else {
                isClickedBack = true
                StyleableToast.makeText(
                    requireActivity(),
                    "한 번 더 누르면 앱이 종료됩니다.",
                    R.style.toast_common
                ).show()
                lifecycleScope.launch {
                    delay(2000)
                    isClickedBack = false
                }
            }
        }
    }

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
        if (LoginData.loginUser.dday != null) loadDDayData()



//구름 애니메이션
        initCloudAnimation()
//꽃 애니메이션
        initFlowerAnimation()
//반딧불 애니메이션
        initFireflyAnimation()

        // UI를 시간에 따라 업데이트
        updateUIBasedOnTime()

        // 주기적으로 UI를 업데이트하여 시간에 따른 변경을 반영
        val handler = android.os.Handler()
        val updateRunnable = object : Runnable {
            override fun run() {
                updateUIBasedOnTime()
                handler.postDelayed(this, 60000) // 매분마다 업데이트
            }
        }
        handler.post(updateRunnable)

        requireActivity().onBackPressedDispatcher.addCallback(backPressedCallback)
    }

    // 현재 시간이 20시 이후인지 확인
    private fun isAfterEightPM(): Boolean {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return hour >= 20
    }

    // 현재 시간이 8시 이전인지 확인
    private fun isBeforeEightAM(): Boolean {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return hour < 8
    }

    // 시간에 따라 UI 업데이트
    private fun updateUIBasedOnTime() {
        if (!isAdded) return // 액티비티에 연결되지 않았다면 리턴

        if (isAfterEightPM() || isBeforeEightAM()) {
            binding.skyBackground.visibility = View.VISIBLE
            binding.homeImgNightBackground.visibility = View.VISIBLE
            binding.petopiaMoon.visibility = View.VISIBLE
            binding.firefly.visibility = View.VISIBLE
            binding.homeImgBackground.visibility = View.GONE
            //글자색 흰색으로
            binding.homeTvNameUser.setTextAppearance(R.style.common_text_16_white)
            binding.homeTvNamePet.setTextAppearance(R.style.point_text_32_white)
        } else {
            binding.skyBackground.visibility = View.GONE
            binding.homeImgNightBackground.visibility = View.GONE
            binding.petopiaMoon.visibility = View.GONE
            binding.firefly.visibility = View.GONE
            binding.homeImgBackground.visibility = View.VISIBLE
            binding.homeTvNameUser.setTextAppearance(R.style.common_text_16_dark_gray)
            binding.homeTvNamePet.setTextAppearance(R.style.point_text_32_dark_gray)
        }


    }

    //구름 애니메이션
    private fun initCloudAnimation() {
        binding.homeImgCloud.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // 레이아웃이 그려진 후 실행
                binding.homeImgCloud.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val displayMetrics = DisplayMetrics()
                requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
                val screenWidth = displayMetrics.widthPixels

                val cloudWidth = binding.homeImgCloud.width.toFloat()
                val endTranslationX = -(cloudWidth + screenWidth)

                // 애니메이션 설정
                val cloudAnimator = ObjectAnimator.ofFloat(
                    binding.homeImgCloud,
                    "translationX",
                    0f,
                    endTranslationX
                )
                cloudAnimator.duration = 150000
                cloudAnimator.repeatCount = ObjectAnimator.INFINITE
                cloudAnimator.repeatMode = ObjectAnimator.RESTART
                cloudAnimator.interpolator = LinearInterpolator()
                cloudAnimator.start()
            }
        })
    }

    private fun initFireflyAnimation() {
        //  반딧불에 애니메이션
        val fireflyImages = listOf(
            binding.firefly1,
            binding.firefly2,
            binding.firefly3,
            binding.firefly4,
            binding.firefly5
        )

        fireflyImages.forEach { firefly ->
            // 반딧불 이동범위를 설정
            val endX = (-500..800).random().toFloat()
            val endY = (-100..800).random().toFloat()

            // X축 애니메이션
            val xAnimator = ObjectAnimator.ofFloat(firefly, "translationX", 0f, endX)
            xAnimator.duration = (5000..10000).random().toLong() // 랜덤한 지속시간
            xAnimator.repeatCount = ObjectAnimator.INFINITE
            xAnimator.repeatMode = ObjectAnimator.REVERSE

            // Y축 애니메이션
            val yAnimator = ObjectAnimator.ofFloat(firefly, "translationY", 0f, endY)
            yAnimator.duration = (5000..10000).random().toLong() // 랜덤한 지속시간
            yAnimator.repeatCount = ObjectAnimator.INFINITE
            yAnimator.repeatMode = ObjectAnimator.REVERSE

            // 애니메이션 시작
            xAnimator.start()
            yAnimator.start()
        }
    }

    //꽃 애니메이션
    private fun initFlowerAnimation() {


        // flower2 애니메이션(left)
        val flower2Animator = ObjectAnimator.ofFloat(binding.homeLeftFlower2, "rotation", -5f, 5f)
        flower2Animator.duration = 2200
        flower2Animator.repeatCount = ObjectAnimator.INFINITE
        flower2Animator.repeatMode = ObjectAnimator.REVERSE
        flower2Animator.start()

        // flower3 애니메이션(left)
        val flower3Animator = ObjectAnimator.ofFloat(binding.homeLeftFlower3, "rotation", -5f, 5f)
        flower3Animator.duration = 2400
        flower3Animator.repeatCount = ObjectAnimator.INFINITE
        flower3Animator.repeatMode = ObjectAnimator.REVERSE
        flower3Animator.start()

        // flower4 애니메이션(left)
        val flower4Animator = ObjectAnimator.ofFloat(binding.homeLeftFlower4, "rotation", -5f, 5f)
        flower4Animator.duration = 2600
        flower4Animator.repeatCount = ObjectAnimator.INFINITE
        flower4Animator.repeatMode = ObjectAnimator.REVERSE
        flower4Animator.start()


        // flower2 애니메이션 (right)
        val rightFlower2Animator =
            ObjectAnimator.ofFloat(binding.homeRightFlower2, "rotation", -5f, 5f)
        rightFlower2Animator.duration = 2300
        rightFlower2Animator.repeatCount = ObjectAnimator.INFINITE
        rightFlower2Animator.repeatMode = ObjectAnimator.REVERSE
        rightFlower2Animator.start()

        // flower3 애니메이션 (right)
        val rightFlower3Animator =
            ObjectAnimator.ofFloat(binding.homeRightFlower3, "rotation", -5f, 5f)
        rightFlower3Animator.duration = 2500
        rightFlower3Animator.repeatCount = ObjectAnimator.INFINITE
        rightFlower3Animator.repeatMode = ObjectAnimator.REVERSE
        rightFlower3Animator.start()

        // flower4 애니메이션 (right)
        val rightFlower4Animator =
            ObjectAnimator.ofFloat(binding.homeRightFlower4, "rotation", -5f, 5f)
        rightFlower4Animator.duration = 2700
        rightFlower4Animator.repeatCount = ObjectAnimator.INFINITE
        rightFlower4Animator.repeatMode = ObjectAnimator.REVERSE
        rightFlower4Animator.start()

        // flower5 애니메이션 (right)
        val rightFlower5Animator =
            ObjectAnimator.ofFloat(binding.homeRightFlower5, "rotation", -5f, 5f)
        rightFlower5Animator.duration = 2900
        rightFlower5Animator.repeatCount = ObjectAnimator.INFINITE
        rightFlower5Animator.repeatMode = ObjectAnimator.REVERSE
        rightFlower5Animator.start()

    }

    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun homePetopiaButtonClickListener() {
        //갤러리버튼 클릭이벤트 : 클릭시 갤러리 이동

        binding.homeIvGallery.setOnClickListener {
            if (mainHomeGuideViewModel.guideStateLiveData.value != "DONE")
                toastMoveUnder() else showAlbumFragment()
        }
        //편지버튼 클릭이벤트 : 클릭시 편지함 이동
        binding.homeIvLetter.setOnClickListener {
            if (mainHomeGuideViewModel.guideStateLiveData.value != "DONE")
                toastMoveUnder()
            else showLetterFragment()
        }
        //가이드 버튼 클릭이벤트 : 클릭시 가이드 시작
        binding.homeTvGuide.setOnClickListener {
            binding.homeTvGuide.clearAnimation() // 애니메이션 중지
            if (mainHomeGuideViewModel.userDataLiveData.value?.completedGuide == true)
                (activity as MainActivity).showWelcomeGuideFragment()
            else (activity as MainActivity).showGuideFragment()
        }

        //디데이 버튼 클릭이벤트 : 클릭시 디데이 설정 이동
        binding.homeIvDate.setOnClickListener {
            if (mainHomeGuideViewModel.guideStateLiveData.value != "DONE")
                toastMoveUnder()
            else DDayFragment().show(childFragmentManager, "ALARM_FRAGMENT")
        }
    }

    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun guideDataObserver() {
        //가이드 상태 변화감지 : 가이드 상태에 따라 화면구성 변경

        mainHomeGuideViewModel.guideStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                "NONE" -> {
                    initGuideAnimation()
                    binding.apply {
                        homeTvNameUser.isVisible = false
                        homeTvNamePet.isVisible = false
                        homeIvPet.isVisible = false
                        homeTvPetMassage.isVisible = false
                        homeIvArrowUnder.isVisible = true
                        homeIvGallery.isVisible = false
                        homeIvDate.isVisible = false
                        homeIvLetter.isVisible = false
                        homeTvGuide.isVisible = true
                        homeIvCall.isVisible = true
                        homeTvCallTitle.isVisible = true
                        homeTvCallSubTitle.isVisible = true
                    }
                }

                "ESSENTIAL", "OPTIONAL" -> {
                    binding.apply {
                        homeTvGuide.isVisible = false
                        homeIvCall.isVisible = false
                        homeTvCallTitle.isVisible = false
                        homeTvCallSubTitle.isVisible = false
                        homeIvArrowUnder.isVisible = false
                    }
                }

                "ESSENTIAL_DONE" -> {
                    if (LoginData.loginUser.pet?.petName == null) mainHomeGuideViewModel.setPetData()
                    binding.apply {
                        homeTvNameUser.isVisible = true
                        homeTvNamePet.isVisible = true
                        homeIvPet.isVisible = true
                    }
                }

                "DONE" -> {
                    binding.apply {
                        homeTvNameUser.isVisible = true
                        homeTvNamePet.isVisible = true
                        homeIvPet.isVisible = true
                        homeIvDate.isVisible = true
                        homeTvPetMassage.isVisible = true
                        homeIvArrowUnder.isVisible = true
                        homeIvGallery.isVisible = true
                        homeIvLetter.isVisible = true
                        homeTvGuide.isVisible = false
                        homeIvCall.isVisible = false
                        homeTvCallTitle.isVisible = false
                        homeTvCallSubTitle.isVisible = false
                    }
                }
            }
        }

        mainHomeGuideViewModel.guideFunctionLiveData.observe(viewLifecycleOwner) {
            when (it) {
                "PETOPIA" -> binding.apply {
                    homeIvGallery.isVisible = false
                    homeIvLetter.isVisible = false
                }

                "GALLERY_LETTER" -> binding.apply {
                    homeIvGallery.isVisible = true
                    homeIvLetter.isVisible = true
                    homeIvDate.isVisible = false
                }

                "D_DAY" -> binding.apply {
                    homeIvDate.isVisible = true
                }

                "MOVE_MEMORY_BRIDGE" -> {
                    binding.homeIvArrowUnder.isVisible = true
                }
            }
        }

        dDayViewModel.dDayLiveData.observe(viewLifecycleOwner) {
            when (it) {
                "" -> binding.homeIvDate.text = "미설정"
                "-0" -> binding.homeIvDate.text = "D-Day"
                else -> binding.homeIvDate.text = "D${it}"
            }
        }


    }


    private fun loadDDayData() {

        dDayViewModel.loadUserDate()
        dDayViewModel.calculateDate()
        dDayViewModel.loadAlarm(requireActivity())
        binding.homeIvDate.text = dDayViewModel.dDayLiveData.value
    }

    private fun toastMoveUnder() {
        if (mainHomeGuideViewModel.guideFunctionLiveData.value == "MOVE_MEMORY_BRIDGE")
            StyleableToast.makeText(
                requireActivity(),
                "아래로 이동해주세요.",
                R.style.toast_common
            ).show()
        else StyleableToast.makeText(
            requireActivity(),
            "가이드 종료 후 이용 가능합니다.",
            R.style.toast_common
        )
            .show()
    }

    //반려동물 메시지 출력해주는 함수
    private fun showPetMassage() {
        binding.homeTvPetMassage.text = homePetopiaViewModel.getPetMassage()
    }

    //반려동물과 보호자 정보를 화면에 표시해주는 함수
    private fun getUserAndPet() {
        binding.apply {
            homeTvNameUser.text = "보호자 : " + mainHomeGuideViewModel.getUserName() + " 님"
            homeTvNamePet.text = mainHomeGuideViewModel.userDataLiveData.value?.pet?.petName
            when (mainHomeGuideViewModel.userDataLiveData.value?.pet?.petAppearance) {
                PetAppearance.ABYSSINIAN -> binding.homeIvPet.setImageResource(R.drawable.img_abyssinian)
                PetAppearance.AMERICANSHORTHAIR -> binding.homeIvPet.setImageResource(R.drawable.img_americanshoerthair)
                PetAppearance.BICHON -> binding.homeIvPet.setImageResource(R.drawable.img_bichon)
                PetAppearance.CHIHUAHUA -> binding.homeIvPet.setImageResource(R.drawable.img_chihuahua)
                PetAppearance.KOREANSHORTHAIR -> binding.homeIvPet.setImageResource(R.drawable.img_koreanshorthair)
                PetAppearance.MALTESE -> binding.homeIvPet.setImageResource(R.drawable.img_maltese)
                PetAppearance.NORWEGIANFOREST -> binding.homeIvPet.setImageResource(R.drawable.img_norwegianforest)
                PetAppearance.PERSIAN -> binding.homeIvPet.setImageResource(R.drawable.img_persian)
                PetAppearance.POMERANIAN -> binding.homeIvPet.setImageResource(R.drawable.img_pomeranian)
                PetAppearance.POODLE -> binding.homeIvPet.setImageResource(R.drawable.img_poodle)
                PetAppearance.RETRIEVER -> binding.homeIvPet.setImageResource(R.drawable.img_retriever)
                PetAppearance.RUSSIANBLUE -> binding.homeIvPet.setImageResource(R.drawable.img_russianblue)
                PetAppearance.SCOTTISHFOLD -> binding.homeIvPet.setImageResource(R.drawable.img_scottishfold)
                PetAppearance.SHIBA -> binding.homeIvPet.setImageResource(R.drawable.img_shiba)
                PetAppearance.SHIHTZU -> binding.homeIvPet.setImageResource(R.drawable.img_shihtzu)
                PetAppearance.SIAMESE -> binding.homeIvPet.setImageResource(R.drawable.img_siamese)
                PetAppearance.TURKISHANGORA -> binding.homeIvPet.setImageResource(R.drawable.img_turkishangora)
                PetAppearance.WELSHCORGI -> binding.homeIvPet.setImageResource(R.drawable.img_welshcorgi)
                null -> binding.homeIvPet.setImageResource(R.drawable.img_shihtzu)
            }
        }
    }

    private fun showAlbumFragment() {
        (activity as MainActivity).showAlbumFragment()
    }

    private fun initGuideAnimation() {
        binding.homeTvGuide.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), R.anim.concentrate_button)
        )
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
        mainHomeGuideViewModel.userDataLiveData.observe(viewLifecycleOwner) {
            getUserAndPet()
        }
        initAnimation()
        showPetMassage()
        mainHomeGuideViewModel.checkUserGuideState()

    }

}