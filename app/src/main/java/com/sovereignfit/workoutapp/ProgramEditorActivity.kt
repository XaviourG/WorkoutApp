package com.sovereignfit.workoutapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sovereignfit.workoutapp.adapters.ProgramBuildAdapter
import com.sovereignfit.workoutapp.adapters.ProgramSearchAdapter
import com.sovereignfit.workoutapp.data.exercisedb.ExerciseViewModel
import com.sovereignfit.workoutapp.data.exercisedb.Program
import com.sovereignfit.workoutapp.databinding.ActivityProgamEditorBinding

class ProgramEditorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProgamEditorBinding
    private lateinit var programSearchAdapter: ProgramSearchAdapter
    private lateinit var programBuildAdapter: ProgramBuildAdapter
    private lateinit var program: Program
    private var isNewProgram = false

    private val exerciseViewModel: ExerciseViewModel by viewModels {
        ExerciseViewModel.ExerciseViewModelFactory((application as MyApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //repress top bar
        getSupportActionBar()!!.hide()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.dark_grey))

        binding = ActivityProgamEditorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title = "Program Builder"

        binding.etTitle.hint = "Program Title"
        binding.etTitle.text.clear()

        programBuildAdapter = ProgramBuildAdapter(this, exerciseViewModel)
        binding.rvBuildSpace.adapter = programBuildAdapter
        binding.rvBuildSpace.layoutManager = LinearLayoutManager(this)

        programSearchAdapter = ProgramSearchAdapter(this, programBuildAdapter, exerciseViewModel, this)
        binding.rvSearchResults.adapter = programSearchAdapter
        binding.rvSearchResults.layoutManager = LinearLayoutManager(this)

        var pid = intent.getIntExtra("PID",-1)
        if (pid == -1) { //Creating a new program
            isNewProgram = true
        } else { //Editing an existing program
            exerciseViewModel.allPrograms.observe(this, {
                programList -> programList.let {
                    for(p in it){
                        if(p.PID == pid){
                            program = p
                            programBuildAdapter.setProgram(p)
                            binding.etTitle.setText(p.title)
                            binding.etDescription.setText(p.description)
                        }
                    }
            }
            })
        }

        binding.svBuild.setOnQueryTextListener( object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                programSearchAdapter.filter(query)
                hideKeyboard()
                return false
            }
            override fun onQueryTextChange(query: String): Boolean {
                binding.rvSearchResults.visibility = View.VISIBLE
                programSearchAdapter.filter(query)
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

        exerciseViewModel.allWorkouts.observe(this, {
            list -> list.let{
                programSearchAdapter.setData(it)
        }
        })

        binding.rvSearchResults.visibility = View.GONE

        binding.svBuild.setOnSearchClickListener {
            programSearchAdapter.filter("")
            binding.rvSearchResults.visibility = View.VISIBLE }

        binding.btnSave.setOnClickListener {
            if(programBuildAdapter.getWorkoutIDs().isEmpty()) {
                //Its an empty program, don't build it
                val alertBuilder = AlertDialog.Builder(this)
                alertBuilder.setTitle("Cannot Save!")
                alertBuilder.setMessage("Program is empty.")
                alertBuilder.show()
            } else {
                if (isNewProgram) {
                    exerciseViewModel.insertProgram(
                        Program(
                            title = binding.etTitle.text.toString(),
                            workoutIDs = programBuildAdapter.getWorkoutIDs(),
                            description = binding.etDescription.text.toString()
                        )
                    )
                } else {
                    exerciseViewModel.updateProgram(
                        Program(
                            PID = pid,
                            title = binding.etTitle.text.toString(),
                            workoutIDs = programBuildAdapter.getWorkoutIDs(),
                            description = binding.etDescription.text.toString(),
                            active = program.active,
                            position = 0
                        )
                    )
                }
                val i = Intent(this@ProgramEditorActivity, ProgramActivity::class.java)
                startActivity(i)
            }
        }
        binding.btnX.setOnClickListener {
            val i = Intent(this@ProgramEditorActivity, ProgramActivity::class.java)
            startActivity(i)
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