package com.example.workoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log.e
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutapp.adapters.WorkoutPlayerAdapter
import com.example.workoutapp.data.exercisedb.*
import com.example.workoutapp.databinding.ActivityWorkoutEditorBinding
import com.example.workoutapp.databinding.ActivityWorkoutPlayerBinding
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class WorkoutPlayer : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutPlayerBinding
    private lateinit var wpAdapter: WorkoutPlayerAdapter
    private lateinit var workout: Workout
    private lateinit var program: Program

    private var timeElapsed = 0L
    private var timeHandler: Handler? = null

    private val exerciseViewModel: ExerciseViewModel by viewModels {
        ExerciseViewModel.ExerciseViewModelFactory((application as MyApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = ActivityWorkoutPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getSupportActionBar()!!.hide()

        wpAdapter = WorkoutPlayerAdapter(this, exerciseViewModel)
        binding.rvPlayer.adapter = wpAdapter
        binding.rvPlayer.layoutManager = LinearLayoutManager(this)

        workout = Workout(title="FakeWorkout",exercises=mutableListOf<ExerciseInstance>())
        var wid = intent.getIntExtra("WID",1)
        exerciseViewModel.allWorkouts.observe(this, { list ->
            list.let {
                for (w in it) {
                    if (w.WID == wid) {
                        workout = w
                        println("!!!!!!!!!!!! setting workout to: $w")
                        wpAdapter.setWorkout(workout)
                        binding.tvTitle.setText(workout.title)
                    }
                }
            }
        })

        binding.btnX.setOnClickListener {
            stopTimer()
            val i = Intent(this@WorkoutPlayer, MainActivity::class.java)
            startActivity(i)
        }

        binding.btnFin.setOnClickListener {
            //Logging functionality
            val elapsed = binding.tvTime.text.toString()
            println("Workout Completed in: $elapsed")
            stopTimer()
            for(l in wpAdapter.getLogs()){
                exerciseViewModel.insertLog(l)
            }

            //eventually make this go to a finished page
            val i = Intent(this@WorkoutPlayer, MainActivity::class.java)
            startActivity(i)
        }
        //Starting timer
        timeHandler = Handler(Looper.getMainLooper())
        mStatusChecker.run()

    }

    private var mStatusChecker: Runnable = object : Runnable {
        override fun run() {
            try {
                timeElapsed += 1
                android.util.Log.e("timeInSeconds", timeElapsed.toString())
                updateStopWatchView(timeElapsed)
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                timeHandler!!.postDelayed(this, 1000.toLong())
            }
        }
    }

    private fun updateStopWatchView(timeInSeconds: Long) {
        val formattedTime = getFormattedStopWatch((timeInSeconds * 1000))
        android.util.Log.e("formattedTime", formattedTime)
        binding?.tvTime.text = formattedTime
    }
    fun getFormattedStopWatch(ms: Long): String {
        var milliseconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        return "${if (hours < 10) "0" else ""}$hours:" +
                "${if (minutes < 10) "0" else ""}$minutes:" +
                "${if (seconds < 10) "0" else ""}$seconds"
    }

    private fun stopTimer() {
        timeHandler?.removeCallbacks(mStatusChecker)
    }



}