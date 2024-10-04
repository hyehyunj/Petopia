package com.djhb.petopia.presentation.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.djhb.petopia.R
import com.djhb.petopia.databinding.FragmentAlbumMainBinding
import com.djhb.petopia.presentation.MainActivity
import com.yasic.library.particletextview.MovingStrategy.RandomMovingStrategy
import com.yasic.library.particletextview.Object.ParticleTextViewConfig
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//앨범 메인 프래그먼트 : 앨범 첫번째 페이지
class AlbumMainFragment : Fragment() {

    private val _binding: FragmentAlbumMainBinding by lazy {
        FragmentAlbumMainBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
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
                StyleableToast.makeText(
                    requireActivity(),
                    "한 번 더 누르면 앱이 종료됩니다.",
                    R.style.toast_common
                ).show()
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
        showAlbumMainTextAnimation()

    }


    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun albumMainButtonClickListener() {
        binding.albumBtnBack.setOnClickListener {
            (activity as MainActivity).removeAlbumFragment()
        }
        requireActivity().onBackPressedDispatcher.addCallback(backPressedCallbackAlbum)
    }


    //앨범 메인 텍스트 애니메이션 함수 : 앨범 첫페이지의 문구 애니메이션을 정의하는 함수
    private fun showAlbumMainTextAnimation() {
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