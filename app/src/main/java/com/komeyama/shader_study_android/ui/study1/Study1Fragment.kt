package com.komeyama.shader_study_android.ui.study1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.komeyama.shader_study_android.R
import com.komeyama.shader_study_android.databinding.FragmentStudy1Binding

class Study1Fragment : Fragment(R.layout.fragment_study1) {
    private lateinit var glView: Study1SurfaceView
    private var _binding: FragmentStudy1Binding? = null
    private val binding get() = _binding!!

    private var rgbOfTriangle = floatArrayOf(255f / 2f, 255f / 2f, 255f / 2f, 1.0f)

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
        glView = binding.glSurfaceStudy1
        glView.setOnTouchListener { _, motionEvent ->
            glView.onTouchEvent(motionEvent)
        }

        binding.sliderR.addOnChangeListener { _, rValue, _ ->
            rgbOfTriangle[0] = rValue
            glView.changeTriangleColor(rgbOfTriangle)
        }
        binding.sliderG.addOnChangeListener { _, gValue, _ ->
            rgbOfTriangle[1] = gValue
            glView.changeTriangleColor(rgbOfTriangle)
        }
        binding.sliderB.addOnChangeListener { _, bValue, _ ->
            rgbOfTriangle[2] = bValue
            glView.changeTriangleColor(rgbOfTriangle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}