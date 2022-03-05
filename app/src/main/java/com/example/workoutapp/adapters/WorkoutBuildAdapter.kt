package com.example.workoutapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.BuildWorkoutActivity
import com.example.workoutapp.data.exercisedb.Exercise
import com.example.workoutapp.data.exercisedb.ExerciseInstance
import com.example.workoutapp.data.exercisedb.Workout
import com.example.workoutapp.databinding.ExerciseListingBinding
import com.example.workoutapp.databinding.FragmentExerciseBinding
import kotlinx.coroutines.currentCoroutineContext
import kotlin.coroutines.coroutineContext

class WorkoutBuildAdapter(private val context : AppCompatActivity) : RecyclerView.Adapter<WorkoutBuildAdapter.WorkoutBuildViewHolder>() {

    data class Inst(
        val EI: ExerciseInstance,
        var adapter: SetBuildAdapter? = null
    )

    var list = mutableListOf<Inst>()

    class WorkoutBuildViewHolder(val binding: ExerciseListingBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viwType: Int): WorkoutBuildViewHolder {
        val binding = ExerciseListingBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutBuildViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutBuildAdapter.WorkoutBuildViewHolder, position: Int) {
        holder.binding.tvName.text = list[position].EI.exercise.name
        holder.binding.btnDeleteExercise.setOnClickListener {
            removeExerciseByPos(position)
        }
        var setAdapter = SetBuildAdapter()
        holder.binding.rvSets.adapter = setAdapter
        holder.binding.rvSets.layoutManager = LinearLayoutManager(context)
        for(set in list[position].EI.sets) {
            setAdapter.addSet()
        }
        holder.binding.btnAddSet.setOnClickListener {
            setAdapter.addSet()
        }
        list[position].adapter = setAdapter
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addExercise(ex: Exercise){
        var inst = Inst(ExerciseInstance(ex))
        list.add(inst)
        notifyItemInserted(list.size - 1)
    }

    fun removeExerciseByPos(position: Int){
        list.removeAt(position)
        notifyItemRemoved(position)
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
        var newList = mutableListOf<Inst>()
        println("Iterating through ${workout}")
        for(ex in workout.exercises) {
            var newInst = Inst(ex)
            newList.add(newInst)
        }
        println("Set workout to newList: $newList")
        list = newList
        notifyDataSetChanged()
    }

}