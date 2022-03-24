package com.example.workoutapp

import android.app.ActionBar
import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
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
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title="Homepage"
        //setTheme()
        getSupportActionBar()!!.hide()

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
                            setNextWorkoutText(p.workoutIDs[p.position])
                            binding.btnMyProgram.text = "My Program \n- " + p.title +" -"
                            binding.tvTotalLoad.text = "Program Cycle:\nWorkout ${p.position+1}/${p.workoutIDs.size}"
                            val progress: Float = (p.position).toFloat() / p.workoutIDs.size
                            binding.progressBar.apply {
                                progressMax = 100f
                                setProgressWithAnimation((progress*100),1000)
                                progressBarWidth = 6f
                                backgroundProgressBarWidth = 3f
                                //progressBarColor = Color.WHITE
                                progressBarColorDirection = CircularProgressBar.GradientDirection.LEFT_TO_RIGHT
                                progressBarColorStart = Color.parseColor("#b29456") //#c09a6b
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
                        binding.btnNextWorkout.text = "Start Next Workout! \n- " +
                                w.title + " -"
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