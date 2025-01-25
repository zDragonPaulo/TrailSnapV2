package com.example.trailsnapv2.ui.achievements

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.trailsnapv2.R

class AchievementsFragment : Fragment() {

    companion object {
        fun newInstance() = AchievementsFragment()
    }

    private val viewModel: AchievementsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_achievements, container, false)
    }
}