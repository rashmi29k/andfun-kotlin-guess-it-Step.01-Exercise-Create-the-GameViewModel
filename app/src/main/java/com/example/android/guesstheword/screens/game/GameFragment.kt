package com.example.android.guesstheword.screens.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {
    private lateinit var gameViewModel: GameViewModel

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.game_fragment,
            container,
            false
        )
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        binding.gameViewModel = gameViewModel
        binding.lifecycleOwner = this

        gameViewModel.eventGameFinished.observe(viewLifecycleOwner, Observer {
            if(it){
                val action = GameFragmentDirections.actionGameToScore(gameViewModel.score.value ?: 0)
                findNavController(this).navigate(action)
                gameViewModel.onGameFinishComplete()
            }
        })

        gameViewModel.eventBuzz.observe(viewLifecycleOwner, Observer {
            if(it!=GameViewModel.BuzzType.NO_BUZZ){
                buzz(it.pattern)
                gameViewModel.onBuzzCompleted()
            }
        })
        return binding.root

    }

    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()
        buzzer.let {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer?.vibrate(VibrationEffect.createWaveform(pattern,-1))
            } else {
                buzzer?.vibrate(pattern,-1)
            }
        }
    }
}
