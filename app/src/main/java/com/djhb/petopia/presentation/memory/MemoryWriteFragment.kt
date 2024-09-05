package com.djhb.petopia.presentation.memory

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
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.Memory
import com.djhb.petopia.data.remote.MemoryRepository
import com.djhb.petopia.data.remote.MemoryRepositoryImpl
import com.djhb.petopia.databinding.FragmentMemoryWriteBinding
import com.djhb.petopia.presentation.memory.ViewModel.MemoryViewModel
import java.text.SimpleDateFormat
import java.util.Locale


class MemoryWriteFragment(
    private val isEditMode: Boolean = false,
    private val onMemorySaved: ((Memory) -> Unit)? = null
) : DialogFragment() {

    private var _binding: FragmentMemoryWriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var memoryViewModel: MemoryViewModel
    private var memoryToEdit: Memory? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemoryWriteBinding.inflate(inflater, container, false)

        memoryViewModel = ViewModelProvider(requireActivity()).get(MemoryViewModel::class.java)
        memoryViewModel.memoryTitle.observe(viewLifecycleOwner) { title ->
            binding.tvMemoryWriteQuestion.text = title
        }

        Log.d("MemoryWriteFragment", "$isEditMode")
        //수정하기를 통해 왔을경우
        if (isEditMode) {

            memoryViewModel.selectedMemory.observe(viewLifecycleOwner) { selectedMemory ->
                memoryToEdit = selectedMemory
                binding.tvMemoryWriteQuestion.text = selectedMemory.title
                binding.etMemoryWriteContent.setText(selectedMemory.content)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
                val date = dateFormat.format(selectedMemory.createdDate)
                binding.tvMemoryWriteDate.text = date
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDialog()

        val currentTime: Long = System.currentTimeMillis()

        if (!isEditMode) {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
            binding.tvMemoryWriteDate.text = simpleDateFormat.format(currentTime)
        }

        binding.btnMemoryWriteCheck.setOnClickListener {

            if (isEditMode) {
                updateData()
            } else {
                saveData()
            }
        }

        binding.btnMemoryWriteExit.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .remove(this).commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun saveData() {
        val title = binding.tvMemoryWriteQuestion.text.toString()
        val content = binding.etMemoryWriteContent.text.toString()

        Log.d("MemoryWriteFragment", "제목: $title, 내용: $content")
        // 여기서 작성한 데이터를 받아서 memoryFragment의 리사이클러뷰에 전달해야함
        if (title.isNotEmpty() && content.isNotEmpty()) {
            val memory = Memory(title, content, LoginData.loginUser)
            memoryViewModel.addMemoryList(memory)
            memoryViewModel.setMemorySaved(true)

            (parentFragment as? MemoryFragment)?.onMemorySaved(memory)
            parentFragmentManager.beginTransaction().remove(this).commit()
        } else {
            Log.d("MemoryWriteFragment", "제목 또는 내용이 비어있습니다.")
        }
    }

    private fun updateData() {
        val title = binding.tvMemoryWriteQuestion.text.toString()
        val content = binding.etMemoryWriteContent.text.toString()

        memoryToEdit?.let {
            it.title = title
            it.content = content
            memoryViewModel.updateMemoryList(it)
            memoryViewModel.setMemorySaved(true)
            onMemorySaved?.invoke(it)
            (parentFragment as? MemoryFragment)?.onMemorySaved(it)
            parentFragmentManager.beginTransaction().remove(this).commit()
        }
    }

}