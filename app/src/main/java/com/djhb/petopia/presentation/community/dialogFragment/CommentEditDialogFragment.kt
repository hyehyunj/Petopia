package com.djhb.petopia.presentation.community.dialogFragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.djhb.petopia.data.CommentModel
import com.djhb.petopia.databinding.FragmentCommentAddBinding
import com.djhb.petopia.presentation.community.CommunityDetailViewModel
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [CommentEditDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommentEditDialogFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private val ARG_PARAM1 = "comment"

    private lateinit var comment: CommentModel

    private val binding: FragmentCommentAddBinding by lazy {
        FragmentCommentAddBinding.inflate(layoutInflater)
    }

    private val detailViewModel: CommunityDetailViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            comment = it.getParcelable(ARG_PARAM1, CommentModel::class.java)?: CommentModel()
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

    override fun onDetach() {
        super.onDetach()
        Log.i("CommentAddDialogFragment", "detach()")
//        detailFragment.observeComment()
//        detailViewModel.comments.value = detailViewModel.commentsResult

    }

    private fun removeDialog(){
        requireActivity().supportFragmentManager
            .beginTransaction()
            .remove(this)
            .commit()
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
                    putParcelable(ARG_PARAM1, comment)
                }
            }
    }
}