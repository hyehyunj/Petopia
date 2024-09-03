package com.android.petopia.presentation.letter

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
import com.android.petopia.data.LetterModel
import com.android.petopia.data.LoginData
import com.android.petopia.data.UserModel
import com.android.petopia.data.remote.LetterRepositoryImpl
import com.android.petopia.databinding.FragmentLetterBinding
import com.android.petopia.presentation.MainActivity
import com.android.petopia.presentation.home.HomeSharedViewModel
import com.android.petopia.presentation.memory.ViewModel.MemoryViewModel
import com.android.petopia.presentation.memory.adapter.ListRecyclerViewAdapter


class LetterFragment : DialogFragment() {
    private var _binding: FragmentLetterBinding? = null
    private val binding get() = _binding!!
//    private lateinit var homeSharedViewModel: HomeSharedViewModel

    private lateinit var letterViewModel: LetterViewModel
    private lateinit var letterListRecyclerViewAdapter: LetterListRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLetterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        homeSharedViewModel =
//            ViewModelProvider(requireParentFragment()).get(HomeSharedViewModel::class.java)

        initAdapter()
        initDialog()


        val letterRepository = LetterRepositoryImpl() // 추후 연결
        val factory = LetterViewModel.LetterViewModelFactory(letterRepository)

        letterViewModel =
            ViewModelProvider(requireActivity(), factory).get(LetterViewModel::class.java)


        val curentUser = getCurrentUser()
        //메모리 리스트가 변경될때마다 관찰하여 리사이클러뷰에 업데이트
        letterViewModel.letterListLiveData.observe(viewLifecycleOwner) { letterList ->
            letterListRecyclerViewAdapter.submitList(letterList)
        }

        letterViewModel.loadLetterList(curentUser)

        binding.letterIvAdd.setOnClickListener {
            showLetterWritingPadFragment()
        }

        binding.btnLetterExit.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .remove(this).commit()
        }

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.beginTransaction()
                    .remove(this@LetterFragment).commit()
            }
        })

    }


    private fun initAdapter() {
        letterListRecyclerViewAdapter = LetterListRecyclerViewAdapter(
            itemClickListener = { item ->
                Toast.makeText(requireContext(), "${item.title} 클릭", Toast.LENGTH_SHORT)
                    .show()

            }, itemLongClickListener = { item ->
                Toast.makeText(requireContext(), "${item.title} 롱클릭", Toast.LENGTH_SHORT)
                    .show()
                (activity as MainActivity).showDialog() // 롱클릭시 삭제 다이얼로그 띄우기(삭제기능은 아직 구현X)
            })
        binding.rvLetterList.adapter = letterListRecyclerViewAdapter // 어댑터 연결
        binding.rvLetterList.layoutManager = LinearLayoutManager(requireContext())
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showLetterWritingPadFragment() {
        LetterWritingPadFragment().show(childFragmentManager, "LETTER_ADD_FRAGMENT")
    }

    fun onLetterSaved(letterModel: LetterModel) {
        Log.d("LetterFragment", "편지 저장: $letterModel")
        letterViewModel.addLetterList(letterModel)

    }

    fun getCurrentUser(): UserModel {
        return LoginData.loginUser
    }

    private fun showDeleteDialog(letterModel: LetterModel) {
        val deleteDialog = LetterDeleteDialog(letterModel) { deletedLetter ->
            letterViewModel.deleteLetterList(deletedLetter)
            letterViewModel.letterListLiveData.value.let { updateList ->
                letterListRecyclerViewAdapter.submitList(updateList)
            }
        }
        deleteDialog.show(childFragmentManager, "DELETE_DIALOG")

    }

}

