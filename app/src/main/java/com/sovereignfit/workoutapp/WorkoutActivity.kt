package com.sovereignfit.workoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sovereignfit.workoutapp.databinding.ActivityWorkoutBinding

class WorkoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title = "Workout"
        binding.btnDone.setOnClickListener {
            val i = Intent(this@WorkoutActivity, MainActivity::class.java)
            startActivity(i)
        }
    }
}