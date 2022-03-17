package com.example.workoutapp.adapters

import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.data.exercisedb.Exercise
import com.example.workoutapp.databinding.FragmentExerciseBinding
import com.example.workoutapp.databinding.FragmentSetBinding

class SetBuildAdapter : RecyclerView.Adapter<SetBuildAdapter.SetBuildViewHolder>() {

    var sets = mutableListOf<Int>()
    class SetBuildViewHolder(val binding: FragmentSetBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viwType: Int): SetBuildAdapter.SetBuildViewHolder {
        val binding = FragmentSetBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SetBuildAdapter.SetBuildViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SetBuildAdapter.SetBuildViewHolder, position: Int) {
        //Do nothing I suppose, we just want the empty fields.
        holder.binding.btnDeleteSet.setOnClickListener{
            if(sets.size == 1){
                //If you remove all sets the damn thing crashes, pop up here saying
                // "Please remove exercise" or just automatically doing that maybe.
            } else {
                rmSetByPos(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return sets.size
    }

    fun addSet() {
        sets.add(1)
        notifyItemInserted(sets.size - 1)
    }

    fun rmSetByPos(position: Int) {
        sets.removeAt(position)
        notifyDataSetChanged()
    }

    fun getSets(): Array<Int>{
        return sets.toTypedArray()
    }
}