package com.example.workoutapp.adapters

import android.opengl.Visibility
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

        //Type View
        var params = holder.itemView.layoutParams
        params.height = 150
        holder.itemView.layoutParams = params
        hideEverything(holder.binding)
        showRegularSet(holder.binding)


        //Let type button reveal options and hide everything else
        holder.binding.btnType.setOnClickListener {
            hideEverything(holder.binding)
            showTypeOptions(holder.binding)
        }
        holder.binding.btnReg.setOnClickListener {
            params.height = 150
            holder.itemView.layoutParams = params
            hideEverything(holder.binding)
            showRegularSet(holder.binding)
            //Update set type
            var data = sets[position].first.split(":").toTypedArray()
            data[2] = "none"
            val newString = "${data[0]}:${data[1]}:${data[2]}"
            sets[position] = Pair(newString, holder)
        }
        holder.binding.btnDS.setOnClickListener {
            //Redo the view
            params.height = 300
            holder.itemView.layoutParams = params
            hideEverything(holder.binding)
            showDropset(holder.binding)
            //Update set type
            var data = sets[position].first.split(":").toTypedArray()
            data[2] = "drop"
            val newString = "${data[0]}:${data[1]}:${data[2]}"
            sets[position] = Pair(newString, holder)
        }
        holder.binding.btnMyo.setOnClickListener {
            params.height = 150
            holder.itemView.layoutParams = params
            hideEverything(holder.binding)
            showRegularSet(holder.binding)
            holder.binding.tvMyo.visibility = View.VISIBLE
            //Update set type
            var data = sets[position].first.split(":").toTypedArray()
            data[2] = "myo"
            val newString = "${data[0]}:${data[1]}:${data[2]}"
            sets[position] = Pair(newString, holder)
        }


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
        if(info[2] == "drop") { // drop set
            //Redo the view
            params.height = 300
            holder.itemView.layoutParams = params
            hideEverything(holder.binding)
            showDropset(holder.binding)
        } else if(info[2] == "myo") { //myo rep set
            holder.binding.tvMyo.visibility = View.VISIBLE
        } else{ // must be regular do nothing
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
                        s.first.split(":")[2] // eventually modified set functionality
                setList.add(set)
            }
        }
        return setList.toTypedArray()
    }
}

fun hideEverything(binding: FragmentSetBinding) {
    binding.btnReg.visibility = View.INVISIBLE
    binding.btnSS.visibility = View.INVISIBLE
    binding.btnDS.visibility = View.INVISIBLE
    binding.btnMyo.visibility = View.INVISIBLE
    binding.etDropLoad.visibility = View.INVISIBLE
    binding.etDropReps.visibility = View.INVISIBLE
    binding.tvDropMid.visibility = View.INVISIBLE
    binding.tvDS.visibility = View.INVISIBLE
    binding.tvTimes.visibility = View.INVISIBLE
    binding.etLoad.visibility = View.INVISIBLE
    binding.etReps.visibility = View.INVISIBLE
    binding.btnType.visibility = View.INVISIBLE
    binding.btnDeleteSet.visibility = View.INVISIBLE
    binding.tvMyo.visibility = View.INVISIBLE
}

fun showTypeOptions(binding: FragmentSetBinding) {
    binding.btnReg.visibility = View.VISIBLE
    binding.btnSS.visibility = View.VISIBLE
    binding.btnDS.visibility = View.VISIBLE
    binding.btnMyo.visibility = View.VISIBLE
}

fun showRegularSet(binding: FragmentSetBinding) {
    //Standard Stuff
    binding.tvTimes.visibility = View.VISIBLE
    binding.etLoad.visibility = View.VISIBLE
    binding.etReps.visibility = View.VISIBLE
    binding.btnType.visibility = View.VISIBLE
    binding.btnDeleteSet.visibility = View.VISIBLE
}

fun showDropset(binding: FragmentSetBinding) {
    //Dropset specific stuff
    binding.etDropLoad.visibility = View.VISIBLE
    binding.etDropReps.visibility = View.VISIBLE
    binding.tvDropMid.visibility = View.VISIBLE
    binding.tvDS.visibility = View.VISIBLE
    //Standard Stuff
    showRegularSet(binding)
}