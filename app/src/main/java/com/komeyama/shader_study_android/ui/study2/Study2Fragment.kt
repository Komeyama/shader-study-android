package com.komeyama.shader_study_android.ui.study2

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.komeyama.shader_study_android.R
import com.komeyama.shader_study_android.databinding.FragmentStudy2Binding

class Study2Fragment: Fragment(R.layout.fragment_study2) {
    private lateinit var glView: MyGLSurfaceView2
    private var _binding: FragmentStudy2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStudy2Binding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        glView = binding.myGlSurface

        binding.sliderConversionFactor.addOnChangeListener{ _, value, _ ->
            glView.changeConversionFactor(value)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}