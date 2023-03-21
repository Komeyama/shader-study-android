package com.komeyama.shader_study_android.ui.study15

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.komeyama.shader_study_android.R
import com.komeyama.shader_study_android.databinding.FragmentStudy15Binding

class Study15Fragment : Fragment(R.layout.fragment_study15), Callback {
    private lateinit var glView: Study15SurfaceView
    private var _binding: FragmentStudy15Binding? = null
    private val binding get() = _binding!!

    private val result =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isObtained ->
            if (isObtained) {
                glView.createARCoreSession()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudy15Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        result.launch(Manifest.permission.CAMERA)
        glView = binding.glSurfaceStudy15
        glView.setCallback(this)
        binding.scaleFactor.addOnChangeListener { _, value, _ ->
            glView.setScaleFactor(value)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun updatePointCount(count: String) {
        _binding?.pointNum?.text = count
    }
}

interface Callback {
    fun updatePointCount(count: String)
}