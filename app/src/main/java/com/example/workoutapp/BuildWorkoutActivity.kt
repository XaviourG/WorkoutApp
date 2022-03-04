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
import com.example.workoutapp.adapters.WorkoutListAdapter
import com.example.workoutapp.data.exercisedb.*
import com.example.workoutapp.databinding.ActivityBuildWorkoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BuildWorkoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuildWorkoutBinding
    private lateinit var srAdapter: SearchResultsAdapter

    private val exerciseViewModel: ExerciseViewModel by viewModels {
        ExerciseViewModel.ExerciseViewModelFactory((application as MyApplication).repository)
    }

    //private val searchResultAdapter: SearchResultAdapter by lazy {SearchResultAdapter()}
    //private lateinit var workoutListAdapter: WorkoutListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuildWorkoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title="New Workout"

        srAdapter = SearchResultsAdapter()
        binding.rvSearchResults.adapter = srAdapter
        binding.rvSearchResults.layoutManager = LinearLayoutManager(this)

        binding.btnX.setOnClickListener {
            val i = Intent(this@BuildWorkoutActivity, WorkoutListActivity::class.java)
            startActivity(i)
        }

        exerciseViewModel.allExercises.observe(this, {
                list -> list.let{
            srAdapter.setData(it)
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

    }


}

private fun SearchView.setOnCloseListener(onCloseListener: SearchView.OnCloseListener, function: () -> Unit) {

}

