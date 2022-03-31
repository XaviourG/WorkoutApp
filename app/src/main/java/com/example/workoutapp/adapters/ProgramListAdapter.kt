package com.example.workoutapp.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.ProgramEditorActivity
import com.example.workoutapp.R
import com.example.workoutapp.WorkoutEditor
import com.example.workoutapp.data.exercisedb.ExerciseViewModel
import com.example.workoutapp.data.exercisedb.Program
import com.example.workoutapp.data.exercisedb.Workout
import com.example.workoutapp.databinding.ActivityProgramBinding
import com.example.workoutapp.databinding.FragmentProgramListingBinding
import com.example.workoutapp.databinding.WorkoutListingPopupBinding

class ProgramListAdapter (private val context: Context, private val exerciseViewModel: ExerciseViewModel)
    : RecyclerView.Adapter<ProgramListAdapter.ProgramListViewHolder>() {

    private var programs = mutableListOf<Program>()

    class ProgramListViewHolder(val binding: FragmentProgramListingBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viwType: Int): ProgramListViewHolder {
        val binding = FragmentProgramListingBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ProgramListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProgramListAdapter.ProgramListViewHolder, position: Int) {
        holder.binding.tvProgramTitle.text = programs[position].title
        holder.binding.tvDescription.text = programs[position].description

        if(programs[position].active){
            holder.binding.tvProgramTitle.setTextColor(context.getColor(R.color.hot_pink))
            holder.itemView.setBackgroundResource(R.drawable.item_border_highlight)
        } else {
            holder.binding.tvProgramTitle.setTextColor(context.getColor(R.color.dark_grey))
            holder.itemView.setBackgroundResource(R.drawable.item_border)
        }

        //Popup
        holder.binding.tvProgramTitle.setOnClickListener {
            val popup = PopupWindow(context)
            val popupBinding = WorkoutListingPopupBinding.inflate(LayoutInflater.from(context))
            popupBinding.btnStart.text = "Activate"
            popup.contentView = popupBinding.root

            popupBinding.btnCancel.setOnClickListener {
                popup.dismiss()
            }
            popupBinding.btnStart.setOnClickListener {
                //de-activate the active program (if it exists)
                for(p in programs){
                    if(p.active){
                        p.active = false
                        exerciseViewModel.updateProgram(p)
                        break
                    }
                }
                //set this program to active
                programs[position].active = true
                println("Activated: ${programs[position]}")
                //update database
                exerciseViewModel.updateProgram(programs[position])
                notifyDataSetChanged()
                popup.dismiss() //replace with setProgram functionality
            }
            popupBinding.btnEdit.setOnClickListener {
                //edit program
                val i = Intent(context, ProgramEditorActivity::class.java)
                i.putExtra("PID", programs[position].PID)
                startActivity(context, i, null)
            }
            popup.showAtLocation(popupBinding.root, Gravity.CENTER, 0, 0)
        }
    }

    override fun getItemCount(): Int {
        return programs.size
    }

    fun setData(newData: MutableList<Program>){
        programs = newData
        notifyDataSetChanged()
    }
}