package com.komeyama.shader_study_android.ui.study5

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.komeyama.shader_study_android.R
import com.komeyama.shader_study_android.databinding.FragmentStudy5Binding

class Study5Fragment : Fragment(R.layout.fragment_study5) {
    private lateinit var glView: Study5SurfaceView
    private var _binding: FragmentStudy5Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStudy5Binding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        glView = binding.glSurfaceStudy5
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}