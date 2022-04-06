package com.example.workoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.workoutapp.databinding.ActivityWorkoutSummaryBinding

class WorkoutSummary : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutSummaryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //repress top bar
        getSupportActionBar()!!.hide()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.mid_grey))

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