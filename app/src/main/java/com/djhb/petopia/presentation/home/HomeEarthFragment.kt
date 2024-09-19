package com.djhb.petopia.presentation.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.djhb.petopia.R
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.databinding.FragmentHomeEarthBinding
import com.djhb.petopia.presentation.MainActivity
import com.djhb.petopia.presentation.admin.AdminFragment
import com.djhb.petopia.presentation.admin.AdminViewModel
import com.djhb.petopia.presentation.admin.post.AdminPostLeftFragment
import com.djhb.petopia.presentation.admin.post.AdminPostRightFragment
import com.djhb.petopia.presentation.community.Authority
import com.djhb.petopia.presentation.community.CommunityMainFragment
import com.djhb.petopia.presentation.my.MyFragment
import com.djhb.petopia.presentation.setting.SettingFragment
import io.github.muddz.styleabletoast.StyleableToast
import java.util.Calendar

class HomeEarthFragment : Fragment() {
    private val _binding: FragmentHomeEarthBinding by lazy {
        FragmentHomeEarthBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private lateinit var mainHomeGuideViewModel: MainHomeGuideSharedViewModel
    private val adminViewModel: AdminViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainHomeGuideViewModel =
            ViewModelProvider(requireActivity()).get(MainHomeGuideSharedViewModel::class.java)

        homeEarthButtonClickListener()
        homeEarthDataObserver()
        // 꽃잎 애니메이션 시작
        startPetalsAnimation()
        updateUIBasedOnTime()//시간 체크
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
        if (isAfterEightPM() || isBeforeEightAM()) {
            binding.earthNightSky.visibility = View.VISIBLE
            binding.homeImgNightBackground.visibility = View.VISIBLE
            binding.homeImgBackground.visibility = View.GONE


        } else {
            binding.earthNightSky.visibility = View.GONE
            binding.homeImgNightBackground.visibility = View.GONE
            binding.homeImgBackground.visibility = View.VISIBLE

        }


    }
    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun homeEarthButtonClickListener() {
        //설정버튼 클릭이벤트 : 클릭시 설정 이동
        binding.homeEarthIvMy.setOnClickListener {

            if (LoginData.loginUser.authority == Authority.ADMIN) {
                AdminFragment().show(childFragmentManager, "ADMIN_FRAGMENT")
            } else {
                MyFragment().show(childFragmentManager, "MY_FRAGMENT")
            }
        }

        //좌측구름버튼 클릭이벤트 : 클릭시 관리자 추천글 배웅하기 이동
        binding.homeEarthIvCloudLeft.setOnClickListener {
            AdminPostLeftFragment().show(childFragmentManager, "ADMIN_POST_LEFT_FRAGMENT")
        }

        //우측구름버튼 클릭이벤트 : 클릭시 관리자 추천글 잘지내기 이동
        binding.homeEarthIvCloudRight.setOnClickListener {
            AdminPostRightFragment().show(childFragmentManager, "ADMIN_POST_RIGHT_FRAGMENT")        }

        //커뮤니티버튼 클릭이벤트 : 클릭시 커뮤니티 이동
        binding.homeEarthIvCommunity.setOnClickListener {
            if (mainHomeGuideViewModel.guideStateLiveData.value != "DONE" && mainHomeGuideViewModel.guideStateLiveData.value != "NONE") StyleableToast.makeText(
                requireActivity(),
                "가이드 종료 후 이용 가능합니다.",
                R.style.toast_common
            )
                .show() else

            childFragmentManager.beginTransaction()
                .replace(
                    R.id.home_earth_container, CommunityMainFragment()
                )
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()

        }

    }

    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun homeEarthDataObserver() {
        //가이드 상태 변화감지 : 가이드 상태에 따라 화면구성 변경

    }

