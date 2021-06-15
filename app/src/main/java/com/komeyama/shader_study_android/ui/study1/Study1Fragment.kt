package com.komeyama.shader_study_android.ui.study1

import android.annotation.SuppressLint
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.komeyama.shader_study_android.R
import com.komeyama.shader_study_android.databinding.FragmentStudy1Binding

class Study1Fragment: Fragment(R.layout.fragment_study1) {
    private lateinit var glView: GLSurfaceView
    private var _binding: FragmentStudy1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStudy1Binding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        glView = binding.myGlSurface
        glView.setOnTouchListener { _, motionEvent ->
            glView.onTouchEvent(motionEvent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}