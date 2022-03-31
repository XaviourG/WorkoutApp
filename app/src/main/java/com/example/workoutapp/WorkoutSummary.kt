package com.example.workoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import com.example.workoutapp.databinding.ActivityWorkoutSummaryBinding

class WorkoutSummary : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutSummaryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //repress top bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        getSupportActionBar()!!.hide()

        binding = ActivityWorkoutSummaryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.tvTitle.setText(intent.getStringExtra("title"))
        binding.tvTimeElapsed.setText(intent.getStringExtra("timeElapsed"))
        val load = "${intent.getFloatExtra("totalLoad", 0f).toInt().toString()}kg"
        binding.tvTotalLoad.setText(load)

        binding.btnX.setOnClickListener {
            val i = Intent(this@WorkoutSummary, MainActivity::class.java)
            startActivity(i)
        }

    }
}