package com.djhb.petopia.presentation.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.databinding.FragmentAlbumMainBinding
import com.djhb.petopia.presentation.MainActivity
import com.yasic.library.particletextview.MovingStrategy.RandomMovingStrategy
import com.yasic.library.particletextview.Object.ParticleTextViewConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AlbumMainFragment : Fragment() {


    private val _binding: FragmentAlbumMainBinding by lazy {
        FragmentAlbumMainBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding


    private val albumSharedViewModel by viewModels<AlbumSharedViewModel> {
        AlbumSharedViewModelFactory()
    }
//    private val gallerySharedViewModel by viewModels<GallerySharedViewModel>()
    private val backPressedCallbackAlbum = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            (activity as MainActivity).removeAlbumFragment()
            requireActivity().onBackPressedDispatcher.addCallback(backPressedCallbackEntire)
        }
    }

    private var isClickedBack = false
    private val backPressedCallbackEntire = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(isClickedBack) {
                requireActivity().finish()
            } else {
                isClickedBack = true
                Toast.makeText(requireActivity(), "한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
                lifecycleScope.launch {
                    delay(2000)
                    isClickedBack = false
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //버튼 클릭이벤트
        albumMainButtonClickListener()
        //데이터 변화감지
        albumMainDataObserver()

    }


    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun albumMainButtonClickListener() {
        binding.albumBtnBack.setOnClickListener {
            (activity as MainActivity).removeAlbumFragment()
        }

        requireActivity().onBackPressedDispatcher.addCallback(backPressedCallbackAlbum)
    }


    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun albumMainDataObserver() {

       val particleTextBuilder = ParticleTextViewConfig.Builder()
            .setRowStep(8)
            .setColumnStep(8)
            .setTargetText("ALBUM")
            .setReleasing(0.1)
            .setParticleRadius(4F)
            .setMiniDistance(0.1)
           .setDelay(2000)
            .setTextSize(150)
            .setMovingStrategy(RandomMovingStrategy())
            .instance()

        binding.albumTvTitle.apply {
            setConfig(particleTextBuilder)
            startAnimation()
        }
    }

    override fun onStop() {
        super.onStop()
        binding.albumTvTitle.stopAnimation()
    }


}