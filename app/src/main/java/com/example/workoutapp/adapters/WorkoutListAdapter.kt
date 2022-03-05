package com.example.workoutapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.R
import com.example.workoutapp.WorkoutEditor
import com.example.workoutapp.WorkoutListActivity
import com.example.workoutapp.data.exercisedb.Exercise
import com.example.workoutapp.data.exercisedb.ExerciseViewModel
import com.example.workoutapp.data.exercisedb.Workout
import com.example.workoutapp.databinding.ActivityBuildWorkoutBinding
import com.example.workoutapp.databinding.WorkoutListingBinding


class WorkoutListAdapter(private val context: Context)
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
                binding.tvWorkoutListing.setOnClickListener {
                    val i = Intent(context,
                        WorkoutEditor::class.java)
                    i.putExtra("WID", workouts[position].WID)
                    startActivity(context, i, null)
                }
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