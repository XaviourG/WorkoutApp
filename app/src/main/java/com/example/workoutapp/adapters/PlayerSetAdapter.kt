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

class PlayerSetAdapter(private val context: Context, private val unit: Int) : RecyclerView.Adapter<PlayerSetAdapter.PlayerSetViewHolder>() {

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
        //Manage View
        var params = holder.itemView.layoutParams
        if(goals[position].split(":")[2] == "drop") { // show drop set
            params.height = 300
            holder.itemView.layoutParams = params
            hideEverything(holder.binding)
            showDropSet(holder.binding)
        } else { //show regular set
            params.height = 150
            holder.itemView.layoutParams = params
            hideEverything(holder.binding)
            showRegSet(holder.binding)
        }

        //setting units
        if(unit == 0){ //kg
            holder.binding.tvMid.setText("(kg)x")
            holder.binding.tvDropMid.setText("(kg)x")
        } else { //lbs
            holder.binding.tvMid.setText("(lbs)x")
            holder.binding.tvDropMid.setText("(lbs)x")
        }

        //Checkmark highlighting and log updating functionality
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
            } else if ((goals[position].split(":")[2] == "drop") and
                        ((holder.binding.etDropLoad.text.toString() == "") or
                        (holder.binding.etDropReps.text.toString() == ""))) {
                val alertBuilder = AlertDialog.Builder(context)
                alertBuilder.setTitle("Cannot Log")
                alertBuilder.setMessage("Dropset field empty.")
                alertBuilder.show()
            } else {
                holder.binding.etLoad.setTextColor(Color.WHITE)
                holder.binding.etLoad.setShadowLayer(10f, 0f, 0f, Color.BLACK)
                holder.binding.etReps.setTextColor(Color.WHITE)
                holder.binding.etReps.setShadowLayer(10f, 0f, 0f, Color.BLACK)
                holder.binding.btnTick.setTextColor(Color.WHITE)
                holder.binding.btnTick.setShadowLayer(10f, 0f, 0f, Color.BLACK)
                if(goals[position].split(":")[2] == "drop") { //dropset stuff
                    //highlight dropset stuff
                    holder.binding.etDropLoad.setTextColor(Color.WHITE)
                    holder.binding.etDropLoad.setShadowLayer(10f, 0f, 0f, Color.BLACK)
                    holder.binding.etDropReps.setTextColor(Color.WHITE)
                    holder.binding.etDropReps.setShadowLayer(10f, 0f, 0f, Color.BLACK)
                    //dropset values are stored load1+load2:reps1+reps2:drop
                    sets[position] =
                        holder.binding.etLoad.text.toString() + "+" +
                                holder.binding.etDropLoad.text.toString() + ":" +
                                holder.binding.etReps.text.toString() + "+" +
                                holder.binding.etDropReps.text.toString() + ":drop"
                } else {
                    sets[position] =
                        holder.binding.etLoad.text.toString() + ":" + holder.binding.etReps.text.toString() + ":" + "none"
                }
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
                holder.binding.etDropLoad.setTextColor(dark)
                holder.binding.etDropLoad.setShadowLayer(0f,0f,0f,Color.TRANSPARENT)
                holder.binding.etDropReps.setTextColor(dark)
                holder.binding.etDropReps.setShadowLayer(0f,0f,0f,Color.TRANSPARENT)
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
                holder.binding.etDropLoad.setTextColor(dark)
                holder.binding.etDropLoad.setShadowLayer(0f,0f,0f,Color.TRANSPARENT)
                holder.binding.etDropReps.setTextColor(dark)
                holder.binding.etDropReps.setShadowLayer(0f,0f,0f,Color.TRANSPARENT)
            }
        })
        holder.binding.etDropLoad.addTextChangedListener(object : TextWatcher {
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
                holder.binding.etDropLoad.setTextColor(dark)
                holder.binding.etDropLoad.setShadowLayer(0f,0f,0f,Color.TRANSPARENT)
                holder.binding.etDropReps.setTextColor(dark)
                holder.binding.etDropReps.setShadowLayer(0f,0f,0f,Color.TRANSPARENT)
            }
        })

        holder.binding.etDropReps.addTextChangedListener(object : TextWatcher {
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
                holder.binding.etDropLoad.setTextColor(dark)
                holder.binding.etDropLoad.setShadowLayer(0f,0f,0f,Color.TRANSPARENT)
                holder.binding.etDropReps.setTextColor(dark)
                holder.binding.etDropReps.setShadowLayer(0f,0f,0f,Color.TRANSPARENT)
            }
        })

        if(prevs[position] == "--"){//No existing log show goals
            var info = goals[position].split(":")
            if(info[0] == ""){ // default value do nothing
                holder.binding.etLoad.setHint("Load")
            } else {
                holder.binding.etLoad.setHint(info[0])
            }
            if(info[1] == ""){ // default value do nothing
                holder.binding.etReps.setHint("Reps")
            } else {
                holder.binding.etReps.setHint(info[1])
            }
        } else { //last performance exists, show that instead

            if(goals[position].contains("drop")){ // drop set
                val info = prevs[position].split("\n")
                val set1 = info[0].split("kg x")
                val set2 = info[1].split("kg x")
                holder.binding.etLoad.setHint(set1[0])
                holder.binding.etReps.setHint(set1[1])
                holder.binding.etDropLoad.setHint(set2[0])
                holder.binding.etDropReps.setHint(set2[1])
            } else {
                // default set
                val info = prevs[position].split("kg x")
                holder.binding.etLoad.setHint(info[0])
                if(info[1].contains("\n")) { //last exec was dropset now reg set
                    holder.binding.etReps.setHint(info[1].split("\n")[0])
                } else {
                    holder.binding.etReps.setHint(info[1])
                }
            }
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
            if ((l == "0:0:none") or (l == "0:0:drop")) {
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
            if(i == sets.size) { break } //workout edited to have less sets handling
            var bits = p.split(":")
            if (bits[0] == ""){}
            else if((p == "0:0:none") or (p == "0:0:drop")){
                prevs[i] = "--"
            }
            else {
                if(bits[2] == "drop") {
                    val loads = bits[0].toString().split("+")
                    val reps = bits[1].toString().split("+")
                    prevs[i] =
                        loads[0] + "kg x" + reps[0] + "\n" +
                                loads[1] + "kg x" + reps[1]
                } else {
                    prevs[i] =
                        bits[0] + "kg x" + bits[1]
                }
            }
            i++
        }
        notifyDataSetChanged()
    }
}
fun hideEverything(b: FragmentPlayerSetBinding) {
    //Hide Regular Stuff
    b.tvPrevEx.visibility = View.INVISIBLE
    b.etLoad.visibility = View.INVISIBLE
    b.tvMid.visibility = View.INVISIBLE
    b.etReps.visibility = View.INVISIBLE
    b.btnTick.visibility = View.INVISIBLE
    //Hide Dropset Stuff
    b.tvDS.visibility = View.INVISIBLE
    b.etDropLoad.visibility = View.INVISIBLE
    b.tvDropMid.visibility = View.INVISIBLE
    b.etDropReps.visibility = View.INVISIBLE
}

fun showRegSet(b: FragmentPlayerSetBinding) {
    b.tvPrevEx.visibility = View.VISIBLE
    b.etLoad.visibility = View.VISIBLE
    b.tvMid.visibility = View.VISIBLE
    b.etReps.visibility = View.VISIBLE
    b.btnTick.visibility = View.VISIBLE
}

fun showDropSet(b: FragmentPlayerSetBinding) {
    showRegSet(b)
    b.tvDS.visibility = View.VISIBLE
    b.etDropLoad.visibility = View.VISIBLE
    b.tvDropMid.visibility = View.VISIBLE
    b.etDropReps.visibility = View.VISIBLE
}