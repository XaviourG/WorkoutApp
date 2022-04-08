package com.sovereignfit.workoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sovereignfit.workoutapp.adapters.ProgramListAdapter
import com.sovereignfit.workoutapp.data.exercisedb.ExerciseViewModel
import com.sovereignfit.workoutapp.databinding.ActivityProgramBinding

class ProgramActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProgramBinding
    private lateinit var plAdapter: ProgramListAdapter
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