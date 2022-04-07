package com.example.workoutapp.adapters

import android.opengl.Visibility
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.data.exercisedb.Exercise
import com.example.workoutapp.data.exercisedb.Workout
import com.example.workoutapp.databinding.FragmentExerciseBinding
import com.example.workoutapp.databinding.FragmentSetBinding

class SetBuildAdapter(private val unit: Int, private val wba: WorkoutBuildAdapter) : RecyclerView.Adapter<SetBuildAdapter.SetBuildViewHolder>() {

    class SetBuildViewHolder(val binding: FragmentSetBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viwType: Int): SetBuildAdapter.SetBuildViewHolder {
        val binding = FragmentSetBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SetBuildAdapter.SetBuildViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SetBuildAdapter.SetBuildViewHolder, position: Int) {
        holder.setIsRecyclable(false) //Disable Recycling
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
        //Selecting non-drop set, update wba sets
        holder.binding.btnReg.setOnClickListener {
            params.height = 150
            holder.itemView.layoutParams = params
            hideEverything(holder.binding)
            showRegularSet(holder.binding)
            //Update set type
            var sets = wba.getSetsByAdapter(this)
            var data = sets[position].split(":").toTypedArray()
            data[2] = "none"
            val newString = "${data[0]}:${data[1]}:${data[2]}"
            sets[position] = newString
            wba.updateSetsByAdapter(sets, this)
            //need to tell the view holder the data sets changed
            notifyDataSetChanged()
        }
        //Selecting Drop Set, update wba sets
        holder.binding.btnDS.setOnClickListener {
            //Redo the view
            params.height = 270
            holder.itemView.layoutParams = params
            hideEverything(holder.binding)
            showDropset(holder.binding)
            //Update set type
            var sets = wba.getSetsByAdapter(this)
            var data = sets[position].split(":").toTypedArray()
            data[2] = "drop"
            val newString = "${data[0]}:${data[1]}:${data[2]}"
            sets[position] = newString
            wba.updateSetsByAdapter(sets, this)
            //need to tell the view holder the data sets changed
            notifyDataSetChanged()
        }
        //Deleting a Set, update wba
        holder.binding.btnDeleteSet.setOnClickListener{
            var sets = wba.getSetsByAdapter( this)
            if(sets.size == 1){
                //If you remove all sets the damn thing crashes, pop up here saying
                // "Please remove exercise" or just automatically doing that maybe.
            } else {
                sets.removeAt(position)
                wba.updateSetsByAdapter(sets, this)
                notifyDataSetChanged()
            }
        }

        //Update set fields from wba sets
        var sets = wba.getSetsByAdapter(this)
        var info = sets[position].split(":")
        if(info[2] == "drop") { // drop set
            //Redo the view
            params.height = 300
            holder.itemView.layoutParams = params
            hideEverything(holder.binding)
            showDropset(holder.binding)
            //set the fields
            if(info[0].contains("+")) {
                val loads = info[0].split("+")
                if(loads[0] == ""){ // default value do nothing
                } else {
                    holder.binding.etLoad.setText(loads[0])
                }
                if(loads[1] == ""){ // default value do nothing
                } else {
                    holder.binding.etDropLoad.setText(loads[1])
                }
            }
            if(info[1].contains("+")) {
                val reps = info[1].split("+")
                if(reps[0] == ""){ // default value do nothing
                } else {
                    holder.binding.etReps.setText(reps[0])
                }
                if(reps[1] == ""){ // default value do nothing
                } else {
                    holder.binding.etDropReps.setText(reps[1])
                }
            }


        } else{ // must be regular
            if(info[0] == ""){ // default value do nothing
            } else {
                holder.binding.etLoad.setText(info[0])
            }
            if(info[1] == ""){ // default value do nothing
            } else {
                holder.binding.etReps.setText(info[1])
            }
        }

        //set correct unit kg/lbs
        if(unit == 0) {//set to kg
            holder.binding.tvTimes.setText("(kg)x")
        } else {//set to lbs
            holder.binding.tvTimes.setText("(lbs)x")
        }

        //Set edit text listeners
        val isDropSet = (wba.getSetsByAdapter(this)[position].split(":")[2] == "drop")
        holder.binding.etLoad.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //do nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //do nothing
            }

