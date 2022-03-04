package com.example.workoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutapp.adapters.WorkoutListAdapter
import com.example.workoutapp.data.exercisedb.ExerciseViewModel
import com.example.workoutapp.data.exercisedb.Workout
import com.example.workoutapp.databinding.ActivityWorkoutListBinding
import android.util.Log
import android.view.View
import kotlinx.coroutines.delay


class WorkoutListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutListBinding
    private lateinit var wlAdapter: WorkoutListAdapter
    private val exerciseViewModel: ExerciseViewModel by viewModels {
        ExerciseViewModel.ExerciseViewModelFactory((application as MyApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title="Workout DB"
        exerciseViewModel.updateWorkouts()

        /*
        val fakeList = mutableListOf<Workout>()
        val fakeExs = listOf<String>("Ex1", "Ex2", "Ex3")
        fakeList.add(Workout(title = "Lower Body", exercises = fakeExs))
        fakeList.add(Workout(title = "Upper Body", exercises = fakeExs))
        fakeList.add(Workout(title = "Full Body", exercises = fakeExs))
        fakeList.add(Workout(title = "Hardcore Parkour", exercises = fakeExs))

         */
        wlAdapter = WorkoutListAdapter()
        binding.rvWorkoutList.adapter = wlAdapter
        binding.rvWorkoutList.layoutManager = LinearLayoutManager(this)
        exerciseViewModel.allWorkouts.observe(this, { list ->
            list.let {
                wlAdapter.setData(it)
            }
        })

        binding.btnHomeWL.setOnClickListener {
            val i = Intent(this@WorkoutListActivity, MainActivity::class.java)
            startActivity(i)
        }
        binding.btnNewWorkout.setOnClickListener {
            val i = Intent(this@WorkoutListActivity, BuildWorkoutActivity::class.java)
            startActivity(i)
        }

            /*
        binding.btnRefreshList.setOnClickListener {
            exerciseViewModel.updateWorkouts()
            wlAdapter.updateWorkouts(exerciseViewModel.workouts)
            binding.btnRefreshList.visibility = View.GONE
        }

             */
        //binding.btnNewWorkout.text = workouts[0].title
}}