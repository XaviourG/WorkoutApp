package com.sovereignfit.workoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sovereignfit.workoutapp.adapters.WorkoutPlayerAdapter
import com.sovereignfit.workoutapp.data.exercisedb.*
import com.sovereignfit.workoutapp.databinding.ActivityWorkoutPlayerBinding
import kotlinx.coroutines.Job
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

class WorkoutPlayer : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutPlayerBinding
    private lateinit var wpAdapter: WorkoutPlayerAdapter
    private lateinit var workout: Workout
    private lateinit var program: Program

    private var timeElapsed = 0L
    private var timeHandler: Handler? = null
    private var timeStarted: Long = -1

    private var consistency: List<MutableList<String>?>? = null

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

        binding = ActivityWorkoutPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        wpAdapter = WorkoutPlayerAdapter(this, exerciseViewModel, this)
        binding.rvPlayer.adapter = wpAdapter
        binding.rvPlayer.layoutManager = LinearLayoutManager(this)

        workout = Workout(title="FakeWorkout",exercises=mutableListOf<ExerciseInstance>(),supersets= mutableListOf<String>(), notes = mutableListOf<String>())
        var wid = intent.getIntExtra("WID",1)
        exerciseViewModel.allWorkouts.observe(this, { list ->
            list.let {
                for (w in it) {
                    if (w.WID == wid) {
                        workout = w
                        wpAdapter.setWorkout(workout)
                        binding.tvTitle.setText(workout.title)
                        checkSessions(workout.WID!!)
                    }
                }
            }
        })

        binding.btnX.setOnClickListener {
            stopTimer()
            val i = Intent(this@WorkoutPlayer, MainActivity::class.java)
            i.putExtra("regress", true)
            startActivity(i)
        }

        binding.btnFin.setOnClickListener {
            //Logging functionality
            val elapsed = binding.tvTime.text.toString()
            println("Workout Completed in: $elapsed")
            stopTimer()
            var totalLoad = 0f
            for(l in wpAdapter.getLogs()){
                //Insert log into database
                exerciseViewModel.insertLog(l)
                //Calculate total load
                var exLoad = 0f
                println("\n \n CALCUALTING TOTAL LOAD OF ${l} \n \n")
                var execs = l.performance.split("|")
                for (ex in execs) {
                    if ((ex == "") or (ex.contains("::"))){
                    } else {
                        val bits = ex.split(":")
                        if(bits[2].contains("drop")) {
                            val loads = bits[0].split("+")
                            val reps = bits[1].split("+")
                            exLoad += ((loads[0].toFloat() * reps[0].toInt()) + (loads[1].toFloat() * reps[1].toInt()))
                        } else {
                            exLoad += (bits[0].toFloat() * bits[1].toInt())
                        }
                    }
                }
                //kg or lbs handing

                if(EIDisKG(l.exerciseID)){
                    totalLoad += exLoad
                } else { //is in lbs so
                    totalLoad += (exLoad / 2.205f)
                }
            }
            println("Total Volume: ${totalLoad}kg")

            //eventually make this go to a finished page
            val i = Intent(this@WorkoutPlayer, WorkoutSummary::class.java)
            i.putExtra("totalLoad", totalLoad)
            i.putExtra("timeElapsed", elapsed)
            i.putExtra("title", workout.title)
            //i.putExtra("PRs", mutableListOf<String>().toTypedArray())
            exerciseViewModel.deleteSession(Session(SID = 1, WID = workout.WID!!, sets = toSets(consistency), startTime = timeStarted))
            startActivity(i)
        }
        //Starting timer
        timeHandler = Handler(Looper.getMainLooper())
        mStatusChecker.run()

        //consistency handling for app switching


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

    fun EIDisKG(eid: Int): Boolean {
        for(ex in workout.exercises){
            if(ex.exercise.EID == eid) {
                return (ex.exercise.unit == 0)
            }
        }
        println("EXERCISE NOT FOUND IN WORKOUT ERROR (Toal Load Calc)")
        return true
    }

    fun updateConsistency(newConsistency: List<MutableList<String>?>){
        consistency = newConsistency
    }

    fun getConsistency(): List<MutableList<String>?> {
        return consistency!!
    }

    fun consistencyExists(): Boolean {
        return !(consistency == null)
    }

    fun checkSessions(WID: Int){
        //Check if any existing sessions have this Workout ID (has the app closed mid workout)
        exerciseViewModel.allSessions.observe(this, {sessions ->
            sessions.let {
                var sessionFound = false
                var startTime: Long = -1
                for (session in it) {
                    if(session.WID == WID) {
                        sessionFound = true
                        println("<<<<< FOUND SESSION >>>>>")
                        //Use this session
                        consistency = fromSets(session.sets)
                        println("${consistency}")
                        //TODO("Timer update code here"
                        startTime = session.startTime
                        timeStarted = session.startTime

                        //make sure the adapters know it
                        wpAdapter.forceConsistencyCheck()
                        break
                    }
                }
                if(sessionFound){
                   //update timer with startTime, with have start time in epoch seconds,
                       // so now in epoch minus that is time elapsed
                    val timeElapsedInSeconds = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - startTime
                    timeElapsed = timeElapsedInSeconds
                } else {
                    //set start time
                    timeStarted = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
                }
            }
        })
    }

    fun updateSession() {
        exerciseViewModel.insertSession(Session(SID = 1, WID = workout.WID!!, sets = toSets(consistency), startTime = timeStarted))
    }


}