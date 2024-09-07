package com.djhb.petopia.presentation.community

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.djhb.petopia.R
import com.djhb.petopia.data.UserModel
import com.djhb.petopia.databinding.FragmentCommunityMainBinding
import com.djhb.petopia.presentation.MainActivity
import com.djhb.petopia.presentation.community.adapter.PostAdapter
import com.djhb.petopia.presentation.community.adapter.RankPostAdapter
import kotlinx.coroutines.launch


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CommunityMainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommunityMainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val binding: FragmentCommunityMainBinding by lazy {
        FragmentCommunityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: CommunityViewModel by activityViewModels()

    private val mainActivity: MainActivity by lazy {
        requireActivity() as MainActivity
    }

    private val rankPostAdapter by lazy {
        RankPostAdapter{ post ->
//            Toast.makeText(requireActivity(), "post = ${post}", Toast.LENGTH_SHORT).show()

            val postAddedViewCount = post.copy(viewCount = post.viewCount + 1)
            val detailFragment = CommunityDetailFragment.newInstance(postAddedViewCount)

            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_sub_frame, detailFragment)
                .addToBackStack(null)
                .commit()

            lifecycleScope.launch {
                viewModel.updatePost(postAddedViewCount)
            }

            mainActivity.hideViewPager()

        }
    }

    private val allPostAdapter by lazy {
        PostAdapter{ post ->
//            Toast.makeText(requireActivity(), "post = ${post}", Toast.LENGTH_SHORT).show()

            val postAddedViewCount = post.copy(viewCount = post.viewCount + 1)
            val detailFragment = CommunityDetailFragment.newInstance(postAddedViewCount)
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_sub_frame, detailFragment)
                .addToBackStack(null)
                .commit()

            lifecycleScope.launch {
                viewModel.updatePost(postAddedViewCount)
            }

            mainActivity.hideViewPager()
        }
    }

//    private val binding: FragmentImageTestBinding by lazy {
//        FragmentImageTestBinding.inflate(layoutInflater)
//    }

//    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val user = UserModel("id1", "password1", "name1", "nickname1")
//        val gallery = GalleryModel()
        initAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserve()
        initListener()
    }

    private fun initView(){
        lifecycleScope.launch {
            viewModel.createRankList()
            viewModel.createAllList()
        }
    }

    private fun initListener(){
        binding.header.ivBack.setOnClickListener{
            Toast.makeText(requireActivity(), "click back", Toast.LENGTH_SHORT).show()
        }

        binding.header.ivSearch.setOnClickListener {
            Toast.makeText(requireActivity(), "click search", Toast.LENGTH_SHORT).show()
        }

        binding.btnCreatePost.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_sub_frame, CommunityCreateFragment())
                .addToBackStack(null)
                .commit()

            mainActivity.hideViewPager()
        }
    }

    private fun initObserve(){
        viewModel.rankPosts.observe(viewLifecycleOwner) {
            Log.i("CommunityMainFragment", "observe rank : ${it}")
            rankPostAdapter.submitList(it)
        }
        viewModel.searchPost.observe(viewLifecycleOwner){
            allPostAdapter.submitList(it)
        }

//        viewModel.isCompleteRankPost.observe(viewLifecycleOwner){
//            viewModel.listRankPost()
//        }
    }

    private fun initAdapter(){
        binding.recyclerViewQuestionRank.adapter = rankPostAdapter
        binding.recyclerViewQuestionMain.adapter = allPostAdapter
//        binding.recyclerViewQuestionRank.layoutManager = LinearLayoutManager(requireActivity())
    }


    // imageTestBinding으로 해야 동작
//    private fun initImageView(reference: StorageReference) {
//        binding.ivUpload.setOnClickListener {
//
//            if (ContextCompat.checkSelfPermission(
//                    requireActivity(),
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE
//                )
//                == PackageManager.PERMISSION_DENIED
//            ) {
//                requireActivity().requestPermissions(
//                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
//                    1
//                )
//            }
//
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.setDataAndType(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"
//            )
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
////            imageLauncher.launch(intent)
//        }
//
//        binding.btnOk.setOnClickListener {
//
//
//            val uploadTask = reference.child("1").child("test2.png")
//                .putFile(imageUri ?: throw Exception("image Uri is null"))
//
//            uploadTask.addOnSuccessListener {
//                Toast.makeText(requireActivity(), "upload success", Toast.LENGTH_SHORT).show()
//            }.addOnFailureListener {
//                Toast.makeText(requireActivity(), "upload fail", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        binding.btnDownload.setOnClickListener {
//            val downloadTask = reference.child("1").child("test2.png").downloadUrl
//            downloadTask.addOnSuccessListener { uri ->
//                Glide.with(requireActivity())
//                    .load(uri)
//                    .centerCrop()
//                    .into(binding.ivDownload)
//            }.addOnFailureListener {
//                Log.e("FirebaseTest", "Download fail : ${it.message}")
//            }
//        }
//    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CommunityFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CommunityMainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}