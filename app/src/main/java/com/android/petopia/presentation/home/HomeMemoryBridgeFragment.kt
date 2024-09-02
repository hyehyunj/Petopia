package com.android.petopia.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.petopia.R
import com.android.petopia.databinding.FragmentHomeMemoryBridgeBinding
import com.android.petopia.presentation.guide.GuideFragment
import com.android.petopia.presentation.memory.MemoryFragment

class HomeMemoryBridgeFragment : Fragment() {

    private val _binding: FragmentHomeMemoryBridgeBinding by lazy {
        FragmentHomeMemoryBridgeBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private val mainHomeGuideViewModel by viewModels<MainHomeGuideSharedViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




    }
    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun homeMemoryBridgeButtonClickListener() {

        //메모리버튼 클릭이벤트 : 클릭시 메모리북 이동
        binding.homeMemoryBridgeTvMemoryBtn.setOnClickListener {
            setMemoryFragment()
        }
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
                "MOVE_UNDER" -> binding.homeMemoryBridgeIvArrowUnder.isVisible = true
                }
            }
        }






    private fun setMemoryFragment() {
//        val transaction = requireActivity().supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.home_layout, MemoryFragment())
//        transaction.addToBackStack(null)
//        transaction.commit()

        MemoryFragment().show(childFragmentManager, "MEMORY_FRAGMENT")


    }



    private fun showGuideFragment() {
        childFragmentManager.beginTransaction()
            .replace(
                R.id.home_memory_bridge_container, GuideFragment()
            )
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()

    }

    override fun onResume() {
        super.onResume()
        showGuideFragment()
        homeMemoryBridgeButtonClickListener()
        homeMemoryBridgeDataObserver()

    }

}