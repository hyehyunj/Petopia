package com.djhb.petopia.presentation.letter

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
import androidx.activity.OnBackPressedCallback
import androidx.databinding.adapters.AbsListViewBindingAdapter.OnScroll
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.djhb.petopia.data.LetterModel
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.UserModel
import com.djhb.petopia.data.remote.LetterRepositoryImpl
import com.djhb.petopia.databinding.FragmentLetterBinding


class LetterFragment : DialogFragment() {

    private val _binding: FragmentLetterBinding by lazy {
        FragmentLetterBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding

//    private lateinit var homeSharedViewModel: HomeSharedViewModel

    private lateinit var letterViewModel: LetterViewModel
    private lateinit var letterListRecyclerViewAdapter: LetterListRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        homeSharedViewModel =
//            ViewModelProvider(requireParentFragment()).get(HomeSharedViewModel::class.java)


        initDialog()


        val letterRepository = LetterRepositoryImpl() // 추후 연결
        val factory = LetterViewModel.LetterViewModelFactory(letterRepository)

        letterViewModel =
            ViewModelProvider(requireActivity(), factory).get(LetterViewModel::class.java)


        //메모리 리스트가 변경될때마다 관찰하여 리사이클러뷰에 업데이트
        letterViewModel.letterListLiveData.observe(viewLifecycleOwner) { letterList ->
            letterListRecyclerViewAdapter.submitList(letterList)
        }

        val curentUser = getCurrentUser()
        letterViewModel.loadInitLetterList(curentUser)



        binding.letterIvAdd.setOnClickListener {
            showLetterWriteFragment()
            if (letterListRecyclerViewAdapter.isDeleteMode) {
                letterListRecyclerViewAdapter.toggleDeleteMode()
                binding.btnImageDeleteCancel.visibility = View.GONE
                binding.btnLetterDelete.visibility = View.VISIBLE
                binding.btnLetterDelete2.visibility = View.GONE
                letterListRecyclerViewAdapter.clearSelections()
            }

        }

        binding.btnLetterExit.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .remove(this).commit()
        }

        binding.btnLetterDelete.setOnClickListener {
            letterListRecyclerViewAdapter.toggleDeleteMode()
            binding.btnImageDeleteCancel.visibility = View.VISIBLE
            binding.btnLetterDelete.visibility = View.GONE
            binding.btnLetterDelete2.visibility = View.VISIBLE
        }

        binding.btnLetterDelete2.setOnClickListener {
            if (letterListRecyclerViewAdapter.isDeleteMode) {
                val selectedItems = letterListRecyclerViewAdapter.getSelectedItems()
                Log.d("LetterFragment", "selectedItems : ${selectedItems}")
                selectedItems.forEach { item ->
                    letterViewModel.deleteLetterList(item)
                    Log.d("LetterFragment", "deletedLetter : ${item}")
                }

                Log.d("LetterFragment", "letterList : ${letterViewModel.letterListLiveData.value}")

                letterListRecyclerViewAdapter.toggleDeleteMode()

                binding.btnImageDeleteCancel.visibility = View.GONE
                binding.btnLetterDelete.visibility = View.VISIBLE
                binding.btnLetterDelete2.visibility = View.GONE
                letterListRecyclerViewAdapter.clearSelections()
            }
            letterViewModel.letterListLiveData.observe(viewLifecycleOwner) { letterList ->
                letterListRecyclerViewAdapter.submitList(letterList)
            }
        }

        binding.btnImageDeleteCancel.setOnClickListener {
            if (letterListRecyclerViewAdapter.isDeleteMode) {
                letterListRecyclerViewAdapter.toggleDeleteMode()
            }

            if (!letterListRecyclerViewAdapter.isCleared) {
                letterListRecyclerViewAdapter.clearSelections()
            }
            binding.btnImageDeleteCancel.visibility = View.GONE
            binding.btnLetterDelete.visibility = View.VISIBLE
            binding.btnLetterDelete2.visibility = View.GONE
        }

        letterViewModel.loadInitLetterList(getCurrentUser())
        letterListRecyclerViewAdapter.notifyDataSetChanged()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.beginTransaction()
                        .remove(this@LetterFragment).commit()
                }
            })

        binding.rvLetterList.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(1)) {
                    Log.i("LetterFragment", "end scroll")
                    letterViewModel.loadLetterList(curentUser)
                }

            }
        })

    }


    private fun initAdapter() {
        letterListRecyclerViewAdapter = LetterListRecyclerViewAdapter(
            itemClickListener = { item ->
                letterViewModel.setSelectedLetter(item)
                showLetterDetailFragment()

            }, itemLongClickListener = { item ->
                showDeleteDialog(item)
            })

        val manager = LinearLayoutManager(requireContext())
        binding.rvLetterList.adapter = letterListRecyclerViewAdapter // 어댑터 연결
        binding.rvLetterList.layoutManager = LinearLayoutManager(requireContext())

        letterListRecyclerViewAdapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                binding.rvLetterList.smoothScrollToPosition(0)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.rvLetterList.smoothScrollToPosition(0)
            }
        })

        manager.reverseLayout = true
        manager.stackFromEnd = true
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
        params?.height = (deviceWidth * 1.8).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun onLetterSaved(letterModel: LetterModel) {
        Log.d("LetterFragment", "편지 저장: $letterModel")
        letterViewModel.addLetterList(letterModel)

        letterViewModel.letterListLiveData.value.let { updateList ->
            letterListRecyclerViewAdapter.submitList(updateList)
        }


    }

    fun getCurrentUser(): UserModel {
        return LoginData.loginUser
    }

    fun onLetterUpdated(updatedLetter: LetterModel) {
        letterViewModel.updateLetterList(updatedLetter)
        letterViewModel.loadInitLetterList(getCurrentUser())
    }

    private fun showLetterDetailFragment() {

        LetterDetailFragment().show(childFragmentManager, "DETAIL_DIALOG")

    }

    private fun showDeleteDialog(letterModel: LetterModel) {
        val deleteDialog = LetterDeleteDialog(letterModel) { deletedLetter ->
            letterViewModel.deleteLetterList(deletedLetter)

            letterViewModel.letterListLiveData.value.let { updateList ->
                letterListRecyclerViewAdapter.submitList(updateList)
            }
        }
        deleteDialog.show(childFragmentManager, "DELETE_DIALOG")

        letterViewModel.letterListLiveData.value.let { updateList ->
            letterListRecyclerViewAdapter.submitList(updateList)
        }

    }

    private fun showLetterWriteFragment() {
        LetterWriteFragment().show(childFragmentManager, "LETTER_WRITE_FRAGMENT")
    }

}

