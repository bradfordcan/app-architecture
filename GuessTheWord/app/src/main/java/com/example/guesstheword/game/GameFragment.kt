package com.example.guesstheword.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.guesstheword.R
import com.example.guesstheword.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel

    private lateinit var binding: FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)

        Log.i("GameFragment", "Called ViewModelProviders.of")
        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)
        binding.buttonCorrect.setOnClickListener{
            viewModel.onCorrect()
            updateWordText()
        }

        binding.gameViewModel = viewModel
        binding.lifecycleOwner = this
        binding.buttonSkip.setOnClickListener{
            viewModel.onSkip()
            updateWordText()
        }

        binding.textWord.text = viewModel.word.value
        binding.textScore.text = viewModel.score.value.toString()

        viewModel.word.observe(viewLifecycleOwner, Observer { newWord ->
            binding.textWord.text = newWord
        })

        viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
            binding.textScore.text = newScore.toString()
        })

        viewModel.currentTimeStr.observe(viewLifecycleOwner, Observer { newTime ->
            binding.textTimer.text = newTime
        })

        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { hasFinished ->
            if(hasFinished) {
                gameFinished()
                viewModel.onGameFinishComplete()
            }
        })

        viewModel.buzzEvent.observe(viewLifecycleOwner, Observer {
           if(it != GameViewModel.BuzzType.NO_BUZZ) {
               buzz(it.pattern)
               viewModel.onBuzzComplete()
           }
        })

        updateWordText()
        return binding.root
    }

    private fun gameFinished() {
        Toast.makeText(this.activity, "Game finished!", Toast.LENGTH_SHORT).show()
        val action = GameFragmentDirections.actionGameFragmentToScoreFragment(viewModel.score.value ?: 0)
        findNavController().navigate(action)
    }

    private fun updateWordText() {
        binding.textWord.text = viewModel.word.value
    }

    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()

        buzzer?.let {
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }

}
