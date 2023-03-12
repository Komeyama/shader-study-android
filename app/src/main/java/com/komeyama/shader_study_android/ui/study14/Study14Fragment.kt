package com.komeyama.shader_study_android.ui.study14

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.komeyama.shader_study_android.R
import com.komeyama.shader_study_android.databinding.FragmentStudy14Binding

class Study14Fragment : Fragment(R.layout.fragment_study14) {
    private lateinit var glView: Study14SurfaceView
    private var _binding: FragmentStudy14Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudy14Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        glView = binding.glSurfaceStudy14
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}