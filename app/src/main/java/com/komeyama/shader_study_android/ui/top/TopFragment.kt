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
            "Coming soon!" to R.id.action_topFragment_to_study2Fragment
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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