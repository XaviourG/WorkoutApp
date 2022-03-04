package com.example.workoutapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.R
import com.example.workoutapp.data.exercisedb.Exercise
import com.example.workoutapp.data.exercisedb.Workout
import com.example.workoutapp.databinding.ActivityBuildWorkoutBinding
import com.example.workoutapp.databinding.WorkoutListingBinding


class WorkoutListAdapter
    : RecyclerView.Adapter<WorkoutListAdapter.WorkoutListViewHolder>() {

    private var workouts = listOf<Workout>()

    class WorkoutListViewHolder(val binding: WorkoutListingBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viwType: Int): WorkoutListViewHolder {
        val binding = WorkoutListingBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutListViewHolder(binding)
    }

    /*fun updateWorkouts(newList : MutableList<Workout>) {
        for(w in newList){
            if(w !in workouts) {
                workouts.add(w)
                notifyItemInserted(workouts.size - 1)
            }
        }

        println("WITHIN ADAPTER LIST UPDATED : $workouts")
    }

     */

    override fun onBindViewHolder(holder: WorkoutListViewHolder, position: Int) {
        with(holder) {
            with(workouts[position]) {
                binding.tvWorkoutListing.text = title
                //ADD CODE HERE TO FLESH OUT WORKOUT BOXES } }
            }
        }
    }

    override fun getItemCount(): Int {
        return workouts.size
    }

    fun setData(newData: List<Workout>){
        workouts = newData
        notifyDataSetChanged()
    }
}