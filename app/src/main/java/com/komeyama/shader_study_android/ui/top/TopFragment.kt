package com.komeyama.shader_study_android.ui.top

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.komeyama.shader_study_android.R
import com.komeyama.shader_study_android.databinding.FragmentTopBinding
import com.komeyama.shader_study_android.databinding.ItemTopBinding
import com.xwray.groupie.*
import com.xwray.groupie.viewbinding.BindableItem

class TopFragment : Fragment(R.layout.fragment_top) {
    private var _binding: FragmentTopBinding? = null
    private val binding get() = _binding!!
    private val titleList: Map<String, Int> =
        mapOf(
            "Triangle" to R.id.action_topFragment_to_study1Fragment,
            "Square" to R.id.action_topFragment_to_study2Fragment,
            "Light Sphere" to R.id.action_topFragment_to_study3Fragment,
            "Sketchpad" to R.id.action_topFragment_to_study4Fragment,
            "Light Ring" to R.id.action_topFragment_to_study5Fragment,
            "Mandelbrot" to R.id.action_topFragment_to_study6Fragment,
            "White Noise" to R.id.action_topFragment_to_study7Fragment,
            "Expanding Circle" to R.id.action_topFragment_to_study8Fragment,
            "Galaxy" to R.id.action_topFragment_to_study9Fragment,
            "Cube" to R.id.action_topFragment_to_study10Fragment,
            "Blur" to R.id.action_topFragment_to_study11Fragment,
            "Particle" to R.id.action_topFragment_to_study12Fragment,
            "Box of particles" to R.id.action_topFragment_to_study13Fragment,
            "Picture of particles" to R.id.action_topFragment_to_study14Fragment,
            "Point Cloud" to R.id.action_topFragment_to_study15Fragment,
            "Gradation" to R.id.action_topFragment_to_study16Fragment,
            "Simple Filter" to R.id.action_topFragment_to_study17Fragment,
            "Stencil" to R.id.action_topFragment_to_study18Fragment,
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleItemList: List<TopItem> = titleList.map {
            TopItem(it.key, it.value)
        }
        val adapter = GroupieAdapter().apply {
            setOnItemClickListener { item, _ ->
                if (item is TopItem && item.title.isNotEmpty()) {
                    Log.d("TopFragment", item.title)
                    findNavController().navigate(item.navigateId)
                }
            }
        }
        binding.topRecyclerView.adapter = adapter
        val section = Section()
        section.update(titleItemList)
        adapter.add(section)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class TopItem(val title: String, val navigateId: Int) :
    BindableItem<ItemTopBinding>() {
    override fun getLayout(): Int = R.layout.item_top

    override fun initializeViewBinding(view: View): ItemTopBinding = ItemTopBinding.bind(view)

    override fun bind(viewBinding: ItemTopBinding, position: Int) {
        viewBinding.studyTitleText.text = title
    }
}