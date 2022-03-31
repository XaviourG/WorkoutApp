package com.example.workoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutapp.adapters.ProgramListAdapter
import com.example.workoutapp.data.exercisedb.ExerciseViewModel
import com.example.workoutapp.databinding.ActivityProgramBinding

class ProgramActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProgramBinding
    private lateinit var plAdapter: ProgramListAdapter
    private val exerciseViewModel: ExerciseViewModel by viewModels {
        ExerciseViewModel.ExerciseViewModelFactory((application as MyApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //repress top bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        getSupportActionBar()!!.hide()

        binding = ActivityProgramBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title="My Program"

        binding.btnHome.setOnClickListener {
            val i = Intent(this@ProgramActivity, MainActivity::class.java)
            startActivity(i)
        }

        plAdapter = ProgramListAdapter(this, exerciseViewModel)
        binding.rvProgramList.adapter = plAdapter
        binding.rvProgramList.layoutManager = LinearLayoutManager(this)
        exerciseViewModel.allPrograms.observe(this, {programList ->
            programList.let {
                plAdapter.setData(it)
            }
        })

        binding.btnNewProgram.setOnClickListener {
            val i = Intent(this@ProgramActivity, ProgramEditorActivity::class.java)
            startActivity(i)
        }
    }
}