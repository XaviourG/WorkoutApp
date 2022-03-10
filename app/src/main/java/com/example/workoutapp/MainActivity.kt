package com.example.workoutapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.workoutapp.databinding.ActivityBuildWorkoutBinding
import com.example.workoutapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title="Homepage"
        binding.btnNextWorkout.setOnClickListener {
            val intent = Intent(this@MainActivity, WorkoutPlayer::class.java)
            startActivity(intent)
        }
        binding.btnWorkouts.setOnClickListener {
            val intent = Intent(this@MainActivity, WorkoutListActivity::class.java)
            startActivity(intent)
        }
        binding.btnMyProgram.setOnClickListener {
            val intent = Intent(this@MainActivity, ProgramActivity::class.java)
            startActivity(intent)
        }

    }
}