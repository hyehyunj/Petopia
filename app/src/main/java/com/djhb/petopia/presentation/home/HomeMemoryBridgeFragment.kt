package com.djhb.petopia.presentation.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.djhb.petopia.R
import com.djhb.petopia.presentation.memory.MemoryViewModel
import com.djhb.petopia.data.remote.MemoryRepositoryImpl
import com.djhb.petopia.databinding.FragmentHomeMemoryBridgeBinding
import com.djhb.petopia.presentation.memory.MemoryFragment
import io.github.muddz.styleabletoast.StyleableToast
import java.util.Calendar
import java.util.concurrent.TimeUnit


class HomeMemoryBridgeFragment : Fragment() {

    private val _binding: FragmentHomeMemoryBridgeBinding by lazy {
        FragmentHomeMemoryBridgeBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private lateinit var mainHomeGuideViewModel: MainHomeGuideSharedViewModel
    private lateinit var memoryViewModel: MemoryViewModel


    private lateinit var emojiViews: List<ImageView>

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

        val memoryRepository = MemoryRepositoryImpl()
        val factory = MemoryViewModel.MemoryViewModelFactory(memoryRepository)

        memoryViewModel =
            ViewModelProvider(requireActivity(), factory).get(MemoryViewModel::class.java)

        loadMemory()
        scheduledMemory()


        memoryViewModel.memoryTitle.observe(viewLifecycleOwner) { text ->
            Log.d("memoryText", text)
            binding.homeMemoryBridgeTvMemoryTitle.text = text
        }

        val currentTitle = binding.homeMemoryBridgeTvMemoryTitle.text.toString()
        memoryViewModel.setMemoryTitle(currentTitle)

        homeMemoryBridgeButtonClickListener()
        homeMemoryBridgeDataObserver()

        // 초기 상태로 설정
        resetEmojiState()

        // `homeMemoryBridgeIvEmotion` 클릭 시 이모지를 다시 보이도록 설정
        binding.homeMemoryBridgeIvEmotion.setOnClickListener {
            if (mainHomeGuideViewModel.guideStateLiveData.value != "DONE")
                toastMoveUnder()
            else {

                resetEmojiState() // 클릭 시 이모지를 초기화
                binding.homeMemoryBridgeFeelingsContainer.visibility = View.VISIBLE
                binding.feelingsText.visibility = View.VISIBLE
                emojiViews.forEach { it.visibility = View.VISIBLE } // emojiViews 사용
            }
        }

        // emojiViews 초기화
        emojiViews = listOf(
            binding.emjSmile,
            binding.emjWhirl,
            binding.emjGloomy,
            binding.emjSoso,
            binding.emjHappy,
            binding.emjBad,
            binding.emjSick,
            binding.emjSad
        )

        setupEmojiClickListeners()
        updateUIBasedOnTime()
    }



    private fun setupEmojiClickListeners() {
        emojiViews.forEach { emoji ->
            emoji.setOnClickListener {
                // 선택된 이모지를 확대, 다른 이모지는 GONE
                animateSelectedEmoji(emoji)

                // 풍선 배경 변경
                changeBalloonBackground(emoji)


            }
        }
    }

