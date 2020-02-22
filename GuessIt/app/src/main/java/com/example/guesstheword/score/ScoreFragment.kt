package com.example.guesstheword.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.guesstheword.R
import com.example.guesstheword.databinding.FragmentScoreBinding

class ScoreFragment : Fragment() {

    private lateinit var viewModel: ScoreViewModel
    private lateinit var viewModelFactory: ScoreViewModelFactory

    private lateinit var binding: FragmentScoreBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_score, container, false)
        val scoreFragmentArgs by navArgs<ScoreFragmentArgs>()
        viewModelFactory = ScoreViewModelFactory(scoreFragmentArgs.score)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ScoreViewModel::class.java)
        binding.textScore.text = scoreFragmentArgs.score.toString()
        binding.buttonPlayAgain.setOnClickListener {
            onPlayAgain()
        }
        return binding.root
    }

    private fun onPlayAgain() {
        findNavController().navigate(R.id.action_scoreFragment_to_gameFragment)
    }

}