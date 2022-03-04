package com.example.workoutapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.BuildWorkoutActivity
import com.example.workoutapp.data.exercisedb.Exercise
import com.example.workoutapp.databinding.ExerciseListingBinding
import com.example.workoutapp.databinding.FragmentExerciseBinding
import kotlinx.coroutines.currentCoroutineContext
import kotlin.coroutines.coroutineContext

class WorkoutBuildAdapter(private val context : BuildWorkoutActivity) : RecyclerView.Adapter<WorkoutBuildAdapter.WorkoutBuildViewHolder>() {

    data class ExerciseInstance(
        val exercise: Exercise,
        var sets: Array<Int> = arrayOf(1)
    )

    var exerciseList = mutableListOf<ExerciseInstance>()

    class WorkoutBuildViewHolder(val binding: ExerciseListingBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viwType: Int): WorkoutBuildViewHolder {
        val binding = ExerciseListingBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutBuildViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutBuildAdapter.WorkoutBuildViewHolder, position: Int) {
        holder.binding.tvName.text = exerciseList[position].exercise.name
        holder.binding.btnDeleteExercise.setOnClickListener {
            removeExerciseByPos(position)
        }
        var setAdapter = SetBuildAdapter()
        holder.binding.rvSets.adapter = setAdapter
        holder.binding.rvSets.layoutManager = LinearLayoutManager(context)
        setAdapter.addSet()
        holder.binding.btnAddSet.setOnClickListener {
            setAdapter.addSet()
        }
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }

    fun addExercise(ex: Exercise){
        var exI = ExerciseInstance(ex)
        exerciseList.add(exI)
        notifyItemInserted(exerciseList.size - 1)
    }

    fun removeExerciseByPos(position: Int){
        exerciseList.removeAt(position)
        notifyItemRemoved(position)
    }

}