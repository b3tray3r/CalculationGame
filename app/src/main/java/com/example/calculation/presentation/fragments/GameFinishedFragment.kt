package com.example.calculation.presentation.fragments

import android.media.MediaFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.calculation.R
import com.example.calculation.databinding.FragmentGameFinishedBinding
import com.example.calculation.domain.entity.GameResult
import com.example.calculation.domain.entity.Level

class GameFinishedFragment : Fragment() {
    private val args by navArgs<GameFinishedFragmentArgs>()

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinished = null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        bindingViews()
        binding.buttonRetry.setOnClickListener {
            restartGame()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun restartGame() {
        findNavController().popBackStack()
    }

    private fun setupClickListeners() {
        binding.buttonRetry.setOnClickListener {
            restartGame()
        }
    }

    private fun bindingViews() {
        with(binding) {
            emojiResult.setImageResource(getSmileResId())
            tvRequiredAnswers.text = String.format(
                getString(R.string.required_score),
                args.gameResult.gameSettings.minCountAnswers
            )
            tvScoreAnswers.text = String.format(
                getString(R.string.score_answers),
                args.gameResult.countAnswers
            )
            tvRequiredPercentage.text = String.format(
                getString(R.string.required_percentage),
                args.gameResult.gameSettings.minPercentAnswers
            )
            tvScorePercentage.text = String.format(
                getString(R.string.score_percentage),
                getPercentOfRightAnswers()
            )
        }
    }

    private fun getPercentOfRightAnswers() = with(args.gameResult){
        if (countQuestions==0) {
            0
        } else {
            ((countAnswers/countQuestions.toDouble())*100).toInt()
        }
    }

    private fun getSmileResId(): Int {
        return if (args.gameResult.win) {
            R.drawable.ic_smile
        } else {
            R.drawable.ic_sad
        }
    }

}