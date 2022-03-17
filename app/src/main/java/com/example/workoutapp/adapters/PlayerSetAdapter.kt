package com.example.workoutapp.adapters

import android.graphics.Color
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.data.exercisedb.Exercise
import com.example.workoutapp.data.exercisedb.Log
import com.example.workoutapp.databinding.FragmentExerciseBinding
import com.example.workoutapp.databinding.FragmentPlayerSetBinding
import com.example.workoutapp.databinding.FragmentSetBinding

class PlayerSetAdapter : RecyclerView.Adapter<PlayerSetAdapter.PlayerSetViewHolder>() {

    private var sets = mutableListOf<String>()
    private var prevs = mutableListOf<String>()

    class PlayerSetViewHolder(val binding: FragmentPlayerSetBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viwType: Int): PlayerSetAdapter.PlayerSetViewHolder {
        val binding = FragmentPlayerSetBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerSetAdapter.PlayerSetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerSetAdapter.PlayerSetViewHolder, position: Int) {
        //Do nothing I suppose, we just want the empty fields.
        holder.binding.btnTick.setOnClickListener{
            holder.binding.etLoad.setTextColor(Color.GREEN)
            holder.binding.etReps.setTextColor(Color.GREEN)
            holder.binding.btnTick.setBackgroundColor(Color.GREEN)
            sets[position] = holder.binding.etLoad.text.toString() + ":" + holder.binding.etReps.text.toString() + ":" + "none"
        }
        holder.binding.tvPrevEx.text = prevs[position]

        var info = sets[position].split(":")
        if(info[0] == "0"){ // default value do nothing
        } else {
            holder.binding.etLoad.setText(info[0])
        }
        if(info[1] == "0"){ // default value do nothing
        } else {
            holder.binding.etReps.setText(info[1])
        }
        if(info[2] == "none"){ // default value do nothing
        } else {
            TODO() //implement modified set handling
        }
    }

    override fun getItemCount(): Int {
        return sets.size
    }

    fun addSet(set: String = "0:0:none") {
        //load.reps.modifier
        sets.add(set)
        prevs.add("--")
        notifyItemInserted(sets.size - 1)
    }

    fun rmSetByPos(position: Int) {
        sets.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getLog(): String {
        var s = ""
        for(l in sets) {
            if ((l == "0:0:none")) {
                //don't add
            } else {
                s = "$s$l|"
            }
        }
        return s
    }

    fun updatePrev(log: Log) {
        println("UPDATING PREVIOUS EXECUTIONS")
        var i = 0
        var performances = log.performance.split("|")
        performances = performances.subList(0, performances.size - 1) //always a "" on end.
        println("PRE CRASH: $prevs")
        for(p in performances){
            var bits = p.split(":")
            prevs[i] =
                bits[0] + "kg x" + bits[1]
            println("!!! Set ex:${i+1} to ${prevs[i]}")
            i++
        }
        notifyDataSetChanged()
    }
}