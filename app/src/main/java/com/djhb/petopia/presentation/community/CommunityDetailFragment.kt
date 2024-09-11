package com.djhb.petopia.presentation.community

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.djhb.petopia.DateFormatUtils
import com.djhb.petopia.R
import com.djhb.petopia.ReportContentType
import com.djhb.petopia.data.CommentModel
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.PostModel
import com.djhb.petopia.data.ReportModel
import com.djhb.petopia.databinding.FragmentCommunityDetailBinding
import com.djhb.petopia.presentation.MainActivity
import com.djhb.petopia.presentation.community.adapter.DetailCommentAdapter
import com.djhb.petopia.presentation.community.adapter.DetailImageAdapter
import com.djhb.petopia.presentation.community.adapter.OnClickComment
import com.djhb.petopia.presentation.community.dialogFragment.CommentEditDialogFragment
import com.djhb.petopia.presentation.community.dialogFragment.PostDeleteDialogFragment
import com.djhb.petopia.presentation.gallery.GalleryFragment
import com.djhb.petopia.presentation.gallery.GallerySharedViewModel
import com.djhb.petopia.presentation.report.ReportFragment
import com.djhb.petopia.presentation.report.ReportViewModel
import kotlinx.coroutines.launch
import me.relex.circleindicator.CircleIndicator
import me.relex.circleindicator.CircleIndicator3

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CommunityDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommunityDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var post: PostModel

    private val binding: FragmentCommunityDetailBinding by lazy {
        FragmentCommunityDetailBinding.inflate(layoutInflater)
    }

    private val mainActivity: MainActivity by lazy {
        requireActivity() as MainActivity
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {


            if (isEnabled) {
                isEnabled = false
                mainActivity.showViewPager()
            }
        }
    }

    private val commentAdapter: DetailCommentAdapter by lazy {
        DetailCommentAdapter(object : OnClickComment {
            override fun onClickEdit(comment: CommentModel) {
//                lifecycleScope.launch {
//                    viewModel.updateComment(comment)
//                }

                val commentAddDialogFragment = CommentEditDialogFragment.newInstance(comment)
                commentAddDialogFragment.show(
                    requireActivity().supportFragmentManager,
                    "addCommentDialog"
                )
            }

            override fun onClickDelete(key: String) {
                lifecycleScope.launch {
                    detailViewModel.deleteComment(key)
                }
            }

            override fun onClickReport(key: String, targetUserId: String) {
//                reportViewModel.setContent(ReportContentType.QUESTION_COMMENT, key)
                reportViewModel.setContent(ReportModel(
                        reporterId = LoginData.loginUser.id,
                        targetUserId = targetUserId,
                        contentType = ReportContentType.QUESTION_COMMENT,
                        contentUid = key
                    )
                )
                Log.i("CommunityDetailFragment", "click post report")
                ReportFragment().show(childFragmentManager, "REPORT_FRAGMENT")
            }

        })
    }

    private val detailViewModel: CommunityDetailViewModel by activityViewModels()
    private val communityViewModel: CommunityViewModel by activityViewModels()
    private val reportViewModel: ReportViewModel by activityViewModels()

    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerAdapter: DetailImageAdapter
    private lateinit var indicator: CircleIndicator3
    private lateinit var key: String


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            post = it.getParcelable(ARG_PARAM1, PostModel::class.java) ?: PostModel()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

//        viewPagerAdapter = DetailImagePagerAdapter(requireContext(), mutableListOf<String>())