    private fun changeBalloonBackground(selectedEmoji: ImageView) {
        val balloon1 = binding.homeMemoryBridgeIvEmotion2
        val balloon2 = binding.homeMemoryBridgeIvEmotion3
        val balloon3 = binding.homeMemoryBridgeIvEmotion

        when (selectedEmoji) {
            binding.emjSmile -> {
                balloon1.setImageResource(R.drawable.img_yello__balloon)
                balloon2.setImageResource(R.drawable.img_yello__balloon)
                balloon3.setImageResource(R.drawable.img_yello__balloon)
            }
            binding.emjSad -> {
                balloon1.setImageResource(R.drawable.img_blue__balloon)
                balloon2.setImageResource(R.drawable.img_blue__balloon)
                balloon3.setImageResource(R.drawable.img_blue__balloon)
            }
            binding.emjBad -> {
                balloon1.setImageResource(R.drawable.img_red_balloon)
                balloon2.setImageResource(R.drawable.img_red_balloon)
                balloon3.setImageResource(R.drawable.img_red_balloon)
            }
            binding.emjSoso -> {
                balloon1.setImageResource(R.drawable.img_green__balloon)
                balloon2.setImageResource(R.drawable.img_green__balloon)
                balloon3.setImageResource(R.drawable.img_green__balloon)
            }
            binding.emjWhirl -> {
                balloon1.setImageResource(R.drawable.img_purple__balloon)
                balloon2.setImageResource(R.drawable.img_purple__balloon)
                balloon3.setImageResource(R.drawable.img_purple__balloon)
            }
            binding.emjGloomy -> {
                balloon1.setImageResource(R.drawable.img_sodomy__balloon)
                balloon2.setImageResource(R.drawable.img_sodomy__balloon)
                balloon3.setImageResource(R.drawable.img_sodomy__balloon)
            }
            binding.emjSick -> {
                balloon1.setImageResource(R.drawable.img_orange_balloon)
                balloon2.setImageResource(R.drawable.img_orange_balloon)
                balloon3.setImageResource(R.drawable.img_orange_balloon)
            }
        }

        binding.homeMemoryBridgeFeelingsContainer.visibility = View.GONE
        emojiViews.filter { it != selectedEmoji }.forEach { otherEmoji ->
            otherEmoji.visibility = View.GONE
        }
        binding.feelingsText.visibility = View.GONE
    }



    // 선택된 이모지를 중앙에 배치하고 확대
    private fun animateSelectedEmoji(selectedEmoji: ImageView) {
        // 이모지를 확대 및 중앙으로 이동시키기 위한 애니메이션
        val scaleXAnimator = ObjectAnimator.ofFloat(selectedEmoji, "scaleX", 1f, 3f)
        val scaleYAnimator = ObjectAnimator.ofFloat(selectedEmoji, "scaleY", 1f, 3f)

        // 이모지의 마진을 제거하여 중앙 배치
        val layoutParams = selectedEmoji.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, 0, 0, 0)
        selectedEmoji.layoutParams = layoutParams

        // 이모지를 부모 레이아웃의 중앙으로 배치
        val parentView = binding.root
        val centerX = (parentView.width / 2) - (selectedEmoji.width * 1.5f / 2)
        val centerY = (parentView.height / 2) - (selectedEmoji.height * 1.5f / 2)

        val moveXAnimator = ObjectAnimator.ofFloat(selectedEmoji, "x", selectedEmoji.x, centerX)
        val moveYAnimator = ObjectAnimator.ofFloat(selectedEmoji, "y", selectedEmoji.y, centerY)

        // Y축 애니메이션
        val floatAnimatorY = ObjectAnimator.ofFloat(selectedEmoji, "translationY", 0f, -50f, 0f)
        floatAnimatorY.duration = 2000
        floatAnimatorY.repeatMode = ValueAnimator.REVERSE
        floatAnimatorY.repeatCount = ValueAnimator.INFINITE
        floatAnimatorY.interpolator = LinearInterpolator()

        // 애니메이션 실행
        moveXAnimator.duration = 500
        moveYAnimator.duration = 500
        scaleXAnimator.duration = 500
        scaleYAnimator.duration = 500

        moveXAnimator.start()
        moveYAnimator.start()
        scaleXAnimator.start()
        scaleYAnimator.start()

        // 이모지 애니메이션 시작
        floatAnimatorY.start()
        selectedEmoji.tag =
            listOf(scaleXAnimator, scaleYAnimator, floatAnimatorY, moveXAnimator, moveYAnimator)
        selectedEmoji.isClickable = false
        selectedEmoji.isFocusable = false