            override fun afterTextChanged(p0: Editable?) {
                //update wba sets
                println("Updating LOAD!!!!")
                var sets = wba.getSetsByAdapter(this@SetBuildAdapter)
                println("GOT >> ${sets[position]}")
                var data = sets[position].split(":")
                val load = holder.binding.etLoad.text.toString()
                if(!isDropSet) { //regular set format as such
                    sets[position] = "${load}:${data[1]}:${data[2]}"
                } else { //drop set must format properly
                    var dropLoad = ""
                    if(data[0].contains("+")) {
                        dropLoad = data[0].split("+")[1]
                    }
                    sets[position] = "${load}+${dropLoad}:${data[1]}:${data[2]}"
                }
                println("UPDATED TOO >> ${sets[position]}")
                wba.updateSetsByAdapter(sets, this@SetBuildAdapter)
            }

        })
        holder.binding.etReps.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //do nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //do nothing
            }

            override fun afterTextChanged(p0: Editable?) {
                //update wba sets
                var sets = wba.getSetsByAdapter(this@SetBuildAdapter)
                var data = sets[position].split(":")
                val reps = holder.binding.etReps.text.toString()
                if(!isDropSet) { //regular set format as such
                    sets[position] = "${data[0]}:${reps}:${data[2]}"
                } else { //drop set must format properly
                    var dropReps = ""
                    if(data[1].contains("+")) {
                        dropReps = data[1].split("+")[1]
                    }
                    sets[position] = "${data[0]}:${reps}+${dropReps}:${data[2]}"
                }
                wba.updateSetsByAdapter(sets, this@SetBuildAdapter)
            }

        })
        if(isDropSet) { //set dropset listeners
            holder.binding.etDropLoad.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //do nothing
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //do nothing
                }

                override fun afterTextChanged(p0: Editable?) {
                    //update wba sets
                    var sets = wba.getSetsByAdapter(this@SetBuildAdapter)
                    var data = sets[position].split(":")
                    val dropLoad = holder.binding.etDropLoad.text.toString()
                    val load = data[0].split("+")[0]
                    sets[position] = "${load}+${dropLoad}:${data[1]}:${data[2]}"
                    wba.updateSetsByAdapter(sets, this@SetBuildAdapter)
                }

            })
            holder.binding.etDropReps.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //do nothing
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //do nothing
                }

                override fun afterTextChanged(p0: Editable?) {
                    //update wba sets
                    var sets = wba.getSetsByAdapter(this@SetBuildAdapter)
                    var data = sets[position].split(":")
                    val dropReps = holder.binding.etDropReps.text.toString()
                    val reps = data[1].split("+")[0]
                    sets[position] = "${data[0]}:${reps}+${dropReps}:${data[2]}"
                    wba.updateSetsByAdapter(sets, this@SetBuildAdapter)
                }

            })
        }
    }

    override fun getItemCount(): Int {
        return wba.getSetsByAdapter(this).size
    }

    fun addSet(set: String = "::none") {
        var sets = wba.getSetsByAdapter(this)
        sets.add(set)
        wba.updateSetsByAdapter(sets, this)
        notifyItemInserted(sets.size - 1)
    }
}

fun hideEverything(binding: FragmentSetBinding) {
    binding.btnReg.visibility = View.INVISIBLE
    binding.btnDS.visibility = View.INVISIBLE
    binding.etDropLoad.visibility = View.INVISIBLE
    binding.etDropReps.visibility = View.INVISIBLE
    binding.tvDropMid.visibility = View.INVISIBLE
    binding.tvDS.visibility = View.INVISIBLE
    binding.tvTimes.visibility = View.INVISIBLE
    binding.etLoad.visibility = View.INVISIBLE
    binding.etReps.visibility = View.INVISIBLE
    binding.btnType.visibility = View.INVISIBLE
    binding.btnDeleteSet.visibility = View.INVISIBLE
}

fun showTypeOptions(binding: FragmentSetBinding) {
    binding.btnReg.visibility = View.VISIBLE
    binding.btnDS.visibility = View.VISIBLE
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