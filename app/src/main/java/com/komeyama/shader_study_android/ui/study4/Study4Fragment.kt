package com.komeyama.shader_study_android.ui.study4

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.komeyama.shader_study_android.R
import com.komeyama.shader_study_android.databinding.FragmentStudy4Binding

class Study4Fragment : Fragment(R.layout.fragment_study4) {

    private lateinit var glView: Study4SurfaceView
    private var _binding: FragmentStudy4Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStudy4Binding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        glView = binding.glSurfaceStudy4
        binding.clearButton.setOnClickListener {
            glView.queueEvent {
                glView.clear()
            }
        }

        binding.changeBlackButton.setOnClickListener {
            changeColor(floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f))
        }

        binding.changeRedButton.setOnClickListener {
            changeColor(floatArrayOf(1.0f, 0.0f, 0.0f, 1.0f))
        }

        binding.changeGreenButton.setOnClickListener {
            changeColor(floatArrayOf(0.0f, 1.0f, 0.0f, 1.0f))
        }

        binding.changeBlueButton.setOnClickListener {
            changeColor(floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changeColor(rgb: FloatArray) {
        glView.queueEvent {
            glView.changeColor(rgb)
        }
    }
}