package com.djhb.petopia.presentation.community.dialogFragment

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.djhb.petopia.Table
import com.djhb.petopia.data.CommentModel
import com.djhb.petopia.databinding.FragmentCommentAddBinding
import com.djhb.petopia.presentation.community.CommunityDetailFragment
import com.djhb.petopia.presentation.community.CommunityDetailViewModel
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [CommentEditDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommentEditDialogFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private val ARG_COMMENT = "comment"
    private val ARG_POST_TYPE = "postType"

    private lateinit var comment: CommentModel
    private lateinit var postType: String
    private lateinit var detailFragment: CommunityDetailFragment

    private val binding: FragmentCommentAddBinding by lazy {
        FragmentCommentAddBinding.inflate(layoutInflater)
    }

//    private val detailViewModel: CommunityDetailViewModel by activityViewModels()
    private val detailViewModel: CommunityDetailViewModel by lazy {
//        CommunityDetailViewModel(postType)
        detailFragment.detailViewModel
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            comment =
                if(SDK_INT >= TIRAMISU)
                    it.getParcelable(ARG_COMMENT, CommentModel::class.java)?: CommentModel()
                else
                    it.getParcelable(ARG_COMMENT)?:CommentModel()

            postType = it.getString(ARG_POST_TYPE)?: Table.NONE.tableName
            val fragments = parentFragmentManager.fragments
//
            for (fragmentIndex in fragments.size-1 downTo 0) {
                val fragment = fragments[fragmentIndex]
                if(fragment is CommunityDetailFragment) {
                    detailFragment = fragment
                    break
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListener()
        initDialog()
    }

    private fun initView(){
        binding.etEditComment.setText(comment.content)
    }

    private fun initListener(){

        binding.btnEditCommentComplete.setOnClickListener {

            val etComment = binding.etEditComment.text.toString()

            if(etComment.isNotBlank()) {
                lifecycleScope.launch {
                    detailViewModel.updateComment(comment.copy(content = etComment))
                }
            } else {
                Toast.makeText(requireActivity(), "댓글을 입력하세요.", Toast.LENGTH_SHORT).show()
            }
            removeDialog()
        }


        binding.btnEditCommentCancel.setOnClickListener {
            removeDialog()
        }

    }

    private fun removeDialog(){
        requireActivity().supportFragmentManager
            .beginTransaction()
            .remove(this)
            .commit()
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



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CommentAddFragment.
         */
        // TODO: Rename and change types and number of parameters

        @JvmStatic
        fun newInstance(comment: CommentModel) =
            CommentEditDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_COMMENT, comment)
                }
            }

        @JvmStatic
        fun newInstance(comment: CommentModel, postType: String) =
            CommentEditDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_COMMENT, comment)
                    putString(ARG_POST_TYPE, postType)
                }
            }
    }
}