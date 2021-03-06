package com.sovereignfit.workoutapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sovereignfit.workoutapp.databinding.ActivityBuildWorkoutBinding
import com.sovereignfit.workoutapp.adapters.SearchResultsAdapter
import com.sovereignfit.workoutapp.adapters.WorkoutBuildAdapter
import com.sovereignfit.workoutapp.data.exercisedb.*

class BuildWorkoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuildWorkoutBinding
    private lateinit var srAdapter: SearchResultsAdapter
    private lateinit var wlAdapter: WorkoutBuildAdapter

    private val exerciseViewModel: ExerciseViewModel by viewModels {
        ExerciseViewModel.ExerciseViewModelFactory((application as MyApplication).repository)
    }

    //private val searchResultAdapter: SearchResultAdapter by lazy {SearchResultAdapter()}
    //private lateinit var workoutListAdapter: WorkoutListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //repress top bar
        getSupportActionBar()!!.hide()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.dark_grey))

        binding = ActivityBuildWorkoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        title="Workout Builder"

        wlAdapter = WorkoutBuildAdapter(this, exerciseViewModel)
        binding.rvBuildSpace.adapter = wlAdapter
        binding.rvBuildSpace.layoutManager = LinearLayoutManager(this)

        srAdapter = SearchResultsAdapter(this, wlAdapter, exerciseViewModel, this)
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

        binding.svBuild.setOnSearchClickListener {
            srAdapter.filter("")
            binding.rvSearchResults.visibility = View.VISIBLE }

        binding.svBuild.setOnQueryTextListener( object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                srAdapter.filter(query)
                hideKeyboard()
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
                hideKeyboard()
                return false
            }
        })

        //Save button pressed, display 'enter name' popup then save to database.
        binding.btnSave.setOnClickListener{
            if(wlAdapter.isNotEmpty()) {
                var title = binding.etTitle.text.toString()
                val workout = Workout(
                    title = title,
                    exercises = wlAdapter.getExerciseList(),
                    supersets = wlAdapter.getSupersets(),
                    notes = wlAdapter.getNotes()
                )
                exerciseViewModel.insertWorkout(workout)
                val i = Intent(this@BuildWorkoutActivity, WorkoutListActivity::class.java)
                startActivity(i)
            } else {
                val alertBuilder = AlertDialog.Builder(this)
                alertBuilder.setTitle("Invalid Workout")
                alertBuilder.setMessage("A workout is not a workout without exercises!")
                alertBuilder.show()
            }
        }

    }

    fun closeSearch(){
        println("\n\n\n CLOSING!!\n\n")
        binding.rvSearchResults.visibility = View.INVISIBLE
        binding.svBuild.setQuery("", false)
        binding.svBuild.setIconified(true)
        hideKeyboard()
    }

    fun Fragment.hideKeyboard() {
        view?.let {activity?.hideKeyboard(it)}
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


}

private fun SearchView.setOnCloseListener(onCloseListener: SearchView.OnCloseListener, function: () -> Unit) {

}

