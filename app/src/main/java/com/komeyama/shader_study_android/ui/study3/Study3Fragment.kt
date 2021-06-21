package com.komeyama.shader_study_android.ui.study3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.komeyama.shader_study_android.R
import com.komeyama.shader_study_android.databinding.FragmentStudy3Binding

class Study3Fragment: Fragment(R.layout.fragment_study3) {
    private lateinit var glView: Study3SurfaceView
    private var _binding: FragmentStudy3Binding? = null
    private val binding get() = _binding!!
    private var rgbArray = floatArrayOf(0.5f, 0.5f, 0.5f)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStudy3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        glView = binding.glSurfaceStudy3
        binding.sliderR.addOnChangeListener { _, rValue, _ ->
            rgbArray[0] = rValue / 255.0f
            glView.changeRGB(rgbArray)
        }
        binding.sliderG.addOnChangeListener { _, gValue, _ ->
            rgbArray[1] = gValue / 255.0f
            glView.changeRGB(rgbArray)
        }
        binding.sliderB.addOnChangeListener { _, bValue, _ ->
            rgbArray[2] = bValue / 255.0f
            glView.changeRGB(rgbArray)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}