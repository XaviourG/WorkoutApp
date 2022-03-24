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

class SetBuildAdapter(private val unit: Int) : RecyclerView.Adapter<SetBuildAdapter.SetBuildViewHolder>() {

    var sets = mutableListOf<Pair<String, SetBuildViewHolder?>>()
    class SetBuildViewHolder(val binding: FragmentSetBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viwType: Int): SetBuildAdapter.SetBuildViewHolder {
        val binding = FragmentSetBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SetBuildAdapter.SetBuildViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SetBuildAdapter.SetBuildViewHolder, position: Int) {
        //Do nothing I suppose, we just want the empty fields.
        sets[position] = Pair(sets[position].first, holder)
        holder.binding.btnDeleteSet.setOnClickListener{
            if(sets.size == 1){
                //If you remove all sets the damn thing crashes, pop up here saying
                // "Please remove exercise" or just automatically doing that maybe.
            } else {
                rmSetByPos(position)
            }
        }
        var info = sets[position].first.split(":")
        println("INFO == $info")
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

        //set correct unit kg/lbs
        if(unit == 0) {//set to kg
            holder.binding.tvTimes.setText("(kg)x")
        } else {//set to lbs
            holder.binding.tvTimes.setText("(lbs)x")
        }
    }

    override fun getItemCount(): Int {
        return sets.size
    }

    fun addSet(set: String = "0:0:none") {
        sets.add(Pair(set, null))
        notifyItemInserted(sets.size - 1)
    }

    fun rmSetByPos(position: Int) {
        sets.removeAt(position)
        notifyDataSetChanged()
    }

    fun getSets(): Array<String>{
        var setList = arrayListOf<String>()
        for(s in sets){
            if(s.second == null){}
            else {
                var set = s.second!!.binding.etLoad.text.toString() + ":" +
                        s.second!!.binding.etReps.text.toString() + ":" +
                        "none" // eventually modified set functionality
                setList.add(set)
            }
        }
        return setList.toTypedArray()
    }
}