package com.example.workoutapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.MyApplication
import androidx.activity.viewModels
import com.example.workoutapp.data.exercisedb.*
import com.example.workoutapp.databinding.ExerciseListingBinding
import com.example.workoutapp.databinding.ExercisePlayerListingBinding
import java.time.LocalDateTime

class WorkoutPlayerAdapter(private val context : AppCompatActivity, private val exerciseViewModel: ExerciseViewModel) : RecyclerView.Adapter<WorkoutPlayerAdapter.WorkoutPlayerViewHolder>() {

    data class ModifiedInst(
        val EI: ExerciseInstance,
        var adapter: PlayerSetAdapter? = null
    )

    var list = mutableListOf<ModifiedInst>()

    class WorkoutPlayerViewHolder(val binding: ExercisePlayerListingBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viwType: Int): WorkoutPlayerViewHolder {
        val binding = ExercisePlayerListingBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutPlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutPlayerAdapter.WorkoutPlayerViewHolder, position: Int) {
        println("!! CREATING ${list[position]} in Recycler view!")
        holder.binding.tvName.text = list[position].EI.exercise.name

        var setAdapter = PlayerSetAdapter()

        val eid = list[position].EI.exercise.EID
        var previousExecution: Log? = null

        holder.binding.rvSets.adapter = setAdapter
        holder.binding.rvSets.layoutManager = LinearLayoutManager(context)
        for(set in list[position].EI.sets) {
            setAdapter.addSet(set)
        }

        list[position].adapter = setAdapter

        exerciseViewModel.allLogs.observe(context, {list ->
            list.let {
                for(l in it) {
                    if (l.exerciseID == eid) {
                        previousExecution = l
                        println("Found previous execution: $previousExecution")
                        setAdapter.updatePrev(previousExecution!!)
                        break
                    }
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getExerciseList(): List<ExerciseInstance> {
        return list.map {it.EI}
    }
    fun setWorkout(workout: Workout){
        var newList = mutableListOf<ModifiedInst>()
        println("Iterating through $workout")
        for(ex in workout.exercises) {
            var newInst = ModifiedInst(ex)
            newList.add(newInst)
        }
        /*
        var i=0
        for(inst in newList){
            list.add(inst)
            notifyItemInserted(i)
            i++
        }
        */


        //*
        list = newList
        println("!!! Recycler View Updated to: $newList")
        notifyDataSetChanged()
        //*/
    }

    fun getLogs(): MutableList<Log> {
        println("$list")
        var logs = mutableListOf<Log>()
        for(ex in list){
            println("$$$$ Iterating through: $ex")
            if(ex.adapter == null){ //skip
            } else {
                val setLog = ex.adapter!!.getLog()
                if (setLog == null || setLog == "") {
                } else {
                    val l = Log(
                        date = LocalDateTime.now().toString(),
                        exerciseID = ex.EI.exercise.EID!!,
                        performance = setLog
                    )
                    logs.add(l)
                }
            }
        }
        return logs

        //to make this work we need a new set build adapter with ticks to lock in logs!
    }

}