//        arguments.let {
//            key = it?.getString(ARG_PARAM1)?:"no key"
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initListener()
        initView()


    }

    private fun initView() {
        lifecycleScope.launch {

//            val imageUris = mutableListOf("https://firebasestorage.googleapis.com/v0/b/petopia-92b56.appspot.com/o/questionPost%2F88f60ac7-ca6f-4474-a65f-409dd6ed9edb%2F20240904_01.png?alt=media&token=d0ded94c-6ddc-453e-b52c-d94329148fd3",
//                "https://firebasestorage.googleapis.com/v0/b/petopia-92b56.appspot.com/o/questionPost%2F7c27e5cb-0d35-4343-b4ce-ce403c1485c0%2F20240905_01.png?alt=media&token=42bc100a-2b0b-4eed-8c6f-61d48de1b743")

//            viewModel.selectDetailImageUris(key)
            viewPager = binding.vpImage
//            viewPagerAdapter = DetailImagePagerAdapter(requireActivity(), viewModel.imageUriResults)
            viewPagerAdapter = DetailImageAdapter()
//            Log.i("CommunityDetailFragment", "imageUriResults = ${viewModel.imageUriResults}")
//            viewPagerAdapter.submitList(viewModel.imageUriResults)
//            viewPagerAdapter.submitList(imageUris)
            detailViewModel.selectDetailImageUris(post.key)
            viewPager.adapter = viewPagerAdapter

            binding.rvComment.adapter = commentAdapter
            val indicatorDetail = binding.indicatorDetail
            indicatorDetail.setViewPager(viewPager)
//            Log.i("CommunityDetailFragment", "imageUris.value.size = ${detailViewModel.imageUris.value?.size}")
            indicatorDetail.createIndicators(detailViewModel.imageUris.value?.size ?: 0, 0)

            detailViewModel.selectAllCommentFromPost(post.key)

            requireActivity().onBackPressedDispatcher.addCallback(
                requireActivity(),
                backPressedCallback
            )

        }

        // mvp 이후 활성화===
        binding.ivLike.visibility = ImageView.INVISIBLE
        binding.tvLike.visibility = TextView.INVISIBLE
        //=================

        if(post.writer.id == LoginData.loginUser.id)
            binding.ivReport.visibility = ImageView.INVISIBLE

        binding.tvDetailTitle.text = post.title
        binding.tvContent.text = post.content
        binding.tvViewCount.text = post.viewCount.toString()
        binding.tvLike.text = post.likeCount.toString()
        binding.tvWriter.text = post.writer.nickname
        binding.tvCreatedDate.text = DateFormatUtils.convertToPostFormat(post.createdDate)


        binding.indicatorDetail.setViewPager(viewPager)
    }

    private fun initListener() {
        binding.ivReport.setOnClickListener {
//            reportViewModel.setContent(ReportContentType.QUESTION_POST, post.key)
            reportViewModel.setContent(ReportModel(
                    reporterId = LoginData.loginUser.id,
                    targetUserId = post.writer.id,
                    contentType = ReportContentType.QUESTION_POST,
                    contentUid = post.key
                )
            )
            ReportFragment().show(childFragmentManager, "REPORT_FRAGMENT")
        }


        binding.btnAddComment.setOnClickListener {
            val comment = binding.etComment.text.toString()
            if (comment.isBlank()) {
                Toast.makeText(requireActivity(), "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    detailViewModel.createComment(
                        CommentModel(
                            post.key,
                            LoginData.loginUser,
                            comment
                        )
                    )
                }
            }
        }

        binding.header.ivBack.setOnClickListener {
            mainActivity.showViewPager()
        }


        if (post.writer.id == LoginData.loginUser.id) {
            binding.btnDelete.visibility = ImageView.VISIBLE
            binding.btnEdit.visibility = ImageView.VISIBLE

//            binding.btnDelete.setOnClickListener {
//                lifecycleScope.launch {
//                    communityViewModel.deletePost(post.key)
//                }
//
//
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.main_sub_frame,CommunityMainFragment())
//                    .commit()
//            }


            binding.btnDelete.setOnClickListener {
                val deleteDialogFragment = PostDeleteDialogFragment(post.key) { key ->
                    lifecycleScope.launch {
                        communityViewModel.deletePost(key)
                        communityViewModel.deletePostImages(key)
                    }
                }
                deleteDialogFragment.show(childFragmentManager, "DELETE POST")
            }

            binding.btnEdit.setOnClickListener {
                val editFragment = CommunityEditFragment.newInstance(post)
//                Log.i("communityDetailFragment", "post.imageUris.size = ${post.imageUris.size}")
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_sub_frame, editFragment)
                    .addToBackStack(null)
                    .commit()
            }

        } else {
            binding.btnDelete.visibility = ImageView.GONE
            binding.btnEdit.visibility = ImageView.GONE
        }
    }

    private fun initObserver() {
        detailViewModel.comments.observe(viewLifecycleOwner) {
//            Log.i("CommunityDetailFragment", "observe comments.size = ${it.size}")
//            commentAdapter.submitList(viewModel.commentsResult)
//            commentAdapter.submitList(it.toMutableList())
            commentAdapter.submitList(it.toMutableList())
        }

        detailViewModel.imageUris.observe(viewLifecycleOwner) {
//            Log.i("CommunityDetailFragment", "observe imageUris.size = ${it.size}")
            viewPagerAdapter.submitList(it.toMutableList())
            binding.indicatorDetail.createIndicators(it.size, 0)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CommunityDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CommunityDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        @JvmStatic
        fun newInstance(key: String) =
            CommunityDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, key)
                }
            }

        @JvmStatic
        fun newInstance(post: PostModel) =
            CommunityDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, post)
                }
            }
    }
}