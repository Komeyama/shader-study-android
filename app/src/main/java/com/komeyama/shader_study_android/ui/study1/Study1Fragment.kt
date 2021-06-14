package com.komeyama.shader_study_android.ui.study1

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class Study1Fragment: Fragment() {
    private lateinit var gLView: GLSurfaceView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gLView = MyGLSurfaceView(requireContext())
        return gLView
    }
}