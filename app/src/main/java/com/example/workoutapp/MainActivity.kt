package com.example.workoutapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.workoutapp.data.exercisedb.ExerciseViewModel
import com.example.workoutapp.data.exercisedb.Program
import com.example.workoutapp.data.exercisedb.Workout
import com.example.workoutapp.databinding.ActivityBuildWorkoutBinding
import com.example.workoutapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var program: Program
    private val exerciseViewModel: ExerciseViewModel by viewModels {
        ExerciseViewModel.ExerciseViewModelFactory((application as MyApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title="Homepage"
        binding.btnNextWorkout.setOnClickListener {
            val intent = Intent(this@MainActivity, WorkoutPlayer::class.java)
            intent.putExtra("WID", program.workoutIDs[program.position])
            intent.putExtra("PID", program.PID)

            //Increment position
            if(program.position >= program.workoutIDs.size - 1) {
                program.position = 0
            } else {
                program.position++
            }
            exerciseViewModel.updateProgram(program)
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

        program = Program(title = "FakeProgram", workoutIDs = emptyList())
        exerciseViewModel.allPrograms.observe(this, {
                programList -> programList.let {
                    for(p in it){
                        if(p.active){
                            setProgram(p)
                            setNextWorkoutText(p.workoutIDs[p.position])
                            binding.btnMyProgram.text = "My Program \n- " + p.title +" -"
                            break
                        }
                    }
        }
        })

    }

    private fun setNextWorkoutText(wid: Int) {
        exerciseViewModel.allWorkouts.observe(this, {
            list -> list.let {
                for(w in it){
                    if(w.WID == wid){
                        binding.btnNextWorkout.text = "Start Next Workout! \n- " +
                                w.title + " -"
                        break
                    }
                }
        }
        })
    }

    private fun setProgram(p: Program) {
        program = p
    }
}