    // 꽃잎 애니메이션 함수
    private fun startPetalsAnimation() {
        // 첫 번째 꽃잎 애니메이션
        val translationX = ObjectAnimator.ofFloat(
            binding.earthPetals1,
            "translationX",
            0f, 100f, 200f, 150f, 300f, 400f, 800f
        ).apply{
            repeatCount = ValueAnimator.INFINITE  // 무한 반복
            repeatMode = ValueAnimator.RESTART  // 끝나면 처음부터 다시 시작
        }
        val translationY = ObjectAnimator.ofFloat(
            binding.earthPetals1,
            "translationY",
            -1000f, 0f, 600f, 900f, 1200f, 2400f
        ).apply{
            repeatCount = ValueAnimator.INFINITE  // 무한 반복
            repeatMode = ValueAnimator.RESTART  // 끝나면 처음부터 다시 시작
        }

        val animatorSet = AnimatorSet().apply {
            playTogether(translationX, translationY)
            duration = 10000
            interpolator = AccelerateInterpolator()
        }
        animatorSet.start()

        // 두 번째 꽃잎 애니메이션
        val translationX2 = ObjectAnimator.ofFloat(
            binding.earthPetals2,
            "translationX",
            0f, 120f, 250f, 450f, 650f, 600f
        ).apply{
            repeatCount = ValueAnimator.INFINITE  // 무한 반복
            repeatMode = ValueAnimator.RESTART  // 끝나면 처음부터 다시 시작
        }
        val translationY2 = ObjectAnimator.ofFloat(
            binding.earthPetals2,
            "translationY",
            -1000f, 0f, 350f, 700f, 1050f, 1400f, 2400f
        ).apply{
            repeatCount = ValueAnimator.INFINITE  // 무한 반복
            repeatMode = ValueAnimator.RESTART  // 끝나면 처음부터 다시 시작
        }

        val animatorSet2 = AnimatorSet().apply {
            playTogether(translationX2, translationY2)
            duration = 8500
            interpolator = AccelerateInterpolator()
        }
        animatorSet2.start()

        // 세 번째 꽃잎 애니메이션 (10초 후 시작)
        binding.earthPetals3.postDelayed({
            val translationX3 = ObjectAnimator.ofFloat(
                binding.earthPetals3,
                "translationX",
                -100f, 0f, 150f, 300f, 500f, 700f, 900f
            ).apply{
                repeatCount = ValueAnimator.INFINITE  // 무한 반복
                repeatMode = ValueAnimator.RESTART  // 끝나면 처음부터 다시 시작
            }
            val translationY3 = ObjectAnimator.ofFloat(
                binding.earthPetals3,
                "translationY",
                -1000f, 0f, 400f, 800f, 1200f, 2400f
            ).apply{
                repeatCount = ValueAnimator.INFINITE  // 무한 반복
                repeatMode = ValueAnimator.RESTART  // 끝나면 처음부터 다시 시작
            }

            val animatorSet3 = AnimatorSet().apply {
                playTogether(translationX3, translationY3)
                duration = 10000
                interpolator = AccelerateInterpolator()
            }
            animatorSet3.start()
        }, 10000)

        // 네 번째 꽃잎 애니메이션 (10초 후 시작)
        binding.earthPetals4.postDelayed({
            val translationX4 = ObjectAnimator.ofFloat(
                binding.earthPetals4,
                "translationX",
                0f, 170f, 100f, 340f, 520f, 480f, 200f, 300f
            ).apply{
                repeatCount = ValueAnimator.INFINITE  // 무한 반복
                repeatMode = ValueAnimator.RESTART  // 끝나면 처음부터 다시 시작
            }
            val translationY4 = ObjectAnimator.ofFloat(
                binding.earthPetals4,
                "translationY",
                -1000f, 0f, 500f, 900f, 1300f, 2400f
            ).apply{
                repeatCount = ValueAnimator.INFINITE  // 무한 반복
                repeatMode = ValueAnimator.RESTART  // 끝나면 처음부터 다시 시작
            }

            val animatorSet4 = AnimatorSet().apply {
                playTogether(translationX4, translationY4)
                duration = 12000
                interpolator = AccelerateInterpolator()
            }
            animatorSet4.start()
        }, 10000)
    }


    fun showUndoToast() {
        StyleableToast.makeText(
            requireActivity(),
            getString(R.string.messege_undo),
            R.style.toast_undo
        ).show()
    }


}