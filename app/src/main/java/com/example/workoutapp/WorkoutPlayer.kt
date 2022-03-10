package com.example.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutapp.adapters.WorkoutPlayerAdapter
import com.example.workoutapp.data.exercisedb.ExerciseInstance
import com.example.workoutapp.data.exercisedb.ExerciseViewModel
import com.example.workoutapp.data.exercisedb.Workout
import com.example.workoutapp.databinding.ActivityWorkoutEditorBinding
import com.example.workoutapp.databinding.ActivityWorkoutPlayerBinding

class WorkoutPlayer : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutPlayerBinding
    private lateinit var wpAdapter: WorkoutPlayerAdapter
    private lateinit var workout: Workout

    private val exerciseViewModel: ExerciseViewModel by viewModels {
        ExerciseViewModel.ExerciseViewModelFactory((application as MyApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        wpAdapter = WorkoutPlayerAdapter(this)
        binding.rvPlayer.adapter = wpAdapter
        binding.rvPlayer.layoutManager = LinearLayoutManager(this)

        workout = Workout(title="FakeWorkout",exercises=mutableListOf<ExerciseInstance>())
        var wid = intent.getIntExtra("WID",14)
        exerciseViewModel.allWorkouts.observe(this, { list ->
            list.let {
                println("Searching for wid:$wid in >> $it")
                for (w in it) {
                    if (w.WID == wid) {
                        workout = w
                        println("FOUND WORKOUT: $workout")
                        wpAdapter.setWorkout(workout)
                        title = workout.title
                    }
                }
            }
        })
    }

}