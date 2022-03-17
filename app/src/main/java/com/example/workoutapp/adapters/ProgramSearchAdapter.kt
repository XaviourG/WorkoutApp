package com.example.workoutapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.data.exercisedb.Exercise
import com.example.workoutapp.data.exercisedb.ExerciseViewModel
import com.example.workoutapp.data.exercisedb.Workout
import com.example.workoutapp.databinding.FragmentExerciseBinding

class ProgramSearchAdapter(private val programBuildAdapter: ProgramBuildAdapter,
                           private val viewModel: ExerciseViewModel
) : RecyclerView.Adapter<ProgramSearchAdapter.ProgramSearchViewHolder>() {

    var shownData = emptyList<Workout>()
    var fullData = emptyList<Workout>()
    class ProgramSearchViewHolder(val binding: FragmentExerciseBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viwType: Int): ProgramSearchViewHolder {
        val binding = FragmentExerciseBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ProgramSearchViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ProgramSearchViewHolder, position: Int) {
        holder.binding.tvExName.text = shownData[position].title
        holder.binding.tvExType.text = shownData[position].notes
        holder.binding.tvExName.setOnClickListener {
                programBuildAdapter.addWorkout(shownData[position])

        }
    }

    fun setData(newData: List<Workout>){
        shownData = newData
        fullData = newData
        notifyDataSetChanged()
    }

    fun filter (query : String){
        shownData = fullData.filter {query.lowercase() in it.title.lowercase()}
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return shownData.size
    }
}