        //이모지 5초뒤 사라지기
        selectedEmoji.postDelayed({
            val fadeOutAnimator = ObjectAnimator.ofFloat(selectedEmoji, "alpha", 1f, 0f)
            fadeOutAnimator.duration = 1000
            fadeOutAnimator.start()

            // 애니메이션이 끝난 후 뷰를 GONE으로 설정
            fadeOutAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {  // Animator 타입으로 변경
                    selectedEmoji.visibility = View.GONE
                    Log.d("FadeOutAnimator", "Emoji is set to GONE after fade out animation ends.")
                }
            })

        }, 5000)
    }

    // 이모지 상태 초기화 함수
    private fun resetEmojiState() {
        val emojiViews = listOf(
            binding.emjSmile,
            binding.emjWhirl,
            binding.emjGloomy,
            binding.emjSoso,
            binding.emjHappy,
            binding.emjBad,
            binding.emjSick,
            binding.emjSad
        )

        emojiViews.forEach { emoji ->
            // 애니메이션 중지 (tag가 null이 아닌지 확인하고 안전하게 캐스팅)
            (emoji.tag as? List<ObjectAnimator>)?.forEach { it.cancel() }

            // 이모지 초기화
            emoji.scaleX = 1f
            emoji.scaleY = 1f
            emoji.translationX = 0f
            emoji.translationY = 0f
            emoji.alpha = 1f
            emoji.visibility = View.GONE // 초기 GONE 설정
        }


        // feelings_container와 텍스트 다시 보이도록 설정
        binding.homeMemoryBridgeFeelingsContainer.visibility = View.GONE
        binding.feelingsText.visibility = View.GONE
    }

    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun homeMemoryBridgeButtonClickListener() {
        val memoryRepository = MemoryRepositoryImpl()
        val factory = MemoryViewModel.MemoryViewModelFactory(memoryRepository)

        memoryViewModel =
            ViewModelProvider(requireActivity(), factory).get(MemoryViewModel::class.java)

        //메모리버튼 클릭이벤트 : 클릭시 메모리북 이동
        binding.homeMemoryBridgeMemoryContainer.setOnClickListener {
            if (mainHomeGuideViewModel.guideStateLiveData.value == "OPTIONAL")
                toastMoveUnder() else setMemoryFragment()

            Log.d("memorybuttonclick", "메모리버튼 클릭")

            // 메모리 작성 완료시 투데이 메모리문구, 버튼 변경
            memoryViewModel.isMemorySaved.observe(viewLifecycleOwner) {
                if (it == true) {
                    binding.homeMemoryBridgeTvMemoryTitle.text = "메모리북 기록 완료"
                }
            }
        }

        //편지버튼 클릭이벤트 : 클릭시 편지 이동
//        binding.homeMemoryBridgeBtnLetter.setOnClickListener {
//            setLetterFragment()
//        }
    }

    private fun scheduledMemory() {
        val currentDate = Calendar.getInstance()

        val targetDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        if (currentDate.after(targetDate)) {
            targetDate.add(Calendar.DAY_OF_MONTH, 1)
        }

        val initialDelay = targetDate.timeInMillis - currentDate.timeInMillis

        Log.d("initialDelay", initialDelay.toString())

        val workRequest =
            PeriodicWorkRequestBuilder<UpdateMemoryTextWorker>(1, TimeUnit.DAYS).setInitialDelay(
                initialDelay,
                TimeUnit.MILLISECONDS
            ).build()

        WorkManager.getInstance(requireContext()).enqueue(workRequest)
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
            binding.skyBackground.visibility = View.VISIBLE
            binding.homeImgNightBackground.visibility = View.VISIBLE


        } else {
            binding.skyBackground.visibility = View.GONE
            binding.homeImgNightBackground.visibility = View.GONE

        }


    }


    private fun loadMemory() {
        val sharedPreferences =
            requireContext().getSharedPreferences("Memory", Context.MODE_PRIVATE)
        val memoryText = sharedPreferences.getString("memoryText", null)

        binding.homeMemoryBridgeTvMemoryTitle.text = memoryText
        memoryViewModel.setMemoryTitle(memoryText.toString())

        Log.d("memoryText", memoryText.toString())
    }

    private fun homeMemoryBridgeDataObserver() {
        mainHomeGuideViewModel.guideStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                "OPTIONAL" -> binding.homeMemoryBridgeIvArrowUnder.isVisible = false
                "NONE", "DONE" -> binding.homeMemoryBridgeIvArrowUnder.isVisible = true
            }
        }

        mainHomeGuideViewModel.guideFunctionLiveData.observe(viewLifecycleOwner) {
            when (it) {
                "MOVE_EARTH" -> binding.homeMemoryBridgeIvArrowUnder.isVisible = true
            }
        }
    }

    private fun toastMoveUnder() {
        if (mainHomeGuideViewModel.guideFunctionLiveData.value == "MOVE_EARTH")
            StyleableToast.makeText(
                requireActivity(),
                "아래로 이동해주세요.",
                R.style.toast_common
            ).show()
        else StyleableToast.makeText(
            requireActivity(),
            "가이드 종료 후 이용 가능합니다.",
            R.style.toast_common
        ).show()
    }

    private fun initAnimation() {
        binding.homeMemoryBridgeIvArrowUnder.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), R.anim.move_under)
        )
        binding.homeMemoryBridgeIvEmotion.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), R.anim.ballon)
        )


        //  반짝이는 효과
        val alphaAnimator = ObjectAnimator.ofFloat(binding.bridgeShiny1, "alpha", 1f, 0f, 1f)
        alphaAnimator.duration = 2500 //2.5초에 한번
        alphaAnimator.repeatMode = ValueAnimator.REVERSE
        alphaAnimator.repeatCount = ValueAnimator.INFINITE

        //위아래 둥둥효과
        val floatAnimator_Y =
            ObjectAnimator.ofFloat(binding.bridgeShiny1, "translationY", 0f, -50f, 0f, 50f, 0f)
        floatAnimator_Y.duration = 5000
        floatAnimator_Y.repeatMode = ValueAnimator.REVERSE
        floatAnimator_Y.repeatCount = ValueAnimator.INFINITE
        floatAnimator_Y.interpolator = LinearInterpolator()

        // 애니메이션 시작

        alphaAnimator.start()
        floatAnimator_Y.start()

        val floatAnimatorY =
            ObjectAnimator.ofFloat(binding.homeMemoryBridgeIvEmotion2, "translationY", 0f, -30f, 0f)
        floatAnimatorY.duration = 2000
        floatAnimatorY.repeatMode = ValueAnimator.REVERSE
        floatAnimatorY.repeatCount = ValueAnimator.INFINITE
        floatAnimatorY.interpolator = LinearInterpolator()

        val floatAnimatorX = ObjectAnimator.ofFloat(
            binding.homeMemoryBridgeIvEmotion2,
            "translationX",
            0f,
            -10f,
            10f,
            0f
        )
        floatAnimatorX.duration = 3000
        floatAnimatorX.repeatMode = ValueAnimator.REVERSE
        floatAnimatorX.repeatCount = ValueAnimator.INFINITE
        floatAnimatorX.interpolator = LinearInterpolator()


        //imator3 애니메이션
        val floatAnimatorY3 =
            ObjectAnimator.ofFloat(binding.homeMemoryBridgeIvEmotion3, "translationY", 0f, -30f, 0f)
        floatAnimatorY3.duration = 2000
        floatAnimatorY3.repeatMode = ValueAnimator.REVERSE
        floatAnimatorY3.repeatCount = ValueAnimator.INFINITE
        floatAnimatorY3.interpolator = LinearInterpolator()
        floatAnimatorY3.startDelay = 500 // 0.5초 늦게 시작

        val floatAnimatorX3 = ObjectAnimator.ofFloat(
            binding.homeMemoryBridgeIvEmotion3,
            "translationX",
            0f,
            -10f,
            10f,
            0f
        )
        floatAnimatorX3.duration = 3000
        floatAnimatorX3.repeatMode = ValueAnimator.REVERSE
        floatAnimatorX3.repeatCount = ValueAnimator.INFINITE
        floatAnimatorX3.interpolator = LinearInterpolator()
        floatAnimatorX3.startDelay = 1000

        floatAnimatorY.start()
        floatAnimatorX.start()

        floatAnimatorY3.start()
        floatAnimatorX3.start()

        // 메모리 컨테이너 애니메이션 (주석 처리됨)
//        binding.homeMemoryBridgeMemoryContainer.startAnimation(
//            AnimationUtils.loadAnimation(requireContext(), R.anim.concentrate_button)
//        )
    }

    private fun setMemoryFragment() {
//        val transaction = requireActivity().supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.home_layout, MemoryFragment())
//        transaction.addToBackStack(null)
//        transaction.commit()

        MemoryFragment().show(childFragmentManager, "MEMORY_FRAGMENT")
    }

//    private fun setLetterFragment() {
//        LetterFragment().show(childFragmentManager, "LETTER_FRAGMENT")
//    }

    override fun onResume() {
        super.onResume()
        initAnimation()
    }
}
