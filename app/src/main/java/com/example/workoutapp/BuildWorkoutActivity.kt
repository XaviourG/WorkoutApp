package com.example.workoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.workoutapp.adapters.WorkoutListAdapter
import com.example.workoutapp.data.exercisedb.*
import com.example.workoutapp.databinding.ActivityBuildWorkoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BuildWorkoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuildWorkoutBinding

    private val exerciseViewModel: ExerciseViewModel by viewModels {
        ExerciseViewModel.ExerciseViewModelFactory((application as MyApplication).repository)
    }
    //private val searchResultAdapter: SearchResultAdapter by lazy {SearchResultAdapter()}
    private lateinit var workoutListAdapter: WorkoutListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuildWorkoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title="New Workout"

        binding.btnX.setOnClickListener {
            val i = Intent(this@BuildWorkoutActivity, WorkoutListActivity::class.java)
            startActivity(i)
        }

        binding.btnTestInsert.setOnClickListener {
            val exName = binding.ptTest.text.toString()
            val ex = Exercise(name = exName)
            exerciseViewModel.insert(ex)
            binding.ptTest.text.clear()
        }

        binding.btnTestPrintDB.setOnClickListener {
            val name = binding.ptTest.text.toString()
            val list = listOf<String>("Bench Press, Squat, Deadlift")
            val w = Workout(title = name, exercises = list)
            exerciseViewModel.insertWorkout(w)
            binding.ptTest.text.clear()
        }




    }
}