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
import com.djhb.petopia.data.Memory
import com.djhb.petopia.data.remote.MemoryRepositoryImpl
import com.djhb.petopia.databinding.FragmentMemoryDetailBinding
import com.djhb.petopia.presentation.memory.ViewModel.MemoryViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class MemoryDetailFragment : DialogFragment() {

    private var _binding: FragmentMemoryDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var memoryViewModel: MemoryViewModel
    private var memoryToEdit: Memory? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDialog()

        val memoryRepository = MemoryRepositoryImpl()
        val factory = MemoryViewModel.MemoryViewModelFactory(memoryRepository)

        memoryViewModel =
            ViewModelProvider(requireActivity(), factory).get(MemoryViewModel::class.java)

        memoryViewModel.selectedMemory.observe(viewLifecycleOwner) { selectedMemory ->
            memoryToEdit = selectedMemory
            binding.tvMemoryDetailQuestion.text = selectedMemory.title
            binding.etMemoryDetailContent.text = selectedMemory.content

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
            val date = dateFormat.format(selectedMemory.createdDate)
            binding.tvMemoryDetailDate.text = date
        }

        binding.btnMemoryDetailModify.setOnClickListener {
            showMemoryModifyFragment()
        }

        binding.btnMemoryDetailExit.setOnClickListener {
            memoryToEdit?.let { memory ->
                updateMemoryList(memory)
                memoryViewModel.updateMemoryList(memory)
            }
            dismiss()
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
        params?.height = (deviceWidth * 1.4).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun showMemoryModifyFragment() {
        MemoryWriteFragment(isEditMode = true) { updatedMemory ->
            updateMemoryList(updatedMemory)

        }.show(childFragmentManager, "MEMORY_MODIFY_FRAGMENT")
    }

    private fun updateMemoryList(memory: Memory) {
        memoryToEdit = memory
        binding.tvMemoryDetailQuestion.text = memory.title
        binding.etMemoryDetailContent.text = memory.content
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        val date = dateFormat.format(memory.createdDate)
        binding.tvMemoryDetailDate.text = date

        (parentFragment as? MemoryFragment)?.onMemoryUpdated(memory)
    }

}