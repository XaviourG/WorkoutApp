package com.example.workoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.workoutapp.databinding.ActivityWorkoutListBinding

class WorkoutListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title="Workout DB"
        binding.btnHomeWL.setOnClickListener {
            val i = Intent(this@WorkoutListActivity, MainActivity::class.java)
            startActivity(i)
        }
        binding.btnNewWorkout.setOnClickListener {
            val i = Intent(this@WorkoutListActivity, BuildWorkoutActivity::class.java)
            startActivity(i)
        }
    }
}