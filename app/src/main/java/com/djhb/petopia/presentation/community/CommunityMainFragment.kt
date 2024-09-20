package com.djhb.petopia.presentation.community

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.djhb.petopia.FilteringType
import com.djhb.petopia.R
import com.djhb.petopia.data.UserModel
import com.djhb.petopia.databinding.FragmentCommunityMainBinding
import com.djhb.petopia.presentation.MainActivity
import com.djhb.petopia.presentation.community.adapter.PostAdapter
import com.djhb.petopia.presentation.community.adapter.RankPostAdapter
import com.djhb.petopia.presentation.home.HomeEarthFragment
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.async
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

//    private val viewModel: CommunityViewModel by activityViewModels()
    private val viewModel: CommunityViewModel by lazy {
        CommunityViewModel()
    }

    private val mainActivity: MainActivity by lazy {
        requireActivity() as MainActivity
    }

    private val selectedCategories = mutableListOf<FilteringType>()

    private val rankPostAdapter by lazy {
        RankPostAdapter { post ->
//            Toast.makeText(requireActivity(), "post = ${post}", Toast.LENGTH_SHORT).show()

//            val postAddedViewCount = post.copy(viewCount = post.viewCount + 1)
//            val detailFragment = CommunityDetailFragment.newInstance(postAddedViewCount)
            val detailFragment = CommunityDetailFragment.newInstance(post.key)

            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_sub_frame, detailFragment)
                .addToBackStack("questionMain")
                .commit()

            lifecycleScope.launch {
//                viewModel.updatePost(postAddedViewCount)
                viewModel.addPostViewCount(post.key)
            }

            mainActivity.hideViewPager()

        }
    }

    private val allPostAdapter by lazy {
        PostAdapter { post ->
//            Toast.makeText(requireActivity(), "post = ${post}", Toast.LENGTH_SHORT).show()
//            Log.i("CommunityMainFramgment", "before copy imageUris.size = ${post.imageUris.size}")
//            val postAddedViewCount = post.copy(viewCount = post.viewCount + 1)
//            Log.i("CommunityMainFramgment", "after copy imageUris.size = ${postAddedViewCount.imageUris.size}")
//            val detailFragment = CommunityDetailFragment.newInstance(postAddedViewCount)
            val detailFragment = CommunityDetailFragment.newInstance(post.key)
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_sub_frame, detailFragment)
                .addToBackStack(null)
                .commit()

            lifecycleScope.launch {
//                viewModel.updatePost(postAddedViewCount)
//                viewModel.addPostViewCount(postAddedViewCount.key)
                viewModel.addPostViewCount(post.key)
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
//        val user = UserModel("id1", "password1", "name1", "nickname1")
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

    private fun initView() {
        lifecycleScope.launch {
            async { viewModel.selectRankList(mutableListOf())}.await()
            async { viewModel.selectInitPostList(mutableListOf())}.await()
//            async { viewModel.selectAllImageList()}.await()
        }
        binding.recyclerViewQuestionMain.isNestedScrollingEnabled = false
        binding.recyclerViewQuestionRank.isNestedScrollingEnabled = false
        binding.header.ivSearch.visibility = ImageView.INVISIBLE

    }

    private fun initListener() {
        binding.header.ivBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.home_earth_container, HomeEarthFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.header.ivSearch.setOnClickListener {
            StyleableToast.makeText(
                requireActivity(),
                getString(R.string.messege_undo),
                R.style.toast_undo
            ).show()
        }

        binding.btnCreatePost.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_sub_frame, CommunityCreateFragment())
                .addToBackStack(null)
                .commit()

            mainActivity.hideViewPager()
        }

//        binding.recyclerViewQuestionMain.addOnScrollListener(object : OnScrollListener(){
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if(!recyclerView.canScrollVertically(1)) {
//                    lifecycleScope.launch {
//                        viewModel.selectNextPostList()
//                    }
//                }
//            }
//        })

        binding.svMain.setOnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->

            if (!view.canScrollVertically(1) && viewModel.isProgressing.value == false) {
                lifecycleScope.launch {
//                    Log.i("12444", "end scroll")
                    async { viewModel.selectNextPostList(viewModel.filteringResult)}.await()
                }
            }

        }

        binding.cbMainDog.setOnCheckedChangeListener { buttonView, isChecked ->

            if (viewModel.isProgressing.value == true) {
                Toast.makeText(requireActivity(), "조회가 완료된 이후에 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnCheckedChangeListener
            }

            if(isChecked) {
                viewModel.addCategories(FilteringType.DOG)
            } else {
                viewModel.deleteCategories(FilteringType.DOG)
            }
        }

        binding.cbMainCat.setOnCheckedChangeListener { buttonView, isChecked ->

            if (viewModel.isProgressing.value == true) {
                Toast.makeText(requireActivity(), "조회가 완료된 이후에 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnCheckedChangeListener
            }

            if(isChecked) {
                viewModel.addCategories(FilteringType.CAT)
            } else {
                viewModel.deleteCategories(FilteringType.CAT)
            }
        }

    }

    private fun initObserve() {
        viewModel.rankPosts.observe(viewLifecycleOwner) {
//            Log.i("CommunityMainFragment", "observe rank : ${it}")
//            Log.i("CommunityMainFragment", "observe rank.hashCode : ${it.hashCode()}")
//            for (postModel in it) {
//                Log.i("CommunityMainFragment", "observe rank postModel.hashCode : ${postModel.hashCode()}")
//            }

            rankPostAdapter.submitList(it.toMutableList())
        }
        viewModel.searchPost.observe(viewLifecycleOwner){
//            Log.i("CommunityMainFragment", "123. observe search : ${it}")
            lifecycleScope.launch {
//                allPostAdapter.submitList(it.toMutableList())
                allPostAdapter.submitList(viewModel.searchPostResult.toMutableList())
//                async { viewModel.selectAllImageList()}.await()
                async { viewModel.selectAllImageList()}.await()
            }
        }

        viewModel.addedSearchPost.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
//                allPostAdapter.submitList(it.toMutableList())
//                allPostAdapter.submitList(viewModel.searchPostResult.toMutableList())
//                async { viewModel.selectAllImageList()}.await()
                async { viewModel.selectAllImageList()}.await()
            }
        }

        viewModel.postImageUris.observe(viewLifecycleOwner) {
//            Log.i("CommunityMainFragment", "123. observe postImageUris : ${it}")
//            Log.i("CommunityMainFragment", "123. observe postImageUris.size : ${it.size}")
//            for (postModel in allPostAdapter.currentList) {
//                Log.i("CommunityMainFragment", "before postImageUris adapter element = ${postModel}")
//            }
            viewModel.combinePostToImages(it.toMutableList())
//            Log.i("CommunityMainFragment", "it == livedata.value : ${it == viewModel.postImageUris.value}")
//            Log.i("CommunityMainFragment", "it == livedata.value : ${it.hashCode() == viewModel.postImageUris.value.hashCode()}")
//            for (postModel in viewModel.searchPostResult) {
//                Log.i("CommunityMainFragment", "searchResult = ${postModel}")
//            }
//            for (postModel in allPostAdapter.currentList) {
//                Log.i("CommunityMainFragment", "after postImageUris adapter element = ${postModel}")
//            }
//            allPostAdapter.notifyDataSetChanged()
            allPostAdapter.submitList(viewModel.searchPostResult.toMutableList())
        }

        viewModel.searchPostWithImages.observe(viewLifecycleOwner) {
//            Log.i("CommunityMainFragment", "123. observe searchPostWithImages : ${it}")
//            Log.i("CommunityMainFragment", "123. adapterList.hashCode = ${allPostAdapter.currentList.hashCode()}")
//            for (postModel in allPostAdapter.currentList) {
//                Log.i("CommunityMainFragment", "adapter element = ${postModel}")
//            }
//            Log.i("CommunityMainFragment", "123. livedate.hashCode : ${it.hashCode()}")
//            Log.i("CommunityMainFragment", "123. livedate.toList.hashCode. : ${it.toMutableList().hashCode()}")
//            Log.i("CommunityMainFragment", "123. viewModel.searchPostResult.hashCode : ${viewModel.searchPostResult.hashCode()}")
//            Log.i("CommunityMainFragment", "123. viewModel.searchPostResult.toMutableList().hashCode : ${viewModel.searchPostResult.toMutableList().hashCode()}")

            allPostAdapter.submitList(it.toList())
//            allPostAdapter.submitList(viewModel.searchPostResult.toMutableList())
        }

        viewModel.isProgressing.observe(viewLifecycleOwner) { isProcessing ->
            if(isProcessing)
                binding.progressBarPost.visibility = ProgressBar.VISIBLE
            else
                binding.progressBarPost.visibility = ProgressBar.GONE
        }

        viewModel.filteringCategories.observe(viewLifecycleOwner) {
            Log.i("CommunityManinFragment", "filteringCategories = ${it}")

            lifecycleScope.launch {
                viewModel.selectRankList(it)
                viewModel.selectInitPostList(it)
            }

        }

//        viewModel.isCompleteRankPost.observe(viewLifecycleOwner){
//            viewModel.listRankPost()
//        }
    }

    private fun initAdapter() {
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