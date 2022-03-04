package com.example.workoutapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.data.exercisedb.Exercise
import com.example.workoutapp.databinding.ExerciseListingBinding
import com.example.workoutapp.databinding.FragmentExerciseBinding

class WorkoutBuildAdapter : RecyclerView.Adapter<WorkoutBuildAdapter.WorkoutBuildViewHolder>() {

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
        //holder.binding.sets.text = shownData[position].exType

    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }

    fun addExercise(ex: Exercise){
        var exI = ExerciseInstance(ex)
        exerciseList.add(exI)
        notifyItemInserted(exerciseList.size - 1)
    }

}