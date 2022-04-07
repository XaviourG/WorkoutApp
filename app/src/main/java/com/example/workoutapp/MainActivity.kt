package com.example.workoutapp

import android.app.ActionBar
import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.workoutapp.data.exercisedb.ExerciseViewModel
import com.example.workoutapp.data.exercisedb.Program
import com.example.workoutapp.data.exercisedb.Workout
import com.example.workoutapp.databinding.ActivityBuildWorkoutBinding
import com.example.workoutapp.databinding.ActivityMainBinding
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var program: Program
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

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title="Homepage"
        //setTheme()

        binding.btnNextWorkout.setOnClickListener {
            if(program.title == "FakeProgram"){
            //No active program and hence no next workout. Redirect to workouts page.
                val intent = Intent(this@MainActivity, WorkoutListActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this@MainActivity, WorkoutPlayer::class.java)
                intent.putExtra("WID", program.workoutIDs[program.position])
                intent.putExtra("PID", program.PID)

                //Increment position
                if (program.position >= program.workoutIDs.size - 1) {
                    program.position = 0
                } else {
                    program.position++
                }
                exerciseViewModel.updateProgram(program)
                startActivity(intent)
            }
        }
        binding.btnWorkouts.setOnClickListener {
            val intent = Intent(this@MainActivity, WorkoutListActivity::class.java)
            startActivity(intent)
        }
        binding.btnMyProgram.setOnClickListener {
            val intent = Intent(this@MainActivity, ProgramActivity::class.java)
            startActivity(intent)
        }

        program = Program(title = "FakeProgram", workoutIDs = emptyList())
        exerciseViewModel.allPrograms.observe(this, {
                programList -> programList.let {
                    for(p in it){
                        if(p.active){
                            setProgram(p)
                            //If we are here as a result of a back arrow, regress position by 1.
                            if(intent.getBooleanExtra("regress", false)){
                                if (program.position <= 0) {
                                    program.position = program.workoutIDs.size-1
                                } else {
                                    program.position--
                                }
                            }
                            setNextWorkoutText(p.workoutIDs[p.position])
                            binding.tvProgram.text = "- ${p.title} -"
                            binding.tvTotalLoad.text = "Program Cycle:\nWorkout ${p.position+1}/${p.workoutIDs.size}"
                            val progress: Float = (p.position+1).toFloat() / p.workoutIDs.size
                            binding.progressBar.apply {
                                progressMax = 100f
                                setProgressWithAnimation((progress*100),1000)
                                progressBarWidth = 8f
                                backgroundProgressBarWidth = 4f
                                //progressBarColor = Color.WHITE
                                progressBarColorDirection = CircularProgressBar.GradientDirection.LEFT_TO_RIGHT
                                progressBarColorStart = Color.parseColor("#9615DB") //#c09a6b
                                progressBarColorEnd = Color.WHITE
                                startAngle = 45f

                            }

                            break
                        }
                    }
        }
        })

    }

    private fun setNextWorkoutText(wid: Int) {
        exerciseViewModel.allWorkouts.observe(this, {
            list -> list.let {
                for(w in it){
                    if(w.WID == wid){
                        binding.btnNextWorkout.text = "Start Next Workout!\n"
                        binding.tvNextWorkout.setText("- ${w.title} -")
                        break
                    }
                }
        }
        })
    }

    private fun setProgram(p: Program) {
        program = p
    }
}