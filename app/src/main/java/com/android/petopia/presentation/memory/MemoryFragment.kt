package com.android.petopia.presentation.memory

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.android.petopia.R
import com.android.petopia.data.Memory
import com.android.petopia.data.MemoryModel
import com.android.petopia.data.MemoryReposirotyImpl
import com.android.petopia.data.UserModel
import com.android.petopia.data.remote.MemoryRepository
import com.android.petopia.data.remote.MemoryRepositoryImpl
import com.android.petopia.databinding.FragmentHomeBinding
import com.android.petopia.databinding.FragmentLetterBinding
import com.android.petopia.databinding.FragmentMemoryBinding
import com.android.petopia.presentation.MainActivity
import com.android.petopia.presentation.gallery.PhotoFragment
import com.android.petopia.presentation.home.HomeSharedViewModel
import com.android.petopia.presentation.memory.ViewModel.MemoryViewModel
import com.android.petopia.presentation.memory.adapter.ListRecyclerViewAdapter


class MemoryFragment : DialogFragment() {

    private var _binding: FragmentMemoryBinding? = null
    private val binding get() = _binding!!
//    private var memoryList = mutableListOf<Memory>()

    private lateinit var listRecyclerViewAdapter: ListRecyclerViewAdapter

    //private lateinit var homeSharedViewModel: HomeSharedViewModel

    private val memoryRepository: MemoryRepository by lazy {
        MemoryRepositoryImpl()
    }

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
        initDialog()
//        homeSharedViewModel =
//            ViewModelProvider(requireParentFragment()).get(HomeSharedViewModel::class.java)
        //가상데이터

        //1. MemoryViewModel -> Memory 로 변경하기
        memoryViewModel =
            ViewModelProvider(requireParentFragment()).get(MemoryViewModel::class.java)
        memoryViewModel.memoryListLiveData.observe(viewLifecycleOwner) {
            listRecyclerViewAdapter.submitList(it)
        }

        val user1 = UserModel("id1", "password1", "name1", "nickname1", "email1@gmail.com")

        val memoryList = listOf(
            Memory("title1", "content1", user1)
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
                Toast.makeText(requireContext(), "${item.title} 클릭", Toast.LENGTH_SHORT)
                    .show()


            }, itemLongClickListener = { item ->
                Toast.makeText(requireContext(), "${item.title} 롱클릭", Toast.LENGTH_SHORT)
                    .show()
                (activity as MainActivity).showDialog()
            })
        binding.rvMemoryList.adapter = listRecyclerViewAdapter
        binding.rvMemoryList.layoutManager = GridLayoutManager(requireContext(), 1)
    }

    private fun initDialog() {
        val windowManager =
            requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay

        val size = Point()
        display.getSize(size)
        size.x
        size.y
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.9).toInt()
        params?.height = (deviceWidth * 1.4).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }



}