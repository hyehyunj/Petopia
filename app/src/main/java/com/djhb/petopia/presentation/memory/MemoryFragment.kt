package com.djhb.petopia.presentation.memory


import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.Memory
import com.djhb.petopia.data.UserModel
import com.djhb.petopia.data.remote.MemoryRepositoryImpl
import com.djhb.petopia.databinding.FragmentMemoryBinding


class MemoryFragment() : DialogFragment() {

    private lateinit var listRecyclerViewAdapter: ListRecyclerViewAdapter
    private val _binding: FragmentMemoryBinding by lazy {
        FragmentMemoryBinding.inflate(layoutInflater)
    }

    private val binding get() = _binding

    //private lateinit var homeSharedViewModel: HomeSharedViewModel

    private lateinit var memoryViewModel: MemoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDialog()
//        homeSharedViewModel =
//            ViewModelProvider(requireParentFragment()).get(HomeSharedViewModel::class.java)

        //메모리 작성되면 오늘의 메모리 작성칸 사라짐


        val memoryRepository = MemoryRepositoryImpl()
        val factory = MemoryViewModel.MemoryViewModelFactory(memoryRepository)

        memoryViewModel =
            ViewModelProvider(requireActivity(), factory).get(MemoryViewModel::class.java)

        memoryViewModel.memoryListLiveData.observe(viewLifecycleOwner) { memoryList ->
            listRecyclerViewAdapter.submitList(memoryList)
        }

        //메모리 최초 작성시 버튼 사라지게 함
//        if (memoryViewModel.isMemorySaved.value == true) {
//            binding.btnAnswer.visibility = View.GONE
//        }

        //현재 로그인한 유저의 정보를 로드
        val currentUser = getCurrentUser()
        memoryViewModel.loadMemoryList(currentUser)

        memoryViewModel.memoryTitle.observe(viewLifecycleOwner) { text ->
            binding.tvTodayMemoryContent.text = text
        }
        var currentTitle = memoryViewModel.memoryTitle.value
        memoryViewModel.setMemoryTitle(currentTitle.toString())


        if (memoryViewModel.isMemorySaved.value == true) {
            binding.memoryTodayMemoryLayout.visibility = View.GONE
        }


        binding.memoryTodayMemoryLayout.setOnClickListener {
            setMemoryWriteFragment() // 메모리 작성 프래그먼트 이동
            if (listRecyclerViewAdapter.isDeleteMode) {
                listRecyclerViewAdapter.toggleDeleteMode()
                binding.btnImageDeleteCancel.visibility = View.GONE
                binding.btnMemoryDelete.visibility = View.VISIBLE
                binding.btnMemoryDelete2.visibility = View.GONE
                listRecyclerViewAdapter.clearSelections()
                //삭제모드가 켜있을때 작성하기를 누르면 삭제모드가 꺼짐
            }
        }

        binding.btnMemoryExit.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .remove(this).commit()
        }

        binding.btnMemoryDelete.setOnClickListener {
            listRecyclerViewAdapter.toggleDeleteMode()
            binding.btnImageDeleteCancel.visibility = View.VISIBLE
            binding.btnMemoryDelete.visibility = View.GONE
            binding.btnMemoryDelete2.visibility = View.VISIBLE
        }

        binding.btnMemoryDelete2.setOnClickListener {
            if (listRecyclerViewAdapter.isDeleteMode) {
                val selectedItems = listRecyclerViewAdapter.getSelectedItems()
                selectedItems.forEach { item ->
                    memoryViewModel.deleteMemoryList(item)
                }
                listRecyclerViewAdapter.toggleDeleteMode()

                binding.btnImageDeleteCancel.visibility = View.GONE
                binding.btnMemoryDelete.visibility = View.VISIBLE
                binding.btnMemoryDelete2.visibility = View.GONE

                listRecyclerViewAdapter.clearSelections()
            }
            memoryViewModel.memoryListLiveData.value.let { updateList ->
                listRecyclerViewAdapter.submitList(updateList)
            }
        }


        binding.btnImageDeleteCancel.setOnClickListener {
            if (listRecyclerViewAdapter.isDeleteMode) {
                listRecyclerViewAdapter.toggleDeleteMode()
            }
            Log.d("MemoryFragment", "${listRecyclerViewAdapter.isCleared}")
            if (!listRecyclerViewAdapter.isCleared) {
                listRecyclerViewAdapter.clearSelections()
            }

            binding.btnImageDeleteCancel.visibility = View.GONE
            binding.btnMemoryDelete.visibility = View.VISIBLE
            binding.btnMemoryDelete2.visibility = View.GONE
        }

        //리사이클러뷰 강제로 업데이트
        memoryViewModel.loadMemoryList(getCurrentUser())
        listRecyclerViewAdapter.notifyDataSetChanged()

        // 기기의 뒤로가기 버튼
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.beginTransaction()
                        .remove(this@MemoryFragment).commit()
                }
            })
    }

    private fun initAdapter() {
        listRecyclerViewAdapter = ListRecyclerViewAdapter(
            itemClickListener = { item ->
                memoryViewModel.setSelectedMemory(item)
                showDetailFragment()
                Log.d("데이터", item.key)
            }, itemLongClickListener = { item ->
                Log.d("MemoryFragment", "롱클릭")
                showDeleteDialog(item)

                //(activity as MainActivity).showDialog() // 롱클릭시 삭제 다이얼로그 띄우기(삭제기능은 아직 구현X)
            })
        val manager = LinearLayoutManager(requireContext())

        binding.rvMemoryList.adapter = listRecyclerViewAdapter // 어댑터 연결
        binding.rvMemoryList.layoutManager = LinearLayoutManager(requireContext())

        listRecyclerViewAdapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                if (!listRecyclerViewAdapter.isDeleteMode) {
                    binding.rvMemoryList.smoothScrollToPosition(0)
                }
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                if (!listRecyclerViewAdapter.isDeleteMode) {
                    binding.rvMemoryList.smoothScrollToPosition(0)
                }
            }
        })


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
        params?.height = (deviceWidth * 1.8).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    // 메모리 작성 프래그먼트 이동함수
    private fun setMemoryWriteFragment() {
        MemoryWriteFragment().show(childFragmentManager, "MEMORY_WRITE_FRAGMENT")
    }

    // 메모리작성프래그먼트에서 작성한 내용을 메모리리스트에 저장 함수
    fun onMemorySaved(memory: Memory) {
        Log.d("MemoryFragment", "메모리 저장: $memory")
        memoryViewModel.addMemoryList(memory)

        memoryViewModel.memoryListLiveData.value.let { updateList ->
            listRecyclerViewAdapter.submitList(updateList)
        }
        binding.memoryTodayMemoryLayout.visibility = View.GONE

    }

    // 수정된 메모리를 업데이트하는 함수
    fun onMemoryUpdated(updatedMemory: Memory) {
        memoryViewModel.updateMemoryList(updatedMemory)
        memoryViewModel.loadMemoryList(getCurrentUser())

    }

    fun getCurrentUser(): UserModel {
        return LoginData.loginUser
    }

    private fun showDetailFragment() {
        MemoryDetailFragment().show(childFragmentManager, "DETAIL_DIALOG")
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