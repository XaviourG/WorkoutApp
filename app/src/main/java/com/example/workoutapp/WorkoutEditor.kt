package com.example.workoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.workoutapp.adapters.SearchResultsAdapter
import com.example.workoutapp.adapters.WorkoutBuildAdapter
import com.example.workoutapp.adapters.WorkoutListAdapter
import com.example.workoutapp.data.exercisedb.*
import com.example.workoutapp.databinding.ActivityBuildWorkoutBinding
import com.example.workoutapp.databinding.ActivityWorkoutEditorBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WorkoutEditor : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutEditorBinding
    private lateinit var srAdapter: SearchResultsAdapter
    private lateinit var wlAdapter: WorkoutBuildAdapter
    private lateinit var workout: Workout
    private var WID = 1

    private val exerciseViewModel: ExerciseViewModel by viewModels {
        ExerciseViewModel.ExerciseViewModelFactory((application as MyApplication).repository)
    }

    //private val searchResultAdapter: SearchResultAdapter by lazy {SearchResultAdapter()}
    //private lateinit var workoutListAdapter: WorkoutListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutEditorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title="Workout Editor"

        wlAdapter = WorkoutBuildAdapter(this)
        binding.rvBuildSpace.adapter = wlAdapter
        binding.rvBuildSpace.layoutManager = LinearLayoutManager(this)

        srAdapter = SearchResultsAdapter(wlAdapter, exerciseViewModel)
        binding.rvSearchResults.adapter = srAdapter
        binding.rvSearchResults.layoutManager = LinearLayoutManager(this)

        exerciseViewModel.allExercises.observe(this, {
                list -> list.let{
            srAdapter.setData(it)
        }
        })
        workout = Workout(title="FakeWorkout",exercises=mutableListOf<ExerciseInstance>())
        var wid = intent.getIntExtra("WID",1)
        exerciseViewModel.allWorkouts.observe(this, {
                list -> list.let{
            println("Searching for wid:$wid in >> $it")
            for(w in it){
                if(w.WID == wid){
                    workout = w
                    println("FOUND WORKOUT: $workout")
                    wlAdapter.setWorkout(workout)
                    binding.etTitle.setText(workout.title)
                    //exerciseViewModel.deleteWorkout(workout)
                }
            }
        }
        })

        binding.rvSearchResults.visibility = View.GONE

        binding.svBuild.setOnQueryTextListener( object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                srAdapter.filter(query)
                return false
            }
            override fun onQueryTextChange(query: String): Boolean {
                binding.rvSearchResults.visibility = View.VISIBLE
                srAdapter.filter(query)
                return false
            }
        })

        binding.svBuild.setOnCloseListener( object : SearchView.OnCloseListener,
            androidx.appcompat.widget.SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                binding.rvSearchResults.visibility = View.GONE
                return false
            }
        })

        //Save button pressed, display 'enter name' popup then save to database.
        binding.btnDone.setOnClickListener{
            var title = binding.etTitle.text.toString()
            wlAdapter.updateSets()
            val workout = Workout(WID=wid, title=title, exercises = wlAdapter.getExerciseList())
            //exerciseViewModel.insertWorkout(workout)
            exerciseViewModel.updateWorkout(workout)
            val i = Intent(this@WorkoutEditor, WorkoutListActivity::class.java)
            startActivity(i)
        }

    }


}

