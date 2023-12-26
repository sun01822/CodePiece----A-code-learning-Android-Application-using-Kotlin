package com.example.codepiece.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.codepiece.R
import com.example.codepiece.adapter.GridAdapter
import com.example.codepiece.data.GridItem
import com.example.codepiece.databinding.FragmentTestYourselfBinding
import com.example.codepiece.fragments.languages.CFragment
import com.example.codepiece.fragments.languages.CPPFragment
import com.example.codepiece.fragments.languages.JavaFragment
import com.example.codepiece.fragments.languages.PythonFragment
import com.example.codepiece.helper.FragmentHelper

class TestYourselfFragment : Fragment() {
    private lateinit var binding : FragmentTestYourselfBinding
    private lateinit var gridRecyclerView: RecyclerView
    private lateinit var gridAdapter: GridAdapter
    private lateinit var value : String
    private val gridItems =listOf(
        GridItem("C programming", "https://res.cloudinary.com/practicaldev/image/fetch/s--jfMWJNMp--/c_imagga_scale,f_auto,fl_progressive,h_900,q_auto,w_1600/https://dev-to-uploads.s3.amazonaws.com/uploads/articles/b22wdroslo9khnoyssmn.png"),
        GridItem("C++", "https://www.nicepng.com/png/detail/111-1116276_computer-science-i-syllabus-and-grading-policy-c.png"),
        GridItem("Java", "https://static.vecteezy.com/system/resources/previews/020/111/555/non_2x/java-editorial-logo-free-download-free-vector.jpg"),
        GridItem("Python", "https://www.pngitem.com/pimgs/m/159-1595932_python-logo-png-transparent-images-logo-transparent-background.png"),
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentTestYourselfBinding.inflate(layoutInflater)
        Glide.with(this.requireContext()).load(R.drawable.quiz).into(binding.quizImage)
        gridRecyclerView = binding.gridRecyclerView
        gridRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        gridAdapter = GridAdapter(requireContext(), gridItems)
        gridRecyclerView.adapter = gridAdapter
        Glide.with(this.requireContext()).load(R.drawable.quiz).into(binding.quizImage)
        gridAdapter.setOnclickListener(object : GridAdapter.OnClickListener{
            override fun onClick(position: Int, gridItem: GridItem) {
                val fragment = when (position) {
                    0 -> {
                        value = gridItem.name.toString()
                        FragmentHelper.getFragmentWithValue(CFragment(),value)
                    }
                    1 -> {
                        value = gridItem.name.toString()
                        FragmentHelper.getFragmentWithValue(CPPFragment(),value)
                    }
                    2 -> {
                        value = gridItem.name.toString()
                        FragmentHelper.getFragmentWithValue(JavaFragment(),value)
                    }
                    3 -> {
                        value = gridItem.name.toString()
                        FragmentHelper.getFragmentWithValue(PythonFragment(),value)
                    }
                    else -> {
                        null
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment!!)
                    .addToBackStack(null)
                    .commit()
            }

            override fun onClick(p0: View?) {

            }
        })
        return binding.root
    }
}