package com.sovereignfit.workoutapp.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sovereignfit.workoutapp.data.exercisedb.Exercise
import com.sovereignfit.workoutapp.data.exercisedb.ExerciseInstance
import com.sovereignfit.workoutapp.data.exercisedb.ExerciseViewModel
import com.sovereignfit.workoutapp.data.exercisedb.Workout
import com.sovereignfit.workoutapp.databinding.ExerciseListingBinding

class WorkoutBuildAdapter(private val context : AppCompatActivity,
                          private val exerciseViewModel: ExerciseViewModel)
    : RecyclerView.Adapter<WorkoutBuildAdapter.WorkoutBuildViewHolder>() {

    data class Inst(
        val EI: ExerciseInstance,
        var adapter: SetBuildAdapter? = null,
        var notes: String? = "",
        var sets: ArrayList<String>? = null,
        var holder: WorkoutBuildViewHolder? = null
    )

    private var supersets = mutableListOf<String>()
    var list = mutableListOf<Inst>()

    class WorkoutBuildViewHolder(val binding: ExerciseListingBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viwType: Int): WorkoutBuildViewHolder {
        val binding = ExerciseListingBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutBuildViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutBuildAdapter.WorkoutBuildViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        list[position].holder = holder

        //Show notes
        holder.binding.etNotes.setText(list[position].notes)

        //Hide SS Features
        //holder.binding.btnSSAbove.visibility = View.INVISIBLE
        //holder.binding.btnSSBelow.visibility = View.INVISIBLE
        holder.binding.tvName.text = list[position].EI.exercise.name
        if(supersets[position] == "none"){ // do nothing
        } else {
            holder.binding.tvName.setText(list[position].EI.exercise.name + "    -[${supersets[position]}]")
        }


        holder.binding.btnDeleteExercise.setOnClickListener {
            removeExerciseByPos(position)
        }

        if(list[position].sets == null){
            println("Creating new set array for position:$position")
            //This must be a new exercise, load from Exercise Instance if possible
            list[position].sets = list[position].EI.sets.toCollection(ArrayList())
            println("Set array = ${list[position].sets}")
        }

        var setAdapter = SetBuildAdapter(list[position].EI.exercise.unit, this)
        holder.binding.rvSets.adapter = setAdapter
        holder.binding.rvSets.layoutManager = LinearLayoutManager(context)
        for(set in list[position].sets!!) {
            println("------------------ ADDING SET <<>> TO ${list[position].EI.exercise.name}")
            setAdapter.addSet(set)
        }
        holder.binding.btnAddSet.setOnClickListener {
            setAdapter.addSet()
        }

        holder.binding.btnMoveUp.setOnClickListener {
            if(position == 0) { //Can't move upwards do nothing
            } else { //Swap this with the entity above it and notify.
                //val setsTop = list[position-1].adapter!!.getSets()
                //val setsBot = list[position].adapter!!.getSets()
                val tmp = list[position-1]
                list[position-1] = list[position]
                list[position] = tmp
                notifyDataSetChanged()
                //list[position].adapter!!.setSets(setsTop)
                //list[position-1].adapter!!.setSets(setsBot)
                //notifyDataSetChanged()
            }
        }
        holder.binding.btnMoveDown.setOnClickListener {
            if(position == list.size - 1) { //Can't move downwards do nothing
            } else { //Swap this with the entity below it and notify.
                val tmp = list[position+1]
                list[position+1] = list[position]
                list[position] = tmp
                notifyDataSetChanged()
            }
        }

        //notes listener
        holder.binding.etNotes.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //do nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //do nothing
            }

            override fun afterTextChanged(p0: Editable?) {
                //update notes
                list[position].notes = holder.binding.etNotes.text.toString()
            }

        })

        list[position].adapter = setAdapter
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addExercise(ex: Exercise){
        if(ex.EID == null){
            exerciseViewModel.allExercises.observe(context, {l ->
                l.let {
                    for (exercise in it) {
                        if (exercise.name == ex.name) {
                            ex.EID = exercise.EID
                            var inst = Inst(ExerciseInstance(ex))
                            list.add(inst)
                            supersets.add("none")
                            notifyItemInserted(list.size - 1)
                            break
                        }
                    }
                }
            })
        }
        else{
            var inst = Inst(ExerciseInstance(ex))
            list.add(inst)
            supersets.add("none") //ADD SUPERSET PRE HANDLING HERE
            notifyItemInserted(list.size - 1)
        }

    }

    fun removeExerciseByPos(position: Int){
        list.removeAt(position)
        supersets.removeAt(position)
        //notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    fun getExerciseList(): List<ExerciseInstance> {
        for(inst in list) {
            if(inst.sets != null) {
                inst.EI.sets = inst.sets!!.toTypedArray()
            }
        }
        return list.map {it.EI}
    }

    fun setWorkout(workout: Workout){

        var newList = mutableListOf<Inst>()
        var i = 0
        while(i < workout.exercises.size){
            var newInst = Inst(workout.exercises[i])
            newInst.notes = workout.notes!![i]
            newList.add(newInst)
            println("ADDED TO WORKOUT >>> $newInst")
            i++
        }
        list = newList

        var newSS = mutableListOf<String>()
        for(SS in workout.supersets) {
            newSS.add(SS)
        }
        supersets = newSS

        notifyDataSetChanged()
    }

    fun getSupersets(): List<String> {
        return supersets.toList()
    }

    fun getNotes(): List<String> {
        return list.map {it.notes!!}
    }

    fun getSetsByAdapter(sba: SetBuildAdapter): ArrayList<String> {
        //The setBuildAdapter calls this and gives itself to be allocated the appropriate set array.
        for(inst in list){
            if(inst.adapter == sba){
                return inst.sets!!
            }
        }
        //It should never make it here
        println("FAILED TO GET SETS CONSISTENCY BY ADAPTER")
        return arrayListOf<String>()
    }
    fun updateSetsByAdapter(sets: ArrayList<String>, sba: SetBuildAdapter) {
        //The setBuildAdapter must pass all set changes through the WorkoutBuildAdapter.
        for(inst in list){
            if(inst.adapter == sba){
                inst.sets = sets
                println("${inst.EI.exercise.name} >>> Updated consistent sets to:")
                println("\n ${inst.sets}")
                break
            }
        }
    }


}