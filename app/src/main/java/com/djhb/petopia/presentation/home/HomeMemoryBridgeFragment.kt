package com.djhb.petopia.presentation.home

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.app.NotificationCompat
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
        Log.d("loadMemory", "memoryText: ${binding.homeMemoryBridgeTvMemoryTitle.text}")
        scheduledMemory()


        memoryViewModel.memoryTitle.observe(viewLifecycleOwner) { text ->
            Log.d("memoryText", text)
            if (memoryViewModel.isMemorySaved.value == false) {
                binding.homeMemoryBridgeTvMemoryTitle.text = text
            }
        }

        val currentTitle = binding.homeMemoryBridgeTvMemoryTitle.text.toString()
        memoryViewModel.setMemoryTitle(currentTitle)

        //binding.homeMemoryBridgeTvMemoryTitle.setText("오늘의 메모리")
//        binding.homeMemoryBridgeTvMemoryBtn.setText("작성하기")

        homeMemoryBridgeButtonClickListener()
        homeMemoryBridgeDataObserver()

        // 미구현 버튼
        binding.homeMemoryBridgeIvEmotion.setOnClickListener {
            showUndoToast()
        }

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
                    binding.homeMemoryBridgeTvMemoryTitle.setText("메모리북 기록 완료")
                }
            }
        }


        //편지버튼 클릭이벤트 : 클릭시 편지 이동e
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
            PeriodicWorkRequestBuilder<UpdateMemoryTextWorker>(
                1,
                TimeUnit.DAYS
            ).build()

        WorkManager.getInstance(requireContext()).enqueue(workRequest)

//        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<UpdateMemoryTextWorker>().build()
//        WorkManager.getInstance(requireContext()).enqueue(oneTimeWorkRequest)

        //처음에 데이터가 없을때 한번만 실행
//        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
//            .observe(viewLifecycleOwner) { workInfo ->
//                if (workInfo.state.isFinished) {
//                    scheduledMemory()
//                    loadMemory()
//                }
//            }
    }


    private fun loadMemory() {
        val sharedPreferences =
            requireContext().getSharedPreferences("Memory", Context.MODE_PRIVATE)
        val memoryText = sharedPreferences.getString("memoryText", null)

        Log.d("loadMemory", "memoryText: $memoryText")

        binding.homeMemoryBridgeTvMemoryTitle.text = memoryText

        if (memoryViewModel.isMemorySaved.value != true) {
            binding.homeMemoryBridgeTvMemoryTitle.text = memoryText
            memoryViewModel.setMemoryTitle(memoryText.toString())
        }


        Log.d("memoryText", memoryText.toString())
    }

    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun homeMemoryBridgeDataObserver() {
        //가이드 상태 변화감지 : 가이드 상태에 따라 화면구성 변경

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
        )
            .show()
    }


    private fun initAnimation() {
        binding.homeMemoryBridgeIvArrowUnder.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), R.anim.move_under)
        )
        binding.homeMemoryBridgeIvEmotion.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), R.anim.ballon)
        )
        binding.homeMemoryBridgeMemoryEffectShine1.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), R.anim.shine1)
        )
        binding.homeMemoryBridgeMemoryEffectShine2.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), R.anim.shine2)
        )
        binding.homeMemoryBridgeMemoryEffectShine3.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), R.anim.shine3)
        )

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

    fun showUndoToast() {
        StyleableToast.makeText(
            requireActivity(),
            getString(R.string.messege_undo),
            R.style.toast_undo
        ).show()
    }

//    private fun setLetterFragment() {
//        LetterFragment().show(childFragmentManager, "LETTER_FRAGMENT")
//    }

    override fun onResume() {
        super.onResume()
        initAnimation()

    }

}