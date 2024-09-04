package com.android.petopia.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.android.petopia.R
import com.android.petopia.data.remote.MemoryRepositoryImpl
import com.android.petopia.databinding.FragmentHomeMemoryBridgeBinding
import com.android.petopia.presentation.MainActivity
import com.android.petopia.presentation.guide.GuideFragment
import com.android.petopia.presentation.memory.MemoryFragment

import io.github.muddz.styleabletoast.StyleableToast

import com.android.petopia.presentation.memory.ViewModel.MemoryViewModel


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
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainHomeGuideViewModel =
            ViewModelProvider(requireActivity()).get(MainHomeGuideSharedViewModel::class.java)

        homeMemoryBridgeButtonClickListener()
        homeMemoryBridgeDataObserver()

    }

    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun homeMemoryBridgeButtonClickListener() {

        val memoryRepository = MemoryRepositoryImpl()
        val factory = MemoryViewModel.MemoryViewModelFactory(memoryRepository)

        memoryViewModel =
            ViewModelProvider(requireActivity(), factory).get(MemoryViewModel::class.java)

        //메모리버튼 클릭이벤트 : 클릭시 메모리북 이동
        binding.homeMemoryBridgeTvMemoryBtn.setOnClickListener {
if (mainHomeGuideViewModel.guideStateLiveData.value == "OPTIONAL")
                toastMoveUnder() else setMemoryFragment()


            Log.d("memorybuttonclick", "메모리버튼 클릭")

            // 메모리 작성 완료시 투데이 메모리문구, 버튼 변경
            if (memoryViewModel.isMemorySaved.value == true) {
                binding.homeMemoryBridgeTvMemoryTitle.setText("메모리북 기록 완료")
                binding.homeMemoryBridgeTvMemoryBtn.setText("전체보기")
            }


        }


        //편지버튼 클릭이벤트 : 클릭시 편지 이동e
//        binding.homeMemoryBridgeBtnLetter.setOnClickListener {
//            setLetterFragment()
//        }


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

                "MEMORY" -> binding.homeMemoryBridgeIvArrowUnder.isVisible = false

                }
            }


        }



    private fun toastMoveUnder() {
        if (mainHomeGuideViewModel.guideFunctionLiveData.value == "MOVE_EARTH")
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


}