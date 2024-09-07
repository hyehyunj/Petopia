package com.djhb.petopia.presentation.community.dialogFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djhb.petopia.data.PostModel
import com.djhb.petopia.databinding.FragmentDialogBinding
import com.djhb.petopia.presentation.dialog.DialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "postKey"

/**
 * A simple [Fragment] subclass.
 * Use the [PostDeleteDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostDeleteDialogFragment(
    private val postKey: String, private val onClickDelete: (String) -> Unit
) : DialogFragment() {
    // TODO: Rename and change types of parameters
//    private var postKey: String? = null
    private val binding: FragmentDialogBinding by lazy {
        FragmentDialogBinding.inflate(layoutInflater)
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            postKey = it.getString(ARG_PARAM1)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_post_edit_dialog, container, false)

        binding.dialogTvAction.setOnClickListener {
            onClickDelete(postKey)
            dismiss()
        }

        binding.dialogTvCancel.setOnClickListener {
            dismiss()
        }
        return binding.root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param pokstKey current post's key.
         * @return A new instance of fragment PostEditDialogFragment.
         */
        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(postKey: String) =
//            PostDeleteDialogFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, postKey)
//                }
//            }
    }
}