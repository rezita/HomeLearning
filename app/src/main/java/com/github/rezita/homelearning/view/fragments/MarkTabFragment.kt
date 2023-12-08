package com.github.rezita.homelearning.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.databinding.FragmentErikTabBinding
import com.github.rezita.homelearning.databinding.FragmentMarkTabBinding
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.view.ReadingActivity

private const val ARG_NAME = "Name"
private const val ARG_POSITION = "Position"

class MarkTabFragment : Fragment() {
    private var paramName: String? = null
    private var paramPosition: Int? = null

    private var _binding: FragmentMarkTabBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramName = it.getString(ARG_NAME)
            paramPosition = it.getInt(ARG_POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMarkTabBinding.inflate(inflater, container, false)
        binding.btnStartReading.setOnClickListener { startReading(SheetAction.READ_READING_WORDS) }
        binding.btnReadingCEW.setOnClickListener { startReading(SheetAction.READ_READING_CEW) }
        return binding.root

    }

    private fun startReading(readingAction: SheetAction) {
        val intent = Intent(activity, ReadingActivity::class.java)
        intent.putExtra("action", readingAction.value)
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance(paramName: String, paramPosition: Int) =
            ErikTabFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NAME, paramName)
                    putInt(ARG_POSITION, paramPosition)
                }
            }
    }


}