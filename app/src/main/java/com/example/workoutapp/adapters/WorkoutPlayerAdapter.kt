package com.example.workoutapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.data.exercisedb.Exercise
import com.example.workoutapp.data.exercisedb.ExerciseInstance
import com.example.workoutapp.data.exercisedb.Workout
import com.example.workoutapp.databinding.ExerciseListingBinding
import com.example.workoutapp.databinding.ExercisePlayerListingBinding

class WorkoutPlayerAdapter(private val context : AppCompatActivity) : RecyclerView.Adapter<WorkoutPlayerAdapter.WorkoutPlayerViewHolder>() {

    var list = mutableListOf<WorkoutBuildAdapter.Inst>()

    class WorkoutPlayerViewHolder(val binding: ExercisePlayerListingBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viwType: Int): WorkoutPlayerAdapter.WorkoutPlayerViewHolder {
        val binding = ExercisePlayerListingBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutPlayerAdapter.WorkoutPlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutPlayerAdapter.WorkoutPlayerViewHolder, position: Int) {
        holder.binding.tvName.text = list[position].EI.exercise.name

        var setAdapter = SetBuildAdapter()
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

    fun updateSets(){
        for(inst in list){
            inst.EI.sets = inst.adapter!!.getSets()
        }
    }

    fun getExerciseList(): List<ExerciseInstance> {
        return list.map {it.EI}
    }
    fun setWorkout(workout: Workout){
        var newList = mutableListOf<WorkoutBuildAdapter.Inst>()
        println("Iterating through $workout")
        for(ex in workout.exercises) {
            var newInst = WorkoutBuildAdapter.Inst(ex)
            newList.add(newInst)
        }
        println("Set workout to newList: $newList")
        list = newList
        notifyDataSetChanged()
    }

}