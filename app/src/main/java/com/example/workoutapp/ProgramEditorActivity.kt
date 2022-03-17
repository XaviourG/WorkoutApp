package com.example.workoutapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutapp.adapters.ProgramBuildAdapter
import com.example.workoutapp.adapters.ProgramSearchAdapter
import com.example.workoutapp.data.exercisedb.ExerciseViewModel
import com.example.workoutapp.data.exercisedb.Program
import com.example.workoutapp.databinding.ActivityBuildWorkoutBinding
import com.example.workoutapp.databinding.ActivityProgamEditorBinding
import java.util.function.ToDoubleBiFunction

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
        binding = ActivityProgamEditorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title = "Program Builder"

        binding.etTitle.hint = "Program Title"
        binding.etTitle.text.clear()

        programBuildAdapter = ProgramBuildAdapter(this, exerciseViewModel)
        binding.rvBuildSpace.adapter = programBuildAdapter
        binding.rvBuildSpace.layoutManager = LinearLayoutManager(this)

        programSearchAdapter = ProgramSearchAdapter(programBuildAdapter, exerciseViewModel)
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
                return false
            }
        })

        exerciseViewModel.allWorkouts.observe(this, {
            list -> list.let{
                programSearchAdapter.setData(it)
        }
        })

        binding.rvSearchResults.visibility = View.GONE

        binding.btnSave.setOnClickListener {
            if(isNewProgram) {
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
                        position = program.position
                    )
                )
            }
            val i = Intent(this@ProgramEditorActivity, ProgramActivity::class.java)
            startActivity(i)
        }
        binding.btnX.setOnClickListener {
            val i = Intent(this@ProgramEditorActivity, ProgramActivity::class.java)
            startActivity(i)
        }


    }

}