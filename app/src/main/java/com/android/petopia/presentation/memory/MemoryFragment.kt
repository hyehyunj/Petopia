package com.android.petopia.presentation.memory

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.android.petopia.R
import com.android.petopia.data.MemoryModel
import com.android.petopia.data.MemoryReposirotyImpl
import com.android.petopia.databinding.FragmentHomeBinding
import com.android.petopia.databinding.FragmentLetterBinding
import com.android.petopia.databinding.FragmentMemoryBinding
import com.android.petopia.presentation.home.HomeSharedViewModel
import com.android.petopia.presentation.memory.ViewModel.MemoryViewModel
import com.android.petopia.presentation.memory.adapter.ListRecyclerViewAdapter


class MemoryFragment : Fragment() {

    private var _binding: FragmentMemoryBinding? = null
    private val binding get() = _binding!!
    private var memoryList = mutableListOf<MemoryModel>()

    private lateinit var listRecyclerViewAdapter: ListRecyclerViewAdapter

    //private lateinit var homeSharedViewModel: HomeSharedViewModel
    private lateinit var memoryViewModel: MemoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemoryBinding.inflate(inflater, container, false)

//        val memoryRepository =
//            MemoryReposirotyImpl(memoryViewModel.memoryListLiveData.value ?: listOf())
//        val factory = MemoryViewModel.MemoryViewModelFactory(memoryRepository)
//        memoryViewModel = ViewModelProvider(this, factory).get(MemoryViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
//        homeSharedViewModel =
//            ViewModelProvider(requireParentFragment()).get(HomeSharedViewModel::class.java)
        //가상데이터

        memoryViewModel = ViewModelProvider(this).get(MemoryViewModel::class.java)
        memoryViewModel.memoryListLiveData.observe(viewLifecycleOwner) {
            listRecyclerViewAdapter.submitList(it)
        }

        val memoryList = listOf(
            MemoryModel("title1", "date1", "content1"),
            MemoryModel("title2", "date2", "content2"),
            MemoryModel("title3", "date3", "content3"),
            MemoryModel("title4", "date4", "content4"),
            MemoryModel("title5", "date5", "content5"),
            MemoryModel("title6", "date6", "content6"),
            MemoryModel("title7", "date7", "content7"),
            MemoryModel("title8", "date8", "content8"),
            MemoryModel("title9", "date9", "content9"),
            MemoryModel("title10", "date10", "content10")
        )
        memoryList.forEach {
            memoryViewModel.addMemoryList(it)
        }

        listRecyclerViewAdapter.submitList(memoryList)

        binding.btnMemoryExit.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .remove(this).commit()
        }

        // 기기의 뒤로가기 버튼
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.beginTransaction()
                    .remove(this@MemoryFragment).commit()
            }
        })

    }

    private fun initAdapter() {
        listRecyclerViewAdapter = ListRecyclerViewAdapter(
            itemClickListener = { item ->
                Toast.makeText(requireContext(), "${item.memoryTitle} 클릭", Toast.LENGTH_SHORT)
                    .show()

            }, itemLongClickListener = { item ->
                Toast.makeText(requireContext(), "${item.memoryTitle} 롱클릭", Toast.LENGTH_SHORT)
                    .show()
            })
        binding.rvMemoryList.adapter = listRecyclerViewAdapter
        binding.rvMemoryList.layoutManager = GridLayoutManager(requireContext(), 1)
    }

}