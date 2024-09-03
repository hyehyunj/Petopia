package com.android.petopia.presentation.memory


import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.petopia.data.LoginData
import com.android.petopia.data.Memory
import com.android.petopia.data.UserModel
import com.android.petopia.data.remote.MemoryRepository
import com.android.petopia.data.remote.MemoryRepositoryImpl
import com.android.petopia.databinding.FragmentMemoryBinding
import com.android.petopia.presentation.MainActivity
import com.android.petopia.presentation.memory.ViewModel.MemoryViewModel
import com.android.petopia.presentation.memory.adapter.ListRecyclerViewAdapter


class MemoryFragment() : DialogFragment() {

    private var _binding: FragmentMemoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var listRecyclerViewAdapter: ListRecyclerViewAdapter

    //private lateinit var homeSharedViewModel: HomeSharedViewModel

    private lateinit var memoryViewModel: MemoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemoryBinding.inflate(inflater, container, false)
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

        //현재 로그인한 유저의 정보를 로드
        val currentUser = getCurrentUser()

        var currentTitle = binding.tvTodayMemoryContent.text.toString()
        memoryViewModel.setMemoryTitle(currentTitle)


        //메모리 리스트가 변경될때마다 관찰하여 리사이클러뷰에 업데이트
        memoryViewModel.memoryListLiveData.observe(viewLifecycleOwner) { memoryList ->
            listRecyclerViewAdapter.submitList(memoryList)
        }

        memoryViewModel.loadMemoryList(currentUser)



        binding.btnAnswer.setOnClickListener {
            setMemoryWriteFragment() // 메모리 작성 프래그먼트 이동
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
                Log.d("MemoryFragment", "롱클릭")
                showDeleteDialog(item)

                //(activity as MainActivity).showDialog() // 롱클릭시 삭제 다이얼로그 띄우기(삭제기능은 아직 구현X)
            })
        val manager = LinearLayoutManager(requireContext())

        binding.rvMemoryList.adapter = listRecyclerViewAdapter // 어댑터 연결
        binding.rvMemoryList.layoutManager = LinearLayoutManager(requireContext())
        manager.reverseLayout = true
        manager.stackFromEnd = true
    }


    //다이얼로그 크기 조절 함수
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 메모리 작성 프래그먼트 이동함수
    private fun setMemoryWriteFragment() {
        MemoryWriteFragment().show(childFragmentManager, "MEMORY_WRITE_FRAGMENT")
    }

    // 메모리작성프래그먼트에서 작성한 내용을 메모리리스트에 저장 함수
    fun onMemorySaved(memory: Memory) {
        Log.d("MemoryFragment", "메모리 저장: $memory")
        memoryViewModel.addMemoryList(memory)

    }

    fun getCurrentUser(): UserModel {
        return LoginData.loginUser
    }

    private fun showDeleteDialog(memory: Memory) {
        val deleteDialog = MemoryDeleteDialog(memory) { deletedMemory ->
            memoryViewModel.deleteMemoryList(deletedMemory)
            memoryViewModel.memoryListLiveData.value.let { updateList ->
                listRecyclerViewAdapter.submitList(updateList)
            }
        }
        deleteDialog.show(childFragmentManager, "DELETE_DIALOG")

        memoryViewModel.memoryListLiveData.value.let { updateList ->
            listRecyclerViewAdapter.submitList(updateList) // 넘버링 실시간 반영
        }

    }


}