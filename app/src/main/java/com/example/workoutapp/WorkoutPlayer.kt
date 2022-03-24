package com.example.workoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutapp.adapters.WorkoutPlayerAdapter
import com.example.workoutapp.data.exercisedb.ExerciseInstance
import com.example.workoutapp.data.exercisedb.ExerciseViewModel
import com.example.workoutapp.data.exercisedb.Program
import com.example.workoutapp.data.exercisedb.Workout
import com.example.workoutapp.databinding.ActivityWorkoutEditorBinding
import com.example.workoutapp.databinding.ActivityWorkoutPlayerBinding
import kotlinx.coroutines.delay

class WorkoutPlayer : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutPlayerBinding
    private lateinit var wpAdapter: WorkoutPlayerAdapter
    private lateinit var workout: Workout
    private lateinit var program: Program

    private val exerciseViewModel: ExerciseViewModel by viewModels {
        ExerciseViewModel.ExerciseViewModelFactory((application as MyApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = ActivityWorkoutPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getSupportActionBar()!!.hide()

        wpAdapter = WorkoutPlayerAdapter(this, exerciseViewModel)
        binding.rvPlayer.adapter = wpAdapter
        binding.rvPlayer.layoutManager = LinearLayoutManager(this)

        workout = Workout(title="FakeWorkout",exercises=mutableListOf<ExerciseInstance>())
        var wid = intent.getIntExtra("WID",1)
        exerciseViewModel.allWorkouts.observe(this, { list ->
            list.let {
                for (w in it) {
                    if (w.WID == wid) {
                        workout = w
                        wpAdapter.setWorkout(workout)
                        title = workout.title
                    }
                }
            }
        })

        binding.btnX.setOnClickListener {
            val i = Intent(this@WorkoutPlayer, MainActivity::class.java)
            startActivity(i)
        }

        binding.btnFin.setOnClickListener {
            //Logging functionality
            for(l in wpAdapter.getLogs()){
                exerciseViewModel.insertLog(l)
            }

            //eventually make this go to a finished page
            val i = Intent(this@WorkoutPlayer, MainActivity::class.java)
            startActivity(i)
        }
    }

}