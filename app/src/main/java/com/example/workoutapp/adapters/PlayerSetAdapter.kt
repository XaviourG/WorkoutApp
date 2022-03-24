package com.example.workoutapp.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.R
import com.example.workoutapp.data.exercisedb.Exercise
import com.example.workoutapp.data.exercisedb.Log
import com.example.workoutapp.databinding.FragmentExerciseBinding
import com.example.workoutapp.databinding.FragmentPlayerSetBinding
import com.example.workoutapp.databinding.FragmentSetBinding

class PlayerSetAdapter(private val context: Context) : RecyclerView.Adapter<PlayerSetAdapter.PlayerSetViewHolder>() {

    private var sets = mutableListOf<String>()
    private var prevs = mutableListOf<String>()
    private var goals = mutableListOf<String>()

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
            if(holder.binding.etLoad.text.toString() == ""){ //can't log
                val alertBuilder = AlertDialog.Builder(context)
                alertBuilder.setTitle("Cannot Log")
                alertBuilder.setMessage("Load field is empty.")
                alertBuilder.show()
            } else if (holder.binding.etReps.text.toString() == "") {
                val alertBuilder = AlertDialog.Builder(context)
                alertBuilder.setTitle("Cannot Log")
                alertBuilder.setMessage("Reps field is empty.")
                alertBuilder.show()
            } else {
                holder.binding.etLoad.setTextColor(Color.WHITE)
                holder.binding.etLoad.setShadowLayer(10f, 0f, 0f, Color.BLACK)
                holder.binding.etReps.setTextColor(Color.WHITE)
                holder.binding.etReps.setShadowLayer(10f, 0f, 0f, Color.BLACK)
                holder.binding.btnTick.setTextColor(Color.WHITE)
                holder.binding.btnTick.setShadowLayer(10f, 0f, 0f, Color.BLACK)
                sets[position] =
                    holder.binding.etLoad.text.toString() + ":" + holder.binding.etReps.text.toString() + ":" + "none"
            }
        }
        holder.binding.tvPrevEx.text = prevs[position]

        val dark = ContextCompat.getColor(context, R.color.dark)
        holder.binding.etLoad.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Do nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Do nothing
            }

            override fun afterTextChanged(p0: Editable?) {
                //If text is at all edited, unhighlight it.
                holder.binding.etLoad.setTextColor(dark)
                holder.binding.etLoad.setShadowLayer(0f,0f,0f,Color.TRANSPARENT)
                holder.binding.etReps.setTextColor(dark)
                holder.binding.etReps.setShadowLayer(0f,0f,0f,Color.TRANSPARENT)
                holder.binding.btnTick.setTextColor(dark)
                holder.binding.btnTick.setShadowLayer(0f,0f,0f,Color.TRANSPARENT)
            }
        })

        holder.binding.etReps.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Do nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Do nothing
            }

            override fun afterTextChanged(p0: Editable?) {
                //If text is at all edited, unhighlight it.
                holder.binding.etLoad.setTextColor(dark)
                holder.binding.etLoad.setShadowLayer(0f,0f,0f,Color.TRANSPARENT)
                holder.binding.etReps.setTextColor(dark)
                holder.binding.etReps.setShadowLayer(0f,0f,0f,Color.TRANSPARENT)
                holder.binding.btnTick.setTextColor(dark)
                holder.binding.btnTick.setShadowLayer(0f,0f,0f,Color.TRANSPARENT)
            }
        })

        if(prevs[position] == "--"){//No existing log show goals
            var info = goals[position].split(":")
            if(info[0] == "0"){ // default value do nothing
                holder.binding.etLoad.setHint("?")
            } else {
                holder.binding.etLoad.setHint(info[0])
            }
            if(info[1] == "0"){ // default value do nothing
                holder.binding.etReps.setHint("?")
            } else {
                holder.binding.etReps.setHint(info[1])
            }
        } else { //last performance exists, show that instead
            var info = prevs[position].split("kg x")
            holder.binding.etLoad.setHint(info[0])
            holder.binding.etReps.setHint(info[1])
        }
        if(sets[position].split(":")[2] == "none"){ // default value do nothing
        } else {
            TODO() //implement modified set handling
        }
    }

    override fun getItemCount(): Int {
        return sets.size
    }

    fun addSet(goal: String = "0:0:none") {
        //load.reps.modifier
        sets.add("0:0:none") //alter none to reflect type
        prevs.add("--")
        goals.add(goal)
        notifyItemInserted(sets.size - 1)
    }

    fun rmSetByPos(position: Int) {
        sets.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getLog(): String {
        var s = ""
        var logPresent = false
        for(l in sets) {
            if ((l == "0:0:none")) {
                //don't add
            } else {
                logPresent = true
            }
            s = "$s$l|"
        }

        if(logPresent){ return s}
        else{return ""}
    }

    fun updatePrev(log: Log) {
        var i = 0
        var performances = log.performance.split("|")
        performances = performances.subList(0, performances.size - 1) //always a "" on end.
        for(p in performances){
            var bits = p.split(":")
            if (bits[0] == ""){}
            else if(p == "0:0:none"){
                prevs[i] = "--"
            }
            else {
                prevs[i] =
                    bits[0] + "kg x" + bits[1]
            }
            i++
        }
        notifyDataSetChanged()
    }
}