package com.example.workoutapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.R
import com.example.workoutapp.data.exercisedb.Exercise
import com.example.workoutapp.data.exercisedb.Workout
import com.example.workoutapp.databinding.ActivityBuildWorkoutBinding
import com.example.workoutapp.databinding.FragmentExerciseBinding
import com.example.workoutapp.databinding.WorkoutListingBinding


class SearchResultsAdapter(private val wlAdapter: WorkoutBuildAdapter) : RecyclerView.Adapter<SearchResultsAdapter.SearchResultsViewHolder>() {

    var shownData = emptyList<Exercise>()
    var fullData = emptyList<Exercise>()

    class SearchResultsViewHolder(val binding: FragmentExerciseBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viwType: Int): SearchResultsViewHolder {
        val binding = FragmentExerciseBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultsViewHolder(binding)
    }

    /*
    fun updateWorkouts(newList : MutableList<Workout>) {
        for(w in newList){
            if(w !in workouts) {
                workouts.add(w)
                notifyItemInserted(workouts.size - 1)
            }
        }
    } */

    override fun onBindViewHolder(holder: SearchResultsViewHolder, position: Int) {
        holder.binding.tvExName.text = shownData[position].name
        holder.binding.tvExType.text = shownData[position].exType
        holder.binding.tvExName.setOnClickListener {
            wlAdapter.addExercise(shownData[position])
            //var name = shownData[position].name
            //println("Adding $name to workout!!!")
        }
    }

    fun setData(newData: List<Exercise>){
        shownData = newData
        fullData = newData
        notifyDataSetChanged()
    }

    fun filter (query : String){
        shownData = fullData.filter {query.lowercase() in it.name.lowercase()}
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return shownData.size
    }
}