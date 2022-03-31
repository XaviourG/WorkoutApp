package com.example.workoutapp.adapters

import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.PopupWindow
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.R
import com.example.workoutapp.WorkoutEditor
import com.example.workoutapp.WorkoutListActivity
import com.example.workoutapp.WorkoutPlayer
import com.example.workoutapp.data.exercisedb.Exercise
import com.example.workoutapp.data.exercisedb.ExerciseViewModel
import com.example.workoutapp.data.exercisedb.Workout
import com.example.workoutapp.databinding.ActivityBuildWorkoutBinding
import com.example.workoutapp.databinding.WorkoutListingBinding
import com.example.workoutapp.databinding.WorkoutListingPopupBinding


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
                    holder.binding.tvWorkoutListing.setTextColor(context.getColor(R.color.hot_pink))
                    holder.binding.tvWorkoutListing.setBackgroundResource(R.drawable.item_border_highlight)
                    val popup = PopupWindow(context)
                    val binding2 = WorkoutListingPopupBinding.inflate(LayoutInflater.from(context))
                    //val view = LayoutInflater.from(context).inflate(R.layout.workout_listing_popup, null)
                    popup.contentView = binding2.root
                    binding2.btnCancel.setOnClickListener{
                        holder.binding.tvWorkoutListing.setTextColor(context.getColor(R.color.dark_grey))
                        holder.binding.tvWorkoutListing.setBackgroundResource(R.drawable.item_border)
                        popup.dismiss() }
                    binding2.btnEdit.setOnClickListener{
                        val i = Intent(context,
                            WorkoutEditor::class.java)
                        i.putExtra("WID", workouts[position].WID)
                        startActivity(context, i, null)
                    }
                    binding2.btnStart.setOnClickListener{
                        //start workout player for this workout
                        val i = Intent(context, WorkoutPlayer::class.java)
                        i.putExtra("WID", workouts[position].WID)
                        startActivity(context, i, null)
                    }
                    popup.showAtLocation(binding2.root, Gravity.CENTER, 0, 0)

                    /*
                    val i = Intent(context,
                        WorkoutEditor::class.java)
                    i.putExtra("WID", workouts[position].WID)
                    startActivity(context, i, null)

                     */
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