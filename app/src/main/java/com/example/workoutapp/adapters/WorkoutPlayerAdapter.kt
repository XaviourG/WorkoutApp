package com.example.workoutapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.data.exercisedb.Exercise
import com.example.workoutapp.data.exercisedb.ExerciseInstance
import com.example.workoutapp.data.exercisedb.Log
import com.example.workoutapp.data.exercisedb.Workout
import com.example.workoutapp.databinding.ExerciseListingBinding
import com.example.workoutapp.databinding.ExercisePlayerListingBinding
import java.time.LocalDateTime

class WorkoutPlayerAdapter(private val context : AppCompatActivity) : RecyclerView.Adapter<WorkoutPlayerAdapter.WorkoutPlayerViewHolder>() {

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
        holder.binding.rvSets.adapter = setAdapter
        holder.binding.rvSets.layoutManager = LinearLayoutManager(context)
        for(set in list[position].EI.sets) {
            setAdapter.addSet()
        }

        list[position].adapter = setAdapter
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
        var logs = mutableListOf<Log>()
        for(ex in list){
            val setLog = ex.adapter!!.getLog()
            val l = Log(date=LocalDateTime.now().toString(),exerciseID = ex.EI.exercise.EID!!,load=setLog)
            logs.add(l)
        }
        return logs

        //to make this work we need a new set build adapter with ticks to lock in logs!
    }

}