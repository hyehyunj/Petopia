package com.android.petopia.presentation.memory


import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.android.petopia.data.Memory
import com.android.petopia.data.UserModel
import com.android.petopia.data.remote.MemoryRepository
import com.android.petopia.data.remote.MemoryRepositoryImpl
import com.android.petopia.databinding.FragmentMemoryBinding
import com.android.petopia.presentation.MainActivity
import com.android.petopia.presentation.memory.ViewModel.MemoryViewModel
import com.android.petopia.presentation.memory.adapter.ListRecyclerViewAdapter


class MemoryFragment : DialogFragment() {

    private var _binding: FragmentMemoryBinding? = null
    private val binding get() = _binding!!
//    private var memoryList = mutableListOf<Memory>()

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
        initDialog()
//        homeSharedViewModel =
//            ViewModelProvider(requireParentFragment()).get(HomeSharedViewModel::class.java)

        val memoryRepository = MemoryRepositoryImpl()
        val factory = MemoryViewModel.MemoryViewModelFactory(memoryRepository)

        memoryViewModel =
            ViewModelProvider(requireActivity(), factory).get(MemoryViewModel::class.java)

        memoryViewModel.memoryListLiveData.observe(viewLifecycleOwner) { memoryList ->
            listRecyclerViewAdapter.submitList(memoryList)
        }

        binding.btnAnswer.setOnClickListener {

            setMemoryWriteFragment() // 메모리 작성 프래그먼트 이동

//            val user1 = UserModel("id1", "password1", "name1", "nickname1", "email1@gmail.com")
//            //유저는 로그인한 유저의 정보를 로그인 할때 받아와야함
//
//            val memoryList = listOf(
//                Memory("title1", "content1", user1)
//            )
//            memoryList.forEach {
//                memoryViewModel.addMemoryList(it)
//            }
//
//            listRecyclerViewAdapter.submitList(memoryList)

        }


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


    private fun setMemoryWriteFragment() {
        MemoryWriteFragment().show(childFragmentManager, "MEMORY_WRITE_FRAGMENT")
    }

}