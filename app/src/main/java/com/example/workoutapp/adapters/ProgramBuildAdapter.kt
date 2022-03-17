package com.example.workoutapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.data.exercisedb.ExerciseViewModel
import com.example.workoutapp.data.exercisedb.Program
import com.example.workoutapp.data.exercisedb.Workout
import com.example.workoutapp.databinding.ExerciseListingBinding
import com.example.workoutapp.databinding.FragmentWorkoutListingEditableBinding

class ProgramBuildAdapter(private val context : AppCompatActivity,
                          private val exerciseViewModel: ExerciseViewModel)
    : RecyclerView.Adapter<ProgramBuildAdapter.ProgramBuildViewHolder>() {

    private var workouts = mutableListOf<Workout>()

    class ProgramBuildViewHolder(val binding: FragmentWorkoutListingEditableBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viwType: Int): ProgramBuildAdapter.ProgramBuildViewHolder {
        val binding = FragmentWorkoutListingEditableBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ProgramBuildAdapter.ProgramBuildViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProgramBuildAdapter.ProgramBuildViewHolder, position: Int) {
        holder.binding.tvTitle.text = workouts[position].title
        holder.binding.btnMoveUp.setOnLongClickListener {
            TODO()
        }
        holder.binding.btnMoveDown.setOnLongClickListener {
            TODO()
        }
        holder.binding.btnDelete.setOnLongClickListener {
            TODO()
        }
    }

    override fun getItemCount(): Int {
        return workouts.size
    }

    fun addWorkout(workout: Workout){
        workouts.add(workout)
        notifyItemInserted(workouts.size - 1)
    }

    fun removeWorkoutByPos(position: Int){
        workouts.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setProgram(program: Program){
        for(wid in program.workoutIDs){
            exerciseViewModel.allWorkouts.observe(context, {workoutList ->
            workoutList.let{
                for(w in it){
                    if(w.WID == wid){
                        workouts.add(w)
                        break
                    }
                }
            }
            })
        }
        notifyDataSetChanged()
    }

    fun getWorkoutIDs(): List<Int> {
        return workouts.map {it.WID!!}
    }
}